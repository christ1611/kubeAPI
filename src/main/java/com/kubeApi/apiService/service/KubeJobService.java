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
public class KubeJobService {

    private final KubeTemplate kubeTemplate;
    private final StringUtil stringUtil;
    private final KubeSetBody kubeSetBody;

    // Job 생성
    /** 직접 Job 을 생성하는 경우에는 배치실행이력 저장 안함 */
    public JobInfo createJob(KubeJobCreateInput input) throws Exception {
        String apiUri = stringUtil.makeFullPath(KubeDefine.JOB_NAMESPACE_URI, input.getNamespace());

        log.debug("Create Job API URI = [{}]", apiUri);

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

        JobInfo result = kubeTemplate.callApi(JobInfo.class, HttpMethCd.POST, apiUri,
                kubeSetBody.setCreateJob(kubeTemplate.getKUBE_NAMESPACE(), input.getAppName(), input.getAppVersion(), input.getParameter(),
                        input.getTerminationGracePeriodSeconds(), input.getTtlSecondsAfterFinished(), input.getParallelism(), input.getCompletions(),
                        input.getBackOffLimit(), podName));

        log.debug("Create Job Result = [{}]", result);

        return result;
    }

    // Job 삭제
    public JobInfo deleteJob(KubeDeleteInput input) throws Exception {
        String apiUri = stringUtil.makeFullPath(KubeDefine.JOB_NAMESPACE_NAME_URI, input.getNamespace(), input.getName());

        log.debug("Delete Job API URI = [{}]", apiUri);

        /** Job 삭제 시 Body (DeleteOption) 필요한 경우 Body 세팅하는 것으로 변경 필요 */
//        JobInfo result = kubeTemplate.callApi(JobInfo.class, HttpMethCd.DELETE, apiUri, input.getDeleteOptionInfo());
        JobInfo result = kubeTemplate.callApi(JobInfo.class, HttpMethCd.DELETE, apiUri);

        log.debug("Delete Job Result = [{}]", result);

        return result;
    }

    // Job 조회 - 특정 namespace 에 등록된 Job 목록 조회
    public KubeJobInquiryOutput inquiryNamespace(KubeInquiryInput input) throws Exception {
        String apiUri = stringUtil.makeFullPath(KubeDefine.JOB_NAMESPACE_URI, input.getNamespace());

        log.debug("Inquiry Namespace Job API URI = [{}]", apiUri);

        KubeJobInquiryOutput result = kubeTemplate.callApi(KubeJobInquiryOutput.class, HttpMethCd.GET, apiUri);

        log.debug("Inquiry Namespace Job Result = [{}]", result);

        return result;
    }

    // Job 조회 - 특정 namespace 에 등록된 특정 Job 정보 조회
    public JobInfo inquiryNamespaceJob(KubeInquiryInput input) throws Exception {
        String apiUri = stringUtil.makeFullPath(KubeDefine.JOB_NAMESPACE_NAME_URI, input.getNamespace(), input.getName());

        log.debug("Inquiry Job API URI = [{}]", apiUri);

        JobInfo result = kubeTemplate.callApi(JobInfo.class, HttpMethCd.GET, apiUri);

        log.debug("Inquiry Job Result = [{}]", result);

        return result;
    }

    // Job 조회 - 모든 namespace 에 등록된 Job 조회
    public KubeJobInquiryOutput inquiryAllJob() throws Exception {
        String apiUri = stringUtil.makeFullPath(KubeDefine.JOB_URI);

        log.debug("Inquiry All Job API URI = [{}]", apiUri);

        KubeJobInquiryOutput result = kubeTemplate.callApi(KubeJobInquiryOutput.class, HttpMethCd.GET, apiUri);

        log.debug("Inquiry All Job Result = [{}]", result);

        return result;
    }

    // Job 상태 조회
    public JobInfo inquiryJobStatus(KubeInquiryInput input) throws Exception {
        String apiUri = stringUtil.makeFullPath(KubeDefine.JOB_STATUS_URI, input.getNamespace(), input.getName());

        log.debug("Inquiry Job Status API URI = [{}]", apiUri);

        JobInfo result = kubeTemplate.callApi(JobInfo.class, HttpMethCd.GET, apiUri);

        log.debug("Inquiry Job Status Result = [{}]", result);

        return result;
    }
}