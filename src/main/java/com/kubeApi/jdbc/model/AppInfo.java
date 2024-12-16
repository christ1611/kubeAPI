package com.kubeApi.jdbc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class AppInfo {
    private String          appId;                    // Application ID
    private String          appNm;                    // Application 명칭
    private String          appStCd;                  // Application 상태 (0: Active, 9: InActive)
    private String          appInfo;                  // Application 정보 (LongTerm, ShortTerm)
    private String          appDvCd;                  // Application 실행 구분 코드 (0: 독립적인 Application 으로 실행, 1: Daemon Server)
    private String          appCategory;              // Application 업무 구분 (Common, Deposit, Loan, Account)
    private String          serverId;                 // Server ID
    private LocalDateTime   lstExecDt;                // 마지막 실행 시간
    private Long            lstJobExecutionId = 0L;   // 마지막 실행한 JobExecutionId
    private String          regUserId;
    private LocalDateTime   sysRegDtm;
    private String          updUserId;
    private LocalDateTime   sysUpdDtm;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
