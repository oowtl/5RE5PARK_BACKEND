package com.oreo.finalproject_5re5_be.member.exception;

import static com.oreo.finalproject_5re5_be.global.exception.ErrorCode.*;

import com.oreo.finalproject_5re5_be.global.exception.BusinessException;

// 중복된 아이디 등록시 발생하는 예외
public class MemberDuplicatedIdException extends BusinessException {

    public MemberDuplicatedIdException() {
        this(MEMBER_DUPLICATED_ID_ERROR.getMessage());
    }

    public MemberDuplicatedIdException(String message) {
        super(message, MEMBER_DUPLICATED_ID_ERROR);
    }
}
