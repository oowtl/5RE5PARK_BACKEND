package com.oreo.finalproject_5re5_be.tts.exception;

import com.oreo.finalproject_5re5_be.global.exception.BusinessException;
import com.oreo.finalproject_5re5_be.global.exception.ErrorCode;

public class SaveTtsMakeResultException extends BusinessException {

    public SaveTtsMakeResultException() {
        super(ErrorCode.INTERNAL_SERVER_ERROR.getMessage(), ErrorCode.INTERNAL_SERVER_ERROR);
    }

    public SaveTtsMakeResultException(String message) {
        super(message, ErrorCode.INTERNAL_SERVER_ERROR);
    }
}
