package com.oreo.finalproject_5re5_be.member.exception;

import static com.oreo.finalproject_5re5_be.global.exception.ErrorCode.*;

import com.oreo.finalproject_5re5_be.global.exception.BusinessException;

// 존재하지 않는 이메일일 때 사용하는 예외
public class MemberNotFoundEmailException extends BusinessException {
    public MemberNotFoundEmailException() {
        this(MEMBER_NOT_FOUND_EMAIL_ERROR.getMessage());
    }

    public MemberNotFoundEmailException(String message) {
        super(message, MEMBER_NOT_FOUND_EMAIL_ERROR);
    }
}
