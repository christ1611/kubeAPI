package com.kubeApi.apiService.iomodel;

import com.kubeApi.apiService.define.KubeDefine;
import io.fabric8.kubernetes.api.model.IntOrString;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class KubeServiceCreateInput implements Serializable {

    private String namespace = KubeDefine.DEFAULT_NAMESPACE;
    private String appName;
    private int port;
    private IntOrString targetPort;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}