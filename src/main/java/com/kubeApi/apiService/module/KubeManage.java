package com.kubeApi.apiService.module;

import com.kubeApi.apiService.iomodel.*;
import com.kubeApi.core.define.CoreDefine;
import com.kubeApi.core.exception.ApiCallException;
import com.kubeApi.core.module.KubeTemplate;
import com.kubeApi.core.util.StringUtil;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.kubeApi.core.define.CoreDefine.*;
import static com.kubeApi.apiService.define.KubeDefine.JOB_NAMESPACE_NAME_URI;
import static com.kubeApi.apiService.define.KubeDefine.JOB_NAMESPACE_URI;

@Slf4j
@Component
@RequiredArgsConstructor
public class KubeManage {

    private final KubeTemplate kubeTemplate;
    private final StringUtil stringUtil;
    private final KubeSetBody kubeSetBody;

    // Job 생성 - 일회성
    public KubeJobCreateOutput createJob(KubeJobCreateInput input) throws Exception {
        String apiUri = stringUtil.makeFullPath(JOB_NAMESPACE_URI, input.getNamespace());

        log.debug("Create Job API URI = [{}]", apiUri);

        JobInfo result = new JobInfo();
        Map<String, Object> errorMessage = null;
        KubeJobCreateOutput output = new KubeJobCreateOutput();

        try {
            result = kubeTemplate.callApi(JobInfo.class, CoreDefine.HttpMethCd.POST, StringUtils.isEmpty(input.getServerAddr()) ? null : input.getServerAddr(), apiUri,
                    null, null, kubeSetBody.setCreateJob(kubeTemplate.getKUBE_NAMESPACE(), input.getAppName(), input.getAppVersion(),
                            input.getParameter(), input.getTerminationGracePeriodSeconds(), input.getTtlSecondsAfterFinished(), input.getParallelism(),
                            input.getCompletions(), input.getBackOffLimit(), input.getContainerName(), input.getScheduleExecYn(), input.getAppId(), input.getAppHisSeq()));
        } catch (ApiCallException e) {
            Map<String, Object> errorResponse = e.getResponseBodyMap();

            if(errorResponse != null && !errorResponse.isEmpty()) {
                Object errorsObject = errorResponse.get("errors");

                errorMessage = new HashMap<>();
                if(errorsObject == null) {
                    errorMessage.put("code", errorResponse.get("code"));
                    errorMessage.put("message", errorResponse.get("message"));
                }
                else {
                    if (errorsObject instanceof ArrayList) {
                        List<LinkedHashMap<String, Object>> errorsList = (ArrayList<LinkedHashMap<String, Object>>) errorsObject;

                        for (LinkedHashMap<String, Object> error : errorsList) {
                            errorMessage.putAll(error);
                        }
                    } else if (errorsObject instanceof LinkedHashMap) {
                        LinkedHashMap<String, Object> error = (LinkedHashMap<String, Object>) errorsObject;
                        errorMessage.putAll(error);
                    }
                }
            }
        }

        if(errorMessage != null && errorMessage.size() != 0) {
            String code = "";
            String message = "";

            if(errorMessage.get("code") != null) {
                code = errorMessage.get("code").toString();
            }
            if(errorMessage.get("message") != null) {
                message = errorMessage.get("message").toString();
            }

            log.error("Code = [{}], Message = [{}]", code, message);

            output.getErrors().put("code", code);
            output.getErrors().put("message", message);
            output.getErrors().put("result", result.toString());

            return output;
        }
        else if(result == null) {
            output.getErrors().put("code", "");
            output.getErrors().put("message", "Api Call Fail");

            return output;
        }

        log.debug("Create Job Result = [{}]", result);

        return output;
    }

    // Job 삭제 - Pod 도 즉시 삭제 처리
    public KubeDeleteOutput stopJob(KubeDeleteInput input) throws Exception {
       return stopJob(input, "");
    }

