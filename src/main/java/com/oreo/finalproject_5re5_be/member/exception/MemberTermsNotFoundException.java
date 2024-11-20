package com.oreo.finalproject_5re5_be.member.exception;

import static com.oreo.finalproject_5re5_be.global.exception.ErrorCode.*;

import com.oreo.finalproject_5re5_be.global.exception.BusinessException;
import com.oreo.finalproject_5re5_be.global.exception.ErrorCode;


// 회원 약관 정보를 찾지 못했을 때 발생하는 예외
public class MemberTermsNotFoundException extends BusinessException {

    public MemberTermsNotFoundException() {
        this(MEMBER_NOT_FOUND_ERROR.getMessage());
    }

    public MemberTermsNotFoundException(String message) {
        super(message, MEMBER_TERM_NOT_FOUND_ERROR);
    }

}
