package com.kubeApi.core.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter @Setter @ToString
public class ErrorResponse {

    private Integer       errorCode  = 0 ;
    private String        message    = "";
    private List<String>  subMessage = new ArrayList<>();
    private LocalDateTime timeStamp  = LocalDateTime.now();

    @JsonIgnore
    private Throwable exception;

    public void addSubMessage(String subMsg)
    {
        if(subMessage == null) subMessage = new ArrayList<>();
        subMessage.add( subMsg );
    }
    public List<String> getSubMessage()
    {
        if(subMessage == null) return new ArrayList<>();
        return subMessage;
    }
}