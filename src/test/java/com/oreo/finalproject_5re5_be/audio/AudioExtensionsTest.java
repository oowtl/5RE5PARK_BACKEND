package com.oreo.finalproject_5re5_be.audio;

import static org.assertj.core.api.Assertions.assertThat;

import com.oreo.finalproject_5re5_be.global.component.audio.AudioExtensions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class AudioExtensionsTest {

    @Test
    @DisplayName("확장자 검사 false 테스트")
    void supportedExtensionTest() {
        // 지원하는 시그니처가 아닌 임의의 값을 넣었을 때 실패
        assertThat(AudioExtensions.isSupported("no")).isFalse();
    }

    @Test
    @DisplayName("확장자 검사 true 테스트")
    void isWavFileExtensionTest() {
        // 지원하는 시그니처인 경우 성공
        assertThat(AudioExtensions.isWavExtension("52494646")).isTrue();
    }
}
