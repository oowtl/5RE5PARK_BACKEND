package com.oreo.finalproject_5re5_be.global.component.audio;

import com.oreo.finalproject_5re5_be.concat.service.concatenator.Concatenator;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import lombok.extern.log4j.Log4j2;

/**
 * @apiNote 병합 되거나 병합되기 이전의 오디오 형식을 일치 시키기 위한 클래스
 * @see Concatenator
 */
@Log4j2
public class AudioResample {
    private final AudioFormat audioFormat;

    /**
     *
     *
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
     *
     * <p>
     */
    public AudioResample(AudioFormat audioFormat) {
        this.audioFormat = audioFormat;
    }

    /**
     *
     *
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
     *
     * <p>
     */
    public AudioResample() {
        audioFormat =
                new AudioFormat(
                        AudioFormat.Encoding.PCM_SIGNED,
                        44100, // 44.1kHz로 변환
                        16, // 16비트
                        2, // 스테레오
                        4, // 2 bytes/frame
                        44100, // frame rate와 샘플링 레이트 일치
                        false // 리틀 엔디안
                        );
    }

    // 리스트 리샘플링
    public List<AudioInputStream> resample(List<AudioInputStream> audioStreams) {
        List<AudioInputStream> resampledAudioStreams = new ArrayList<>();
        for (AudioInputStream audioStream : audioStreams) {
            resampledAudioStreams.add(formatting(audioStream));
        }
        return resampledAudioStreams;
    }

    // 개별 리샘플링
    public AudioInputStream resample(AudioInputStream audioStream) {
        return formatting(audioStream);
    }

    // 바이트 스트림 리샘플링
    public AudioInputStream resample(ByteArrayOutputStream byteArrayOutputStream) {
        byte[] combinedBytes = byteArrayOutputStream.toByteArray();
        ByteArrayInputStream combinedByteArrayInputStream = new ByteArrayInputStream(combinedBytes);
        return new AudioInputStream(
                combinedByteArrayInputStream,
                audioFormat,
                combinedBytes.length / audioFormat.getFrameSize());
    }

    // 리샘플링 포맷 일치화
    public AudioInputStream formatting(AudioInputStream audioInputStream) {
        log.info("[formatting] 현재 오디오 포맷과 타겟 오디오 포맷이 일치하는지 확인 중...");
        log.info("[formatting] 현재 오디오 포맷: {}", audioInputStream.getFormat());
        log.info("[formatting] 타겟 오디오 포맷: {}", audioFormat);

        if (audioInputStream.getFormat().matches(audioFormat)) {
            log.info("[formatting] 타겟 오디오 포맷과 이미 일치하여, 리샘플링이 필요없습니다.");
            return audioInputStream;
        }

        log.info("[formatting] 타겟 오디오 포맷과 일치하지 않아서 리샘플링 중...");
        try {
            AudioInputStream resampledStream =
                    AudioSystem.getAudioInputStream(audioFormat, audioInputStream);
            log.info("[formatting] 리샘플링된 포맷: {}", resampledStream.getFormat());
            return resampledStream;
        } catch (Exception e) {
            log.error("Failed to resample audio format", e);
            throw new IllegalArgumentException("Resampling failed");
        }
    }
}
