package com.kubeApi.core.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kubeApi.core.define.CoreErrCode;
import com.kubeApi.core.mapper.MapperUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Formatter;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class ApiCallException extends ErrorRsltException {

    private static final CoreErrCode DEFAULT_ERROR_CODE = CoreErrCode.UNKNOWN_SYSTEM_ERROR;

    @Getter
    private Map<String, Object> responseBodyMap ;

    public ApiCallException() {
        super(DEFAULT_ERROR_CODE);
    }

    public ApiCallException(CoreErrCode coreErrCode) {
        super(coreErrCode);
    }

    public ApiCallException(CoreErrCode coreErrCode, String subMessage) {
        super(coreErrCode, subMessage);
    }

    public ApiCallException(CoreErrCode coreErrCode, String msgFmt, Object... args) {
        super(coreErrCode, new Formatter().format(msgFmt, args).toString());
    }

    public ApiCallException(CoreErrCode coreErrCode, Throwable cause) {
        super(coreErrCode, cause);
    }

    public ApiCallException(CoreErrCode coreErrCode, String subMessage, String responseBody, Throwable cause) {
        super(coreErrCode, subMessage);

        try{
            responseBodyMap = MapperUtil.mapper.readValue(responseBody, LinkedHashMap.class);
        }
        catch (JsonProcessingException jsonEx )
        {
            log.error("Json Parsing Exception ", jsonEx);
        }
    }
}
