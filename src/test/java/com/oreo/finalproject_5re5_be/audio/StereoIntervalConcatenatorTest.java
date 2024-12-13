package com.oreo.finalproject_5re5_be.audio;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.oreo.finalproject_5re5_be.concat.service.concatenator.AudioProperties;
import com.oreo.finalproject_5re5_be.concat.service.concatenator.IntervalConcatenator;
import com.oreo.finalproject_5re5_be.concat.service.concatenator.StereoIntervalConcatenator;
import com.oreo.finalproject_5re5_be.global.component.audio.*;
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

class StereoIntervalConcatenatorTest {

    @Test
    @DisplayName("오디오 포맷이 스테레오 SR441_B16이다.")
    void monoIntervalConcatenatorFormat_SR441_B16()
            throws UnsupportedAudioFileException, IOException {
        AudioResample audioResample = new AudioResample(AudioFormats.STEREO_FORMAT_SR441_B16);

        // 오디오 불러오기
        ByteArrayInputStream byteArrayInputStream =
                new ByteArrayInputStream(AudioExtensionConverter.mp3ToWav(new File("ttsoutput.mp3")));
        ByteArrayInputStream byteArrayInputStream1 =
                new ByteArrayInputStream(AudioExtensionConverter.mp3ToWav(new File("test.mp3")));

        // 오디오 스트림 변환
        AudioInputStream t1 = AudioSystem.getAudioInputStream(byteArrayInputStream);
        AudioInputStream t2 = AudioSystem.getAudioInputStream(byteArrayInputStream1);

        // AudioProperties 타입으로 리스트에 넣기
        List<AudioProperties> audioInputStream =
                List.of(
                        new AudioProperties(audioResample.resample(t1), 3),
                        new AudioProperties(audioResample.resample(t2), 5f));

        // 병합
        IntervalConcatenator intervalConcatenator =
                new StereoIntervalConcatenator(AudioFormats.STEREO_FORMAT_SR441_B16);
        ByteArrayOutputStream byteArrayOutputStream =
                intervalConcatenator.intervalConcatenate(audioInputStream, 1f);

        AudioInputStream resample = audioResample.resample(byteArrayOutputStream);
        // 테스트
        assertThat(resample.getFormat()).isEqualTo(AudioFormats.STEREO_FORMAT_SR441_B16);
    }

    @Test
    @DisplayName("오디오 포맷이 스테레오 SR441_B32이다.")
    void monoIntervalConcatenatorFormat_SR441_B32()
            throws UnsupportedAudioFileException, IOException {
        AudioResample audioResample = new AudioResample(AudioFormats.STEREO_FORMAT_SR441_B32);

        // 오디오 불러오기
        ByteArrayInputStream byteArrayInputStream =
                new ByteArrayInputStream(AudioExtensionConverter.mp3ToWav(new File("ttsoutput.mp3")));
        ByteArrayInputStream byteArrayInputStream1 =
                new ByteArrayInputStream(AudioExtensionConverter.mp3ToWav(new File("test.mp3")));

        // 오디오 스트림 변환
        AudioInputStream t1 = AudioSystem.getAudioInputStream(byteArrayInputStream);
        AudioInputStream t2 = AudioSystem.getAudioInputStream(byteArrayInputStream1);

        // AudioProperties 타입으로 리스트에 넣기
        List<AudioProperties> audioInputStream =
                List.of(
                        new AudioProperties(audioResample.resample(t1), 3),
                        new AudioProperties(audioResample.resample(t2), 5f));

        // 병합
        IntervalConcatenator intervalConcatenator =
                new StereoIntervalConcatenator(AudioFormats.STEREO_FORMAT_SR441_B32);
        ByteArrayOutputStream byteArrayOutputStream =
                intervalConcatenator.intervalConcatenate(audioInputStream, 2);

        AudioInputStream resample = audioResample.resample(byteArrayOutputStream);
        // 테스트
        assertThat(resample.getFormat()).isEqualTo(AudioFormats.STEREO_FORMAT_SR441_B32);
    }

