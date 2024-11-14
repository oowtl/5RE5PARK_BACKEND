package com.oreo.finalproject_5re5_be.tts.client;

import com.google.cloud.texttospeech.v1.SsmlVoiceGender;
import com.google.cloud.texttospeech.v1.VoiceSelectionParams;

public class VoiceParamsGenerator {

    // 언어 코드, 보이스명, 성별 값을 가지고 VoiceSelectionParmas 객체 생성
    public static VoiceSelectionParams generate(String languageCode, String voiceName, String gender) throws Exception {
        return VoiceSelectionParams.newBuilder()
                .setLanguageCode(languageCode)
                .setName(voiceName)
                .setSsmlGender(convertToSsmlVoiceGender(gender))
                .build();
    }

    // 성별 값(string)을 SsmlVoiceGender 객체로 변환하는 메서드
    private static SsmlVoiceGender convertToSsmlVoiceGender(String gender) {
        return SsmlVoiceGender.valueOf(gender.toUpperCase());
    }
}
