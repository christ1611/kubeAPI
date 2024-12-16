package com.kubeApi.core.exception;


import com.kubeApi.core.define.CoreErrCode;

import java.io.Serial;
import java.time.LocalDateTime;


public class CoreException extends Exception {

    @Serial
    private static final long serialVersionUID = 4706127219501382258L;

    private static final CoreErrCode DEFAULT_ERROR_CODE = CoreErrCode.UNKNOWN_SYSTEM_ERROR;

    protected ErrorResponse errorResponse;

    public CoreException() {
        super(DEFAULT_ERROR_CODE.getDesc());
        initErrorResponse(DEFAULT_ERROR_CODE);
    }

    public CoreException(CoreErrCode coreErrCode) {
        super(coreErrCode.getDesc());
        initErrorResponse(coreErrCode);
    }

    public CoreException(CoreErrCode coreErrCode, String subMessage) {
        super(coreErrCode.getDesc());
        initErrorResponse(coreErrCode);
        this.errorResponse.addSubMessage(subMessage);
    }

    public CoreException(CoreErrCode coreErrCode, String msgFmt, Object... args) {
        super(coreErrCode.getDesc() + "-" + String.format(msgFmt, args));
        initErrorResponse(coreErrCode);
        this.errorResponse.addSubMessage(String.format(msgFmt, args));
    }

    public CoreException(CoreErrCode coreErrCode, Throwable cause) {
        super(coreErrCode.getDesc(), cause);
        initErrorResponse(coreErrCode);
        this.errorResponse.addSubMessage(cause.getMessage());
    }

    private void initErrorResponse(CoreErrCode coreErrCode){
        this.errorResponse = new ErrorResponse();
        this.errorResponse.setErrorCode(coreErrCode.getCode());
        this.errorResponse.setMessage(coreErrCode.getDesc());
        this.errorResponse.setTimeStamp(LocalDateTime.now());
        this.errorResponse.setException(this);
    }

    public ErrorResponse getErrorResponse()
    {
        return errorResponse;
    }
}
