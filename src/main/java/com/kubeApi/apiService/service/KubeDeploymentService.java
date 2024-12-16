package com.kubeApi.apiService.service;

import com.kubeApi.apiService.define.KubeDefine;
import com.kubeApi.apiService.iomodel.*;
import com.kubeApi.apiService.module.KubeSetBody;
import com.kubeApi.core.define.CoreDefine.HttpMethCd;
import com.kubeApi.core.define.CoreErrCode;
import com.kubeApi.core.exception.KubeException;
import com.kubeApi.core.module.KubeTemplate;
import com.kubeApi.core.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class KubeDeploymentService {

    private final KubeTemplate kubeTemplate;
    private final StringUtil stringUtil;
    private final KubeSetBody kubeSetBody;

    // Deployment 생성
    /** 직접 Deployment 를 생성하는 경우에는 배치실행이력 저장 안함 */
    public DeploymentInfo createDeployment(KubeDeploymentCreateInput input) throws Exception {
        String apiUri = stringUtil.makeFullPath(KubeDefine.DEPLOYMENT_NAMESPACE_URI, input.getNamespace());

        log.debug("Create Deployment API URI = [{}]", apiUri);

        if(StringUtils.isEmpty(input.getAppName())) {
            log.debug("Application Name is mandatory.");
            throw new KubeException(CoreErrCode.MANDATORY_BATCH_NAME);
        }
        if(StringUtils.isEmpty(input.getAppVersion())) {
            log.debug("Application Version is mandatory.");
            throw new KubeException(CoreErrCode.MANDATORY_DEFAULT_VERSION);
        }
        if(StringUtils.isEmpty(input.getUserId())) {
            log.debug("User ID is mandatory.");
            throw new KubeException(CoreErrCode.MANDATORY_USER_ID);
        }

        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("-yyyyMMddHHmmssSSS"));
        String podName = input.getAppName().concat(dateTime);

        DeploymentInfo result = kubeTemplate.callApi(DeploymentInfo.class, HttpMethCd.POST, apiUri,
                kubeSetBody.setCreateDeployment(kubeTemplate.getKUBE_NAMESPACE(), input.getAppName(), input.getAppVersion(),
                        input.getParameter(), input.getReplica(), input.getStrategy(), input.getMaxSurge(), input.getMaxUnavailable(),
                        podName));

        log.debug("Create Deployment Result = [{}]", result);

        return result;
    }

    // Deployment 설정 일부 변경 - Patch
    public DeploymentInfo replacePatchDeployment(KubeDeploymentUpdateInput input) throws Exception {
        String apiUri = stringUtil.makeFullPath(KubeDefine.DEPLOYMENT_NAMESPACE_NAME_URI, input.getNamespace(), input.getAppName());

        log.debug("Replace Patch Deployment API URI = [{}]", apiUri);

        DeploymentInfo result = kubeTemplate.callApi(DeploymentInfo.class, HttpMethCd.PATCH, apiUri, "application/strategic-merge-patch+json",
                kubeSetBody.setPatchDeployment(input.getAppName(), input.getAppVersion(),
                        input.getReplica(), input.getStrategy(), input.getMaxSurge(), input.getMaxUnavailable()));

        log.debug("Patch Patch Deployment Result = [{}]", result);

        return result;
    }

    // Deployment 변경
    public DeploymentInfo updateDeployment(KubeDeploymentUpdateInput input) throws Exception {
        String apiUri = stringUtil.makeFullPath(KubeDefine.DEPLOYMENT_NAMESPACE_NAME_URI, input.getNamespace(), input.getAppName());

        log.debug("Update Deployment API URI = [{}]", apiUri);

        if(StringUtils.isEmpty(input.getAppName())) {
            log.debug("Application Name is mandatory.");
            throw new KubeException(CoreErrCode.MANDATORY_BATCH_NAME);
        }
        if(StringUtils.isEmpty(input.getAppVersion())) {
            log.debug("Application Version is mandatory.");
            throw new KubeException(CoreErrCode.MANDATORY_DEFAULT_VERSION);
        }
        if(StringUtils.isEmpty(input.getUserId())) {
            log.debug("User ID is mandatory.");
            throw new KubeException(CoreErrCode.MANDATORY_USER_ID);
        }

        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("-yyyyMMddHHmmssSSS"));
        String podName = input.getAppName().concat(dateTime);

        DeploymentInfo result = kubeTemplate.callApi(DeploymentInfo.class, HttpMethCd.PUT, apiUri,
                kubeSetBody.setCreateDeployment(kubeTemplate.getKUBE_NAMESPACE(), input.getAppName(), input.getAppVersion(),
                        input.getParameter(), input.getReplica(), input.getStrategy(), input.getMaxSurge(), input.getMaxUnavailable(), podName));

        log.debug("Update Deployment Result = [{}]", result);

        return result;
    }

    // Deployment 삭제
    public DeploymentDeleteInfo deleteDeployment(KubeDeleteInput input) throws Exception {
        String apiUri = stringUtil.makeFullPath(KubeDefine.DEPLOYMENT_NAMESPACE_NAME_URI, input.getNamespace(), input.getName());

        log.debug("Delete Deployment API URI = [{}]", apiUri);

        DeploymentDeleteInfo result = kubeTemplate.callApi(DeploymentDeleteInfo.class, HttpMethCd.DELETE, apiUri);

        log.debug("Delete Deployment Result = [{}]", result);

        return result;
    }

    // Deployment 조회 - 특정 namespace 에 등록된 Deployment 목록 조회
    public KubeDeploymentInquiryOutput inquiryNamespace(KubeInquiryInput input) throws Exception {
        String apiUri = stringUtil.makeFullPath(KubeDefine.DEPLOYMENT_NAMESPACE_URI, input.getNamespace());

        log.debug("Inquiry Namespace Deployment API URI = [{}]", apiUri);

        KubeDeploymentInquiryOutput result = kubeTemplate.callApi(KubeDeploymentInquiryOutput.class, HttpMethCd.GET, apiUri);

        log.debug("Inquiry Namespace Deployment Result = [{}]", result);

        return result;
    }

    // Deployment 조회 - 특정 namespace 에 등록된 특정 Deployment 정보 조회
    public DeploymentInfo inquiryNamespaceDeployment(KubeInquiryInput input) throws Exception {
        String apiUri = stringUtil.makeFullPath(KubeDefine.DEPLOYMENT_NAMESPACE_NAME_URI, input.getNamespace(), input.getName());

        log.debug("Inquiry Deployment API URI = [{}]", apiUri);

        DeploymentInfo result = kubeTemplate.callApi(DeploymentInfo.class, HttpMethCd.GET, apiUri);

        log.debug("Inquiry Deployment Result = [{}]", result);

        return result;
    }

    // Deployment 조회 - 모든 namespace 에 등록된 Deployment 조회
    public KubeDeploymentInquiryOutput inquiryAllJob() throws Exception {
        String apiUri = stringUtil.makeFullPath(KubeDefine.DEPLOYMENT_URI);

        log.debug("Inquiry All Deployment API URI = [{}]", apiUri);

        KubeDeploymentInquiryOutput result = kubeTemplate.callApi(KubeDeploymentInquiryOutput.class, HttpMethCd.GET, apiUri);

        log.debug("Inquiry All Deployment Result = [{}]", result);

        return result;
    }

    // Deployment 상태 조회
    public DeploymentInfo inquiryDeploymentStatus(KubeInquiryInput input) throws Exception {
        String apiUri = stringUtil.makeFullPath( KubeDefine.DEPLOYMENT_STATUS_URI, input.getNamespace(), input.getName());

        log.debug("Inquiry Deployment Status API URI = [{}]", apiUri);

        DeploymentInfo result = kubeTemplate.callApi(DeploymentInfo.class, HttpMethCd.GET, apiUri);

        log.debug("Inquiry Deployment Status Result = [{}]", result);

        return result;
    }
}