package com.oreo.finalproject_5re5_be.concat.service.concatenator;

import com.oreo.finalproject_5re5_be.global.component.audio.AudioChannels;

import javax.sound.sampled.AudioInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class StereoConcatenator implements Concatenator {
    private byte[] buffer = new byte[2048];

    @Override
    public ByteArrayOutputStream concatenate(List<AudioInputStream> audioStreams) throws IOException {
        boolean mono = audioStreams.stream()
                .allMatch(as -> as.getFormat().getChannels() == AudioChannels.STEREO);//스테레오 포맷 확인
        if (mono) {
            return merge(audioStreams);//병합
        }
        throw new IllegalArgumentException("잘못된 포맷 입니다.");
    }

    @Override
    public void setBufferSize(int bufferSize) {
        buffer = new byte[bufferSize];
    }

    //오디오 파일 병합 메소드
    private ByteArrayOutputStream merge(List<AudioInputStream> audioStreams) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int bytesRead;
        for (AudioInputStream audioStream : audioStreams) {
            while ((bytesRead = audioStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
        return outputStream;
    }
}
