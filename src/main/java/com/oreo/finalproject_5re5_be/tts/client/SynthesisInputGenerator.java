package com.oreo.finalproject_5re5_be.tts.client;

import com.google.cloud.texttospeech.v1.SynthesisInput;
import com.oreo.finalproject_5re5_be.tts.exception.InvalidTTSParamException;

public class SynthesisInputGenerator {

    private static final int TEXT_MAX_LEN = 2000;

    // 텍스트 값을 전달 받아 SynthesisInput 객체를 만드는 메서드
    public static SynthesisInput generate(String text) {
        if (checkInvalidText(text)) {
            throw new InvalidTTSParamException("text 값이 유효하지 않습니다.");
        }
        return SynthesisInput.newBuilder().setText(text).build();
    }

    // 텍스트 길이 검증 메서드
    private static boolean checkInvalidText(String text) {
        return text.isEmpty() || text.isBlank() || text.length() > TEXT_MAX_LEN;
    }
}
