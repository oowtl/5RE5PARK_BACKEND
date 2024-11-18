package com.oreo.finalproject_5re5_be.concat.service.concatenator;

import javax.sound.sampled.AudioInputStream;

/**
 * @apiNote 무음구간을 포함한 오디오 병합을 위한 레코드 입니다.
 *
 * @param audioInputStream
 * @param silence
 */
public record AudioProperties(AudioInputStream audioInputStream, float silence) {


    /**
     * @apiNote 지정한 파라미터로 {@code AudioProperties} 를 생성합니다.
     * @return List<AudioProperties>
     */
    public static AudioProperties parsAudioProperties(AudioInputStream audioInputStream, float silence) {
        return new AudioProperties(audioInputStream, silence);
    }
}
