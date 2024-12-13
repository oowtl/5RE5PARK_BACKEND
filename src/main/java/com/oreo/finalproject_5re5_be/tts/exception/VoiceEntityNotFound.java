package com.oreo.finalproject_5re5_be.tts.exception;

import com.oreo.finalproject_5re5_be.global.exception.EntityNotFoundException;
import com.oreo.finalproject_5re5_be.global.exception.ErrorCode;

public class VoiceEntityNotFound extends EntityNotFoundException {

    public VoiceEntityNotFound() {
        super(
                ErrorCode.VOICE_ENTITY_NOT_FOUND_ERROR.getMessage(),
                ErrorCode.VOICE_ENTITY_NOT_FOUND_ERROR);
    }

    public VoiceEntityNotFound(String message) {
        super(message, ErrorCode.VOICE_ENTITY_NOT_FOUND_ERROR);
    }

    public VoiceEntityNotFound(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
