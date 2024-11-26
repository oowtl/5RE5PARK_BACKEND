package com.oreo.finalproject_5re5_be.member.exception;

import static com.oreo.finalproject_5re5_be.global.exception.ErrorCode.HUMAN_MEMBER_ERROR;

import com.oreo.finalproject_5re5_be.global.exception.BusinessException;

// 휴먼 회원에 대한 요청이 들어왔을 때 발생하는 예외
public class HumanMemberException extends BusinessException {

    public HumanMemberException() {
        this(HUMAN_MEMBER_ERROR.getMessage());
    }

    public HumanMemberException(String message) {
        super(message, HUMAN_MEMBER_ERROR);
    }
}
