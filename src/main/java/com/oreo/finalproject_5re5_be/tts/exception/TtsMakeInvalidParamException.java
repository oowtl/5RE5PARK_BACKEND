package com.oreo.finalproject_5re5_be.tts.exception;

import com.oreo.finalproject_5re5_be.global.exception.ErrorCode;

public class TtsMakeInvalidParamException extends TtsMakeException {

    public TtsMakeInvalidParamException() {
        super(
                ErrorCode.TTS_MAKE_INVALID_INPUT_VALUE_ERROR.getMessage(),
                ErrorCode.TTS_MAKE_INVALID_INPUT_VALUE_ERROR);
    }

    public TtsMakeInvalidParamException(String message) {
        super(message, ErrorCode.TTS_MAKE_INVALID_INPUT_VALUE_ERROR);
    }

    public TtsMakeInvalidParamException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public TtsMakeInvalidParamException(ErrorCode errorCode) {
        super(errorCode);
    }
}
