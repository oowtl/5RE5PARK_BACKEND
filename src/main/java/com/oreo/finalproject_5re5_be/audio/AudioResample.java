package com.oreo.finalproject_5re5_be.audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * @apiNote 병합 되거나 병합되기 이전의 오디오 형식을 일치 시키기 위한 클래스
 * @see Concatenator
 *
 */
public class AudioResample {
    private final AudioFormat audioFormat;
    /**
     * <table class="striped">
     * <caption>오디오 리샘플링</caption>
     * <thead>
     *   <tr>
     *     <th scope="col">변수명
     *     <th scope="col">변수 타입
     *     <th scope="col">Description
     * </thead>
     * <tbody>
     *   <tr>
     *     <th scope="row">"파라미터"
     *     <td>{@link javax.sound.sampled.AudioFormat AudioFormat}
     *     <td>리샘플링 할 오디오 포맷
     * </tbody>
     * </table>
     * <p>
     */
    public AudioResample(AudioFormat audioFormat) {
        this.audioFormat = audioFormat;
    }

    /**
     * <table class="striped">
     * <caption>오디오 리샘플링</caption>
     * <thead>
     *   <tr>
     *     <th scope="col">변수명
     *     <th scope="col">변수 타입
     *     <th scope="col">Description
     * </thead>
     * <tbody>
     *   <tr>
     *     <th scope="row">"파라미터"
     *     <td>
     *     <td>리샘플링 할 오디오 포맷 <br>모노 44100 SampleRate의 오디오 포맷을 생성
     * </tbody>
     * </table>
     * <p>
     */
    public AudioResample() {
        audioFormat = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                44100, // 44.1kHz로 변환
                16,    // 16비트
                1,     // 모노
                2,     // 2 bytes/frame
                44100, // frame rate와 샘플링 레이트 일치
                false  // 리틀 엔디안
        );
    }

    //리스트 리샘플링
    public List<AudioInputStream> resample(List<AudioInputStream> audioStreams) {
        List<AudioInputStream> resampledAudioStreams = new ArrayList<>();
        for (AudioInputStream audioStream : audioStreams) {
            resampledAudioStreams.add(formatting(audioStream));
        }
        return resampledAudioStreams;
    }

    //개별 리샘플링
    public AudioInputStream resample(AudioInputStream audioStream) {
        return formatting(audioStream);
    }

    //바이트 스트림 리샘플링
    public AudioInputStream resample(ByteArrayOutputStream byteArrayOutputStream) {
        byte[] combinedBytes = byteArrayOutputStream.toByteArray();
        ByteArrayInputStream combinedByteArrayInputStream = new ByteArrayInputStream(combinedBytes);
        return new AudioInputStream(
                combinedByteArrayInputStream,
                audioFormat,
                combinedBytes.length / audioFormat.getFrameSize()
        );
    }

    //리샘플링 포맷 일치화
    public AudioInputStream formatting(AudioInputStream audioInputStream) {
        if (audioInputStream.getFormat().matches(audioFormat)) {
            return audioInputStream;
        }
        return AudioSystem.getAudioInputStream(audioFormat, audioInputStream);
    }
}
