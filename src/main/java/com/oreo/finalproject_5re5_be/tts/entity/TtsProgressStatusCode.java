package com.oreo.finalproject_5re5_be.tts.entity;

public enum TtsProgressStatusCode {
    // 'TTS 생성 요청' 상태
    CREATED,

    // 'TTS 진행중' 상태
    IN_PROGRESS,

    // 'TTS 생성 완료' 상태
    FINISHED,

    // 'TTS 생성 취소' 상태
    CANCELED,

    // 'TTS 생성 실패' 상태
    FAILED
}
