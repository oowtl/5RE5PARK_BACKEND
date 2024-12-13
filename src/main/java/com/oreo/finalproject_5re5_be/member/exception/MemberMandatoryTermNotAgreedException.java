package com.oreo.finalproject_5re5_be.member.exception;

import static com.oreo.finalproject_5re5_be.global.exception.ErrorCode.*;

import com.oreo.finalproject_5re5_be.global.exception.BusinessException;

// 필수 약관에 동의하지 않을 경우 발생하는 예외
public class MemberMandatoryTermNotAgreedException extends BusinessException {

    public MemberMandatoryTermNotAgreedException() {
        this(MEMBER_MANDATORY_TERM_NOT_AGREED_ERROR.getMessage());
    }

    public MemberMandatoryTermNotAgreedException(String message) {
        super(message, MEMBER_MANDATORY_TERM_NOT_AGREED_ERROR);
    }
}
