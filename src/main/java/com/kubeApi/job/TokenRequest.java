package com.kubeApi.job;

import com.kubeApi.core.define.CoreDefine;
import com.kubeApi.core.module.KubeTemplate;
import com.kubeApi.core.util.StringUtil;
import com.kubeApi.jdbc.model.AppTokenInfo;
import com.kubeApi.jdbc.repository.DaoAppTokenInfo;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.kubeApi.apiService.define.KubeDefine.TOKEN_URI;

/**
 * this is a job to regenerate kube token every hour
 * the duration of the token generate need to be set using quartz controller -> saveJobTrigger
 * note: in application-local.yml, we set the namespace as kube and user as deployer, therefore, please insert a entity to save the token in app_token_info
 */
@Slf4j
@Component
@DisallowConcurrentExecution
public class TokenRequest implements Job {

    @Autowired
    private KubeTemplate kubeTemplate;

    @Autowired
    private StringUtil stringUtil;

    @Autowired
    private DaoAppTokenInfo daoToken;

    public synchronized void tokenRequest() {
        try {
            String apiUri = stringUtil.makeFullPath(TOKEN_URI,
                    kubeTemplate.getKUBE_NAMESPACE(), kubeTemplate.getKUBE_SERVICE_ACCOUNT());

            com.kubeApi.core.quartz.iomodel.TokenRequest tokenRequest = new com.kubeApi.core.quartz.iomodel.TokenRequest();
            tokenRequest.setApiVersion("authentication.k8s.io/v1");
            tokenRequest.setKind("TokenRequest");
            tokenRequest.getSpec().getAudiences().add("https://kubernetes.default.svc");
            tokenRequest.getSpec().setExpirationSeconds(3900L);

            com.kubeApi.core.quartz.iomodel.TokenRequest result = kubeTemplate.callApi(com.kubeApi.core.quartz.iomodel.TokenRequest.class, CoreDefine.HttpMethCd.POST, apiUri, tokenRequest);
            log.debug("Result = [{}]", result.toString());

            AppTokenInfo appTokenInfoExt = new AppTokenInfo();
            appTokenInfoExt.setPodNamespace(kubeTemplate.getKUBE_NAMESPACE());
            appTokenInfoExt.setServiceAcct(kubeTemplate.getKUBE_SERVICE_ACCOUNT());
            appTokenInfoExt.setTokenInfo(result.getStatus().getToken());
            appTokenInfoExt.setTokenCreatedDt(LocalDateTime.parse(
                    result.getMetadata().getCreationTimestamp(), DateTimeFormatter.ISO_OFFSET_DATE_TIME));
            appTokenInfoExt.setTokenExpiDt(LocalDateTime.parse(
                    result.getStatus().getExpirationTimestamp(), DateTimeFormatter.ISO_OFFSET_DATE_TIME));

            daoToken.update(appTokenInfoExt);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        tokenRequest();
    }
}
