package com.oreo.finalproject_5re5_be.audio;

import com.oreo.finalproject_5re5_be.global.component.audio.AudioExtensionConverter;
import com.oreo.finalproject_5re5_be.global.component.audio.AudioResample;
import com.oreo.finalproject_5re5_be.concat.service.concatenator.MonoConcatenator;
import org.junit.jupiter.api.Test;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MonoConcatenatorTest {
    public static final MonoConcatenator monoConcatenator = new MonoConcatenator();//기본 병합

    @Test
    void concatenateTest() throws UnsupportedAudioFileException, IOException {
        AudioResample audioResample = new AudioResample();// 기본 리샘플 클래스
        // 오디오 파일 불러오기
        File inputFile = new File("test.mp3");
        byte[] bytes = AudioExtensionConverter.mp3ToWav(inputFile);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        AudioInputStream inputAIS = AudioSystem.getAudioInputStream(byteArrayInputStream);


        File inputFile2 = new File("ttsoutput.mp3");
        byte[] bytes2 = AudioExtensionConverter.mp3ToWav(inputFile2);
        ByteArrayInputStream byteArrayInputStream2 = new ByteArrayInputStream(bytes2);
        AudioInputStream inputAIS2 = AudioSystem.getAudioInputStream(byteArrayInputStream2);


        //병합
        ByteArrayOutputStream concatenate = monoConcatenator
                .concatenate(audioResample.resample(List.of(inputAIS, inputAIS2)));

        //병합된 오디오를 리샘플링
        AudioInputStream resample = audioResample.resample(concatenate);

        //변환 한 오디오 포맷 확인
        assertThat(resample.getFormat()
                .toString()).isEqualTo("PCM_SIGNED 44100.0 Hz, 16 bit, mono, 2 bytes/frame, little-endian");
    }
}