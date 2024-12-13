package com.oreo.finalproject_5re5_be.member.exception;

import static com.oreo.finalproject_5re5_be.global.exception.ErrorCode.*;

import com.oreo.finalproject_5re5_be.global.exception.BusinessException;

// 중복된 이메일 등록시 발생하는 예외
public class MemberDuplicatedEmailException extends BusinessException {
    public MemberDuplicatedEmailException() {
        this(MEMBER_DUPLICATED_EMAIL_ERROR.getMessage());
    }

    public MemberDuplicatedEmailException(String message) {
        super(message, MEMBER_DUPLICATED_EMAIL_ERROR);
    }
}
