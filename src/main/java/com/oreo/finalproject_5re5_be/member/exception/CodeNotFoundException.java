package com.oreo.finalproject_5re5_be.member.exception;

public class CodeNotFoundException extends RuntimeException {

    public CodeNotFoundException(String message) {
        super(message);
    }

    public CodeNotFoundException() {
        this("코드를 찾을 수 없습니다.");
    }

}
