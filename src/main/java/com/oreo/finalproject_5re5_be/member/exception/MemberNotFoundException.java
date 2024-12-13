package com.oreo.finalproject_5re5_be.member.exception;

import static com.oreo.finalproject_5re5_be.global.exception.ErrorCode.*;

import com.oreo.finalproject_5re5_be.global.exception.BusinessException;

// 입력값으로부터 회원을 찾지 못했을 때 발생하는 예외
public class MemberNotFoundException extends BusinessException {

    public MemberNotFoundException() {
        this(MEMBER_NOT_FOUND_ERROR.getMessage());
    }

    public MemberNotFoundException(String message) {
        super(message, MEMBER_NOT_FOUND_ERROR);
    }
}
