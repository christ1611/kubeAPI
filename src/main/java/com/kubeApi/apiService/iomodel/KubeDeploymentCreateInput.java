package com.kubeApi.apiService.iomodel;

import io.fabric8.kubernetes.api.model.IntOrString;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static com.kubeApi.apiService.define.KubeDefine.DEFAULT_NAMESPACE;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class KubeDeploymentCreateInput implements Serializable {

    private String namespace = DEFAULT_NAMESPACE;

    private String appName;     // Batch 이름. Pod 생성하여 어떤 batch 를 실행할지 설정 (Harbor 에 등록된 이미지 파일 명칭)
    private String appVersion;

    private String userId;      // 수행 요청한 userId
    private Map<String, Object> parameter = new HashMap<>();

    private Long replica = 1L;  // Pod 개수
    // TODO : Define 에 정의 (RollingUpdate, Recreate)
    private String strategy = "RollingUpdate";    // Pod 를 업데이트하는 옵션
                                                  //  - RollingUpdate : Pod 를 1개씩 업데이트
                                                  //  - Recreate : 기존 Pod 모두 삭제 후 새로운 Pod 생성
    private IntOrString maxSurge;                 // 업데이트 중 새로 띄울 수 있는 최대 Pod 수
    private IntOrString maxUnavailable;           // 업데이트 중 동시에 종료될 수 있는 최대 Pod 수

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}