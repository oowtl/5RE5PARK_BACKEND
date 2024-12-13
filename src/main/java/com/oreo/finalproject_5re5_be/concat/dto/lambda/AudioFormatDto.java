package com.oreo.finalproject_5re5_be.concat.dto.lambda;

import javax.sound.sampled.AudioFormat;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AudioFormatDto {
    private Float sampleRate;
    private Integer bitDepth;
    private Integer channels;
    private Integer frameSize;
    private Float frameRate;

    public AudioFormatDto() {}

    public AudioFormatDto(AudioFormat audioFormat) {
        this.sampleRate = audioFormat.getSampleRate();
        this.bitDepth = audioFormat.getSampleSizeInBits();
        this.channels = audioFormat.getChannels();
        this.frameSize = audioFormat.getFrameSize();
        this.frameRate = audioFormat.getFrameRate();
    }

    public AudioFormatDto(
            Float sampleRate, Integer bitDepth, Integer channels, Integer frameSize, Float frameRate) {
        this.sampleRate = sampleRate;
        this.bitDepth = bitDepth;
        this.channels = channels;
        this.frameSize = frameSize;
        this.frameRate = frameRate;
    }

    public AudioFormat toAudioFormat() {
        return new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                sampleRate,
                bitDepth,
                channels,
                frameSize,
                frameRate,
                false);
    }
}
