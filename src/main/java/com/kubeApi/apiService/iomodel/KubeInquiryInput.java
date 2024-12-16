package com.kubeApi.apiService.iomodel;

import com.kubeApi.apiService.define.KubeDefine;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class KubeInquiryInput implements Serializable {

    private String name;        // input
    private String namespace = KubeDefine.DEFAULT_NAMESPACE;   // input
    private String labelSelector;
    private String serverAddr;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}