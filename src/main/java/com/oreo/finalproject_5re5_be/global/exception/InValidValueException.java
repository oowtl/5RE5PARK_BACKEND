package com.oreo.finalproject_5re5_be.global.exception;

public class InValidValueException extends BusinessException {

    public InValidValueException() {
        super(ErrorCode.INVALID_INPUT_VALUE.getMessage(), ErrorCode.INVALID_INPUT_VALUE);
    }

    public InValidValueException(ErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode);
    }

    public InValidValueException(String message) {
        super(message, ErrorCode.INVALID_INPUT_VALUE);
    }

    public InValidValueException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
