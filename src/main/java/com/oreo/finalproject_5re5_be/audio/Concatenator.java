package com.oreo.finalproject_5re5_be.audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.util.List;

/**
 * {@code Concatenator} 클래스의 기본 인터페이스 입니다.
 * <br>
 * 모든 Concatenator는 이 인터페이스를 구현합니다.
 */
public interface Concatenator {
     AudioFormat TARGET_FORMAT = new AudioFormat(
            AudioFormat.Encoding.PCM_SIGNED,  // 인코딩 방식
            44100,                            // 샘플 레이트 (예: 44.1kHz)
            16,                               // 샘플 크기 (16비트)
            2,                                // 채널 수 (스테레오)
            4,                                // 프레임 크기 (16비트 스테레오인 경우 4바이트)
            44100,                            // 프레임 레이트
            false                             // 빅 엔디안 여부
    );

    AudioInputStream concatenate(List<AudioInputStream> files);

    static AudioInputStream formatting(AudioInputStream audioInputStream) {
        //오디오 포멧이 타겟 오디오와 같은지 확인
        if (audioInputStream.getFormat().matches(TARGET_FORMAT)) {
            return audioInputStream; // 이미 목표 형식인 경우 변환하지 않음
        }
        // 변환하여 새로운 AudioInputStream 반환
        return AudioSystem.getAudioInputStream(TARGET_FORMAT, audioInputStream);
    }
}
