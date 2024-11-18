package com.oreo.finalproject_5re5_be.audio;

import com.oreo.finalproject_5re5_be.global.component.audio.*;
import com.oreo.finalproject_5re5_be.concat.service.concatenator.AudioProperties;
import com.oreo.finalproject_5re5_be.concat.service.concatenator.IntervalConcatenator;
import com.oreo.finalproject_5re5_be.concat.service.concatenator.MonoIntervalConcatenator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class MonoIntervalConcatenatorTest {

    @Test
    @DisplayName("오디오 포맷이 모노 SR441_B16이다.")
    void monoIntervalConcatenatorFormat_SR441_B16() throws UnsupportedAudioFileException, IOException {
        AudioResample audioResample = new AudioResample(AudioFormats.MONO_FORMAT_SR441_B16);


        //오디오 불러오기
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(AudioExtensionConverter.mp3ToWav(new File("ttsoutput.mp3")));
        ByteArrayInputStream byteArrayInputStream1 = new ByteArrayInputStream(AudioExtensionConverter.mp3ToWav(new File("test.mp3")));

        //오디오 스트림 변환
        AudioInputStream t1 = AudioSystem.getAudioInputStream(byteArrayInputStream);
        AudioInputStream t2 = AudioSystem.getAudioInputStream(byteArrayInputStream1);

        //AudioProperties 타입으로 리스트에 넣기
        List<AudioProperties> audioInputStream = List.of(
                new AudioProperties(audioResample.resample(t1), 3000f),
                new AudioProperties(audioResample.resample(t2), 500f)
        );

        //병합
        IntervalConcatenator intervalConcatenator = new MonoIntervalConcatenator(AudioFormats.MONO_FORMAT_SR441_B16);
        ByteArrayOutputStream byteArrayOutputStream = intervalConcatenator.intervalConcatenate(audioInputStream, 10000f);

        AudioInputStream resample = audioResample.resample(byteArrayOutputStream);
        //테스트
        assertThat(resample.getFormat()).isEqualTo(AudioFormats.MONO_FORMAT_SR441_B16);
    }

    @Test
    @DisplayName("오디오 포맷이 모노 SR441_B32이다.")
    void monoIntervalConcatenatorFormat_SR441_B32() throws UnsupportedAudioFileException, IOException {
        AudioResample audioResample = new AudioResample(AudioFormats.MONO_FORMAT_SR441_B32);


        //오디오 불러오기
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(AudioExtensionConverter.mp3ToWav(new File("ttsoutput.mp3")));
        ByteArrayInputStream byteArrayInputStream1 = new ByteArrayInputStream(AudioExtensionConverter.mp3ToWav(new File("test.mp3")));

        //오디오 스트림 변환
        AudioInputStream t1 = AudioSystem.getAudioInputStream(byteArrayInputStream);
        AudioInputStream t2 = AudioSystem.getAudioInputStream(byteArrayInputStream1);

        //AudioProperties 타입으로 리스트에 넣기
        List<AudioProperties> audioInputStream = List.of(
                new AudioProperties(audioResample.resample(t1), 3000f),
                new AudioProperties(audioResample.resample(t2), 500f)
        );

        //병합
        IntervalConcatenator intervalConcatenator = new MonoIntervalConcatenator(AudioFormats.MONO_FORMAT_SR441_B32);
        ByteArrayOutputStream byteArrayOutputStream = intervalConcatenator.intervalConcatenate(audioInputStream, 2000f);

        AudioInputStream resample = audioResample.resample(byteArrayOutputStream);
        //테스트
        assertThat(resample.getFormat()).isEqualTo(AudioFormats.MONO_FORMAT_SR441_B32);
    }

    @Test
    @DisplayName("오디오 포맷이 모노 SR240_B32이다.")
    void monoIntervalConcatenatorFormat_SR240_B32() throws UnsupportedAudioFileException, IOException {
        AudioResample audioResample = new AudioResample(AudioFormats.MONO_FORMAT_SR240_B32);


        //오디오 불러오기
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(AudioExtensionConverter.mp3ToWav(new File("ttsoutput.mp3")));
        ByteArrayInputStream byteArrayInputStream1 = new ByteArrayInputStream(AudioExtensionConverter.mp3ToWav(new File("test.mp3")));

        //오디오 스트림 변환
        AudioInputStream t1 = AudioSystem.getAudioInputStream(byteArrayInputStream);
        AudioInputStream t2 = AudioSystem.getAudioInputStream(byteArrayInputStream1);

        //AudioProperties 타입으로 리스트에 넣기
        List<AudioProperties> audioInputStream = List.of(
                new AudioProperties(audioResample.resample(t1), 3000f),
                new AudioProperties(audioResample.resample(t2), 500f)
        );

        //병합
        IntervalConcatenator intervalConcatenator = new MonoIntervalConcatenator(AudioFormats.MONO_FORMAT_SR240_B32);
        ByteArrayOutputStream byteArrayOutputStream = intervalConcatenator.intervalConcatenate(audioInputStream, 2000f);

        AudioInputStream resample = audioResample.resample(byteArrayOutputStream);
        //테스트
        assertThat(resample.getFormat()).isEqualTo(AudioFormats.MONO_FORMAT_SR240_B32);
    }

    @Test
    @DisplayName("오디오 포맷이 모노 SR240_B16이다.")
    void monoIntervalConcatenatorFormat_SR240_B16() throws UnsupportedAudioFileException, IOException {
        AudioResample audioResample = new AudioResample(AudioFormats.MONO_FORMAT_SR240_B16);


        //오디오 불러오기
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(AudioExtensionConverter.mp3ToWav(new File("ttsoutput.mp3")));
        ByteArrayInputStream byteArrayInputStream1 = new ByteArrayInputStream(AudioExtensionConverter.mp3ToWav(new File("test.mp3")));

        //오디오 스트림 변환
        AudioInputStream t1 = AudioSystem.getAudioInputStream(byteArrayInputStream);
        AudioInputStream t2 = AudioSystem.getAudioInputStream(byteArrayInputStream1);

        //AudioProperties 타입으로 리스트에 넣기
        List<AudioProperties> audioInputStream = List.of(
                new AudioProperties(audioResample.resample(t1), 3000f),
                new AudioProperties(audioResample.resample(t2), 500f)
        );

        //병합
        IntervalConcatenator intervalConcatenator = new MonoIntervalConcatenator(AudioFormats.MONO_FORMAT_SR240_B16);
        ByteArrayOutputStream byteArrayOutputStream = intervalConcatenator.intervalConcatenate(audioInputStream, 2000f);

        AudioInputStream resample = audioResample.resample(byteArrayOutputStream);
        //테스트
        assertThat(resample.getFormat()).isEqualTo(AudioFormats.MONO_FORMAT_SR240_B16);
    }

    @Test
    @DisplayName("오디오 병합 테스트 성공한다.")
    void monoIntervalConcatSuccessTest() throws UnsupportedAudioFileException, IOException {
        AudioResample audioResample = new AudioResample(AudioFormats.MONO_FORMAT_SR441_B16);


        //오디오 불러오기
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(AudioExtensionConverter.mp3ToWav(new File("ttsoutput.mp3")));
        ByteArrayInputStream byteArrayInputStream1 = new ByteArrayInputStream(AudioExtensionConverter.mp3ToWav(new File("test.mp3")));

        //오디오 스트림 변환
        AudioInputStream t1 = AudioSystem.getAudioInputStream(byteArrayInputStream);
        AudioInputStream t2 = AudioSystem.getAudioInputStream(byteArrayInputStream1);

        //AudioProperties 타입으로 리스트에 넣기
        List<AudioProperties> audioInputStream = List.of(
                new AudioProperties(audioResample.resample(t1), 3000f),
                new AudioProperties(audioResample.resample(t2), 500f)
                );

        //병합
        IntervalConcatenator intervalConcatenator = new MonoIntervalConcatenator(AudioFormats.MONO_FORMAT_SR441_B16);
        ByteArrayOutputStream byteArrayOutputStream = intervalConcatenator.intervalConcatenate(audioInputStream, 10000f);

        //테스트
        assertThatCode(() -> audioResample.resample(byteArrayOutputStream)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("오디오 병합 포맷이 같으면 성공")
    void MonoIntervalConcatAudioFormatTest() throws UnsupportedAudioFileException, IOException {
        AudioResample audioResample = new AudioResample(AudioFormats.MONO_FORMAT_SR441_B16);


        //오디오 불러오기
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(AudioExtensionConverter.mp3ToWav(new File("ttsoutput.mp3")));
        ByteArrayInputStream byteArrayInputStream1 = new ByteArrayInputStream(AudioExtensionConverter.mp3ToWav(new File("test.mp3")));

        //오디오 스트림 변환
        AudioInputStream t1 = AudioSystem.getAudioInputStream(byteArrayInputStream);
        AudioInputStream t2 = AudioSystem.getAudioInputStream(byteArrayInputStream1);

        //AudioProperties 타입으로 리스트에 넣기
        List<AudioProperties> audioInputStream = List.of(
                new AudioProperties(audioResample.resample(t1), 3000f),
                new AudioProperties(audioResample.resample(t2), 500f)
        );

        //병합
        IntervalConcatenator intervalConcatenator = new MonoIntervalConcatenator(AudioFormats.MONO_FORMAT_SR441_B16);
        ByteArrayOutputStream byteArrayOutputStream = intervalConcatenator.intervalConcatenate(audioInputStream, 1f);

        //테스트
        assertThatCode(() -> audioResample.resample(byteArrayOutputStream)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("오디오 병합 포맷이 다르면 실패한다")
    void MonoIntervalConcatFailTest() throws UnsupportedAudioFileException, IOException {
        AudioResample audioResample = new AudioResample(AudioFormats.STEREO_FORMAT_SR441_B16);


        //오디오 불러오기
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(AudioExtensionConverter.mp3ToWav(new File("ttsoutput.mp3")));
        ByteArrayInputStream byteArrayInputStream1 = new ByteArrayInputStream(AudioExtensionConverter.mp3ToWav(new File("test.mp3")));

        //오디오 스트림 변환
        AudioInputStream t1 = AudioSystem.getAudioInputStream(byteArrayInputStream);
        AudioInputStream t2 = AudioSystem.getAudioInputStream(byteArrayInputStream1);

        //AudioProperties 타입으로 리스트에 넣기
        List<AudioProperties> audioInputStream = List.of(
                new AudioProperties(audioResample.resample(t1), 3000f),
                new AudioProperties(audioResample.resample(t2), 500f)
        );

        //병합
        IntervalConcatenator intervalConcatenator = new MonoIntervalConcatenator(AudioFormats.MONO_FORMAT_SR441_B16);
        ByteArrayOutputStream byteArrayOutputStream = intervalConcatenator.intervalConcatenate(audioInputStream, 1f);

        //테스트
        assertThatThrownBy(() -> audioResample.resample(byteArrayOutputStream)).isInstanceOf(NullPointerException.class);
    }
}