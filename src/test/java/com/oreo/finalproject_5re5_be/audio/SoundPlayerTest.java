package com.oreo.finalproject_5re5_be.audio;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class SoundPlayerTest {
    private final SoundPlayer soundPlayer = new SoundPlayer();

    @Test
    @DisabledOnOs({OS.LINUX}) //테스코드가 LINUX 환경에서 돌아가지 않게 해주는 어노테이션
    void soundPlayerTest() {
//        File file = new File("");
//        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);

        byte[] audioData = BeepMaker.makeBeep(100, 1.0f);
        ByteArrayInputStream byteArrayInputStream
                = new ByteArrayInputStream(BeepMaker.makeBeep(100, 1.0f));
        AudioFormat format
                = new AudioFormat(BeepMaker.SAMPLE_RATE, 8, 1, true, false);
        AudioInputStream audioInputStream =
                new AudioInputStream(byteArrayInputStream, format, audioData.length / format.getFrameSize());

        //아무 예외도 던지지 않는것을 확인
        assertThatCode(() -> soundPlayer.play(audioInputStream))
                .doesNotThrowAnyException();
    }


}
