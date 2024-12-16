package com.kubeApi.core.exception;

import com.kubeApi.core.define.CoreErrCode;

import java.util.Formatter;

public class JobException extends ErrorRsltException {

    private static final CoreErrCode DEFAULT_ERROR_CODE = CoreErrCode.UNKNOWN_SYSTEM_ERROR;

    public JobException() {
        super(DEFAULT_ERROR_CODE);
    }

    public JobException(CoreErrCode coreErrCode) {
        super(coreErrCode);
    }

    public JobException(CoreErrCode coreErrCode, String subMessagee) {
        super(coreErrCode, subMessagee);
    }

    public JobException(CoreErrCode coreErrCode, String msgFmt, Object... args) {
        super(coreErrCode, new Formatter().format(msgFmt, args).toString());
    }

    public JobException(CoreErrCode coreErrCode, Throwable cause) {
        super(coreErrCode, cause);
    }
}
