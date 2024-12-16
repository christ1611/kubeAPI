package com.kubeApi.jdbc.repository;

import com.kubeApi.core.data.util.DaoCommonExt;
import com.kubeApi.core.data.util.model.PagingResult;
import com.kubeApi.jdbc.model.AppInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DaoAppInfo {

    private final DaoCommonExt daoCommonExt;

    public AppInfo findById(String appId) throws Exception {
        String sql =
                """
                 SELECT *
                   FROM APP_INFO
                  WHERE APP_ID = :appId
                """;

        HashMap<String, Object> param = new HashMap<>();
        param.put("appId", appId);

        AppInfo appInfo = daoCommonExt.findByDynamic(sql, param, AppInfo.class);

        log.debug("[findById] APP_INFO = [{}]", appInfo);

        return appInfo;
    }

    public AppInfo findByName(String appName, String appStCd) throws Exception {
        String sql =
                """
                 SELECT *
                   FROM APP_INFO
                  WHERE APP_NM = :appName
                    AND APP_ST_CD = :appStCd
                """;

        HashMap<String, Object> param = new HashMap<>();
        param.put("appName", appName);
        param.put("appStCd", appStCd);

        AppInfo appInfo = daoCommonExt.findByDynamic(sql, param, AppInfo.class);

        log.debug("[findByName] APP_INFO = [{}]", appInfo);

        return appInfo;
    }

    public PagingResult<AppInfo> getAppInfoPage(String appId, String appStCd, String appInfo, String appCategory,
                                                long pageSize, long pageNo)throws Exception {
        String sql =
                """
                 SELECT *
                   FROM APP_INFO
                  WHERE (COALESCE(:appId) = '' OR APP_ID = :appId)
                    and (COALESCE(:appStCd) = '' OR APP_ST_CD = :appStCd)
                    and (COALESCE(:appInfo) = '' OR APP_INFO = :appInfo)
                    and (COALESCE(:appCategory) = '' OR APP_CATEGORY = :appCategory)
                """;

        HashMap<String, Object> param = new HashMap<>();
        param.put("appId", appId);
        param.put("appStCd", appStCd);
        param.put("appInfo", appInfo);
        param.put("appCategory", appCategory);

        return daoCommonExt.findByDynamicPaging(sql, param, AppInfo.class, pageSize, pageNo);
    }

    public void insert(AppInfo appInfo) throws Exception {
        String sql =
                """
                 INSERT INTO APP_INFO
                    (APP_ID, APP_NM, APP_ST_CD, APP_INFO, APP_DV_CD, APP_CATEGORY, SERVER_ID, REG_USER_ID, SYS_REG_DTM)
                  VALUES(:appId, :appNm, :appStCd, :appInfo, :appDvCd, :appCategory, :serverId, :regUserId, :sysRegDtm);
                """;

        HashMap<String, Object> param = new HashMap<>();
        param.put("appId", appInfo.getAppId());
        param.put("appNm", appInfo.getAppNm());
        param.put("appStCd", appInfo.getAppStCd());
        param.put("appInfo", appInfo.getAppInfo());
        param.put("appDvCd", appInfo.getAppDvCd());
        param.put("appCategory", appInfo.getAppCategory());
        param.put("serverId", appInfo.getServerId());
        param.put("regUserId", appInfo.getRegUserId());
        param.put("sysRegDtm", appInfo.getSysRegDtm());

        daoCommonExt.executeSql(sql, param);
    }

    public void update(AppInfo appInfo) throws Exception {
        String sql =
                """
                 UPDATE APP_INFO
                    SET APP_ST_CD = :appStCd
                      , APP_INFO = :appInfo
                      , UPD_USER_ID = :updUserId
                      , SYS_UPD_DT = :sysUpdDt
                      , LST_EXEC_DT = :lstExecDt
                  WHERE APP_ID = :appId
                """;

        HashMap<String, Object> param = new HashMap<>();
        param.put("appId", appInfo.getAppId());
        param.put("appStCd", appInfo.getAppStCd());
        param.put("appInfo", appInfo.getAppInfo());
        param.put("updUserId", appInfo.getUpdUserId());
        param.put("sysUpdDt", appInfo.getSysUpdDtm());
        param.put("lstExecDt", appInfo.getLstExecDt());

        daoCommonExt.executeSql(sql, param);
    }
}
