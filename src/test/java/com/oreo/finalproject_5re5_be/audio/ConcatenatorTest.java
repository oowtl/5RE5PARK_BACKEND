package com.oreo.finalproject_5re5_be.audio;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;


import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-ndb-test.properties")
class ConcatenatorTest {

    @Test
    @DisplayName("오디오 포맷 일치 테스트 성공")
    void formattingTestSuccess() throws UnsupportedAudioFileException, IOException {
        File file = new File("test.wav");
        File file2 = new File("formatTest.wav");

        AudioInputStream formatting = Concatenator.formatting(AudioSystem.getAudioInputStream(file));
        AudioInputStream formatting1 = Concatenator.formatting(AudioSystem.getAudioInputStream(file2));

        System.out.println("formatting1.getFormat().toString() = " + formatting1.getFormat().toString());
        assertThat(formatting1.getFormat().toString()).isEqualTo(formatting.getFormat().toString());
    }
    @Test
    @DisplayName("오디오 포맷 일치 테스트 실패")
    void formattingTestFail() throws UnsupportedAudioFileException, IOException {
        File file = new File("test.wav");
        File file2 = new File("formatTest.wav");

        AudioInputStream formatting = Concatenator.formatting(AudioSystem.getAudioInputStream(file));
        AudioInputStream formattingFile2 = AudioSystem.getAudioInputStream(file2);

        System.out.println("formatting1.getFormat().toString() = " + formattingFile2.getFormat().toString());
        assertThat(formattingFile2.getFormat().toString()).isNotEqualTo(formatting.getFormat().toString());
    }

    @Test
    @DisplayName("오디오 포멧 변경 테스트")
    void formattingTest() throws UnsupportedAudioFileException, IOException {
        File file = new File("formatTest.wav");
        AudioInputStream formatting = Concatenator.formatting(AudioSystem.getAudioInputStream(file));

        assertThat(formatting.getFormat().toString())
                .isEqualTo("PCM_SIGNED 44100.0 Hz, 16 bit, stereo, 4 bytes/frame, little-endian");

    }
}