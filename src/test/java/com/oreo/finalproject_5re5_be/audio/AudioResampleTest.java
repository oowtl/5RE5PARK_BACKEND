package com.oreo.finalproject_5re5_be.audio;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AudioResampleTest {

    private static final AudioFormat monoFormat = new AudioFormat(
            AudioFormat.Encoding.PCM_SIGNED,
            60000, // 60kHz로 변환
            16,    // 16비트
            1,     // 모노
            2,     // 2 bytes/frame
            60000, // frame rate와 샘플링 레이트 일치
            false  // 리틀 엔디안
    );
    private static final AudioFormat stereoFormat = new AudioFormat(
            AudioFormat.Encoding.PCM_SIGNED,
            60000, // 60kHz로 변환
            16,    // 16비트
            2,     // 스테레오
            4,     // 4 bytes/frame
            60000, // frame rate와 샘플링 레이트 일치
            false  // 리틀 엔디안
    );

    AudioResample audioMonoResample = new AudioResample(monoFormat);
    AudioResample audioStereoResample = new AudioResample(stereoFormat);



    @Test
    @DisplayName("모노 리셈플링 개별 성공 테스트")
    void resampleMonoPartSuccess() throws UnsupportedAudioFileException, IOException {
        File file = new File("test.wav");
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
        AudioFormat format = audioMonoResample.resample(audioInputStream).getFormat();
        assertThat(format.matches(monoFormat)).isTrue();
    }
    @Test
    @DisplayName("모노 리셈플링 개별 실패 테스트")
    void resampleMonoPartFail() throws UnsupportedAudioFileException, IOException {
        File file = new File("test.wav");
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
        AudioFormat format = audioMonoResample.resample(audioInputStream).getFormat();
        assertThat(format.matches(stereoFormat)).isFalse();
    }

    @Test
    @DisplayName("모노 리셈플링 리스트 성공 테스트")
    void resampleMonoSerialSuccess() throws UnsupportedAudioFileException, IOException {
        File file = new File("test.wav");
        File file2 = new File("test.wav");
        List<AudioInputStream> audioInputStream
                = List.of(AudioSystem.getAudioInputStream(file), AudioSystem.getAudioInputStream(file2));

        audioMonoResample
                .resample(audioInputStream)
                .forEach(ais -> assertThat(ais.getFormat().matches(monoFormat)).isTrue());
    }
    @Test
    @DisplayName("모노 리셈플링 리스트 실패 테스트")
    void resampleMonoSerialFail() throws UnsupportedAudioFileException, IOException {
        File file = new File("test.wav");
        File file2 = new File("test.wav");
        List<AudioInputStream> audioInputStream
                = List.of(AudioSystem.getAudioInputStream(file), AudioSystem.getAudioInputStream(file2));

        audioMonoResample
                .resample(audioInputStream)
                .forEach(ais -> assertThat(ais.getFormat().matches(stereoFormat)).isFalse());
    }

    @Test
    @DisplayName("스테레오 리셈플링 개별 성공 테스트")
    void resampleStereoPartSuccess() throws UnsupportedAudioFileException, IOException {
        File file = new File("test.wav");
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
        AudioFormat format = audioStereoResample.resample(audioInputStream).getFormat();
        assertThat(format.matches(stereoFormat)).isTrue();
    }
    @Test
    @DisplayName("스테레오 리셈플링 개별 실패 테스트")
    void resampleStereoPartFail() throws UnsupportedAudioFileException, IOException {
        File file = new File("test.wav");
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
        AudioFormat format = audioStereoResample.resample(audioInputStream).getFormat();
        assertThat(format.matches(monoFormat)).isFalse();
    }

    @Test
    @DisplayName("스테레오 리셈플링 리스트 성공 테스트")
    void resampleStereoSerialSuccess() throws UnsupportedAudioFileException, IOException {
        File file = new File("test.wav");
        File file2 = new File("test.wav");
        List<AudioInputStream> audioInputStream
                = List.of(AudioSystem.getAudioInputStream(file), AudioSystem.getAudioInputStream(file2));

        audioStereoResample.resample(audioInputStream).forEach(ais -> assertThat(ais.getFormat().matches(stereoFormat)).isTrue());
    }
    @Test
    @DisplayName("스테레오 리셈플링 리스트 실패 테스트")
    void resampleStereoSerialFail() throws UnsupportedAudioFileException, IOException {
        File file = new File("test.wav");
        File file2 = new File("test.wav");
        List<AudioInputStream> audioInputStream
                = List.of(AudioSystem.getAudioInputStream(file), AudioSystem.getAudioInputStream(file2));

        audioStereoResample.resample(audioInputStream).forEach(ais -> assertThat(ais.getFormat().matches(monoFormat)).isFalse());
    }
}