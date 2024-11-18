package com.oreo.finalproject_5re5_be.member.exception;

public class MemberTermsConditionNotFoundException extends RuntimeException{

    public MemberTermsConditionNotFoundException() {
        this("약관이 존재하지 않습니다.");
    }

    public MemberTermsConditionNotFoundException(String message) {
        super(message);
    }
}
