package com.kubeApi.jdbc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class AppScheduleHis {
    private String          schId;          // SequenceId
    private String          seq;            // Sequence
    private String          appId;          // Application ID
    private String          serverId;       // server ID
    private String          execUserId;     // 실행 User ID
    private LocalDate       execDt;         // 실행 일자
    private String          execTm;         // 실행 시간
    private String          execStCd;       // 실행 상태 코드
    private String          execVer;        // 실행 버전
    private String          execParam;      // 실행 파라미터
    private String          errorCode;      // 에러코드
    private Long            jobExecutionId = 0L;
    private String          podName;
    private String          regUserId;
    private LocalDateTime   sysRegDtm;
    private String          updUserId;
    private LocalDateTime   sysUpdDtm;

}
