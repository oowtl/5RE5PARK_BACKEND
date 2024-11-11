package com.oreo.finalproject_5re5_be.audio;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class AudioExtensionConverterTest {

    @Test
    @DisplayName("wav 파일 변환 테스트")
    void convertMp3ToWavTest() throws UnsupportedAudioFileException, IOException {
        byte[] audioInputStream = AudioExtensionConverter.mp3ToWav(new File("test.mp3"));
        boolean wavExtension = AudioExtensionChecker.isWavExtension(audioInputStream);

        assertThat(wavExtension).isTrue();
    }

    @Test
    @DisplayName("이미 wav형식 파일인 경우 테스트")
    void convertWavToWavTest() throws UnsupportedAudioFileException, IOException {
        byte[] audioInputStream = AudioExtensionConverter.mp3ToWav(new File("test.wav"));
        boolean wavExtension = AudioExtensionChecker.isWavExtension(audioInputStream);

        assertThat(wavExtension).isTrue();
    }


}