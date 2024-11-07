package com.oreo.finalproject_5re5_be.audio;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sound.sampled.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class BeepMakerTest {
    private SoundPlayer soundPlayer = new SoundPlayer();

    @Test
    @DisabledIfEnvironmentVariable(named = "CI", matches = "true") //테스코드가 CI 환경에서 돌아가지 않게 해주는 어노테이션
    void makeBeepSectionTest() throws IOException {
        float duration = 0.3f;//오디오 길이 설정
        byte[] audioData = BeepMaker.makeBeep(100, duration);//오디오 생성
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(audioData);//바이트 배열로 변환
        AudioFormat format = new AudioFormat(BeepMaker.SAMPLE_RATE, 8, 1, true, false);//오디오 포멧 생성

        AudioInputStream audioInputStream =
                new AudioInputStream(byteArrayInputStream, format, audioData.length / format.getFrameSize());
        //오디오의 길이는 frame으로 구분된다.
        //frameSize는 각 오디오 샘플을 구성하는 바이트 수를 의미한다.

        long startTime = System.currentTimeMillis();//시작 시간
        soundPlayer.play(audioInputStream);//오디오 재생
        long endTime = System.currentTimeMillis();//종료 시간

        //리소스를 반환하는 시간까지 존재하므로 완전히 같을 수 없음
        assertThat((endTime - startTime)/1000F).isGreaterThan(duration);

        System.out.println("(endTime - startTime)/1000F = " + (endTime - startTime)/1000F);
    }

    @Test
    @DisabledIfEnvironmentVariable(named = "CI", matches = "true")//테스코드가 CI 환경에서 돌아가지 않게 해주는 어노테이션
    void makeSilenceSectionTest() throws IOException {
        float duration = 0.3f;
        byte[] audioData = BeepMaker.makeSilence(duration);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(audioData);
        AudioFormat format = new AudioFormat(BeepMaker.SAMPLE_RATE, 8, 1, true, false);

        AudioInputStream audioInputStream =
                new AudioInputStream(byteArrayInputStream, format, audioData.length / format.getFrameSize());

        //시작 시간
        long startTime = System.currentTimeMillis();
        soundPlayer.play(audioInputStream);
        //종료 시간
        long endTime = System.currentTimeMillis();
        assertThat((endTime - startTime)/1000F).isGreaterThan(duration);

        System.out.println("(endTime - startTime)/1000F = " + (endTime - startTime)/1000F);
    }
}
