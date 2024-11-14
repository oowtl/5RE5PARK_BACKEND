package com.oreo.finalproject_5re5_be.audio;

import org.springframework.web.multipart.MultipartFile;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class AudioDurationUtil {
    public static int getAudioDurationInMilliseconds(MultipartFile audioFile) {
        try {
            // MultipartFile을 InputStream으로 변환
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(audioFile.getBytes());
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(byteArrayInputStream);

            // 오디오 파일 포맷 가져오기
            AudioFileFormat format = AudioSystem.getAudioFileFormat(byteArrayInputStream);

            // 프레임 길이와 프레임 속도를 가져와 재생 시간 계산
            double frameLength = audioInputStream.getFrameLength();
            float frameRate = format.getFormat().getFrameRate();

            // 초 단위 재생 시간 계산 후 밀리초로 변환
            double durationInSeconds = frameLength / frameRate;
            int durationInMilliseconds = (int) (durationInSeconds * 1000);

            return durationInMilliseconds;

        } catch (UnsupportedAudioFileException | IOException e) {
            throw new IllegalStateException("오디오 파일을 처리할 수 없습니다: " + e.getMessage());
        }
    }
}
