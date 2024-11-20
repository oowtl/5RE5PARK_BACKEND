package com.oreo.finalproject_5re5_be.member.exception;

import static com.oreo.finalproject_5re5_be.global.exception.ErrorCode.*;

import com.oreo.finalproject_5re5_be.global.exception.BusinessException;


// 중복된 코드 등록시 발생하는 예외
public class CodeDuplicatedException extends BusinessException {

    public CodeDuplicatedException() {
        this(CODE_DUPLICATED_ERROR.getMessage());
    }

    public CodeDuplicatedException(String message) {
        super(message, CODE_DUPLICATED_ERROR);
    }

}
