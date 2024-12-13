package com.oreo.finalproject_5re5_be.tts.exception;

import com.oreo.finalproject_5re5_be.global.exception.ErrorCode;
import com.oreo.finalproject_5re5_be.global.exception.InValidValueException;

public class InValidRequestException extends InValidValueException {

    public InValidRequestException() {
        super(ErrorCode.INVALID_INPUT_VALUE);
    }

    public InValidRequestException(String message) {
        super(message, ErrorCode.INVALID_INPUT_VALUE);
    }

    public InValidRequestException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
