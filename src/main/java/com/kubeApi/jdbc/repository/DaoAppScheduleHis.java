package com.kubeApi.jdbc.repository;

import com.kubeApi.core.data.util.DaoCommonExt;
import com.kubeApi.jdbc.model.AppScheduleHis;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DaoAppScheduleHis {

    private final DaoCommonExt daoCommonExt;

    public AppScheduleHis findById(String appId, String seq) throws Exception {
        String sql =
                """
                 SELECT *
                   FROM APP_SCHEDULE_HIS
                  WHERE SCH_ID = :schId
                    AND SEQ = :seq
                """;

        HashMap<String, Object> param = new HashMap<>();
        param.put("schId", appId);
        param.put("seq", seq);

        AppScheduleHis appScheduleHis = daoCommonExt.findByDynamic(sql, param, AppScheduleHis.class);

        log.debug("AppScheduleHis = [{}]", appScheduleHis);

        return appScheduleHis;
    }

    public List<AppScheduleHis> findByAppIdExecStCd(String appId, String execStCd) throws Exception {
        String sql =
                """
                 SELECT *
                   FROM APP_SCHEDULE_HIS
                  WHERE APP_ID = :appId
                    AND EXEC_ST_CD = :execStCd
                """;

        HashMap<String, Object> param = new HashMap<>();
        param.put("appId", appId);
        param.put("execStCd", execStCd);

        List<AppScheduleHis> appScheduleHis = daoCommonExt.findByDynamicList(sql, param, AppScheduleHis.class);

        log.debug("AppScheduleHis = [{}]", appScheduleHis);

        return appScheduleHis;
    }

    public List<AppScheduleHis> findBySchIdExecStCd(String schId, String execStCd)throws Exception {
        String sql =
                """
                 SELECT *
                   FROM APP_SCHEDULE_HIS
                  WHERE  SCH_ID = :schId
                    and  EXEC_ST_CD = :execStCd
                """;

        HashMap<String, Object> param = new HashMap<>();
        param.put("schId", schId);
        param.put("execStCd", execStCd);

        return daoCommonExt.findByDynamicList(sql, param, AppScheduleHis.class);
    }

    public void insert(AppScheduleHis appScheduleHis) throws Exception {
        String sql =
                """
                 INSERT INTO app_schedule_his
                         (sch_id, seq, reg_user_id, sys_reg_dtm, app_id, server_id, exec_user_id, exec_dt, exec_tm, exec_st_cd, exec_ver, exec_param, error_code, job_execution_id)
                VALUES(:schId, :seq, :regUserId, :sysRegDtm, :appId, :serverId, :execUserId, :execDt, :execTm, :execStCd, :execVer, :execParam, :errorCode,
                         :jobExecutionId)
                """;

        HashMap<String, Object> param = new HashMap<>();
        param.put("schId", appScheduleHis.getSchId());
        param.put("seq", appScheduleHis.getSeq());
        param.put("appId", appScheduleHis.getAppId());
        param.put("serverId", appScheduleHis.getServerId());
        param.put("execUserId", appScheduleHis.getExecUserId());
        param.put("execDt", appScheduleHis.getExecDt());
        param.put("execTm", appScheduleHis.getExecTm());
        param.put("execStCd", appScheduleHis.getExecStCd());
        param.put("errorCode", appScheduleHis.getErrorCode());
        param.put("execVer", appScheduleHis.getExecVer());
        param.put("execParam", appScheduleHis.getExecParam());
        param.put("regUserId", appScheduleHis.getRegUserId());
        param.put("sysRegDtm", appScheduleHis.getSysRegDtm());
        param.put("jobExecutionId", appScheduleHis.getJobExecutionId());
        daoCommonExt.immediateCommit(sql, param);
    }

    public void update(AppScheduleHis appScheduleHis) throws Exception {
        String sql =
                """
                 UPDATE APP_SCHEDULE_HIS
                    SET SERVER_ID = :serverId
                      , EXEC_USER_ID = :execUserId
                      , EXEC_DT = :execDt
                      , EXEC_TM = :execTm
                      , EXEC_ST_CD = :execStCd
                      , ERROR_CODE = :errorCode
                      , UPD_USER_ID = :updUserId
                      , SYS_UPD_DTM = :sysUpdDtm
                  WHERE APP_ID = :appId
                    AND SEQ = :seq
                """;

        HashMap<String, Object> param = new HashMap<>();
        param.put("appId", appScheduleHis.getAppId());
        param.put("seq", appScheduleHis.getSeq());
        param.put("serverId", appScheduleHis.getServerId());
        param.put("execUserId", appScheduleHis.getExecUserId());
        param.put("execDt", appScheduleHis.getExecDt());
        param.put("execTm", appScheduleHis.getExecTm());
        param.put("execStCd", appScheduleHis.getExecStCd());
        param.put("errorCode", appScheduleHis.getErrorCode());
        param.put("updUserId", appScheduleHis.getUpdUserId());
        param.put("sysUpdDtm", appScheduleHis.getSysUpdDtm());

        daoCommonExt.immediateCommit(sql, param);
    }
}
