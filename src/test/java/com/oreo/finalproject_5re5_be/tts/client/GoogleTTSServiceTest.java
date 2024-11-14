package com.oreo.finalproject_5re5_be.tts.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GoogleTTSClientTest {
    @Test
    public void makeTest() throws Exception {
        // 입력 파라미터 세팅
        String text = "Hello, world";
        String languageCode = "nl-NL";
        String voiceName = "nl-NL-Standard-D";

        GoogleTTSClient ttsClient = new GoogleTTSClient(text, languageCode, voiceName);
        ttsClient.setPitch(2.0);
        ttsClient.setSpeed(2.0);
        ttsClient.setVoiceGender("male");

        // tts 생성 요청
        byte[] audioByteArray = ttsClient.make();

        // tts 생성 결과가 반환되어야 함
        assertTrue(audioByteArray != null);
    }

}