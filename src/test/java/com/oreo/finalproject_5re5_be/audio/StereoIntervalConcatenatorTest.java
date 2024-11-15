package com.oreo.finalproject_5re5_be.audio;

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

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StereoIntervalConcatenatorTest {


    @Test
    @DisplayName("스테레오 병합 성공")
    void stereoIntervalConcatenatorTest() throws UnsupportedAudioFileException, IOException {
        AudioResample audioResample = new AudioResample(AudioFormats.STEREO_FORMAT);

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

        //스테레오 병합
        IntervalConcatenator intervalConcatenator = new StereoIntervalConcatenator(AudioFormats.STEREO_FORMAT);
        ByteArrayOutputStream byteArrayOutputStream = intervalConcatenator.intervalConcatenate(audioInputStream, 100f);

        //테스트
        assertThatCode(() -> audioResample.resample(byteArrayOutputStream)).doesNotThrowAnyException();
    }


    @Test
    @DisplayName("스테레오 포맷이면 성공한다.")
    void stereoIntervalConcatenatorFormatTest() throws UnsupportedAudioFileException, IOException {
        AudioResample audioResample = new AudioResample(AudioFormats.STEREO_FORMAT);

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

        //스테레오 병합
        IntervalConcatenator intervalConcatenator = new StereoIntervalConcatenator(AudioFormats.STEREO_FORMAT);
        ByteArrayOutputStream byteArrayOutputStream = intervalConcatenator.intervalConcatenate(audioInputStream, 100f);

        //테스트
        assertThatCode(() -> audioResample.resample(byteArrayOutputStream)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("스테레오 포맷이 아니면 실패한다.")
    void stereoIntervalConcatenatorFormatFailTest() throws UnsupportedAudioFileException, IOException {
        AudioResample audioResample = new AudioResample(AudioFormats.MONO_FORMAT);

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

        //스테레오 병합
        IntervalConcatenator intervalConcatenator = new StereoIntervalConcatenator(AudioFormats.MONO_FORMAT);
        ByteArrayOutputStream byteArrayOutputStream = intervalConcatenator.intervalConcatenate(audioInputStream, 100f);

        //테스트
        assertThatThrownBy(() -> audioResample.resample(byteArrayOutputStream)).isInstanceOf(NullPointerException.class);
    }
}