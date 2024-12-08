package com.oreo.finalproject_5re5_be.tts.exception;

import com.oreo.finalproject_5re5_be.global.exception.BusinessException;
import com.oreo.finalproject_5re5_be.global.exception.ErrorCode;

public class TtsMakeException extends BusinessException {

    public TtsMakeException() {
        super(ErrorCode.INTERNAL_SERVER_ERROR.getMessage(), ErrorCode.INTERNAL_SERVER_ERROR);
    }

    public TtsMakeException(String message) {
        super(message, ErrorCode.INTERNAL_SERVER_ERROR);
    }

    public TtsMakeException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public TtsMakeException(ErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode);
    }
}
