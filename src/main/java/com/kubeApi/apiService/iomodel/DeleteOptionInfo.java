package com.kubeApi.apiService.iomodel;

import io.fabric8.kubernetes.api.model.Preconditions;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class DeleteOptionInfo implements Serializable{
    /** 설정시 오류 발생하여 주석처리 */
//    private String apiVersion;
//    private String kind;
//    private ObjectMeta metadata = new ObjectMeta();
    private String propagationPolicy;   // 종속 리소스 정의 (Orphan, Foreground, Background)
//    private boolean orphanDependents;   // propagationPolicy 로 대체되어 사용 안함
    private Long gracePeriodSeconds;    // 리소스가 삭제되기 전에 얼마나 오래 대기할지 초 단위로 설정
    private Preconditions preconditions = new Preconditions();  // 삭제 작업 시 특정 조건이 만족될 때 실행하도록 설정

    private List<String> dryRun = new ArrayList<>();

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
