package com.kubeApi.apiService.iomodel;

import io.fabric8.kubernetes.api.model.ListMeta;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class KubePodInquiryOutput implements Serializable{

    private String apiVersion;
    private String kind;
    private ListMeta metadata = new ListMeta();
    private List<PodInfo> items = new ArrayList<>();

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
