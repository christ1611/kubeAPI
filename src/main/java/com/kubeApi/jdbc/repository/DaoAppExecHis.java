package com.kubeApi.jdbc.repository;

import com.kubeApi.core.data.util.DaoCommonExt;
import com.kubeApi.core.data.util.model.PagingResult;
import com.kubeApi.jdbc.model.AppExecHis;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DaoAppExecHis {

    private final DaoCommonExt daoCommonExt;

    public AppExecHis findById(String appId, String seq) throws Exception {
        String sql =
                """
                 SELECT *
                   FROM APP_EXEC_HIS
                  WHERE APP_ID = :appId
                    AND SEQ = :seq
                """;

        HashMap<String, Object> param = new HashMap<>();
        param.put("appId", appId);
        param.put("seq", seq);

        AppExecHis appExecHis = daoCommonExt.findByDynamic(sql, param, AppExecHis.class);

        log.debug("AppExecHis = [{}]", appExecHis);

        return appExecHis;
    }

    public PagingResult<AppExecHis> getAppExecHisPage(String appId, String appStCd, String appInfo, String appCategory, long pageSize, long pageNo)throws Exception {
        String sql =
                """
                 SELECT *
                   FROM APP_EXEC_HIS
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

        return daoCommonExt.findByDynamicPaging(sql, param, AppExecHis.class, pageSize, pageNo);
    }

    public void insert(AppExecHis appExecHis) throws Exception {
        String sql =
                """
                 INSERT INTO APP_EXEC_HIS
                    (APP_ID, SEQ, SERVER_ID, EXEC_USER_ID, EXEC_DT, EXEC_TM, EXEC_ST_CD, EXEC_VER, EXEC_PARAM, ERROR_CODE,
                     JOB_EXECUTION_ID, REG_USER_ID, SYS_REG_DTM)
                  VALUES(:appId, :seq, :serverId, :execUserId, :execDt, :execTm, :execStCd, :errorCode, :execVer, :execParam,
                         :regUserId, :sysRegDtm)
                """;

        HashMap<String, Object> param = new HashMap<>();
        param.put("appId", appExecHis.getAppId());
        param.put("seq", appExecHis.getSeq());
        param.put("serverId", appExecHis.getServerId());
        param.put("execUserId", appExecHis.getExecUserId());
        param.put("execDt", appExecHis.getExecDt());
        param.put("execTm", appExecHis.getExecTm());
        param.put("execStCd", appExecHis.getExecStCd());
        param.put("errorCode", appExecHis.getErrorCode());
        param.put("execVer", appExecHis.getExecVer());
        param.put("execParam", appExecHis.getExecParam());
        param.put("regUserId", appExecHis.getRegUserId());
        param.put("sysRegDtm", appExecHis.getSysRegDtm());

        daoCommonExt.immediateCommit(sql, param);
    }

    public void update(AppExecHis appExecHis) throws Exception {
        String sql =
                """
                 UPDATE APP_EXEC_HIS
                    SET SERVER_ID = :serverId
                      , EXEC_USER_ID = :execUserId
                      , EXEC_DT = :execDt
                      , EXEC_TM = :execTm
                      , EXEC_ST_CD = :execStCd
                      , ERROR_CODE = :errorCode
                      , POD_NAME = :podName
                      , UPD_USER_ID = :updUserId
                      , SYS_UPD_DTM = :sysUpdDtm
                  WHERE APP_ID = :appId
                    AND SEQ = :seq
                """;

        HashMap<String, Object> param = new HashMap<>();
        param.put("appId", appExecHis.getAppId());
        param.put("seq", appExecHis.getSeq());
        param.put("serverId", appExecHis.getServerId());
        param.put("execUserId", appExecHis.getExecUserId());
        param.put("execDt", appExecHis.getExecDt());
        param.put("execTm", appExecHis.getExecTm());
        param.put("execStCd", appExecHis.getExecStCd());
        param.put("podName", appExecHis.getPodName());
        param.put("errorCode", appExecHis.getErrorCode());
        param.put("updUserId", appExecHis.getUpdUserId());
        param.put("sysUpdDtm", appExecHis.getSysUpdDtm());

        daoCommonExt.immediateCommit(sql, param);
    }

    public List<AppExecHis> getAppExecHis(String appId) throws Exception {
        String sql =
                """
                 SELECT *
                  FROM APP_EXEC_HIS
                 WHERE APP_ID = :appId
                """;

        HashMap<String, Object> param = new HashMap<>();
        param.put("appId", appId);

        return daoCommonExt.findByDynamicList(sql, param, AppExecHis.class);
    }
}
