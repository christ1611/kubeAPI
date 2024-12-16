package com.kubeApi.apiService.service;

import com.kubeApi.apiService.define.KubeDefine;
import com.kubeApi.apiService.iomodel.*;
import com.kubeApi.apiService.module.KubeSetBody;
import com.kubeApi.core.define.CoreDefine.*;
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
public class KubePodService {

    private final KubeTemplate kubeTemplate;
    private final StringUtil stringUtil;
    private final KubeSetBody kubeSetBody;

    // Pod 생성
    /** 직접 Pod 를 생성하는 경우에는 배치실행이력 저장 안함 */
    public PodInfo createPod(KubePodCreateInput input) throws Exception {
        String apiUri = stringUtil.makeFullPath(KubeDefine.POD_NAMESPACE_URI, input.getNamespace());

        log.debug("Create Pod API URI = [{}]", apiUri);

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

        PodInfo result = kubeTemplate.callApi(PodInfo.class, HttpMethCd.POST, apiUri,
                kubeSetBody.setCreatePod(kubeTemplate.getKUBE_NAMESPACE(), input.getAppName(), input.getAppVersion(), input.getParameter(), podName));

        log.debug("Create Pod Result = [{}]", result.toString());

        return result;
    }

    // Pod 삭제
    public PodInfo deletePod(KubeDeleteInput input) throws Exception {
        String apiUri = stringUtil.makeFullPath(KubeDefine.POD_NAMESPACE_NAME_URI, input.getNamespace(), input.getName());

        log.debug("Delete Pod API URI = [{}]", apiUri);

        /** Pod 삭제 시 Body (DeleteOption) 필요한 경우 Body 세팅하는 것으로 변경 필요 */
//        PodInfo result = kubeTemplate.callApi(PodInfo.class, HttpMethCd.DELETE, apiUri, input.getDeleteOptionInfo());
        PodInfo result = kubeTemplate.callApi(PodInfo.class, HttpMethCd.DELETE, apiUri);

        log.debug("Delete Pod Result = [{}]", result);

        return result;
    }

    // Pod 조회 - 특정 namespace 에 등록된 Pod 목록 조회
    public KubePodInquiryOutput inquiryNamespace(KubeInquiryInput input) throws Exception {
        String apiUri = stringUtil.makeFullPath(KubeDefine.POD_NAMESPACE_URI, input.getNamespace());

        log.debug("Inquiry Namespace Pod API URI = [{}]", apiUri);

        KubePodInquiryOutput result = kubeTemplate.callApi(KubePodInquiryOutput.class, HttpMethCd.GET, apiUri);

        log.debug("Inquiry Namespace Pod Result = [{}]", result);

        return result;
    }

    // Pod 조회 - 특정 namespace 에 등록된 특정 Pod 정보 조회
    public PodInfo inquiryNamespacePod(KubeInquiryInput input) throws Exception {
        String apiUri = stringUtil.makeFullPath(KubeDefine.POD_NAMESPACE_NAME_URI, input.getNamespace(), input.getName());

        log.debug("Inquiry Pod API URI = [{}]", apiUri);

        PodInfo result = kubeTemplate.callApi(PodInfo.class, HttpMethCd.GET, apiUri);

        log.debug("Inquiry Pod Result = [{}]", result);

        return result;
    }

    // Pod 조회 - 모든 namespace 에 등록된 Pod 조회
    public KubePodInquiryOutput inquiryAllPod() throws Exception {
        String apiUri = stringUtil.makeFullPath(KubeDefine.POD_URI);

        log.debug("Inquiry All Pod API URI = [{}]", apiUri);

        KubePodInquiryOutput result = kubeTemplate.callApi(KubePodInquiryOutput.class, HttpMethCd.GET, apiUri);

        log.debug("Inquiry All Pod Result = [{}]", result);

        return result;
    }

    // Pod 상태 조회
    public PodInfo inquiryPodStatus(KubeInquiryInput input) throws Exception {
        String apiUri = stringUtil.makeFullPath(KubeDefine.POD_STATUS_URI, input.getNamespace(), input.getName());

        log.debug("Inquiry Pod Status API URI = [{}]", apiUri);

        PodInfo result = kubeTemplate.callApi(PodInfo.class, HttpMethCd.GET, apiUri);

        log.debug("Inquiry Pod Status Result = [{}]", result);

        return result;
    }

    public String inquiryPodLog(KubeInquiryInput input) throws Exception {
        String apiUri = stringUtil.makeFullPath(KubeDefine.POD_LOGS_URI, input.getNamespace(), input.getName());

        log.debug("Inquiry Pod Status API URI = [{}]", apiUri);

        String result = kubeTemplate.callApiForLogs(HttpMethCd.GET, apiUri);

        log.debug("Inquiry Pod Status Result = [{}]", result);

        return result;
    }
}