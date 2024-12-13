package com.oreo.finalproject_5re5_be.tts.exception;

import com.oreo.finalproject_5re5_be.global.exception.EntityNotFoundException;
import com.oreo.finalproject_5re5_be.global.exception.ErrorCode;

public class TtsSentenceNotFound extends EntityNotFoundException {

    public TtsSentenceNotFound() {
        super(
                ErrorCode.TTS_SENTENCE_NOT_FOUND_ERROR.getMessage(),
                ErrorCode.TTS_SENTENCE_NOT_FOUND_ERROR);
    }

    public TtsSentenceNotFound(String message) {
        super(message, ErrorCode.TTS_SENTENCE_NOT_FOUND_ERROR);
    }

    public TtsSentenceNotFound(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
