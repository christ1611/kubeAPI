package com.kubeApi.core.exception;


import com.kubeApi.core.define.CoreErrCode;
import lombok.extern.slf4j.Slf4j;

import java.util.Formatter;

@Slf4j
public class DBException extends ErrorRsltException {

    private static final CoreErrCode DEFAULT_ERROR_CODE = CoreErrCode.UNKNOWN_SYSTEM_ERROR;

    public DBException() {
        super(DEFAULT_ERROR_CODE);
    }

    public DBException(CoreErrCode coreErrCode) {
        super(coreErrCode);
    }

    public DBException(CoreErrCode coreErrCode, String subMessagee) {
        super(coreErrCode, subMessagee);
    }

    public DBException(CoreErrCode coreErrCode, String msgFmt, Object... args) {
        super(coreErrCode, new Formatter().format(msgFmt, args).toString());
    }

    public DBException(CoreErrCode coreErrCode, Throwable cause) {
        super(coreErrCode, cause);
    }
}
