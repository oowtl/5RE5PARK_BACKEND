package com.oreo.finalproject_5re5_be.concat.service.concatenator;

import com.oreo.finalproject_5re5_be.global.component.audio.BeepMaker;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;

/**
 * @apiNote 음성간 간격이 있는 오디오를 위함 병합 클래스의 인터페이스 입니다. <br>
 */
public interface IntervalConcatenator extends Concatenator {
    ByteArrayOutputStream intervalConcatenate(List<AudioProperties> audioStreams, float start)
            throws IOException;

    // 공통 IntervalConcatenator에 필요한 로직을 분리
    static List<AudioInputStream> prepareAudioStreams(
            List<AudioProperties> audioStreams, AudioFormat audioFormat) {
        List<AudioInputStream> result = new ArrayList<>();
        int index = 1; // 로그를 위한 인덱스 관리

        for (AudioProperties audioProperties : audioStreams) {
            AudioInputStream audioStream =
                    audioProperties.audioInputStream(); // AudioProperties에서 AudioStream 추출

            // row audio 관련 log
            System.out.printf(
                    "[prepareAudioStreams] 오디오 스트림 추가: index=%d, frameLength=%d, format=%s%n",
                    index, audioStream.getFrameLength(), audioStream.getFormat());

            result.add(audioProperties.audioInputStream()); // 오디오 스트림 추가

            // 무음 구간 생성 및 추가
            AudioInputStream silenceStream =
                    BeepMaker.makeSound(audioProperties.silence() * 1000, audioFormat);

            // row 무음구간 관련 log
            System.out.printf(
                    "[prepareAudioStreams] 무음 구간 추가: index=%d, duration=%dms, frameLength=%d%n",
                    index, (int) (audioProperties.silence() * 1000), silenceStream.getFrameLength());

            result.add(silenceStream);

            index++; // 인덱스 증가
        }
        return result;
    }
}
