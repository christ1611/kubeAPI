package com.kubeApi.apiService;

import com.kubeApi.apiService.iomodel.*;
import com.kubeApi.apiService.service.KubeDeploymentService;
import com.kubeApi.apiService.service.KubeJobService;
import com.kubeApi.apiService.service.KubeNetworkService;
import com.kubeApi.apiService.service.KubePodService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/kube")
public class KubeController {

    /**
     * package에
     * define - KubeDefine
     * iomodel - In/Out
     * Service - Service 만들기
     * */

    private final KubePodService podService;
    private final KubeJobService jobService;
    private final KubeDeploymentService deploymentService;
    private final KubeNetworkService networkService;

    /** Pod 관련 서비스 */
    // Pod 생성
    @PostMapping("/pod/create")
    public PodInfo createPod(@RequestBody KubePodCreateInput input) throws Exception {

        log.debug("[START] /kube/pod/create, INPUT = [{}]", input.toString());

        PodInfo output = podService.createPod(input);

        log.debug("[END] /kube/pod/create");

        return output;
    }

    // Pod 삭제
    @PostMapping("/pod/delete")
    public PodInfo deletePod(@RequestBody KubeDeleteInput input) throws Exception {

        log.debug("[START] /kube/pod/delete, INPUT = [{}]", input.toString());

        PodInfo output = podService.deletePod(input);

        log.debug("[END] /kube/pod/delete");

        return output;
    }

    // Pod 조회 - 특정 namespace 에 등록된 Pod 목록 조회
    @PostMapping("/pod/inquiry/namespace")
    public KubePodInquiryOutput inquiryPodNamespace(@RequestBody KubeInquiryInput input) throws Exception {

        log.debug("[START] /kube/pod/inquiry/namespace, INPUT = [{}]", input.toString());

        KubePodInquiryOutput output = podService.inquiryNamespace(input);

        log.debug("[END] /kube/pod/inquiry/namespace, OUTPUT = [{}]", output.toString());

        return output;
    }

    // Pod 조회 - 특정 namespace 에 등록된 특정 Pod 정보 조회
    @PostMapping("/pod/inquiry/namespace/podName")
    public PodInfo inquiryPodNamespaceName(@RequestBody KubeInquiryInput input) throws Exception {

        log.debug("[START] /kube/pod/inquiry/namespace/podName, INPUT = [{}]", input.toString());

        PodInfo output = podService.inquiryNamespacePod(input);

        log.debug("[END] /kube/pod/inquiry/namespace/podName, OUTPUT = [{}]", output.toString());

        return output;
    }

    // Pod 조회 - 모든 namespace 에 등록된 Pod 조회
    @PostMapping("/pod/inquiry/all")
    public KubePodInquiryOutput inquiryAllPodNamespace() throws Exception {

        log.debug("[START] /kube/pod/inquiry/all");

        KubePodInquiryOutput output = podService.inquiryAllPod();

        log.debug("[END] /kube/pod/inquiry/all, OUTPUT = [{}]", output.toString());

        return output;
    }

    // Pod 상태 조회
    @PostMapping("/pod/inquiry/status")
    public PodInfo inquiryPodStatus(@RequestBody KubeInquiryInput input) throws Exception {

        log.debug("[START] /kube/pod/inquiry/status, INPUT = [{}]", input.toString());

        PodInfo output = podService.inquiryPodStatus(input);

        log.debug("[END] /kube/pod/inquiry/status, OUTPUT = [{}]", output.toString());

        return output;
    }

    @PostMapping("/pod/inquiry/log")
    public  String  inquiryPodLog(@RequestBody KubeInquiryInput input) throws Exception {

        log.debug("[START] /kube/pod/inquiry/log, INPUT = [{}]", input.toString());

        String output = podService.inquiryPodLog(input);

        log.debug("[END] /kube/pod/inquiry/log, OUTPUT = [{}]", output.toString());

        return output;
    }

    /** Job 관련 서비스 */
    // Job 생성
    @PostMapping("/job/create")
    public JobInfo createJob(@RequestBody KubeJobCreateInput input) throws Exception {

        log.debug("[START] /kube/job/create, INPUT = [{}]", input.toString());

        JobInfo output = jobService.createJob(input);

        log.debug("[END] /kube/job/create, OUTPUT = [{}]", output.toString());

        return output;
    }

