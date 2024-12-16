package com.kubeApi.jdbc.repository;

import com.kubeApi.core.data.util.DaoCommonExt;
import com.kubeApi.jdbc.model.BatchJobExecution;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DaoBatchJobExecution {

    private final DaoCommonExt daoCommonExt;

    public BatchJobExecution findById(Long jobExecutionId) throws Exception {
        String sql =
                """
                 SELECT *
                   FROM BATCH_JOB_EXECUTION
                  WHERE JOB_EXECUTION_ID = :jobExecutionId
                """;

        HashMap<String, Object> param = new HashMap<>();
        param.put("jobExecutionId", jobExecutionId);

        BatchJobExecution batchJobExecution = daoCommonExt.findByDynamic(sql, param, BatchJobExecution.class);

        log.debug("BatchJobExecution = [{}]", batchJobExecution);

        return batchJobExecution;
    }

    public void updateStatus(BatchJobExecution batchJobExecution) throws Exception {
        String sql =
                """
                 UPDATE BATCH_JOB_EXECUTION
                    SET STATUS = :status
                      , EXIT_CODE = :exitCode
                      , EXIT_MESSAGE = :exitMessage
                      , END_TIME = :endTime
                      , LAST_UPDATED = :lastUpdated
                  WHERE JOB_EXECUTION_ID = :jobExecutionId
                """;

        Map<String, Object> param = new HashMap<>();
        param.put("jobExecutionId", batchJobExecution.getJobExecutionId());
        param.put("status", batchJobExecution.getStatus());
        param.put("exitCode", batchJobExecution.getExitCode());
        param.put("exitMessage", batchJobExecution.getExitMessage());
        param.put("endTime", batchJobExecution.getEndTime());
        param.put("lastUpdated", batchJobExecution.getLastUpdated());

        daoCommonExt.immediateCommit(sql, param);
    }
}
