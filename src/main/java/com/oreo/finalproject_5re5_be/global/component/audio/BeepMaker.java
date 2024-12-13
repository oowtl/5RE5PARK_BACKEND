package com.oreo.finalproject_5re5_be.global.component.audio;

import java.io.ByteArrayInputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;

/** 파라미터로 전달된 주파수의 오디오를 생성한다. */
public class BeepMaker {
    /**
     * 주어진 주파수로 지정된 길이의 비프 오디오 스트림을 생성합니다.
     *
     * @param frequency 주파수 (Hz)
     * @param duration 지속 시간 (초)
     * @param format 대상 오디오 포맷
     * @return 비프 오디오 스트림
     */
    public static AudioInputStream makeSound(int frequency, float duration, AudioFormat format) {
        int frameSize = format.getFrameSize();
        float frameRate = format.getFrameRate();
        long framesOfBeep = (long) (duration * frameRate / 1000.0f); // 밀리초 단위를 초 단위로 변환하여 프레임 수 계산
        byte[] beepBuffer = new byte[(int) framesOfBeep * frameSize];

        for (int i = 0; i < framesOfBeep; i++) {
            double angle = 2.0 * Math.PI * i * frequency / frameRate;
            short sample = (short) (Math.sin(angle) * Short.MAX_VALUE);

            if (format.getChannels() == 1) { // 모노 타입 포멧 생성
                beepBuffer[i * 2] = (byte) (sample & 0xFF);
                beepBuffer[i * 2 + 1] = (byte) ((sample >> 8) & 0xFF);
            }
            if (format.getChannels() == 2) { // 스테레오 타입 포멧 생성
                beepBuffer[i * 4] = (byte) (sample & 0xFF);
                beepBuffer[i * 4 + 1] = (byte) ((sample >> 8) & 0xFF);
                beepBuffer[i * 4 + 2] = (byte) (sample & 0xFF);
                beepBuffer[i * 4 + 3] = (byte) ((sample >> 8) & 0xFF);
            }
        }

        return new AudioInputStream(new ByteArrayInputStream(beepBuffer), format, framesOfBeep);
    }

    /**
     *
     *
     * <table class="striped">
     * <caption>기본 병합 클래스</caption>
     * </table>
     *
     * @param duration 지속 시간 (초)
     * @param format 대상 오디오 포맷
     * @return 무음 오디오 스트림
     */

    // 무음 오디오 생성
    public static AudioInputStream makeSound(float duration, AudioFormat format) {
        return makeSound(0, duration, format);
    }
}
