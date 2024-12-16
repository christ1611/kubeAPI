package com.kubeApi.core.iomodel;

import io.fabric8.kubernetes.api.model.authentication.TokenReviewSpec;
import io.fabric8.kubernetes.api.model.authentication.TokenReviewStatus;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TokenReview implements Serializable {

    private String apiVersion;
    private String kind;
    private TokenReviewSpec spec = new TokenReviewSpec();
    private TokenReviewStatus status = new TokenReviewStatus();

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}