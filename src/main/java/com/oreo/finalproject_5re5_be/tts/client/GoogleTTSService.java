package com.oreo.finalproject_5re5_be.tts.client;

import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;
import com.oreo.finalproject_5re5_be.global.component.ByteArrayMultipartFile;
import com.oreo.finalproject_5re5_be.tts.exception.InvalidTTSParamException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class GoogleTTSService {
    @Autowired TextToSpeechClient ttsClient;

    public byte[] make(SynthesisInput input, VoiceSelectionParams voice, AudioConfig audioConfig) {
        if (checkInvalidParams(input, voice, audioConfig)) {
            throw new InvalidTTSParamException("TTS 요청 파라미터가 부족합니다");
        }

        // tts 요청
        SynthesizeSpeechResponse response = ttsClient.synthesizeSpeech(input, voice, audioConfig);

        // 응답으로부터 오디오 컨텐츠 얻기
        ByteString audioContents = response.getAudioContent();

        return audioContents.toByteArray();
    }

    public MultipartFile makeToMultipartFile(
            SynthesisInput input,
            VoiceSelectionParams voice,
            AudioConfig audioConfig,
            String ttsFileName) {
        byte[] result = make(input, voice, audioConfig);
        return new ByteArrayMultipartFile(result, ttsFileName + ".wav", "audio/wav");
    }

    // tts 생성에 필요한 파라미터들 null 아닌지 검사
    private boolean checkInvalidParams(
            SynthesisInput input, VoiceSelectionParams voice, AudioConfig audioConfig) {
        return input == null || voice == null || audioConfig == null;
    }
}
