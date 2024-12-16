package com.kubeApi.jdbc.repository;

import com.kubeApi.jdbc.model.AppVer;
import com.kubeApi.core.data.util.DaoCommonExt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DaoAppVer {

    private final DaoCommonExt daoCommonExt;

    public AppVer findById(String appId, String appVersion) throws Exception {
        String sql =
                """
                 SELECT *
                   FROM APP_VER
                  WHERE APP_ID = :appId
                    AND APP_VER = :appVer
                """;

        HashMap<String, Object> param = new HashMap<>();
        param.put("appId", appId);
        param.put("appVer", appVersion);

        return daoCommonExt.findByDynamic(sql, param, AppVer.class);
    }

    public AppVer findDef(String appId) throws Exception {
        String sql =
                """
                 SELECT *
                   FROM APP_VER
                  WHERE APP_ID = :appId
                    AND DEFAULT_YN ='Y'
                """;

        HashMap<String, Object> param = new HashMap<>();
        param.put("appId", appId);

        return daoCommonExt.findByDynamic(sql, param, AppVer.class);
    }

    public void updateDefaultVer(String appId, String updUserId) throws Exception {
        String sql =
                """
                  UPDATE APP_VER
                    SET DEFAULT_YN = 'N',
                        UPD_USER_ID = :updUserId,
                        SYS_UPD_DTM = :sysUpdDtm
                  WHERE APP_ID = :appId
                    AND DEFAULT_YN ='Y'
                """;

        HashMap<String, Object> param = new HashMap<>();
        param.put("appId", appId);
        param.put("updUserId", updUserId);
        param.put("sysUpdDtm", LocalDateTime.now());

        daoCommonExt.executeSql(sql, param);

    }
    public void insert(AppVer appVer) throws Exception {
        String sql =
                """
                 INSERT INTO APP_VER
                    (APP_ID, APP_VER, CREATED_DT, DEFAULT_YN, IMAGE_URI, REG_USER_ID, SYS_REG_DTM)
                  VALUES (:appId, :appVer, :createdDt, :defaultYn, :imageUri, :regUserId, :sysRegDtm)
                """;

        HashMap<String, Object> param = new HashMap<>();
        param.put("appId",      appVer.getAppId());
        param.put("appVer",     appVer.getAppVer());
        param.put("createdDt",  appVer.getCreatedDt());
        param.put("defaultYn",  appVer.getDefaultYn());
        param.put("imageUri",   appVer.getImageUri());
        param.put("regUserId",  appVer.getRegUserId());
        param.put("sysRegDtm",  appVer.getSysRegDtm());
        daoCommonExt.executeSql(sql, param);
    }
}
