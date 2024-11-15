package com.oreo.finalproject_5re5_be.tts.exception;

public class EntityNotFoundException extends TtsException {

    public EntityNotFoundException() {
        super(ErrorCode.ENTITY_NOT_FOUND.getMessage(), ErrorCode.ENTITY_NOT_FOUND);
    }
    public EntityNotFoundException(String message) {
        super(message, ErrorCode.ENTITY_NOT_FOUND);
    }
    public EntityNotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
