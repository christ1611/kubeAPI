package com.kubeApi.apiService.iomodel;

import com.kubeApi.apiService.define.KubeDefine;
import io.fabric8.kubernetes.api.model.IntOrString;
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
public class KubeDeploymentUpdateInput implements Serializable {

    private String namespace = KubeDefine.DEFAULT_NAMESPACE;
    private String appName;        // Deployment 이름
    private String userId;      // 수행 요청한 userId

    /** 설정을 변경할 값 세팅 - 추후 더 필요한 항목 추가 */
    /** 이미지 관련 - 버전 및 파라미터 */
    private String appVersion;  // 이미지 버전
    private Map<String, Object> parameter = new HashMap<>();

    /** Pod 개수 */
    private Long replica = 0L;

    /** 업데이트 전략 변경 */
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