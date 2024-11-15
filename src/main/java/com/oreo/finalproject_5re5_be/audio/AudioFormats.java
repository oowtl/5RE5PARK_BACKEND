package com.oreo.finalproject_5re5_be.audio;

import javax.sound.sampled.AudioFormat;

public final class AudioFormats {
    private AudioFormats() {}

    public static final AudioFormat MONO_FORMAT = new AudioFormat(
            AudioFormat.Encoding.PCM_SIGNED,
            44100, // 44.1kHz로 변환
            16,    // 16비트
            1,     // 모노
            2,     // 2 bytes/frame
            44100, // frame rate와 샘플링 레이트 일치
            false  // 리틀 엔디안
    );

    public static final AudioFormat STEREO_FORMAT = new AudioFormat(
            AudioFormat.Encoding.PCM_SIGNED,
            44100, // 44.1kHz로 변환
            16,    // 16비트
            2,     // 스테레오
            4,     // 4 bytes/frame
            44100, // frame rate와 샘플링 레이트 일치
            false  // 리틀 엔디안
    );
}
