package com.oreo.finalproject_5re5_be.member.exception;

import static com.oreo.finalproject_5re5_be.global.exception.ErrorCode.MEMBER_DUPLICATED_ID_ERROR;
import static com.oreo.finalproject_5re5_be.global.exception.ErrorCode.MEMBER_DUPLICATED_PASSWORD_ERROR;

import com.oreo.finalproject_5re5_be.global.exception.BusinessException;

// 기존에 등록한 비밀번호화 새로 등록할 비밀번호가 같을시 발생하는  예외
public class MemberDuplicatedPasswordException extends BusinessException {

    public MemberDuplicatedPasswordException() {
        this(MEMBER_DUPLICATED_PASSWORD_ERROR.getMessage());
    }

    public MemberDuplicatedPasswordException(String message) {
        super(message, MEMBER_DUPLICATED_ID_ERROR);
    }
}
