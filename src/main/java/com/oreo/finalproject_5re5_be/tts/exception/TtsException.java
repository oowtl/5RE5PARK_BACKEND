package com.oreo.finalproject_5re5_be.tts.exception;

import lombok.Getter;

@Getter
public class TtsException extends RuntimeException {

    private final ErrorCode errorCode;

    public TtsException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public TtsException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
