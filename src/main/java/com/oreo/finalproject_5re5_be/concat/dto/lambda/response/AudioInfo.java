package com.oreo.finalproject_5re5_be.concat.dto.lambda.response;

import com.oreo.finalproject_5re5_be.global.component.audio.AudioExtensionConverter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.AudioInputStream;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AudioInfo {
    private byte[] audioData;
    private String fileName;
    private float contentLength;
    private String contentType;

    public AudioInfo(
            AudioInputStream audioData, String fileName, float contentLength, String contentType) {
        this.audioData = AudioExtensionConverter.mp3ToWav(audioData);
        this.fileName = fileName;
        this.contentLength = contentLength;
        this.contentType = contentType;
    }

    private byte[] getAudioBytes(AudioInputStream audioStream) {

        try {
            return audioStream.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException("오디오 변환 실패", e);
        }
    }

    public Long getContentSize() {
        return (long) audioData.length;
    }

    public InputStream toInputStream() {
        return new ByteArrayInputStream(audioData);
    }
}
