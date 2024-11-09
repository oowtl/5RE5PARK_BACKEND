package com.oreo.finalproject_5re5_be.member.exception;

// 중복된 이메일 등록시 발생하는 예외
public class MemberDuplicatedEmailException extends RuntimeException {

    public MemberDuplicatedEmailException() {
        this("이미 등록된 이메일입니다.");
    }

    public MemberDuplicatedEmailException(String message) {
        super(message);
    }
}
