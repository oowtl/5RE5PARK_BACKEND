package com.oreo.finalproject_5re5_be.member.exception;

import static com.oreo.finalproject_5re5_be.global.exception.ErrorCode.*;

import com.oreo.finalproject_5re5_be.global.exception.BusinessException;

// 입력값으로부터 회원 약관 항목을 찾지 못했을 때 발생하는 예외
public class MemberTermsConditionNotFoundException extends BusinessException {
    public MemberTermsConditionNotFoundException() {
        this(MEMBER_TERMS_CONDITION_NOT_FOUND_ERROR.getMessage());
        System.out.println("ghcnfehla");
    }

    public MemberTermsConditionNotFoundException(String message) {
        super(message, MEMBER_TERMS_CONDITION_NOT_FOUND_ERROR);
    }
}