    // Job 삭제
    @PostMapping("/job/delete")
    public JobInfo deleteJob(@RequestBody KubeDeleteInput input) throws Exception {

        log.debug("[START] /kube/job/delete, INPUT = [{}]", input.toString());

        JobInfo output = jobService.deleteJob(input);

        log.debug("[END] /kube/job/delete, OUTPUT = [{}]", output.toString());

        return output;
    }

    // Job 조회 - 특정 namespace 에 등록된 Job 목록 조회
    @PostMapping("/job/inquiry/namespace")
    public KubeJobInquiryOutput inquiryJobNamespace(@RequestBody KubeInquiryInput input) throws Exception {

        log.debug("[START] /job/inquiry/namespace, INPUT = [{}]", input.toString());

        KubeJobInquiryOutput output = jobService.inquiryNamespace(input);

        log.debug("[END] /job/inquiry/namespace, OUTPUT = [{}]", output.toString());

        return output;
    }

    // Job 조회 - 특정 namespace 에 등록된 특정 Job 정보 조회
    @PostMapping("/job/inquiry/namespace/jobName")
    public JobInfo inquiryJobNamespaceName(@RequestBody KubeInquiryInput input) throws Exception {

        log.debug("[START] /job/inquiry/namespace/jobName, INPUT = [{}]", input.toString());

        JobInfo output = jobService.inquiryNamespaceJob(input);

        log.debug("[END] /job/inquiry/namespace/jobName, OUTPUT = [{}]", output.toString());

        return output;
    }

    // Job 조회 - 모든 namespace 에 등록된 Job 조회 (batch 이외의 namespace 에는 권한이 없기 때문에 오류 발생)
    @PostMapping("/job/inquiry/all")
    public KubeJobInquiryOutput inquiryAllJobNamespace() throws Exception {

        log.debug("[START] /job/inquiry/all");

        KubeJobInquiryOutput output = jobService.inquiryAllJob();

        log.debug("[END] /job/inquiry/all, OUTPUT = [{}]", output.toString());

        return output;
    }

    // Job 상태 조회
    @PostMapping("/job/inquiry/status")
    public JobInfo inquiryJobStatus(@RequestBody KubeInquiryInput input) throws Exception {

        log.debug("[START] /job/inquiry/status, INPUT = [{}]", input.toString());

        JobInfo output = jobService.inquiryJobStatus(input);

        log.debug("[END] /job/inquiry/status, OUTPUT = [{}]", output.toString());

        return output;
    }

    /** Deployment 관련 서비스 */
    // Deployment 생성
    @PostMapping("/deployment/create")
    public DeploymentInfo createDeployment(@RequestBody KubeDeploymentCreateInput input) throws Exception {

        log.debug("[START] /kube/deployment/create, INPUT = [{}]", input.toString());

        DeploymentInfo output = deploymentService.createDeployment(input);

        log.debug("[END] /kube/deployment/create, OUTPUT = [{}]", output.toString());

        return output;
    }

    // Deployment 일부 변경
    @PostMapping("/deployment/patch")
    public DeploymentInfo patchDeployment(@RequestBody KubeDeploymentUpdateInput input) throws Exception {

        log.debug("[START] /kube/deployment/patch, INPUT = [{}]", input.toString());

        DeploymentInfo output = deploymentService.replacePatchDeployment(input);

        log.debug("[END] /kube/deployment/patch, OUTPUT = [{}]", output.toString());

        return output;
    }

    // Deployment 전체 변경
    @PostMapping("/deployment/update")
    public DeploymentInfo updateDeployment(@RequestBody KubeDeploymentUpdateInput input) throws Exception {

        log.debug("[START] /kube/deployment/update, INPUT = [{}]", input.toString());

        DeploymentInfo output = deploymentService.updateDeployment(input);

        log.debug("[END] /kube/deployment/update, OUTPUT = [{}]", output.toString());

        return output;
    }

    // Deployment 삭제
    @PostMapping("/deployment/delete")
    public DeploymentDeleteInfo deleteDeployment(@RequestBody KubeDeleteInput input) throws Exception {

        log.debug("[START] /kube/deployment/delete, INPUT = [{}]", input.toString());

        DeploymentDeleteInfo output = deploymentService.deleteDeployment(input);

        log.debug("[END] /kube/deployment/delete, OUTPUT = [{}]", output.toString());

        return output;
    }

