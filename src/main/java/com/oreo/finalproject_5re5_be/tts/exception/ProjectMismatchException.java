package com.oreo.finalproject_5re5_be.tts.exception;

import com.oreo.finalproject_5re5_be.global.exception.InValidValueException;

public class ProjectMismatchException extends InValidValueException {

    public ProjectMismatchException() {
        super();
    }

    public ProjectMismatchException(String message) {
        super(message);
    }
}
