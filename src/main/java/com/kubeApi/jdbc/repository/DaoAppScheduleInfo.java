package com.kubeApi.jdbc.repository;

import com.kubeApi.core.data.util.DaoCommonExt;
import com.kubeApi.jdbc.model.AppScheduleInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DaoAppScheduleInfo {

    private final DaoCommonExt daoCommonExt;

    public AppScheduleInfo findScheduleByTrigger(String triggerNm, String schStCd) throws Exception {
        String sql =
                """
                SELECT *
                FROM APP_SCHEDULE_INFO
                WHERE TRIGGER_NM = :triggerNm
                    AND SCH_ST_CD = :schStCd
                """;

        HashMap<String, Object> param = new HashMap<>();
        param.put("triggerNm", triggerNm);
        param.put("schStCd", schStCd);
        AppScheduleInfo appScheduleInfo = daoCommonExt.findByDynamic(sql, param, AppScheduleInfo.class);

        log.debug("[findById] APP_INFO = [{}]", appScheduleInfo);

        return appScheduleInfo;
    }

    public List<AppScheduleInfo> findScheduleByAppId(String appId) throws Exception {
        String sql =
                """
                SELECT *
                FROM APP_SCHEDULE_INFO
                WHERE APP_ID = :appId
                """;

        HashMap<String, Object> param = new HashMap<>();
        param.put("appId", appId);
        List<AppScheduleInfo> appScheduleInfo = daoCommonExt.findByDynamicList(sql, param, AppScheduleInfo.class);

        log.debug("[findById] APP_INFO = [{}]", appScheduleInfo);

        return appScheduleInfo;
    }

}
