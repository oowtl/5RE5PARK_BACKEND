package com.oreo.finalproject_5re5_be.tts;
import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class TtsApiCallTest {
    @Test
    public void ttsApiCallTest() throws IOException {
        // TTS 클라이언트 객체화
        try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create()) {

            // 합성할 text 입력
            SynthesisInput input = SynthesisInput.newBuilder().setText("Hello, World!").build();

            // 음성 요청 객체 생성
            VoiceSelectionParams voice =
                    VoiceSelectionParams.newBuilder()
                            .setLanguageCode("en-US")
//                            .setSsmlGender(SsmlVoiceGender.FEMALE)
                            .setName("en-US-Journey-D")
                            .setSsmlGender(SsmlVoiceGender.MALE)
                            .build();


            // 반환될 오디오 파일의 타입 결정
            AudioConfig audioConfig = AudioConfig.newBuilder().setAudioEncoding(AudioEncoding.MP3).build();

            // tts 요청
            SynthesizeSpeechResponse response =
                    textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);

            // 응답으로부터 오디오 컨텐츠 얻기
            ByteString audioContents = response.getAudioContent();

            // 응답을 output 파일으로 write
            try(OutputStream out = new FileOutputStream("output.mp3")) {
                out.write(audioContents.toByteArray());
                System.out.println("Audio content written to file \"output.mp3\"");
            }
        }
    }

}

