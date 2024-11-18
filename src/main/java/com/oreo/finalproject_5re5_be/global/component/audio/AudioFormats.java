package com.oreo.finalproject_5re5_be.global.component.audio;

import javax.sound.sampled.AudioFormat;

public final class AudioFormats {
    private AudioFormats() {}

    public static final AudioFormat MONO_FORMAT_SR441_B16 = new AudioFormat(
            AudioFormat.Encoding.PCM_SIGNED,
            44100, // 44.1kHz로 변환
            16,    // 16비트
            1,     // 모노
            2,     // 2 bytes/frame
            44100, // frame rate와 샘플링 레이트 일치
            false  // 리틀 엔디안
    );

    public static final AudioFormat MONO_FORMAT_SR240_B16 = new AudioFormat(
            AudioFormat.Encoding.PCM_SIGNED,
            24000, // 44.1kHz로 변환
            16,    // 16비트
            1,     // 모노
            2,     // 2 bytes/frame
            24000, // frame rate와 샘플링 레이트 일치
            false  // 리틀 엔디안
    );

    public static final AudioFormat STEREO_FORMAT_SR441_B16 = new AudioFormat(
            AudioFormat.Encoding.PCM_SIGNED,
            44100, // 44.1kHz로 변환
            16,    // 16비트
            2,     // 스테레오
            4,     // 4 bytes/frame
            44100, // frame rate와 샘플링 레이트 일치
            false  // 리틀 엔디안
    );

    public static final AudioFormat STEREO_FORMAT_SR240_B16 = new AudioFormat(
            AudioFormat.Encoding.PCM_SIGNED,
            24000, // 44.1kHz로 변환
            16,    // 16비트
            2,     // 스테레오
            4,     // 4 bytes/frame
            24000, // frame rate와 샘플링 레이트 일치
            false  // 리틀 엔디안
    );

    public static AudioFormat customAudioFormat(int sampleRate, int channels) {
        if (channels != 1 && channels != 2) {
            channels = 1;
        }
        return new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                sampleRate, // 44.1kHz로 변환
                16,    // 16비트
                channels,     // 스테레오
                channels * 2,     // 4 bytes/frame
                sampleRate, // frame rate와 샘플링 레이트 일치
                false  // 리틀 엔디안
        );
    }
}