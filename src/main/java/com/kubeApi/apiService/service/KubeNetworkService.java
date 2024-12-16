package com.kubeApi.apiService.service;

import com.kubeApi.apiService.define.KubeDefine;
import com.kubeApi.apiService.iomodel.*;
import com.kubeApi.apiService.module.KubeSetBody;
import com.kubeApi.core.define.CoreDefine.HttpMethCd;
import com.kubeApi.core.module.KubeTemplate;
import com.kubeApi.core.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KubeNetworkService {

    private final KubeTemplate kubeTemplate;
    private final StringUtil stringUtil;
    private final KubeSetBody kubeSetBody;

    public ServiceInfo createServices(KubeServiceCreateInput input) throws Exception {
        String apiUri = stringUtil.makeFullPath(KubeDefine.SERVICES_URI, input.getNamespace());

        log.debug("Create Service API URI = [{}]", apiUri);

        ServiceInfo result = kubeTemplate.callApi(ServiceInfo.class, HttpMethCd.POST, apiUri,
                kubeSetBody.setCreateServices(kubeTemplate.getKUBE_NAMESPACE(), input.getAppName(), input.getAppName(),  input.getPort(),input.getTargetPort()));

        log.debug("Create Deployment Result = [{}]", result);

        return result;
    }

    // Pod 상태 조회
    public ServiceInfo inquiryNetworkStatus(KubeInquiryInput input) throws Exception {
        String apiUri = stringUtil.makeFullPath(KubeDefine.SERVICES_STATUS_URI, input.getNamespace(), input.getName());

        log.debug("Inquiry network Status API URI = [{}]", apiUri);

        ServiceInfo result = kubeTemplate.callApi(ServiceInfo.class, HttpMethCd.GET, apiUri);

        log.debug("Inquiry network Status Result = [{}]", result);

        return result;
    }



    public ServiceInfo deleteService(KubeDeleteInput input) throws Exception {
        String apiUri = stringUtil.makeFullPath(KubeDefine.SERVICES_NAMESPACE_NAME_URI, input.getNamespace(), input.getName());

        log.debug("Delete network API URI = [{}]", apiUri);

        ServiceInfo result = kubeTemplate.callApi(ServiceInfo.class, HttpMethCd.DELETE, apiUri);

        log.debug("Delete network Result = [{}]", result);

        return result;
    }

    public ServiceInfo createRoute(KubeRouteCreateInput input) throws Exception {
        String apiUri = stringUtil.makeFullPath(KubeDefine.ROUTE_URI, input.getNamespace());

        log.debug("Create route API URI = [{}]", apiUri);

        ServiceInfo result = kubeTemplate.callApi(ServiceInfo.class, HttpMethCd.POST, apiUri,
                kubeSetBody.setCreateRoute(kubeTemplate.getKUBE_NAMESPACE(), input.getAppName(), input.getUri(),input.getTargetPort()));

        log.debug("Create route Result = [{}]", result);

        return result;
    }

    public ServiceInfo inquiryRouteStatus(KubeInquiryInput input) throws Exception {
        String apiUri = stringUtil.makeFullPath(KubeDefine.ROUTE_STATUS_URI, input.getNamespace(), input.getName());

        log.debug("Inquiry route Status API URI = [{}]", apiUri);

        ServiceInfo result = kubeTemplate.callApi(ServiceInfo.class, HttpMethCd.GET, apiUri);

        log.debug("Inquiry route Status Result = [{}]", result);

        return result;
    }

    public ServiceInfo deleteRoute(KubeDeleteInput input) throws Exception {
        String apiUri = stringUtil.makeFullPath(KubeDefine.ROUTE_NAMESPACE_NAME_URI, input.getNamespace(), input.getName());

        log.debug("Delete route API URI = [{}]", apiUri);

        ServiceInfo result = kubeTemplate.callApi(ServiceInfo.class, HttpMethCd.DELETE, apiUri);

        log.debug("Delete route Result = [{}]", result);

        return result;
    }
}