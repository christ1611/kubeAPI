package com.kubeApi.core.iomodel;

import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class QueueInfo implements Serializable{
    /** BATCH_CALL_TARGET 테이블 정보 */
    private String queueId;
    private String taskName;
    private String requestDtm;
    private String executeDtm;
    private String lastPollingDtm;
    private String pollingMinutes;
    private String queueStatus;
    private String callTargetNm;
    private String parentQueueId;
    private String dupCheckCd;
    private String dupCheckParam;
    private Long taskExecutionId;
    private Long jobExecutionId;
    private String userId;
    private String globalId;
    private String relaunchExecYn;
    private Long retryCnt;
    private Long retryExecCnt;

    /** BOOT3_TASK_EXECUTION 테이블 정보 */
    private Long exitCode;
    private String taskParam;
    private String startTime;
    private String endTime;
    private String duration;
    private String status;
    private String schemaTarget;

    private String scdfURL;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