    // Deployment 조회 - 특정 namespace 에 등록된 Deployment 목록 조회
    @PostMapping("/deployment/inquiry/namespace")
    public KubeDeploymentInquiryOutput inquiryDeploymentNamespace(@RequestBody KubeInquiryInput input) throws Exception {

        log.debug("[START] /deployment/inquiry/namespace, INPUT = [{}]", input.toString());

        KubeDeploymentInquiryOutput output = deploymentService.inquiryNamespace(input);

        log.debug("[END] /deployment/inquiry/namespace, OUTPUT = [{}]", output.toString());

        return output;
    }

    // Deployment 조회 - 특정 namespace 에 등록된 특정 Deployment 정보 조회
    @PostMapping("/deployment/inquiry/namespace/deploymentName")
    public DeploymentInfo inquiryDeploymentNamespaceName(@RequestBody KubeInquiryInput input) throws Exception {

        log.debug("[START] /deployment/inquiry/namespace/deploymentName, INPUT = [{}]", input.toString());

        DeploymentInfo output = deploymentService.inquiryNamespaceDeployment(input);

        log.debug("[END] /deployment/inquiry/namespace/deploymentName, OUTPUT = [{}]", output.toString());

        return output;
    }

    // Job 조회 - 모든 namespace 에 등록된 Pod 조회 (batch 이외의 namespace 에는 권한이 없기 때문에 오류 발생)
    @PostMapping("/deployment/inquiry/all")
    public KubeDeploymentInquiryOutput inquiryAllDeploymentNamespace() throws Exception {

        log.debug("[START] /deployment/inquiry/all");

        KubeDeploymentInquiryOutput output = deploymentService.inquiryAllJob();

        log.debug("[END] /deployment/inquiry/all, OUTPUT = [{}]", output.toString());

        return output;
    }

    // Job 상태 조회
    @PostMapping("/deployment/inquiry/status")
    public DeploymentInfo inquiryDeploymentStatus(@RequestBody KubeInquiryInput input) throws Exception {

        log.debug("[START] /deployment/inquiry/status, INPUT = [{}]", input.toString());

        DeploymentInfo output = deploymentService.inquiryDeploymentStatus(input);

        log.debug("[END] /deployment/inquiry/status, OUTPUT = [{}]", output.toString());

        return output;
    }

    @PostMapping("/network/create")
    public ServiceInfo createNetwork(@RequestBody KubeServiceCreateInput input) throws Exception {

        log.debug("[START] /network/create, INPUT = [{}]", input.toString());

        ServiceInfo output = networkService.createServices(input);

        log.debug("[END] /network/create, OUTPUT = [{}]", output.toString());

        return output;
    }

    @PostMapping("/network/delete")
    public ServiceInfo deleteNetwork(@RequestBody KubeDeleteInput input) throws Exception {

        log.debug("[START] /network/delete, INPUT = [{}]", input.toString());

        ServiceInfo output = networkService.deleteService(input);

        log.debug("[END] /network/delete, OUTPUT = [{}]", output.toString());

        return output;
    }

    @PostMapping("/network/inquiry/status")
    public ServiceInfo inquiryNetworkStatus(@RequestBody KubeInquiryInput input) throws Exception {

        log.debug("[START] /network/inquiry/status, INPUT = [{}]", input.toString());

        ServiceInfo output = networkService.inquiryNetworkStatus(input);

        log.debug("[END] /network/inquiry/status, OUTPUT = [{}]", output.toString());

        return output;
    }

    @PostMapping("/route/create")
    public ServiceInfo createRoute(@RequestBody KubeRouteCreateInput input) throws Exception {

        log.debug("[START] /route/create, INPUT = [{}]", input.toString());

        ServiceInfo output = networkService.createRoute(input);

        log.debug("[END] /route/create, OUTPUT = [{}]", output.toString());

        return output;
    }

    @PostMapping("/routes/inquiry/status")
    public ServiceInfo routeNetworkStatus(@RequestBody KubeInquiryInput input) throws Exception {

        log.debug("[START] /network/inquiry/status, INPUT = [{}]", input.toString());

        ServiceInfo output = networkService.inquiryRouteStatus(input);

        log.debug("[END] /network/inquiry/status, OUTPUT = [{}]", output.toString());

        return output;
    }

    @PostMapping("/route/delete")
    public ServiceInfo deleteRoute(@RequestBody KubeDeleteInput input) throws Exception {

        log.debug("[START] /route/delete, INPUT = [{}]", input.toString());

        ServiceInfo output = networkService.deleteRoute(input);

        log.debug("[END] /route/delete, OUTPUT = [{}]", output.toString());

        return output;
    }
}
