package com.oreo.finalproject_5re5_be.code.exeption;

import static com.oreo.finalproject_5re5_be.global.exception.ErrorCode.*;

import com.oreo.finalproject_5re5_be.global.exception.BusinessException;
;

// 입력값으로부터 코드를 찾지 못했을 때 발생하는 예외
public class CodeNotFoundException extends BusinessException {

    public CodeNotFoundException() {
        this(CODE_NOT_FOUND_ERROR.getMessage());
    }

    public CodeNotFoundException(String message) {
        super(message, MEMBER_NOT_FOUND_ERROR);
    }

}
