package com.kubeApi.apiService.iomodel;

import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.ServiceStatus;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class RouteInfo implements Serializable {
    private String apiVersion;
    private String kind;
    private ObjectMeta metadata = new ObjectMeta();
    private RouteSpec spec = new RouteSpec();
    private ServiceStatus status = new ServiceStatus();


    @Getter @Setter
    public static class RouteSpec{
        private Map<String,String> to;
       // private String host;
        private Map<String, Integer> port;
    }
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
