package com.kubeApi.core.exception;




import com.kubeApi.core.define.CoreErrCode;

import java.util.Formatter;

public class ErrorRsltException extends CoreException {

    private static final CoreErrCode DEFAULT_ERROR_CODE = CoreErrCode.UNKNOWN_SYSTEM_ERROR;

    public ErrorRsltException() {
        super(DEFAULT_ERROR_CODE);
    }

    public ErrorRsltException(CoreErrCode coreErrCode) {
        super(coreErrCode);
    }

    public ErrorRsltException(CoreErrCode coreErrCode, String subMessagee) {
        super(coreErrCode, subMessagee);
    }

    public ErrorRsltException(CoreErrCode coreErrCode, String msgFmt, Object... args) {
        super(coreErrCode, new Formatter().format(msgFmt, args).toString());
    }

    public ErrorRsltException(CoreErrCode coreErrCode, Throwable cause) {
        super(coreErrCode, cause);
    }
}
