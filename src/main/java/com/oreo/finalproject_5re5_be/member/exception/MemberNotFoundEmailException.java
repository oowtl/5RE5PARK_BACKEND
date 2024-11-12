package com.oreo.finalproject_5re5_be.member.exception;

// 존재하지 않는 이메일일 때 사용하는 예외
public class MemberNotFoundEmailException extends RuntimeException{

    public MemberNotFoundEmailException() {
        this("이메일이 존재하지 않습니다.");
    }

    public MemberNotFoundEmailException(String message) {
        super(message);
    }
}
