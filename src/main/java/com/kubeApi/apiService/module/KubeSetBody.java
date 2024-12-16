package com.kubeApi.apiService.module;

import com.kubeApi.apiService.iomodel.*;
import com.kubeApi.core.util.StringUtil;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.DeploymentSpec;
import io.fabric8.kubernetes.api.model.apps.DeploymentStrategy;
import io.fabric8.kubernetes.api.model.apps.RollingUpdateDeployment;
import io.fabric8.kubernetes.api.model.batch.v1.JobSpec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class KubeSetBody {

    @Value("${kube.podVer}")
    private String KUBE_POD_VER;

    @Value("${kube.jobVer}")
    private String KUBE_JOB_VER;

    @Value("${kube.deploymentVer}")
    private String KUBE_DEPLOYMENT_VER;

    @Value("${harbor.image.uri}")
    private String KUBE_IMAGE_URI;

    private final StringUtil stringUtil;

    // Pod 관련 API 호출 시 Body 세팅
    public PodInfo setCreatePod(String namespace, String appName, String appVersion, Map<String, Object> parameter, String podName) throws Exception {
        return setCreatePod(namespace, appName, appVersion, parameter, podName, "", "");
    }

    public PodInfo setCreatePod(String namespace, String appName, String appVersion, Map<String, Object> parameter,
                                String podName, String appId, String appHisSeq) throws Exception {
        log.debug("Pod Setting. INPUT - namespace = [{}], appName = [{}], appVersion = [{}], parameter = [{}], " +
                        "appId = [{}], appHisSeq = [{}]", namespace, appName, appVersion, parameter, appId, appHisSeq);

        /** metadata 세팅 */
        ObjectMeta metadata = new ObjectMeta();
        /** 실제 Pod 이름으로 중복으로 설정 불가 */
        metadata.setName(podName);
        metadata.setNamespace(namespace);
        metadata.getLabels().put("app", appName);

        /** spec 세팅 */
        SeccompProfile seccompProfile = new SeccompProfile();
        seccompProfile.setType("RuntimeDefault");

        PodSecurityContext context = new PodSecurityContext();
        context.setRunAsNonRoot(true);
        context.setSeccompProfile(seccompProfile);

        ContainerPort containerPort = new ContainerPort();
        containerPort.setContainerPort(8080);

        Capabilities capabilities = new Capabilities();
        capabilities.getDrop().add("ALL");

        SecurityContext securityContext = new SecurityContext();
        securityContext.setAllowPrivilegeEscalation(false);
        securityContext.setCapabilities(capabilities);

        Container container = new Container();
        container.setName(appName);
        container.setImage(stringUtil.makeCustomPath("%s%s:%s",KUBE_IMAGE_URI, appName, appVersion));
        container.getPorts().add(containerPort);
        container.setSecurityContext(securityContext);

        List<EnvVar> env = new ArrayList<>();
        EnvVar envVar = new EnvVar();
        envVar.setName("TZ");
        envVar.setValue(ZoneId.systemDefault().toString());
        env.add(envVar);

        /** Pod 생성 후 배치에서 필요한 데이터 세팅 */
        if(!StringUtils.isEmpty(appId)) {
            envVar = new EnvVar();
            envVar.setName("SYSTEM_APP_ID");
            envVar.setValue(appId);
            env.add(envVar);
        }

        if(!StringUtils.isEmpty(appHisSeq)) {
            envVar = new EnvVar();
            envVar.setName("SYSTEM_APP_HIS_SEQ");
            envVar.setValue(appHisSeq);
            env.add(envVar);
        }

        if(parameter != null && !parameter.isEmpty()) {
            for(String key : parameter.keySet()) {
                envVar = new EnvVar();
                // Pod 생성 시 들어온 파라미터 값을 구분하기 위해 PARAM_ 인 문자열 추가
                envVar.setName("PARAM_".concat(key));
                envVar.setValue(parameter.get(key).toString());
                env.add(envVar);
            }
        }

        container.setEnv(env);

        PodSpec spec = new PodSpec();
        spec.setSecurityContext(context);
        spec.getContainers().add(container);

        PodInfo podInfo = new PodInfo();
        podInfo.setApiVersion(KUBE_POD_VER);
        podInfo.setKind("Pod");
        podInfo.setMetadata(metadata);
        podInfo.setSpec(spec);

        log.debug("Pod Setting. OUTPUT = [{}]", podInfo.toString());

        return podInfo;
    }

    // Job 관련 API 호출 시 Body 세팅
    public JobInfo setCreateJob(String namespace, String appName, String appVersion, Map<String, Object> parameter,
                                Long terminationGracePeriodSeconds, Long ttlSecondsAfterFinished, Long parallelism, Long completions, Long backOffLimit,
                                String podName) throws Exception {
        return setCreateJob(namespace, appName, appVersion, parameter, terminationGracePeriodSeconds, ttlSecondsAfterFinished,
                parallelism, completions, backOffLimit, podName, "", "", "");
    }

    public JobInfo setCreateJob(String namespace, String appName, String appVersion, Map<String, Object> parameter,
                                Long terminationGracePeriodSeconds, Long ttlSecondsAfterFinished, Long parallelism, Long completions, Long backOffLimit,
                                String podName, String scheduleExecYn, String appId, String appHisSeq)
            throws Exception {
        log.debug("Job Setting. INPUT - namespace = [{}], appName = [{}], appVersion = [{}], parameter = [{}]" +
                        "terminationGracePeriodSeconds = [{}], ttlSecondsAfterFinished = [{}], parallelism = [{}]" +
                        ", completions = [{}], backOffLimit = [{}], podName = [{}], scheduleExecYn = [{}], appId = [{}], appHisSeq = [{}]",
                namespace, appName, appVersion, parameter, terminationGracePeriodSeconds, ttlSecondsAfterFinished, parallelism, completions, backOffLimit,
                podName, scheduleExecYn, appId, appHisSeq);

        /** metadata 세팅 */
        ObjectMeta metadata = new ObjectMeta();
        metadata.setName(podName);
        metadata.setNamespace(namespace);
        metadata.getLabels().put("batchName", appName);
        String jobsGroup = StringUtils.equals(scheduleExecYn,"Y")? "longTermSchedule":"oneTimeBatch";
        metadata.getLabels().put("app.kubernetes.io/part-of", jobsGroup);
        /** spec 세팅 */
        Container container = new Container();
        container.setName(appName);
        container.setImage(stringUtil.makeCustomPath("%s%s:%s",KUBE_IMAGE_URI, appName, appVersion));

        List<EnvVar> env = new ArrayList<>();
        EnvVar envVar = new EnvVar();
        envVar.setName("TZ");
        envVar.setValue(ZoneId.systemDefault().toString());
        env.add(envVar);

        /** Pod 생성 후 배치에서 필요한 데이터 세팅 */
        if(!StringUtils.isEmpty(scheduleExecYn)) {
            envVar = new EnvVar();
            envVar.setName("SYSTEM_SCHEDULE_EXEC_YN");
            envVar.setValue(scheduleExecYn);
            env.add(envVar);
        }

        if(!StringUtils.isEmpty(appId)) {
            envVar = new EnvVar();
            envVar.setName("SYSTEM_APP_ID");
            envVar.setValue(appId);
            env.add(envVar);
        }

        if(!StringUtils.isEmpty(appHisSeq)) {
            envVar = new EnvVar();
            envVar.setName("SYSTEM_APP_HIS_SEQ");
            envVar.setValue(appHisSeq);
            env.add(envVar);
        }

        if(parameter != null && !parameter.isEmpty()) {
            for(String key : parameter.keySet()) {
                envVar = new EnvVar();
                // Pod 생성 시 들어온 파라미터 값을 구분하기 위해 PARAM_ 인 문자열 추가
                envVar.setName("PARAM_".concat(key));
                envVar.setValue(parameter.get(key).toString());
                env.add(envVar);
            }
        }

        container.setEnv(env);

        PodSpec podSpec = new PodSpec();
        podSpec.getContainers().add(container);
        podSpec.setRestartPolicy("Never");  // Job 이 종료된 후 Pod 의 재시작 정책
        podSpec.setTerminationGracePeriodSeconds(terminationGracePeriodSeconds);

        PodTemplateSpec podTemplate = new PodTemplateSpec();
        podTemplate.setSpec(podSpec);
        podTemplate.setMetadata(metadata);

        JobSpec spec = new JobSpec();
        spec.setTtlSecondsAfterFinished(ttlSecondsAfterFinished.intValue());
        spec.setTemplate(podTemplate);
        
        /**
         *  Pod 를 여러개 띄우는 방법
         *   - parallelism, completions, backoffLimit 세팅
         *     > parallelism : 동시에 실행되는 Pod 개수
         *     > completions : 수행할 작업 개수
         *     > backoffLimit : Pod 실패 시 재시도 개수
         * */
        spec.setParallelism(parallelism.intValue());
        spec.setCompletions(completions.intValue());
        spec.setBackoffLimit(backOffLimit.intValue());

        JobInfo jobInfo = new JobInfo();
        jobInfo.setApiVersion(KUBE_JOB_VER);
        jobInfo.setKind("Job");
        jobInfo.setMetadata(metadata);
        jobInfo.setSpec(spec);

        log.debug("Job Setting. OUTPUT = [{}]", jobInfo.toString());

        return jobInfo;
    }

    // Deployment 관련 API 호출 시 Body 세팅
    public DeploymentInfo setCreateDeployment(String namespace, String appName, String appVersion, Map<String, Object> parameter,
                                              Long replica, String strategy, IntOrString maxSurge, IntOrString maxUnavailable,
                                              String podName) throws Exception {
        return setCreateDeployment(namespace, appName, appVersion, parameter, replica, strategy, maxSurge, maxUnavailable, podName, "", "");
    }

    public DeploymentInfo setCreateDeployment(String namespace, String appName, String appVersion, Map<String, Object> parameter,
                                Long replica, String strategy, IntOrString maxSurge, IntOrString maxUnavailable,
                                String podName, String appId, String appHisSeq) throws Exception {
        log.debug("Deployment Setting. INPUT - namespace = [{}], appName = [{}], appVersion = [{}], parameter = [{}] " +
                        "replica = [{}], strategy = [{}], maxSurge = [{}], maxUnavailable = [{}], podName = [{}], " +
                        "appId = [{}], appHisSeq = [{}]",
                namespace, appName, appVersion, parameter, replica, strategy, maxSurge, maxUnavailable, podName, appId, appHisSeq);

        /** metadata 세팅 */
        ObjectMeta metadata = new ObjectMeta();
        metadata.setName(podName);
        metadata.setNamespace(namespace);
        metadata.getLabels().put("app", appName);
        metadata.getLabels().put("batchName", appName);

        /** spec 세팅 */
        LabelSelector labelSelector = new LabelSelector();
        labelSelector.getMatchLabels().put("app", appName);
        labelSelector.getMatchLabels().put("batchName", appName);

        ObjectMeta templateMeta = new ObjectMeta();
        templateMeta.getLabels().put("app", appName);
        templateMeta.getLabels().put("batchName", appName);
        metadata.getLabels().put("app.kubernetes.io/part-of", "daemon");
        ContainerPort containerPort = new ContainerPort();
        containerPort.setContainerPort(8080);
        containerPort.setProtocol("TCP");

        Container container = new Container();
        container.setName(appName);
        container.setImage(stringUtil.makeCustomPath("%s%s:%s",KUBE_IMAGE_URI, appName, appVersion));
        container.getPorts().add(containerPort);

        List<EnvVar> env = new ArrayList<>();
        EnvVar envVar = new EnvVar();
        envVar.setName("TZ");
        envVar.setValue(ZoneId.systemDefault().toString());
        env.add(envVar);

        /** Pod 생성 후 배치에서 필요한 데이터 세팅 */
        if(!StringUtils.isEmpty(appId)) {
            envVar = new EnvVar();
            envVar.setName("SYSTEM_APP_ID");
            envVar.setValue(appId);
            env.add(envVar);
        }

        if(!StringUtils.isEmpty(appHisSeq)) {
            envVar = new EnvVar();
            envVar.setName("SYSTEM_APP_HIS_SEQ");
            envVar.setValue(appHisSeq);
            env.add(envVar);
        }

        if(parameter != null && !parameter.isEmpty()) {
            for(String key : parameter.keySet()) {
                envVar = new EnvVar();
                // Pod 생성 시 들어온 파라미터 값을 구분하기 위해 PARAM_ 인 문자열 추가
                envVar.setName("PARAM_".concat(key));
                envVar.setValue(parameter.get(key).toString());
                env.add(envVar);
            }
        }

        container.setEnv(env);

        PodSpec podSpec = new PodSpec();
        podSpec.getContainers().add(container);

        PodTemplateSpec podTemplate = new PodTemplateSpec();
        podTemplate.setMetadata(templateMeta);
        podTemplate.setSpec(podSpec);
        podTemplate.setMetadata(metadata);

        DeploymentStrategy deploymentStrategy = new DeploymentStrategy();
        deploymentStrategy.setType(strategy);

        if(StringUtils.compare(strategy, "RollingUpdate") == 0) {
            RollingUpdateDeployment rollingUpdate = new RollingUpdateDeployment();
            rollingUpdate.setMaxSurge(maxSurge);
            rollingUpdate.setMaxUnavailable(maxUnavailable);

            deploymentStrategy.setRollingUpdate(rollingUpdate);
        }

        DeploymentSpec spec = new DeploymentSpec();
        spec.setSelector(labelSelector);
        spec.setReplicas(replica.intValue());
        spec.setTemplate(podTemplate);
        spec.setStrategy(deploymentStrategy);

        DeploymentInfo deploymentInfo = new DeploymentInfo();
        deploymentInfo.setApiVersion(KUBE_DEPLOYMENT_VER);
        deploymentInfo.setKind("Deployment");
        deploymentInfo.setMetadata(metadata);
        deploymentInfo.setSpec(spec);

        log.debug("Deployment Setting. OUTPUT = [{}]", deploymentInfo.toString());

        return deploymentInfo;
    }


    // Deployment 관련 API 호출 시 Body 세팅 - Patch
    public DeploymentInfo setPatchDeployment(String appName, String appVersion, Long replica,
                                             String strategy, IntOrString maxSurge, IntOrString maxUnavailable)
            throws Exception {
        log.debug("Deployment Partially Update Setting. INPUT - appVersion = [{}], replica = [{}], " +
                        "strategy = [{}], maxSurge = [{}], maxUnavailable = [{}]",
                appVersion, replica, strategy, maxSurge, maxUnavailable);


        /** spec 세팅 */
        PodSpec podSpec = new PodSpec();
        PodTemplateSpec podTemplate = new PodTemplateSpec();
        if(!StringUtils.isEmpty(appVersion)) {
            Container container = new Container();
            container.setName(appName);
            container.setImage(stringUtil.makeCustomPath("%s%s:%s",KUBE_IMAGE_URI, appName, appVersion));

            podSpec.getContainers().add(container);
            podTemplate.setSpec(podSpec);
        }

        DeploymentSpec spec = new DeploymentSpec();
        spec.setTemplate(podTemplate);

        if(replica != 0) {
            spec.setReplicas(replica.intValue());
        }

        DeploymentStrategy deploymentStrategy = new DeploymentStrategy();
        if(!StringUtils.isEmpty(strategy)) {
            if(StringUtils.compare(strategy, "RollingUpdate") == 0) {
                RollingUpdateDeployment rollingUpdate = new RollingUpdateDeployment();
                rollingUpdate.setMaxSurge(maxSurge);
                rollingUpdate.setMaxUnavailable(maxUnavailable);

                deploymentStrategy.setRollingUpdate(rollingUpdate);
            }

            deploymentStrategy.setType(strategy);
            spec.setStrategy(deploymentStrategy);
        }

        DeploymentInfo deploymentInfo = new DeploymentInfo();
        deploymentInfo.setSpec(spec);

        log.debug("Deployment Partially Update Setting. OUTPUT = [{}]", deploymentInfo.toString());

        return deploymentInfo;
    }

    public ServiceInfo setCreateServices(String namespace, String appName, String podName, int port, IntOrString targetPort) throws Exception
    {
        ServiceInfo serviceInfo = new ServiceInfo();

        ServiceSpec spec = new ServiceSpec();
        spec.getSelector().put("app",appName);

        ServicePort servicePort = new ServicePort();
        servicePort.setPort         (port);
        servicePort.setTargetPort   (targetPort);
        spec.getPorts().add(servicePort);

        ObjectMeta metadata = new ObjectMeta();
        metadata.setName(appName);
        metadata.setNamespace(namespace);


        serviceInfo.setApiVersion("v1");
        serviceInfo.setKind("Service");
        serviceInfo.setMetadata(metadata);
        serviceInfo.setSpec(spec);
        return serviceInfo;
    }

    public RouteInfo setCreateRoute(String namespace, String appName, String host, int targetPort) throws Exception
    {
        RouteInfo routeInfo = new RouteInfo();

        RouteInfo.RouteSpec spec = new RouteInfo.RouteSpec();
        spec.setTo(new HashMap<>());
        spec.getTo().put("kind","Service");
        spec.getTo().put("name", appName);

        spec.setPort(new HashMap<>());
        spec.getPort().put("targetPort",targetPort);

        ObjectMeta metadata = new ObjectMeta();
        metadata.setName(appName);
        metadata.setNamespace(namespace);

        routeInfo.setApiVersion("route.openshift.io/v1");
        routeInfo.setKind("Route");
        routeInfo.setMetadata(metadata);
        routeInfo.setSpec(spec);
        return routeInfo;
    }

    public DeleteOptionInfo setStopJob(String propagationPolicy, Long gracePeriodSeconds) throws Exception {
        log.debug("Job Delete Setting. INPUT - propagationPolicy = [{}], gracePeriodSeconds = [{}]",
                propagationPolicy, gracePeriodSeconds);

        DeleteOptionInfo deleteOptionInfo = new DeleteOptionInfo();
        deleteOptionInfo.setPropagationPolicy(propagationPolicy);
        deleteOptionInfo.setGracePeriodSeconds(gracePeriodSeconds);

        log.debug("Job Delete Setting. OUTPUT = [{}]", deleteOptionInfo);

        return deleteOptionInfo;
    }
}