    @Test
    @DisplayName("오디오 포맷이 스테레오 SR240_B32이다.")
    void monoIntervalConcatenatorFormat_SR240_B32()
            throws UnsupportedAudioFileException, IOException {
        AudioResample audioResample = new AudioResample(AudioFormats.STEREO_FORMAT_SR240_B32);

        // 오디오 불러오기
        ByteArrayInputStream byteArrayInputStream =
                new ByteArrayInputStream(AudioExtensionConverter.mp3ToWav(new File("ttsoutput.mp3")));
        ByteArrayInputStream byteArrayInputStream1 =
                new ByteArrayInputStream(AudioExtensionConverter.mp3ToWav(new File("test.mp3")));

        // 오디오 스트림 변환
        AudioInputStream t1 = AudioSystem.getAudioInputStream(byteArrayInputStream);
        AudioInputStream t2 = AudioSystem.getAudioInputStream(byteArrayInputStream1);

        // AudioProperties 타입으로 리스트에 넣기
        List<AudioProperties> audioInputStream =
                List.of(
                        new AudioProperties(audioResample.resample(t1), 3),
                        new AudioProperties(audioResample.resample(t2), 5f));

        // 병합
        IntervalConcatenator intervalConcatenator =
                new StereoIntervalConcatenator(AudioFormats.STEREO_FORMAT_SR240_B32);
        ByteArrayOutputStream byteArrayOutputStream =
                intervalConcatenator.intervalConcatenate(audioInputStream, 2);

        AudioInputStream resample = audioResample.resample(byteArrayOutputStream);
        // 테스트
        assertThat(resample.getFormat()).isEqualTo(AudioFormats.STEREO_FORMAT_SR240_B32);
    }

    @Test
    @DisplayName("오디오 포맷이 스테레오 SR240_B16이다.")
    void monoIntervalConcatenatorFormat_SR240_B16()
            throws UnsupportedAudioFileException, IOException {
        AudioResample audioResample = new AudioResample(AudioFormats.STEREO_FORMAT_SR240_B16);

        // 오디오 불러오기
        ByteArrayInputStream byteArrayInputStream =
                new ByteArrayInputStream(AudioExtensionConverter.mp3ToWav(new File("ttsoutput.mp3")));
        ByteArrayInputStream byteArrayInputStream1 =
                new ByteArrayInputStream(AudioExtensionConverter.mp3ToWav(new File("test.mp3")));

        // 오디오 스트림 변환
        AudioInputStream t1 = AudioSystem.getAudioInputStream(byteArrayInputStream);
        AudioInputStream t2 = AudioSystem.getAudioInputStream(byteArrayInputStream1);

        // AudioProperties 타입으로 리스트에 넣기
        List<AudioProperties> audioInputStream =
                List.of(
                        new AudioProperties(audioResample.resample(t1), 3),
                        new AudioProperties(audioResample.resample(t2), 5f));

        // 병합
        IntervalConcatenator intervalConcatenator =
                new StereoIntervalConcatenator(AudioFormats.STEREO_FORMAT_SR240_B16);
        ByteArrayOutputStream byteArrayOutputStream =
                intervalConcatenator.intervalConcatenate(audioInputStream, 2);

        AudioInputStream resample = audioResample.resample(byteArrayOutputStream);
        // 테스트
        assertThat(resample.getFormat()).isEqualTo(AudioFormats.STEREO_FORMAT_SR240_B16);
    }

