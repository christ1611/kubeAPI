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
public class AppTokenInfo {
    private String          podNamespace;       // Namespace
    private String          serviceAcct;        // Service Account
    private String          tokenInfo;          // 토큰 정보
    private LocalDateTime   tokenCreatedDt;     // 최종발급일시
    private LocalDateTime   tokenExpiDt;        // 토큰만기일시
    private String          regUserId;
    private LocalDateTime   sysRegDtm;
    private String          updUserId;
    private LocalDateTime   sysUpdDtm;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
