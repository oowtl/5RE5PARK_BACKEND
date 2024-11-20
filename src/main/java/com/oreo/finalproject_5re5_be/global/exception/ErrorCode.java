package com.oreo.finalproject_5re5_be.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // 회원 예외 상태 코드 정의
    RETRY_FAILED_ERROR(500, "서버 내부에 알수없는 예외가 발생했고 재시도 복구를 시도했지만 실패했습니다."),
    MAIL_SEND_FAILED_ERROR(500, "메일 전송에 실패했습니다."),
    MEMBER_NOT_FOUND_ERROR(404, "해당 회원을 찾을 수 없습니다. 회원 정보를 다시 확인해 주세요."),
    MEMBER_NOT_FOUND_EMAIL_ERROR(404, "해당 이메일을 찾을 수 없습니다. 이메일 정보를 다시 확인해 주세요."),
    MEMBER_DUPLICATED_EMAIL_ERROR(409, "이미 사용중인 이메일입니다. 다른 이메일을 사용해 주세요."),
    MEMBER_DUPLICATED_ID_ERROR(409, "이미 사용중인 아이디입니다. 다른 아이디를 사용해 주세요."),
    MEMBER_MANDATORY_TERM_NOT_AGREED_ERROR(400, "필수 약관에 동의하지 않았습니다."),
    MEMBER_WRONG_COUNT_TERM_CONDITION_ERROR(400, "회원 약관 항목 코드와 필수 여부의 개수가 일치하지 않습니다."),
    MEMBER_INVALID_INPUT_VALUE_ERROR(400, "회원 정보 입력값이 올바르지 않습니다."),
    MEMBER_INVALID_TERM_INPUT_VALUE_ERROR(400, "회원 약관 입력값이 올바르지 않습니다."),
    MEMBER_INVALID_TERM_CONDITION_ERROR(400, "회원 약관 항목이 올바르지 않습니다."),
    MEMBER_TERM_NOT_FOUND_ERROR(404, "회원 약관 항목을 찾을 수 없습니다."),
    MEMBER_TERMS_CONDITION_NOT_FOUND_ERROR(404, "회원 약관 항목을 찾을 수 없습니다."),


    // 코드 예외 상태 코드 정의
    CODE_NOT_FOUND_ERROR(404, "해당 코드를 찾을 수 없습니다. 코드 정보를 다시 확인해 주세요."),
    CODE_DUPLICATED_ERROR(409, "이미 사용중인 코드명입니다. 다른 코드명을 사용해 주세요."),



    INVALID_INPUT_VALUE(400, " Invalid Input Value"),
    ENTITY_NOT_FOUND(400, " Entity Not Found"),
    INTERNAL_SERVER_ERROR(500, "Server Error"),
    INVALID_TYPE_VALUE(400, " Invalid Type Value"),
    HANDLE_ACCESS_DENIED(403, "Access is Denied");

    private final String message;
    private final int status;

    ErrorCode(final int status, final String message) {
        this.status = status;
        this.message = message;
    }
}