    @Test
    @DisplayName("스테레오 병합 성공")
    void stereoIntervalConcatenatorTest() throws UnsupportedAudioFileException, IOException {
        AudioResample audioResample = new AudioResample(AudioFormats.STEREO_FORMAT_SR441_B16);

        // 오디오 불러오기
        ByteArrayInputStream byteArrayInputStream =
                new ByteArrayInputStream(AudioExtensionConverter.mp3ToWav(new File("ttsoutput.mp3")));
        ByteArrayInputStream byteArrayInputStream1 =
                new ByteArrayInputStream(AudioExtensionConverter.mp3ToWav(new File("test.mp3")));

        // 오디오 스트림 변환
        AudioInputStream t1 = AudioSystem.getAudioInputStream(byteArrayInputStream);
        AudioInputStream t2 = AudioSystem.getAudioInputStream(byteArrayInputStream1);

        // AudioProperties 타입으로 리스트에 넣기
        List<AudioProperties> audioInputStream =
                List.of(
                        new AudioProperties(audioResample.resample(t1), 3),
                        new AudioProperties(audioResample.resample(t2), 5f));

        // 스테레오 병합
        IntervalConcatenator intervalConcatenator =
                new StereoIntervalConcatenator(AudioFormats.STEREO_FORMAT_SR441_B16);
        ByteArrayOutputStream byteArrayOutputStream =
                intervalConcatenator.intervalConcatenate(audioInputStream, 1f);

        // 테스트
        assertThatCode(() -> audioResample.resample(byteArrayOutputStream)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("스테레오 포맷이면 성공한다.")
    void stereoIntervalConcatenatorFormatTest() throws UnsupportedAudioFileException, IOException {
        AudioResample audioResample = new AudioResample(AudioFormats.STEREO_FORMAT_SR441_B16);

        // 오디오 불러오기
        ByteArrayInputStream byteArrayInputStream =
                new ByteArrayInputStream(AudioExtensionConverter.mp3ToWav(new File("ttsoutput.mp3")));
        ByteArrayInputStream byteArrayInputStream1 =
                new ByteArrayInputStream(AudioExtensionConverter.mp3ToWav(new File("test.mp3")));

        // 오디오 스트림 변환
        AudioInputStream t1 = AudioSystem.getAudioInputStream(byteArrayInputStream);
        AudioInputStream t2 = AudioSystem.getAudioInputStream(byteArrayInputStream1);

        // AudioProperties 타입으로 리스트에 넣기
        List<AudioProperties> audioInputStream =
                List.of(
                        new AudioProperties(audioResample.resample(t1), 3),
                        new AudioProperties(audioResample.resample(t2), 5f));

        // 스테레오 병합
        IntervalConcatenator intervalConcatenator =
                new StereoIntervalConcatenator(AudioFormats.STEREO_FORMAT_SR441_B16);
        ByteArrayOutputStream byteArrayOutputStream =
                intervalConcatenator.intervalConcatenate(audioInputStream, 1f);

        // 테스트
        assertThatCode(() -> audioResample.resample(byteArrayOutputStream)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("스테레오 포맷이 아니면 실패한다.")
    void stereoIntervalConcatenatorFormatFailTest()
            throws UnsupportedAudioFileException, IOException {
        AudioResample audioResample = new AudioResample(AudioFormats.MONO_FORMAT_SR441_B16);

        // 오디오 불러오기
        ByteArrayInputStream byteArrayInputStream =
                new ByteArrayInputStream(AudioExtensionConverter.mp3ToWav(new File("ttsoutput.mp3")));
        ByteArrayInputStream byteArrayInputStream1 =
                new ByteArrayInputStream(AudioExtensionConverter.mp3ToWav(new File("test.mp3")));

        // 오디오 스트림 변환
        AudioInputStream t1 = AudioSystem.getAudioInputStream(byteArrayInputStream);
        AudioInputStream t2 = AudioSystem.getAudioInputStream(byteArrayInputStream1);

        // AudioProperties 타입으로 리스트에 넣기
        List<AudioProperties> audioInputStream =
                List.of(
                        new AudioProperties(audioResample.resample(t1), 3),
                        new AudioProperties(audioResample.resample(t2), 5f));

        // 스테레오 병합
        IntervalConcatenator intervalConcatenator =
                new StereoIntervalConcatenator(AudioFormats.MONO_FORMAT_SR441_B16);

        // 테스트
        assertThatThrownBy(() -> intervalConcatenator.intervalConcatenate(audioInputStream, 1f))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
