package com.oreo.finalproject_5re5_be.audio;

import static org.assertj.core.api.Assertions.*;

import com.oreo.finalproject_5re5_be.global.component.audio.BeepMaker;
import com.oreo.finalproject_5re5_be.global.component.audio.SoundPlayer;
import javax.sound.sampled.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

public class SoundPlayerTest {
    private final SoundPlayer soundPlayer = new SoundPlayer();

    public static final AudioFormat outputFormat =
            new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    44100, // 44.1kHz로 변환
                    16, // 16비트
                    2, // 모노
                    4, // 2 bytes/frame
                    44100, // frame rate와 샘플링 레이트 일치
                    false // 리틀 엔디안
                    );

    @Test
    @DisabledOnOs({OS.LINUX}) // 테스코드가 LINUX 환경에서 돌아가지 않게 해주는 어노테이션
    void soundPlayerTest() {
        //        File file = new File("");
        //        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);

        AudioInputStream audioData = BeepMaker.makeSound(500, 100, outputFormat);
        // 아무 예외도 던지지 않는것을 확인
        assertThatCode(() -> soundPlayer.play(audioData)).doesNotThrowAnyException();
    }
}
