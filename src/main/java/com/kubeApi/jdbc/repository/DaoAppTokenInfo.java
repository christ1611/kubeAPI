package com.kubeApi.jdbc.repository;

import com.kubeApi.core.data.util.DaoCommonExt;
import com.kubeApi.jdbc.model.AppTokenInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DaoAppTokenInfo {

    private final DaoCommonExt daoCommonExt;

    public AppTokenInfo findById(String namespace, String serviceAccount) throws Exception {
        String sql =
                """
                 SELECT *
                   FROM APP_TOKEN_INFO
                  WHERE POD_NAMESPACE = :podNamespace
                    AND SERVICE_ACCT = :serviceAcct
                """;

        HashMap<String, Object> param = new HashMap<>();
        param.put("podNamespace", namespace);
        param.put("serviceAcct", serviceAccount);

        AppTokenInfo appTokenInfo = daoCommonExt.findByDynamic(sql, param, AppTokenInfo.class);

        log.debug("AppTokenInfo = [{}]", appTokenInfo.toString());

        return appTokenInfo;
    }

    public void insert(AppTokenInfo appTokenInfo) throws Exception {
        String sql =
                """
                 INSERT INTO APP_TOKEN_INFO
                    (pod_namespace, service_acct, token_info, token_created_dt, token_expi_dt)
                  VALUES(:podNamespace, :serviceAcct, :tokenInfo, :tokenCreatedDt, :tokenExpiDt)
                """;

        HashMap<String, Object> param = new HashMap<>();
        param.put("podNamespace", appTokenInfo.getPodNamespace());
        param.put("serviceAcct", appTokenInfo.getServiceAcct());
        param.put("tokenInfo", appTokenInfo.getTokenInfo());
        param.put("tokenCreatedDt", appTokenInfo.getTokenCreatedDt());
        param.put("tokenExpiDt", appTokenInfo.getTokenExpiDt());

        daoCommonExt.executeSql(sql, param);
    }

    public void update(AppTokenInfo appTokenInfo) throws Exception {
        String sql =
                """
                 UPDATE APP_TOKEN_INFO
                    SET TOKEN_INFO = :tokenInfo
                      , TOKEN_CREATED_DT = :tokenCreatedDt
                      , TOKEN_EXPI_DT = :tokenExpiDt
                  WHERE POD_NAMESPACE = :podNamespace
                    AND SERVICE_ACCT = :serviceAcct
                """;

        HashMap<String, Object> param = new HashMap<>();
        param.put("podNamespace", appTokenInfo.getPodNamespace());
        param.put("serviceAcct", appTokenInfo.getServiceAcct());
        param.put("tokenInfo", appTokenInfo.getTokenInfo());
        param.put("tokenCreatedDt", appTokenInfo.getTokenCreatedDt());
        param.put("tokenExpiDt", appTokenInfo.getTokenExpiDt());

        daoCommonExt.executeSql(sql, param);
    }
}
