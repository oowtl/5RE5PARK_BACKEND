package com.oreo.finalproject_5re5_be.audio;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.oreo.finalproject_5re5_be.concat.service.concatenator.MonoConcatenator;
import com.oreo.finalproject_5re5_be.global.component.audio.AudioExtensionConverter;
import com.oreo.finalproject_5re5_be.global.component.audio.AudioFormats;
import com.oreo.finalproject_5re5_be.global.component.audio.AudioResample;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MonoConcatenatorTest {
    public static final MonoConcatenator monoConcatenator = new MonoConcatenator(); // 기본 병합

    @Test
    @DisplayName("병합한 오디오가 모노 SR441_B16포맷이다.")
    void concatenateSR441_B16Test() throws UnsupportedAudioFileException, IOException {
        AudioResample audioResample =
                new AudioResample(AudioFormats.MONO_FORMAT_SR441_B16); // 기본 리샘플 클래스
        // 오디오 파일 불러오기
        File inputFile = new File("test.mp3");
        byte[] bytes = AudioExtensionConverter.mp3ToWav(inputFile);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        AudioInputStream inputAIS = AudioSystem.getAudioInputStream(byteArrayInputStream);

        File inputFile2 = new File("ttsoutput.mp3");
        byte[] bytes2 = AudioExtensionConverter.mp3ToWav(inputFile2);
        ByteArrayInputStream byteArrayInputStream2 = new ByteArrayInputStream(bytes2);
        AudioInputStream inputAIS2 = AudioSystem.getAudioInputStream(byteArrayInputStream2);

        // 병합
        ByteArrayOutputStream concatenate =
                monoConcatenator.concatenate(audioResample.resample(List.of(inputAIS, inputAIS2)));

        // 병합된 오디오를 리샘플링
        AudioInputStream resample = audioResample.resample(concatenate);

        // 변환 한 오디오 포맷 확인
        assertThat(resample.getFormat().matches(AudioFormats.MONO_FORMAT_SR441_B16)).isTrue();
    }

    @Test
    @DisplayName("병합한 오디오가 모노 SR441_B32포맷이다.")
    void concatenateSR441_B32Test() throws UnsupportedAudioFileException, IOException {
        AudioResample audioResample =
                new AudioResample(AudioFormats.MONO_FORMAT_SR240_B32); // 기본 리샘플 클래스
        // 오디오 파일 불러오기
        File inputFile = new File("test.mp3");
        byte[] bytes = AudioExtensionConverter.mp3ToWav(inputFile);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        AudioInputStream inputAIS = AudioSystem.getAudioInputStream(byteArrayInputStream);

        File inputFile2 = new File("ttsoutput.mp3");
        byte[] bytes2 = AudioExtensionConverter.mp3ToWav(inputFile2);
        ByteArrayInputStream byteArrayInputStream2 = new ByteArrayInputStream(bytes2);
        AudioInputStream inputAIS2 = AudioSystem.getAudioInputStream(byteArrayInputStream2);

        // 병합
        ByteArrayOutputStream concatenate =
                monoConcatenator.concatenate(audioResample.resample(List.of(inputAIS, inputAIS2)));

        // 병합된 오디오를 리샘플링
        AudioInputStream resample = audioResample.resample(concatenate);

        // 변환 한 오디오 포맷 확인
        assertThat(resample.getFormat().matches(AudioFormats.MONO_FORMAT_SR240_B32)).isTrue();
    }

    @Test
    @DisplayName("병합한 오디오가 모노 SR240_B16포맷이다.")
    void concatenateSR240_B16Test() throws UnsupportedAudioFileException, IOException {
        AudioResample audioResample =
                new AudioResample(AudioFormats.MONO_FORMAT_SR240_B16); // 기본 리샘플 클래스
        // 오디오 파일 불러오기
        File inputFile = new File("test.mp3");
        byte[] bytes = AudioExtensionConverter.mp3ToWav(inputFile);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        AudioInputStream inputAIS = AudioSystem.getAudioInputStream(byteArrayInputStream);

        File inputFile2 = new File("ttsoutput.mp3");
        byte[] bytes2 = AudioExtensionConverter.mp3ToWav(inputFile2);
        ByteArrayInputStream byteArrayInputStream2 = new ByteArrayInputStream(bytes2);
        AudioInputStream inputAIS2 = AudioSystem.getAudioInputStream(byteArrayInputStream2);

        // 병합
        ByteArrayOutputStream concatenate =
                monoConcatenator.concatenate(audioResample.resample(List.of(inputAIS, inputAIS2)));

        // 병합된 오디오를 리샘플링
        AudioInputStream resample = audioResample.resample(concatenate);

        // 변환 한 오디오 포맷 확인
        assertThat(resample.getFormat().matches(AudioFormats.MONO_FORMAT_SR240_B16)).isTrue();
    }

    @Test
    @DisplayName("병합한 오디오가 모노 SR240_B32포맷이다.")
    void concatenateSR240_B32Test() throws UnsupportedAudioFileException, IOException {
        AudioResample audioResample =
                new AudioResample(AudioFormats.MONO_FORMAT_SR240_B32); // 기본 리샘플 클래스
        // 오디오 파일 불러오기
        File inputFile = new File("test.mp3");
        byte[] bytes = AudioExtensionConverter.mp3ToWav(inputFile);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        AudioInputStream inputAIS = AudioSystem.getAudioInputStream(byteArrayInputStream);

        File inputFile2 = new File("ttsoutput.mp3");
        byte[] bytes2 = AudioExtensionConverter.mp3ToWav(inputFile2);
        ByteArrayInputStream byteArrayInputStream2 = new ByteArrayInputStream(bytes2);
        AudioInputStream inputAIS2 = AudioSystem.getAudioInputStream(byteArrayInputStream2);

        // 병합
        ByteArrayOutputStream concatenate =
                monoConcatenator.concatenate(audioResample.resample(List.of(inputAIS, inputAIS2)));

        // 병합된 오디오를 리샘플링
        AudioInputStream resample = audioResample.resample(concatenate);

        // 변환 한 오디오 포맷 확인
        assertThat(resample.getFormat().matches(AudioFormats.MONO_FORMAT_SR240_B32)).isTrue();
    }

    @Test
    @DisplayName("스테레오 포멧을을 넣으면 null을 리턴한다.")
    void concatenateFormatMissMatch() throws UnsupportedAudioFileException, IOException {
        AudioResample audioResample =
                new AudioResample(AudioFormats.STEREO_FORMAT_SR441_B32); // 기본 리샘플 클래스
        // 오디오 파일 불러오기
        File inputFile = new File("test.mp3");
        byte[] bytes = AudioExtensionConverter.mp3ToWav(inputFile);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        AudioInputStream inputAIS = AudioSystem.getAudioInputStream(byteArrayInputStream);

        File inputFile2 = new File("ttsoutput.mp3");
        byte[] bytes2 = AudioExtensionConverter.mp3ToWav(inputFile2);
        ByteArrayInputStream byteArrayInputStream2 = new ByteArrayInputStream(bytes2);
        AudioInputStream inputAIS2 = AudioSystem.getAudioInputStream(byteArrayInputStream2);

        // 병합
        assertThatThrownBy(
                        () ->
                                monoConcatenator.concatenate(audioResample.resample(List.of(inputAIS, inputAIS2))))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
