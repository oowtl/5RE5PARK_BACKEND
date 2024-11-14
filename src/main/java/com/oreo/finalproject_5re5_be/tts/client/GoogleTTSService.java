package com.oreo.finalproject_5re5_be.tts.client;

import com.google.api.gax.rpc.InvalidArgumentException;
import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoogleTTSService {
    @Autowired
    TextToSpeechClient ttsClient;

    public byte[] make(SynthesisInput input, VoiceSelectionParams voice, AudioConfig audioConfig) throws Exception {
        try {
            // tts 요청
            SynthesizeSpeechResponse response = ttsClient.synthesizeSpeech(input, voice, audioConfig);

            // 응답으로부터 오디오 컨텐츠 얻기
            ByteString audioContents = response .getAudioContent();

            return audioContents.toByteArray();
        } catch (InvalidArgumentException e) {
            throw new Exception("TTS 요청 파라미터가 잘못되었습니다.", e);
        }
    }

}
