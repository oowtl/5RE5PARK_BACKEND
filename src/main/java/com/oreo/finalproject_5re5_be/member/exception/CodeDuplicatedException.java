package com.oreo.finalproject_5re5_be.member.exception;

public class CodeDuplicatedException extends RuntimeException {

    public CodeDuplicatedException(String message) {
        super(message);
    }

    public CodeDuplicatedException() {
        this("중복된 코드명입니다.");
    }

}
