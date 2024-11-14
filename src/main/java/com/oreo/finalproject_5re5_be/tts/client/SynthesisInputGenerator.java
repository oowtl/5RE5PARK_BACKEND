package com.oreo.finalproject_5re5_be.tts.client;

import com.google.cloud.texttospeech.v1.SynthesisInput;

public class SynthesisInputGenerator {

    // 텍스트 값을 전달 받아 SynthesisInput 객체를 만드는 메서드
    public static SynthesisInput generate(String text) {
        return SynthesisInput.newBuilder()
                .setText(text)
                .build();
    }
}
