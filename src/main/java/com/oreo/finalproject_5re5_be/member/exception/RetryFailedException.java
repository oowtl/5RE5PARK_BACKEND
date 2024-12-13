package com.oreo.finalproject_5re5_be.member.exception;

import com.oreo.finalproject_5re5_be.global.exception.ErrorCode;

// 재시도 복구에 실패했을 경우 발생하는 예외
public class RetryFailedException extends RuntimeException {

    public RetryFailedException() {
        this(ErrorCode.RETRY_FAILED_ERROR.getMessage());
    }

    public RetryFailedException(String message) {
        super(message);
    }
}
