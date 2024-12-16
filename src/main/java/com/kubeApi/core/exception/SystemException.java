package com.kubeApi.core.exception;


import com.kubeApi.core.define.CoreErrCode;

import java.util.Formatter;

public class SystemException extends ErrorRsltException {

    private static final CoreErrCode DEFAULT_ERROR_CODE = CoreErrCode.UNKNOWN_SYSTEM_ERROR;

    public SystemException() {
        super(DEFAULT_ERROR_CODE);
    }

    public SystemException(CoreErrCode coreErrCode) {
        super(coreErrCode);
    }

    public SystemException(CoreErrCode coreErrCode, String subMessagee) {
        super(coreErrCode, subMessagee);
    }

    public SystemException(CoreErrCode coreErrCode, String msgFmt, Object... args) {
        super(coreErrCode, new Formatter().format(msgFmt, args).toString());
    }
}
