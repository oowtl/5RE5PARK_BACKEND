package com.oreo.finalproject_5re5_be.project.exception;

import com.oreo.finalproject_5re5_be.global.exception.BusinessException;
import com.oreo.finalproject_5re5_be.global.exception.ErrorCode;

public class projectNotMemberException extends BusinessException {
    public projectNotMemberException() {
        super(ErrorCode.HANDLE_ACCESS_DENIED.getMessage(), ErrorCode.HANDLE_ACCESS_DENIED);
    }

    public projectNotMemberException(String message) {
        super(message, ErrorCode.HANDLE_ACCESS_DENIED);
    }
}
