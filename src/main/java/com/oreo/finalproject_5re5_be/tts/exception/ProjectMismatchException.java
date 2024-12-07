package com.oreo.finalproject_5re5_be.tts.exception;

import com.oreo.finalproject_5re5_be.global.exception.ErrorCode;
import com.oreo.finalproject_5re5_be.global.exception.InValidValueException;

public class ProjectMismatchException extends InValidValueException {

    public ProjectMismatchException() {
        super(ErrorCode.PROJECT_MISMATCH_ERROR.getMessage(), ErrorCode.PROJECT_MISMATCH_ERROR);
    }

    public ProjectMismatchException(String message) {
        super(message);
    }
}
