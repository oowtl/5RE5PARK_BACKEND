package com.oreo.finalproject_5re5_be.member.exception;

import static com.oreo.finalproject_5re5_be.global.exception.ErrorCode.*;

import com.oreo.finalproject_5re5_be.global.exception.BusinessException;

// 관리자가 회원 약관 등록시 잘못된 값이 입력되었을 때 발생하는 예외
public class MemberTermInvalidException extends BusinessException {

    public MemberTermInvalidException() {
        this(MEMBER_INVALID_TERM_INPUT_VALUE_ERROR.getMessage());
    }

    public MemberTermInvalidException(String message) {
        super(message, MEMBER_INVALID_TERM_INPUT_VALUE_ERROR);
    }
}
