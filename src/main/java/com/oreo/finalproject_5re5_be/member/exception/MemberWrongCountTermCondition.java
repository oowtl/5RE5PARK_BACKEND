package com.oreo.finalproject_5re5_be.member.exception;

public class MemberWrongCountTermCondition extends RuntimeException {

    public MemberWrongCountTermCondition() {
        this("약관 동의 횟수가 맞지 않습니다.");
    }

    public MemberWrongCountTermCondition(String message) {
        super(message);
    }

}
