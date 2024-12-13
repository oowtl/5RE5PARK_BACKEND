package com.oreo.finalproject_5re5_be.global.exception;

public class EntityNotFoundException extends BusinessException {

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
