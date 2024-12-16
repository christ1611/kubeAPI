package com.kubeApi.apiService.iomodel;

import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

import static com.kubeApi.apiService.define.KubeDefine.DEFAULT_NAMESPACE;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class KubeDeleteInput implements Serializable {

    private String name;        // input
    private String namespace = DEFAULT_NAMESPACE;   // input
    private String serverAddr;

    /** 삭제옵션 */
    /** DeleteOptionInfo 를 직접 받지 않고 필요한 변수로 받아서 세팅하는 것으로 변경 - 필요 시 추가 */
//    private DeleteOptionInfo deleteOptionInfo = new DeleteOptionInfo();    // body parameter (optional)

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}