package com.oreo.finalproject_5re5_be.member.exception;

import static com.oreo.finalproject_5re5_be.global.exception.ErrorCode.*;

import com.oreo.finalproject_5re5_be.global.exception.BusinessException;

public class MemberInvalidTermConditionException extends BusinessException {

    public MemberInvalidTermConditionException() {
        this(MEMBER_INVALID_TERM_CONDITION_ERROR.getMessage());
    }

    public MemberInvalidTermConditionException(String message) {
        super(message, MEMBER_INVALID_TERM_CONDITION_ERROR);
    }

}
