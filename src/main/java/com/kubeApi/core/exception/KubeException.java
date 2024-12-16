package com.kubeApi.core.exception;


import com.kubeApi.core.define.CoreErrCode;

import java.util.Formatter;

public class KubeException extends ErrorRsltException {

    private static final CoreErrCode DEFAULT_ERROR_CODE = CoreErrCode.UNKNOWN_SYSTEM_ERROR;

    public KubeException() {
        super(DEFAULT_ERROR_CODE);
    }

    public KubeException(CoreErrCode coreErrCode) {
        super(coreErrCode);
    }

    public KubeException(CoreErrCode coreErrCode, String subMessagee) {
        super(coreErrCode, subMessagee);
    }

    public KubeException(CoreErrCode coreErrCode, String msgFmt, Object... args) {
        super(coreErrCode, new Formatter().format(msgFmt, args).toString());
    }
}
