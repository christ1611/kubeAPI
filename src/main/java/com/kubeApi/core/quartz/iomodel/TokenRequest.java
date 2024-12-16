package com.kubeApi.core.quartz.iomodel;

import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.authentication.TokenRequestSpec;
import io.fabric8.kubernetes.api.model.authentication.TokenRequestStatus;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TokenRequest implements Serializable {

    private String apiVersion;
    private String kind;
    private ObjectMeta metadata = new ObjectMeta();
    private TokenRequestSpec spec = new TokenRequestSpec();
    private TokenRequestStatus status = new TokenRequestStatus();

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}