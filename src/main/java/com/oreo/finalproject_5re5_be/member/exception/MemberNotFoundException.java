package com.oreo.finalproject_5re5_be.member.exception;

public class MemberNotFoundException extends RuntimeException {

    public MemberNotFoundException(String message) {
        super(message);
    }

    public MemberNotFoundException() {
        this("회원이 존재하지 않습니다.");
    }



}
