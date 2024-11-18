package com.oreo.finalproject_5re5_be.member.exception;

public class MemberInvalidTermConditionException extends RuntimeException {

        public MemberInvalidTermConditionException() {
            this("약관이 유효하지 않습니다.");
        }

        public MemberInvalidTermConditionException(String message) {
            super(message);
        }

}
