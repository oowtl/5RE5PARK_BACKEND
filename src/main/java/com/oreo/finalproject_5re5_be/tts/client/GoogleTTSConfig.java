package com.oreo.finalproject_5re5_be.tts.client;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.texttospeech.v1.TextToSpeechClient;
import com.google.cloud.texttospeech.v1.TextToSpeechSettings;
import java.io.IOException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class GoogleTTSConfig {

    @Bean
    public TextToSpeechClient TextToSpeechClient() throws IOException {
        // 인증 정보 설정
        ClassPathResource resource = new ClassPathResource("tts.json");

        // 세팅 객체에 인증 정보 등록
        TextToSpeechSettings ttsSettings =
                TextToSpeechSettings.newBuilder()
                        .setCredentialsProvider(() -> GoogleCredentials.fromStream(resource.getInputStream()))
                        .build();

        // 세팅 정보 전달하며 TTSClient 객체 생성
        return TextToSpeechClient.create(ttsSettings);
    }
}
