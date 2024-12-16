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
public class KubePodCreateInput implements Serializable {

    private String namespace = KubeDefine.DEFAULT_NAMESPACE;

    /** Input 으로 받지 않고 Batman 에서 세팅하는 것으로 변경 */
//    private PodInfo podInfo = new PodInfo();    // body parameter
    private String appName;     // Batch 이름. Pod 생성하여 어떤 batch 를 실행할지 설정 (Harbor 에 등록된 이미지 파일 명칭)
    private String appVersion;

    private String userId;      // 수행 요청한 userId
    private Map<String, Object> parameter = new HashMap<>();    // 배치 실행 시 필요한 파라미터

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}