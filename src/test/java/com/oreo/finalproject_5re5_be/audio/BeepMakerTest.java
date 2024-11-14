package com.oreo.finalproject_5re5_be.audio;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

import javax.sound.sampled.*;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class BeepMakerTest {
    private final SoundPlayer soundPlayer = new SoundPlayer();

    public static final AudioFormat outputFormat = new AudioFormat(
            AudioFormat.Encoding.PCM_SIGNED,
            44100, // 44.1kHz로 변환
            16,    // 16비트
            2,     // 모노
            4,     // 2 bytes/frame
            44100, // frame rate와 샘플링 레이트 일치
            false  // 리틀 엔디안
    );

    @Test
    @DisabledOnOs({OS.LINUX}) //테스코드가 LINUX 환경에서 돌아가지 않게 해주는 어노테이션
    void makeBeepSectionTest() throws IOException {
        float duration = 300f;//오디오 길이 설정
        AudioInputStream audioData = BeepMaker.makeSound(1000, duration, outputFormat);//오디오 생성

        long startTime = System.currentTimeMillis();//시작 시간
        soundPlayer.play(audioData);//오디오 재생
        long endTime = System.currentTimeMillis();//종료 시간

        //리소스를 반환하는 시간까지 존재하므로 완전히 같을 수 없음
        assertThat((endTime - startTime)/1000F).isGreaterThan(duration/1000);

        System.out.println("(endTime - startTime)/1000F = " + (endTime - startTime)/1000F);
    }

    @Test
    @DisabledOnOs({OS.LINUX}) //테스코드가 LINUX 환경에서 돌아가지 않게 해주는 어노테이션
    void makeSilenceSectionTest() throws IOException {

        float duration = 300f;
        AudioInputStream audioInputStream = BeepMaker.makeSound(duration, outputFormat);

        //시작 시간
        long startTime = System.currentTimeMillis();
        soundPlayer.play(audioInputStream);
        //종료 시간
        long endTime = System.currentTimeMillis();
        assertThat((endTime - startTime)/1000F).isGreaterThan(duration/1000);

        System.out.println("(endTime - startTime)/1000F = " + (endTime - startTime)/1000F);
    }
}
