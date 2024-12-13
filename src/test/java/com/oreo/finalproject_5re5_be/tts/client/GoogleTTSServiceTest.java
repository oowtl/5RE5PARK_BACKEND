package com.oreo.finalproject_5re5_be.tts.client;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-ndb-test.properties")
class GoogleTTSServiceTest {
    @Autowired GoogleTTSService googleTTSService;

    @Test
    public void makeTest() throws Exception {
        // 입력 파라미터 세팅
        String text = "Hello, world";
        String languageCode = "nl-NL";
        String voiceName = "nl-NL-Standard-D";
        String gender = "female";

        // tts 생성 요청
        byte[] audioByteArray =
                googleTTSService.make(
                        SynthesisInputGenerator.generate(text),
                        VoiceParamsGenerator.generate(languageCode, voiceName, gender),
                        AudioConfigGenerator.generate(1.0, 0.0, 0.0));

        // tts 생성 결과가 반환되어야 함
        assertTrue(audioByteArray != null);
    }
}
