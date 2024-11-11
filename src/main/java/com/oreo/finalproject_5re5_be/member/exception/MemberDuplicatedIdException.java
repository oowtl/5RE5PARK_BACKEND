package com.oreo.finalproject_5re5_be.member.exception;

// 중복된 아이디 등록시 발생하는 예외
public class MemberDuplicatedIdException extends RuntimeException {

    public MemberDuplicatedIdException() {
        this("이미 등록된 아이디입니다.");
    }

    public MemberDuplicatedIdException(String message) {
        super(message);
    }

}
