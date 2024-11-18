package com.oreo.finalproject_5re5_be.concat.service.concatenator;

import com.oreo.finalproject_5re5_be.global.component.audio.AudioExtensionConverter;
import com.oreo.finalproject_5re5_be.global.component.audio.AudioResample;
import com.oreo.finalproject_5re5_be.global.component.audio.BeepMaker;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static com.oreo.finalproject_5re5_be.concat.service.concatenator.IntervalConcatenator.prepareAudioStreams;

/**
 * @apiNote 스테레오 포맷의 무음구간을 포함한 병합에 사용되는 클래스 입니다. <br>
 * 오디오 파일은 병합 하려는 포맷 타입과 같거나 같아지도록 리샘플링 되어야 합니다. <br>
 * WAVE 파일만을 지원하며 mp3파일의 경우 {@link AudioExtensionConverter} 를 사용하여 WAVE 파일로 변환합니다.
 * @see AudioResample
 * @see AudioExtensionConverter
 *
 */
public class StereoIntervalConcatenator extends StereoConcatenator implements IntervalConcatenator {

    private final AudioFormat AUDIO_FORMAT;

    public StereoIntervalConcatenator(AudioFormat audioFormat) {
        this.AUDIO_FORMAT = audioFormat;
    }

    /**
     * @apiNote 오디오 스트림과 무음 오디오 스트림을 하나의 바이트 배열로 만들어 반환합니다.
     * @param audioStreams
     * @param start
     * @return
     * @throws IOException
     */
    @Override
    public ByteArrayOutputStream intervalConcatenate(List<AudioProperties> audioStreams, float start) throws IOException {
        List<AudioInputStream> list = prepareAudioStreams(audioStreams, AUDIO_FORMAT);
        list.add(0, BeepMaker.makeSound(start, AUDIO_FORMAT));
        return super.concatenate(list);
    }
}
