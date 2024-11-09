package com.oreo.finalproject_5re5_be.member.exception;

public class RetryFailedException extends RuntimeException {

    public RetryFailedException() {
        this("애플리케이션에서 정한 대기시간과 재시도 횟수만큼 시도했지만, 복구에 실패하였습니다.");
    }

    public RetryFailedException(String message) {
        super(message);
    }
}