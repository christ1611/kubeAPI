package com.kubeApi.apiService.iomodel;

import com.kubeApi.apiService.define.KubeDefine;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class KubeJobCreateInput implements Serializable {

    private String namespace = KubeDefine.DEFAULT_NAMESPACE;

    /** Input 으로 받지 않고 Batman 에서 세팅하는 것으로 변경 */
//    private JobInfo jobInfo = new JobInfo();    // body parameter

    private String appName;     // Batch 이름. Pod 생성하여 어떤 batch 를 실행할지 설정 (Harbor 에 등록된 이미지 파일 명칭)
    private String appVersion;

    private String userId;      // 수행 요청한 userId
    private Map<String, Object> parameter = new HashMap<>();

//    private Long replica = 0L;  // Pod 개수
    private Long parallelism = 1L;  // 동시에 실행되는 Pod 개수
    private Long completions = 1L;  // 수행할 작업 개수
    private Long backOffLimit = 0L; // 실패한 Job 의 최대 재시도 횟수
    private Long terminationGracePeriodSeconds = 60L; // 어플리케이션 종료 작업을 수행할 시간
    private Long ttlSecondsAfterFinished = 3600L;   // Job 및 Pod 완료 후 일정시간 지난 후 삭제하는 옵션 (초)
                                                    //  - 0 으로 설정하는 경우 무한 유지

    /** 배치 수행 후 APP_EXEC_HIS 테이블 관리할 때 필요한 데이터 */
    private String containerName;           // OKD 에 생성된 Job(Pod) 이름
    private String scheduleExecYn = "N";    // Schedule 실행 여부
    private String appId;
    private String appHisSeq;
    private String serverAddr;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}