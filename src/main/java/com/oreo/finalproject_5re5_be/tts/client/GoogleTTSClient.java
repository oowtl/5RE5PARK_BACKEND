package com.oreo.finalproject_5re5_be.tts.client;

import com.google.api.gax.rpc.InvalidArgumentException;
import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;
import lombok.Getter;
import java.util.Arrays;

@Getter
public class GoogleTTSClient {
    // Google TTS API 정책
    private final static String[] VOICE_GENDER_TYPES = { "unspecified", "male", "female" };
    private final static double MAX_SPEED = 4.0;
    private final static double MIN_SPEED = 0.25;
    private final static double MAX_PITCH= 20.0;
    private final static double MIN_PITCH = -20.0;
    private final static double MAX_VOLUME = 16.0;
    private final static double MIN_VOLUME = -96.0;


    // 필수 입력
    private String text = "";
    private String languageCode = "";
    private String voiceName = "";
    private SsmlVoiceGender ssmlVoiceGender = SsmlVoiceGender.SSML_VOICE_GENDER_UNSPECIFIED;
    private final AudioEncoding audioEncoding = AudioEncoding.MP3;

    // 선택 입력
    private double speed = 0.0;
    private double pitch = 0.0;
    private double volume = 0.0;
    private int samplingRate = 0;

    public GoogleTTSClient(String text, String languageCode, String voiceName) {
        this.text = text;
        this.languageCode = languageCode;
        this.voiceName = voiceName;
    }

    public void setVoiceGender(String gender) throws IllegalArgumentException {
        // voice 성별(string)을 SsmlVoiceGender 객체로 변환
        int index = Arrays.asList(VOICE_GENDER_TYPES).indexOf(gender);

        if(index == -1) {
            throw new IllegalArgumentException("잘못된 성별입니다");
        }

        this.ssmlVoiceGender = SsmlVoiceGender.forNumber(index);
    }

    public void setSpeed(double speed) {
        if(speed < MIN_SPEED || speed > MAX_SPEED) {
            throw new IllegalArgumentException("잘못된 속도 값입니다");
        }
        this.speed = speed;
    }

    public void setPitch(double pitch) {
        if(pitch < MIN_PITCH || pitch > MAX_PITCH) {
            throw new IllegalArgumentException("잘못된 피치 값입니다");
        }
        this.pitch = pitch;
    }

    public void setVolume(double volume) {
        if(volume < MIN_VOLUME || volume > MAX_VOLUME) {
            throw new IllegalArgumentException("잘못된 볼륨 값입니다");
        }
        this.volume = volume;
    }

    public void setSamplingRate(int samplingRate) {
        this.samplingRate = samplingRate;
    }

    public byte[] make() throws Exception {
        TextToSpeechClient textToSpeechClient = null;
        try {
            // TTS 클라이언트 객체화
            textToSpeechClient = TextToSpeechClient.create();

            // 합성할 text 입력 객체 생성
            SynthesisInput input = getInput();

            // 음성 요청 객체 생성
            VoiceSelectionParams voice = getVoice();

            // 오디오 설정 객체 생성
            AudioConfig audioConfig = getAudioConfig();

            // tts 요청
            SynthesizeSpeechResponse response = textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);

            // 응답으로부터 오디오 컨텐츠 얻기
            ByteString audioContents = response.getAudioContent();

            return audioContents.toByteArray();
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
            throw new Exception("요청 파라미터가 잘못되었습니다.");
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            textToSpeechClient.close();
        }
    }

    private AudioConfig getAudioConfig() {
        return AudioConfig.newBuilder()
                .setAudioEncoding(AudioEncoding.MP3)
                .setVolumeGainDb(this.volume)
                .setPitch(this.pitch)
                .setSampleRateHertz(this.samplingRate)
                .setSpeakingRate(this.speed)
                .build();
    }

    private VoiceSelectionParams getVoice() throws Exception {
        return VoiceSelectionParams.newBuilder()
                .setLanguageCode(this.languageCode)
                .setName(this.voiceName)
                .setSsmlGender(this.ssmlVoiceGender)
                .build();
    }

    private SynthesisInput getInput() {
        return SynthesisInput.newBuilder()
                .setText(this.text)
                .build();
    }

}
