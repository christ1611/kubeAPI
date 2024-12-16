package com.kubeApi.apiService.iomodel;

import io.fabric8.kubernetes.api.model.*;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ServiceInfo implements Serializable {
    private String apiVersion;
    private String kind;
    private ObjectMeta metadata = new ObjectMeta();
    private ServiceSpec spec = new ServiceSpec();
    private ServiceStatus status = new ServiceStatus();

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
