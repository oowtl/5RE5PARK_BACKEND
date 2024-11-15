package com.oreo.finalproject_5re5_be.tts.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class TtsErrorResponse {
    private int status;
    private Response response;

    // 내부 Response 클래스 정의
    @Getter
    @AllArgsConstructor
    public static class Response {
        private String message;
        private List<FieldErrorDetail> fieldErrors; // 필드별 오류 메시지 리스트
    }

    // 내부 FieldErrorDetail 클래스 정의
    @Getter
    @AllArgsConstructor
    public static class FieldErrorDetail {
        private String field;
        private String message;

        public static FieldErrorDetail of(String field, String message) {
            return new FieldErrorDetail(field, message);
        }
    }

    public static TtsErrorResponse of(int status, String message, List<FieldErrorDetail> fieldErrors) {
        return new TtsErrorResponse(status, new Response(message, fieldErrors));
    }

    public static TtsErrorResponse of(int status, String message) {
        return new TtsErrorResponse(status, new Response(message, null));
    }
}