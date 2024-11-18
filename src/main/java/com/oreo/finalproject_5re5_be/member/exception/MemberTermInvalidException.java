package com.oreo.finalproject_5re5_be.member.exception;

public class MemberTermInvalidException extends RuntimeException {

    public MemberTermInvalidException() {
        this("약관이 유효하지 않습니다.");
    }

    public MemberTermInvalidException(String message) {
        super(message);
    }




}
