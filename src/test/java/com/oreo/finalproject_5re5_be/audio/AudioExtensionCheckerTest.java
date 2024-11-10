package com.oreo.finalproject_5re5_be.audio;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.io.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class AudioExtensionCheckerTest {
    private File mp3 = new File("test.mp3");
    private File wav = new File("test.wav");

    @Test
    @DisplayName("wav 확장자 검사 성공 Parameter : File")
    void checkAudioExtensionIsWav() throws IOException {
        assertThat(AudioExtensionChecker.isWavExtension(wav)).isTrue();
    }

    @Test
    @DisplayName("wav 확장자 검사 실패 Parameter : File")
    void checkAudioWavExtensionIsWavFalse() throws IOException {
        assertThat(AudioExtensionChecker.isWavExtension(mp3)).isFalse();
    }

    @Test
    @DisplayName("mp3 확장자 검사 Parameter : File")
    void checkAudioExtensionIsMP3() throws IOException {
        assertThat(AudioExtensionChecker.isMp3Extension(mp3)).isTrue();
    }

    @Test
    @DisplayName("mp3 확장자 검사 실패 Parameter : File")
    void checkAudioExtensionIsMP3False() throws IOException {
        assertThat(AudioExtensionChecker.isMp3Extension(wav)).isFalse();
    }

    @Test
    @DisplayName("mp3 확장자 검사 Parameter : ByteArray")
    void checkAudioExtensionIsMP3ForByteArray() throws IOException {
        byte[] bytes;
        try (FileInputStream fileInputStream = new FileInputStream(mp3)) {
            bytes = fileInputStream.readAllBytes();
        }
        assertThat(AudioExtensionChecker.isMp3Extension(bytes)).isTrue();
    }

    @Test
    @DisplayName("mp3 확장자 검사 실패 Parameter : ByteArray")
    void checkAudioExtensionIsMP3ForByteArrayFalse() throws IOException {
        byte[] bytes;
        try (FileInputStream fileInputStream = new FileInputStream(wav)) {
            bytes = fileInputStream.readAllBytes();
        }
        assertThat(AudioExtensionChecker.isMp3Extension(bytes)).isFalse();
    }

    @Test
    @DisplayName("wav 확장자 검사 Parameter : ByteArray")
    void checkAudioExtensionIsWavForByteArray() throws IOException {
        byte[] bytes;
        try (FileInputStream fileInputStream = new FileInputStream(wav)) {
            bytes = fileInputStream.readAllBytes();
        }
        assertThat(AudioExtensionChecker.isWavExtension(bytes)).isTrue();
    }

    @Test
    @DisplayName("wav 확장자 검사 실패 Parameter : ByteArray")
    void checkAudioExtensionIsWavForByteArrayFalse() throws IOException {
        byte[] bytes;
        try (FileInputStream fileInputStream = new FileInputStream(mp3)) {
            bytes = fileInputStream.readAllBytes();
        }
        assertThat(AudioExtensionChecker.isWavExtension(bytes)).isFalse();
    }
}