package com.oreo.finalproject_5re5_be.tts.client;

import com.google.cloud.texttospeech.v1.AudioConfig;
import com.google.cloud.texttospeech.v1.AudioEncoding;
import com.oreo.finalproject_5re5_be.global.exception.ErrorCode;
import com.oreo.finalproject_5re5_be.tts.exception.TtsMakeInvalidParamException;

public class AudioConfigGenerator {
    // Google TTS AudioConfig 정책
    private static final double MAX_SPEED = 4.0;
    private static final double MIN_SPEED = 0.25;
    private static final double MAX_PITCH = 20.0;
    private static final double MIN_PITCH = -20.0;
    private static final double MAX_VOLUME = 16.0;
    private static final double MIN_VOLUME = -96.0;

    // 샘플링 레이트는 44100으로 고정(내부 결정)
    private static final int SAMPLING_RATE = 44100;

    // 음성 속도, 피치, 볼륨 값을 받아 AudioConfig 객체로 생성하는 메서드
    public static AudioConfig generate() {
        return generate(1.0, 0.0, 0.0);
    }

    public static AudioConfig generate(double speed, double pitch, double volume) {

        // 1. 검증 통과 못하면 예외 던지기
        checkParams(speed, pitch, volume);

        // 2. 객체 생성
        return AudioConfig.newBuilder()
                .setAudioEncoding(AudioEncoding.LINEAR16)
                .setVolumeGainDb(volume)
                .setPitch(pitch)
                .setSampleRateHertz(SAMPLING_RATE)
                .setSpeakingRate(speed)
                .build();
    }

    // 음성 속도 값 검증
    private static boolean vaildSpeed(double speed) {
        return speed >= MIN_SPEED && speed <= MAX_SPEED;
    }

    // 피치 값 검증
    private static boolean validPitch(double pitch) {
        return pitch >= MIN_PITCH && pitch <= MAX_PITCH;
    }

    // 볼륨 값 검증
    private static boolean validVolume(double volume) {
        return volume > MIN_VOLUME && volume < MAX_VOLUME;
    }

    private static void checkParams(double speed, double pitch, double volume) {
        if (!vaildSpeed(speed)) {
            throw new TtsMakeInvalidParamException(ErrorCode.TTS_MAKE_INVALID_SPEED);
        }

        if (!validPitch(pitch)) {
            throw new TtsMakeInvalidParamException(ErrorCode.TTS_MAKE_FAILED_ERROR);
        }
        if (!validVolume(volume)) {
            throw new TtsMakeInvalidParamException(ErrorCode.TTS_MAKE_INVALID_VOLUME);
        }
    }
}
