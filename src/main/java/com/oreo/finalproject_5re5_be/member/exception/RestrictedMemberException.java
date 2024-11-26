package com.oreo.finalproject_5re5_be.member.exception;

import static com.oreo.finalproject_5re5_be.global.exception.ErrorCode.RESTRICTED_MEMBER_ERROR;

import com.oreo.finalproject_5re5_be.global.exception.BusinessException;

// 제한된 회원에 대한 요청이 들어왔을 때 발생하는 예외
public class RestrictedMemberException extends BusinessException {

    public RestrictedMemberException() {
        this(RESTRICTED_MEMBER_ERROR.getMessage());
    }

    public RestrictedMemberException(String message) {
        super(message, RESTRICTED_MEMBER_ERROR);
    }
}
