package com.oreo.finalproject_5re5_be.audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * @apiNote 음성간 간격이 있는 오디오를 위함 병합 클래스의 인터페이스 입니다. <br>
 *
 */
public interface IntervalConcatenator extends Concatenator {
    ByteArrayOutputStream intervalConcatenate(List<AudioProperties> audioStreams, float start) throws IOException;

    //공통 IntervalConcatenator에 필요한 로직을 분리
    static List<AudioInputStream> prepareAudioStreams(List<AudioProperties> audioStreams, AudioFormat audioFormat) {
        List<AudioInputStream> result = new ArrayList<>();
        for (AudioProperties audioProperties : audioStreams) {
            result.add(audioProperties.audioInputStream());//오디오 파일 넣기
            result.add(BeepMaker.makeSound(audioProperties.silence(), audioFormat));// 무음구간 오디오 만들어서 넣기
        }
        return result;
    }
}
