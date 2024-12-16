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
public class AppVer {
    private String          appId;      // Application ID
    private String          appVer;     // Application Version
    private LocalDateTime   createdDt;  // 생성 일자
    private String          defaultYn;  // Default 여부
    private String          imageUri;   // Harbor 주소
    private String          regUserId;
    private LocalDateTime   sysRegDtm;
    private String          updUserId;
    private LocalDateTime   sysUpdDtm;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
