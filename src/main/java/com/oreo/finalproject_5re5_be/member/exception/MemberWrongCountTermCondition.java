package com.oreo.finalproject_5re5_be.member.exception;

import static com.oreo.finalproject_5re5_be.global.exception.ErrorCode.*;

import com.oreo.finalproject_5re5_be.global.exception.BusinessException;

// 회원 약관 항목 동의 횟수가 맞지 않을 경우 발생하는 예외
public class MemberWrongCountTermCondition extends BusinessException {
    public MemberWrongCountTermCondition() {
        this(MEMBER_WRONG_COUNT_TERM_CONDITION_ERROR.getMessage());
    }

    public MemberWrongCountTermCondition(String message) {
        super(message, MEMBER_WRONG_COUNT_TERM_CONDITION_ERROR);
    }
}
