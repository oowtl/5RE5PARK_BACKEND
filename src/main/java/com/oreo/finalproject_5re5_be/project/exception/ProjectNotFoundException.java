package com.oreo.finalproject_5re5_be.project.exception;

import com.oreo.finalproject_5re5_be.global.exception.EntityNotFoundException;
import com.oreo.finalproject_5re5_be.global.exception.ErrorCode;

public class ProjectNotFoundException extends EntityNotFoundException {

    public ProjectNotFoundException() {
        super(ErrorCode.PROJECT_NOT_FOUND_ERROR.getMessage(), ErrorCode.PROJECT_NOT_FOUND_ERROR);
    }

    public ProjectNotFoundException(String message) {
        super(message, ErrorCode.PROJECT_NOT_FOUND_ERROR);
    }

    public ProjectNotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
