package com.oreo.finalproject_5re5_be.member.exception;

public class MemberTermsNotFoundException extends RuntimeException {

    public MemberTermsNotFoundException() {
        this("약관 동의 정보를 찾을 수 없습니다.");
    }

    public MemberTermsNotFoundException(String message) {
        super(message);
    }

}
