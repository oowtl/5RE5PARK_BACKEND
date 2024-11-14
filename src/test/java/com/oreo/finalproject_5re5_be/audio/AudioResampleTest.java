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

    // 모노 오디오 포맷을 60kHz, 16비트로 설정
    private static final AudioFormat monoFormat = new AudioFormat(
            AudioFormat.Encoding.PCM_SIGNED,
            60000, // 60kHz로 변환
            16,    // 16비트
            1,     // 모노
            2,     // 2 bytes/frame
            60000, // frame rate와 샘플링 레이트 일치
            false  // 리틀 엔디안
    );

    // 스테레오 오디오 포맷을 60kHz, 16비트로 설정
    private static final AudioFormat stereoFormat = new AudioFormat(
            AudioFormat.Encoding.PCM_SIGNED,
            60000, // 60kHz로 변환
            16,    // 16비트
            2,     // 스테레오
            4,     // 4 bytes/frame
            60000, // frame rate와 샘플링 레이트 일치
            false  // 리틀 엔디안
    );

    // 모노 리샘플링 설정 객체 생성
    AudioResample audioMonoResample = new AudioResample(monoFormat);

    // 스테레오 리샘플링 설정 객체 생성
    AudioResample audioStereoResample = new AudioResample(stereoFormat);

    @Test
    @DisplayName("모노 리샘플링 개별 성공 테스트")
    void resampleMonoPartSuccess() throws UnsupportedAudioFileException, IOException {
        // 테스트용 파일 로드
        File file = new File("test.wav");
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);

        // 모노 리샘플링 후 포맷이 기대한 포맷과 일치하는지 확인
        AudioFormat format = audioMonoResample.resample(audioInputStream).getFormat();
        assertThat(format.matches(monoFormat)).isTrue();
    }

    @Test
    @DisplayName("모노 리샘플링 개별 실패 테스트")
    void resampleMonoPartFail() throws UnsupportedAudioFileException, IOException {
        // 테스트용 파일 로드
        File file = new File("test.wav");
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);

        // 모노 리샘플링 후 포맷이 스테레오 포맷과 일치하지 않는지 확인
        AudioFormat format = audioMonoResample.resample(audioInputStream).getFormat();
        assertThat(format.matches(stereoFormat)).isFalse();
    }

    @Test
    @DisplayName("모노 리샘플링 리스트 성공 테스트")
    void resampleMonoSerialSuccess() throws UnsupportedAudioFileException, IOException {
        // 동일한 테스트 파일 두 개로 리스트 생성
        File file = new File("test.wav");
        File file2 = new File("test.wav");
        List<AudioInputStream> audioInputStream = List.of(
                AudioSystem.getAudioInputStream(file), AudioSystem.getAudioInputStream(file2)
        );

        // 리스트 내 모든 오디오가 모노 포맷과 일치하는지 확인
        audioMonoResample
                .resample(audioInputStream)
                .forEach(ais -> assertThat(ais.getFormat().matches(monoFormat)).isTrue());
    }

    @Test
    @DisplayName("모노 리샘플링 리스트 실패 테스트")
    void resampleMonoSerialFail() throws UnsupportedAudioFileException, IOException {
        // 동일한 테스트 파일 두 개로 리스트 생성
        File file = new File("test.wav");
        File file2 = new File("test.wav");
        List<AudioInputStream> audioInputStream = List.of(
                AudioSystem.getAudioInputStream(file), AudioSystem.getAudioInputStream(file2)
        );

        // 리스트 내 모든 오디오가 스테레오 포맷과 일치하지 않는지 확인
        audioMonoResample
                .resample(audioInputStream)
                .forEach(ais -> assertThat(ais.getFormat().matches(stereoFormat)).isFalse());
    }

    @Test
    @DisplayName("스테레오 리샘플링 개별 성공 테스트")
    void resampleStereoPartSuccess() throws UnsupportedAudioFileException, IOException {
        // 테스트용 파일 로드
        File file = new File("test.wav");
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);

        // 스테레오 리샘플링 후 포맷이 기대한 포맷과 일치하는지 확인
        AudioFormat format = audioStereoResample.resample(audioInputStream).getFormat();
        assertThat(format.matches(stereoFormat)).isTrue();
    }

    @Test
    @DisplayName("스테레오 리샘플링 개별 실패 테스트")
    void resampleStereoPartFail() throws UnsupportedAudioFileException, IOException {
        // 테스트용 파일 로드
        File file = new File("test.wav");
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);

        // 스테레오 리샘플링 후 포맷이 모노 포맷과 일치하지 않는지 확인
        AudioFormat format = audioStereoResample.resample(audioInputStream).getFormat();
        assertThat(format.matches(monoFormat)).isFalse();
    }

    @Test
    @DisplayName("스테레오 리샘플링 리스트 성공 테스트")
    void resampleStereoSerialSuccess() throws UnsupportedAudioFileException, IOException {
        // 동일한 테스트 파일 두 개로 리스트 생성
        File file = new File("test.wav");
        File file2 = new File("test.wav");
        List<AudioInputStream> audioInputStream = List.of(
                AudioSystem.getAudioInputStream(file), AudioSystem.getAudioInputStream(file2)
        );

        // 리스트 내 모든 오디오가 스테레오 포맷과 일치하는지 확인
        audioStereoResample
                .resample(audioInputStream)
                .forEach(ais -> assertThat(ais.getFormat().matches(stereoFormat)).isTrue());
    }

    @Test
    @DisplayName("스테레오 리샘플링 리스트 실패 테스트")
    void resampleStereoSerialFail() throws UnsupportedAudioFileException, IOException {
        // 동일한 테스트 파일 두 개로 리스트 생성
        File file = new File("test.wav");
        File file2 = new File("test.wav");
        List<AudioInputStream> audioInputStream = List.of(
                AudioSystem.getAudioInputStream(file), AudioSystem.getAudioInputStream(file2)
        );

        // 리스트 내 모든 오디오가 모노 포맷과 일치하지 않는지 확인
        audioStereoResample
                .resample(audioInputStream)
                .forEach(ais -> assertThat(ais.getFormat().matches(monoFormat)).isFalse());
    }
}
