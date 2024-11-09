package com.oreo.finalproject_5re5_be.member.exception;

// 필수 약관에 동의하지 않을 경우 발생하는 예외
public class MemberMandatoryTermNotAgreedException extends RuntimeException {

    public MemberMandatoryTermNotAgreedException() {
        this("필수 약관에 동의해야 합니다.");
    }

    public MemberMandatoryTermNotAgreedException(String message) {
        super(message);
    }

}
