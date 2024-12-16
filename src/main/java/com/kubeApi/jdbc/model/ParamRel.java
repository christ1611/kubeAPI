package com.kubeApi.jdbc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class ParamRel {

    private String appId;
    private String paramNm;
    private String type;
    private String activeCd;
    private String mandatoryCd;
    private String defaultSetting;
    private String regUserId;
    private LocalDateTime sysRegDtm;
    private String updUserId;
    private LocalDateTime sysUpdDtm;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
