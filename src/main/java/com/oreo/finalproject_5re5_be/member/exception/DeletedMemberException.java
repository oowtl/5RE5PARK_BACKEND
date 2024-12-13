package com.oreo.finalproject_5re5_be.member.exception;

import static com.oreo.finalproject_5re5_be.global.exception.ErrorCode.*;

import com.oreo.finalproject_5re5_be.global.exception.BusinessException;

// 삭제 회원에 대한 요청이 들어왔을 때 발생하는 예외
public class DeletedMemberException extends BusinessException {
    public DeletedMemberException() {
        this(DELETED_MEMBER_ERROR.getMessage());
    }

    public DeletedMemberException(String message) {
        super(message, DELETED_MEMBER_ERROR);
    }
}
