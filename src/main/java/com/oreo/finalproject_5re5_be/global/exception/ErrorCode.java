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
    MEMBER_TERM_NOT_FOUND_ERROR(404, "회원 약관을 찾을 수 없습니다."),
    MEMBER_TERMS_CONDITION_NOT_FOUND_ERROR(404, "회원 약관 항목을 찾을 수 없습니다."),
    MEMBER_DUPLICATED_PASSWORD_ERROR(409, "이전 비밀번호와 동일한 비밀번호는 사용할 수 없습니다."),
    HUMAN_MEMBER_ERROR(401, "이메일 인증 또는 본인 확인 절차를 통해 계정을 활성화해야합니다."),
    RESTRICTED_MEMBER_ERROR(403, "해당 계정은 제한된 상태입니다. 관리자에게 문의하세요."),
    DELETED_MEMBER_ERROR(410, "해당 계정은 삭제된 상태입니다."),

    // 코드 예외 상태 코드 정의
    CODE_NOT_FOUND_ERROR(404, "해당 코드를 찾을 수 없습니다. 코드 정보를 다시 확인해 주세요."),
    CODE_DUPLICATED_ERROR(409, "이미 사용중인 코드명입니다. 다른 코드명을 사용해 주세요."),
    CODE_INVALID_INPUT_VALUE_ERROR(400, "코드 입력값이 올바르지 않습니다."),

    INVALID_INPUT_VALUE(400, " Invalid Input Value"),
    ENTITY_NOT_FOUND(400, " Entity Not Found"),
    INTERNAL_SERVER_ERROR(500, "Server Error"),
    INVALID_TYPE_VALUE(400, " Invalid Type Value"),
    HANDLE_ACCESS_DENIED(403, "Access is Denied"),

    // 프로젝트 ERROR 처리
    PROJECT_NOT_FOUND_ERROR(404, "해당 프로젝트를 찾을 수 없습니다."),

    // TTS ERROR 처리
    PROJECT_MISMATCH_ERROR(400, "요청하신 프로젝트를 소유하고 있지 않습니다."),
    VOICE_ENTITY_NOT_FOUND_ERROR(404, "해당 음성을 찾을 수 없습니다."),
    TTS_SENTENCE_NOT_FOUND_ERROR(404, "해당 문장을 찾을 수 없습니다."),

    // TTS 생성 ERROR 처리
    TTS_MAKE_FAILED_ERROR(500, "TTS 생성에 실패했습니다."),
    TTS_MAKE_INVALID_INPUT_VALUE_ERROR(400, "TTS 생성 입력값이 올바르지 않습니다."),
    TTS_MAKE_INVALID_SPEED(400, "허용되지 않는 TTS 속도입니다."),
    TTS_MAKE_INVALID_PITCH(400, "허용되지 않는 TTS 음높이입니다."),
    TTS_MAKE_INVALID_VOLUME(400, "허용되지 않는 TTS 음량입니다.");

    private final String message;
    private final int status;

    ErrorCode(final int status, final String message) {
        this.status = status;
        this.message = message;
    }
}
