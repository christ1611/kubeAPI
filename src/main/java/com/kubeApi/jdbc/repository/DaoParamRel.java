package com.kubeApi.jdbc.repository;

import com.kubeApi.core.data.util.DaoCommonExt;
import com.kubeApi.jdbc.model.ParamRel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DaoParamRel {

    private final DaoCommonExt daoCommonExt;

    public ParamRel findById(String appId, String paramNm) throws Exception {
        String sql =
                """
                 SELECT *
                   FROM PARAM_REL
                  WHERE APP_ID = :appId
                    AND PARAM_NM = :paramNm
                """;

        HashMap<String, Object> param = new HashMap<>();
        param.put("appId", appId);
        param.put("paramNm", paramNm);

        ParamRel paramRel = daoCommonExt.findByDynamic(sql, param, ParamRel.class);

        log.debug("PARAM_REL = [{}]", paramRel);

        return paramRel;
    }

    public void insert(ParamRel paramRel) throws Exception {
        String sql =
                """
                 INSERT PARAM_REL
                    (APP_ID, PARAM_NM, TYPE, ACTIVE_CD, MANDATORY_CD, DEFAULT_SETTING, REG_USER_ID, SYS_REG_DTM)
                 VALUES(:appId, :paramNm, :type, :activeCd, :mandatoryCd, :defaultSetting, :sysUserId, :sysRegDtm);
                """;

        HashMap<String, Object> param = new HashMap<>();
        param.put("appId", paramRel.getAppId());
        param.put("paramNm", paramRel.getParamNm());
        param.put("type", paramRel.getType());
        param.put("activeCd", paramRel.getActiveCd());
        param.put("mandatoryCd", paramRel.getMandatoryCd());
        param.put("defaultSetting", paramRel.getDefaultSetting());
        param.put("sysUserId", paramRel.getUpdUserId());
        param.put("sysRegDtm", paramRel.getSysRegDtm());

        daoCommonExt.executeSql(sql, param);
    }

    public void update(ParamRel paramRel) throws Exception {
        String sql =
                """
                 UPDATE PARAM_REL
                    SET ACTIVE_CD = :activeCd
                      , MANDATORY_CD = :mandatoryCd
                      , DEFAULT_SETTING = :defaultSetting
                      , UPD_USER_ID = :updUserId
                      , SYS_UPD_DT = :sysUpdDt
                  WHERE APP_ID = :appId
                    AND PARAM_NM = :paramNm
                """;

        HashMap<String, Object> param = new HashMap<>();
        param.put("appId", paramRel.getAppId());
        param.put("paramNm", paramRel.getParamNm());
        param.put("activeCd", paramRel.getActiveCd());
        param.put("mandatoryCd", paramRel.getMandatoryCd());
        param.put("defaultSetting", paramRel.getDefaultSetting());
        param.put("updUserId", paramRel.getUpdUserId());
        param.put("sysUpdDt", paramRel.getSysUpdDtm());

        daoCommonExt.executeSql(sql, param);
    }
}
