package com.oreo.finalproject_5re5_be.tts.exception;

import com.oreo.finalproject_5re5_be.global.exception.BusinessException;
import com.oreo.finalproject_5re5_be.global.exception.ErrorCode;

public class TtsSentenceInValidInput extends BusinessException {
    public TtsSentenceInValidInput() {
        super(ErrorCode.INVALID_INPUT_VALUE.getMessage(), ErrorCode.INVALID_INPUT_VALUE);
    }

    public TtsSentenceInValidInput(String message) {
        super(message, ErrorCode.INVALID_INPUT_VALUE);
    }
}
