package com.oreo.finalproject_5re5_be.member.exception;

// 회원 약관 항목 동의 횟수가 맞지 않을 경우 발생하는 예외
public class MemberWrongCountTermCondition extends RuntimeException {

    public MemberWrongCountTermCondition() {
        this("약관 동의 횟수가 맞지 않습니다.");
    }

    public MemberWrongCountTermCondition(String message) {
        super(message);
    }

}
