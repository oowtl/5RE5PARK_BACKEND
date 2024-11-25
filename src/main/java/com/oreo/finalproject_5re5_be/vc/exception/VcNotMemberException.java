package com.oreo.finalproject_5re5_be.vc.exception;

import com.oreo.finalproject_5re5_be.global.exception.BusinessException;
import com.oreo.finalproject_5re5_be.global.exception.ErrorCode;

public class VcNotMemberException extends BusinessException {
    public VcNotMemberException() {
        super(ErrorCode.HANDLE_ACCESS_DENIED.getMessage(), ErrorCode.HANDLE_ACCESS_DENIED);
    }
    public VcNotMemberException(String message) {
        super(message, ErrorCode.HANDLE_ACCESS_DENIED);
    }
}