    public KubeDeleteOutput stopJob(KubeDeleteInput input, String labelSelector) throws Exception {
        String apiUri = "";
        if(StringUtils.isEmpty(labelSelector)) {
            apiUri = stringUtil.makeFullPath(JOB_NAMESPACE_NAME_URI, input.getNamespace(), input.getName());
        }
        else {
            apiUri = stringUtil.makeFullPath(JOB_NAMESPACE_URI, input.getNamespace()).concat("?");
            apiUri += stringUtil.makeFullPath("labelSelector=batchName=%s", labelSelector);
        }

        log.debug("Stop Job API URI = [{}]", apiUri);

        DeleteOptionInfo result = new DeleteOptionInfo();
        Map<String, Object> errorMessage = null;
        KubeDeleteOutput output = new KubeDeleteOutput();

        try {
            result = kubeTemplate.callApi(DeleteOptionInfo.class, CoreDefine.HttpMethCd.DELETE, input.getServerAddr(), apiUri,
                    null, null, kubeSetBody.setStopJob("Foreground", 60L));
        } catch (ApiCallException e) {
            Map<String, Object> errorResponse = e.getResponseBodyMap();

            if(errorResponse != null && !errorResponse.isEmpty()) {
                Object errorsObject = errorResponse.get("errors");

                errorMessage = new HashMap<>();
                if(errorsObject == null) {
                    errorMessage.put("code", errorResponse.get("code"));
                    errorMessage.put("message", errorResponse.get("message"));
                }
                else {
                    if (errorsObject instanceof ArrayList) {
                        List<LinkedHashMap<String, Object>> errorsList = (ArrayList<LinkedHashMap<String, Object>>) errorsObject;

                        for (LinkedHashMap<String, Object> error : errorsList) {
                            errorMessage.putAll(error);
                        }
                    } else if (errorsObject instanceof LinkedHashMap) {
                        LinkedHashMap<String, Object> error = (LinkedHashMap<String, Object>) errorsObject;
                        errorMessage.putAll(error);
                    }
                }
            }
        }

        if(errorMessage != null && errorMessage.size() != 0) {
            String code = "";
            String message = "";

            if(errorMessage.get("code") != null) {
                code = errorMessage.get("code").toString();
            }
            if(errorMessage.get("message") != null) {
                message = errorMessage.get("message").toString();
            }

            log.error("Code = [{}], Message = [{}]", code, message);

            output.getErrors().put("code", code);
            output.getErrors().put("message", message);
            output.getErrors().put("result", result.toString());

            return output;
        }
        else if(result == null) {
            output.getErrors().put("code", "");
            output.getErrors().put("message", "Api Call Fail");

            return output;
        }

        log.debug("Create Job Result = [{}]", result);

        return output;
    }

