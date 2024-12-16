package com.kubeApi.apiService.iomodel;

import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.kubernetes.client.openapi.models.V1StatusDetails;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class DeploymentDeleteInfo implements Serializable{
    private String apiVersion;
    private String kind;
    private String status;
    private ObjectMeta metadata = new ObjectMeta();
    private V1StatusDetails details = new V1StatusDetails();

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
