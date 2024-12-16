package com.kubeApi.jdbc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/** APP_INFO[] JPA Entity AUTO Generate */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class AppScheduleInfo {


    private String schId            ;
    private String schNm            ;
    private String triggerNm        ;
    private String appId            ;
    private String schStCd          ;
    private String schGrp           ;
    private String schParam         ;
}
