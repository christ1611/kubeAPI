package com.kubeApi.jdbc.repository;

import com.kubeApi.core.data.util.DaoCommonExt;
import com.kubeApi.jdbc.model.BatchServerInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DaoBatchServerInfo {

    private final DaoCommonExt daoCommonExt;

    public BatchServerInfo getBatchServerInfoCategory(String appCategory, String status) throws Exception {
        String sql =
                """
                 SELECT *
                   FROM BATCH_SERVER_INFO
                  WHERE APP_CATEGORY = :appCategory
                    AND STATUS = :status
                """;

        HashMap<String, Object> param = new HashMap<>();
        param.put("appCategory", appCategory);
        param.put("status", status);

        BatchServerInfo batchServerInfo = daoCommonExt.findByDynamic(sql, param, BatchServerInfo.class);

        log.debug("BatchServerInfo = [{}]", batchServerInfo);

        return batchServerInfo;
    }

    public List<BatchServerInfo> getBatchServerInfoMultiCategory(List<String> appCategory, String status) throws Exception {
        String sql =
                """
                 SELECT *
                   FROM BATCH_SERVER_INFO
                  WHERE APP_CATEGORY IN (:appCategory)
                    AND STATUS = :status
                """;

        HashMap<String, Object> param = new HashMap<>();
        param.put("appCategory", appCategory);
        param.put("status", status);

        List<BatchServerInfo> batchServerInfos = daoCommonExt.findByDynamicList(sql, param, BatchServerInfo.class);

        log.debug("All BatchServerInfo = [{}]", batchServerInfos);

        return batchServerInfos;
    }
}