    // Job 조회 - labelSelector 이용
    public KubeInquiryOutput labelInquiryJob(KubeInquiryInput input) throws Exception {
        String apiUri = stringUtil.makeFullPath(JOB_NAMESPACE_URI, input.getNamespace()).concat("?");
        apiUri += stringUtil.makeFullPath("labelSelector=batchName=%s", input.getLabelSelector());

        log.debug("Label Inquiry Job API URI = [{}]", apiUri);

        KubeJobInquiryOutput result = new KubeJobInquiryOutput();
        Map<String, Object> errorMessage = null;
        KubeInquiryOutput output = new KubeInquiryOutput();

        try {
            result = kubeTemplate.callApi(KubeJobInquiryOutput.class, CoreDefine.HttpMethCd.GET, input.getServerAddr(), apiUri, null, null, null);
        } catch (ApiCallException e) {
            Map<String, Object> errorResponse = e.getResponseBodyMap();

            if(errorResponse != null && !errorResponse.isEmpty()) {
                Object errorsObject = errorResponse.get("errors");

                errorMessage = new HashMap<>();
                if(errorsObject == null) {
                    errorMessage.put("code", errorResponse.get("code"));
                    errorMessage.put("message", errorResponse.get("message"));
                }
                else {
                    if (errorsObject instanceof ArrayList) {
                        List<LinkedHashMap<String, Object>> errorsList = (ArrayList<LinkedHashMap<String, Object>>) errorsObject;

                        for (LinkedHashMap<String, Object> error : errorsList) {
                            errorMessage.putAll(error);
                        }
                    } else if (errorsObject instanceof LinkedHashMap) {
                        LinkedHashMap<String, Object> error = (LinkedHashMap<String, Object>) errorsObject;
                        errorMessage.putAll(error);
                    }
                }
            }
        }

        if(errorMessage != null && errorMessage.size() != 0) {
            String code = "";
            String message = "";

            if(errorMessage.get("code") != null) {
                code = errorMessage.get("code").toString();
            }
            if(errorMessage.get("message") != null) {
                message = errorMessage.get("message").toString();
            }

            log.error("Code = [{}], Message = [{}]", code, message);

            output.getErrors().put("code", code);
            output.getErrors().put("message", message);
            output.getErrors().put("result", result.toString());

            return output;
        }
        else if(result == null) {
            output.getErrors().put("code", "");
            output.getErrors().put("message", "Api Call Fail");

            return output;
        }

        List<String> podNames = new ArrayList<>();
        for(JobInfo jobInfo : result.getItems()) {
            ObjectMeta objectMeta = jobInfo.getMetadata();
            podNames.add(objectMeta.getName());
        }


        log.debug("Label Inquiry Job Result = [{}]", result);

        return output;
    }

    // Job 조회
    public Map<String, String> inquiryJob(KubeInquiryInput input) throws Exception {
        String apiUri = stringUtil.makeFullPath(JOB_NAMESPACE_NAME_URI, input.getNamespace(), input.getName());

        log.debug("Inquiry Job API URI = [{}]", apiUri);

        JobInfo result = new JobInfo();
        Map<String, Object> errorMessage = null;
        Map<String, String> output = new HashMap<>();

        try {
            result = kubeTemplate.callApi(JobInfo.class, HttpMethCd.GET, input.getServerAddr(), apiUri, null, null, null);
        } catch (ApiCallException e) {
            Map<String, Object> errorResponse = e.getResponseBodyMap();

            if(errorResponse != null && !errorResponse.isEmpty()) {
                Object errorsObject = errorResponse.get("errors");

                errorMessage = new HashMap<>();
                if(errorsObject == null) {
                    errorMessage.put("code", errorResponse.get("code"));
                    errorMessage.put("message", errorResponse.get("message"));
                }
                else {
                    if (errorsObject instanceof ArrayList) {
                        List<LinkedHashMap<String, Object>> errorsList = (ArrayList<LinkedHashMap<String, Object>>) errorsObject;

                        for (LinkedHashMap<String, Object> error : errorsList) {
                            errorMessage.putAll(error);
                        }
                    } else if (errorsObject instanceof LinkedHashMap) {
                        LinkedHashMap<String, Object> error = (LinkedHashMap<String, Object>) errorsObject;
                        errorMessage.putAll(error);
                    }
                }
            }
        }

        if(errorMessage != null && errorMessage.size() != 0) {
            String code = "";
            String message = "";

            if(errorMessage.get("code") != null) {
                code = errorMessage.get("code").toString();
            }
            if(errorMessage.get("message") != null) {
                message = errorMessage.get("message").toString();
            }

            log.error("Error Code = [{}], Error Message = [{}]", code, message);

            output.put("code", code);
            output.put("message", message);
            output.put("result", result.toString());

            return output;
        }
        else if(result == null) {
            output.put("code", "");
            output.put("message", "Api Call Fail");

            return output;
        }

        output.put("podName", result.getMetadata().getName());

        log.debug("Inquiry Job Result = [{}]", result);

        return output;
    }
}