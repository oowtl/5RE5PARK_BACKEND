package com.oreo.finalproject_5re5_be.audio;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class AudioExtensionsTest {

    @Test
    @DisplayName("확장자 검사 false 테스트")
    void supportedExtensionTest() {
        assertThat(AudioExtensions.isSupported("no")).isFalse();
    }

    @Test
    @DisplayName("확장자 검사 true 테스트")
    void isWavFileExtensionTest() {
        assertThat(AudioExtensions.isWavExtension("52494646")).isTrue();
    }
}