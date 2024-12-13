package com.oreo.finalproject_5re5_be.concat.service.bgm;

import com.oreo.finalproject_5re5_be.global.component.audio.AudioExtensionChecker;
import com.oreo.finalproject_5re5_be.global.component.audio.AudioExtensionConverter;
import com.oreo.finalproject_5re5_be.global.component.audio.AudioFormats;
import com.oreo.finalproject_5re5_be.global.component.audio.AudioResample;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public class BgmProcessor {

    public static AudioInputStream prepareBgm(File bgmFile) throws IOException {
        try {
            AudioResample resampler =
                    new AudioResample(AudioFormats.STEREO_FORMAT_SR441_B32); // 고정된 목표 포맷

            // 1. 파일 포맷 검사
            if (AudioExtensionChecker.isSupported(bgmFile)) {
                System.out.println("BGM 파일이 MP3 형식입니다. WAV로 변환 중...");
                byte[] wavBytes = AudioExtensionConverter.mp3ToWav(bgmFile);
                AudioInputStream wavStream =
                        AudioSystem.getAudioInputStream(new ByteArrayInputStream(wavBytes));
                // 변환 후에도 리샘플링 수행
                return resampler.resample(wavStream);
            } else if (AudioExtensionChecker.isWavExtension(bgmFile)) {
                System.out.println("BGM 파일이 WAV 형식입니다. 리샘플링 중...");
                AudioInputStream wavStream = AudioSystem.getAudioInputStream(bgmFile);
                // WAV 파일도 리샘플링 수행
                return resampler.resample(wavStream);
            } else {
                throw new IllegalArgumentException("지원되지 않는 파일 형식입니다.");
            }
        } catch (Exception e) {
            throw new IOException("BGM 파일을 처리하는 중 오류가 발생했습니다.", e);
        }
    }

    // BGM이 이미 source보다 길 경우 필요
    public static AudioInputStream trimBgm(AudioInputStream bgm, long targetFrames)
            throws IOException {
        System.out.println("Trimming BGM to " + targetFrames + " frames.");
        return new AudioInputStream(bgm, bgm.getFormat(), Math.min(targetFrames, bgm.getFrameLength()));
    }

    public static AudioInputStream extendBgm(AudioInputStream bgm, long targetFrames)
            throws IOException {
        // BGM 데이터 읽기
        byte[] bgmData = bgm.readAllBytes();
        AudioFormat format = bgm.getFormat();
        int frameSize = format.getFrameSize();

        // 현재 BGM의 프레임 길이 계산
        long frameLength = bgmData.length / frameSize;
        System.out.println("Original BGM Frame Length: " + frameLength);

        // 목표 프레임 길이만큼 확장
        ByteArrayOutputStream extendedStream = new ByteArrayOutputStream();
        long totalFrames = 0;

        while (totalFrames < targetFrames) {
            long remainingFrames = targetFrames - totalFrames;
            long framesToAdd = Math.min(remainingFrames, frameLength);

            if (framesToAdd <= 0) {
                throw new IllegalStateException("Frames to add is zero or negative, check the input data.");
            }

            System.out.println(
                    "Current Total Frames: " + totalFrames + ", Frames To Add: " + framesToAdd);

            // 데이터 추가
            extendedStream.write(bgmData, 0, (int) (framesToAdd * frameSize));
            totalFrames += framesToAdd;
        }

        System.out.println("Extended BGM total frames: " + totalFrames);

        // 새로운 AudioInputStream 생성
        return new AudioInputStream(
                new ByteArrayInputStream(extendedStream.toByteArray()), format, totalFrames);
    }

    public static long calculateTargetFrames(AudioInputStream audioStream) throws IOException {
        AudioFormat format = audioStream.getFormat();
        int frameSize = format.getFrameSize();
        byte[] buffer = new byte[4096];
        long totalBytes = 0;
        int bytesRead;

        while ((bytesRead = audioStream.read(buffer)) != -1) {
            totalBytes += bytesRead;
        }

        long calculatedFrames = totalBytes / frameSize;
        System.out.println("Calculated Frames: " + calculatedFrames);
        return calculatedFrames;
    }

    // BGM 길이 조정 로직 분리
    public static AudioInputStream adjustBgmLength(
            AudioInputStream bgmStream, long targetFrames, long bgmFrames) throws IOException {
        return bgmFrames > targetFrames
                ? BgmProcessor.trimBgm(bgmStream, targetFrames)
                : BgmProcessor.extendBgm(bgmStream, targetFrames);
    }

    public static AudioInputStream mixAudio(AudioInputStream source, AudioInputStream bgm)
            throws IOException {
        AudioFormat format = source.getFormat();
        // 첫 번째 오디오 파일과 두 번째 오디오 파일의 길이를 얻음
        byte[] buffer1 = source.readAllBytes();
        byte[] buffer2 = bgm.readAllBytes();

        // 믹싱을 위한 새로운 버퍼 생성
        byte[] mixedBuffer = new byte[buffer1.length];

        // 두 오디오 파일을 샘플 단위로 믹싱
        for (int i = 0; i < buffer1.length; i += 2) { // 16비트 기준
            // 각 버퍼에서 샘플을 가져옴
            short sample1 = (short) ((buffer1[i + 1] << 8) | (buffer1[i] & 0xFF));
            short sample2 = (short) ((buffer2[i + 1] << 8) | (buffer2[i] & 0xFF));

            // 샘플을 믹싱 (평균값 계산)
            short mixedSample = (short) ((sample1 + sample2) / 2);

            // 믹싱된 샘플을 mixedBuffer에 저장
            mixedBuffer[i] = (byte) (mixedSample & 0xFF);
            mixedBuffer[i + 1] = (byte) ((mixedSample >> 8) & 0xFF);
        }

        // 믹싱된 오디오 데이터로 AudioInputStream 생성
        ByteArrayInputStream bais = new ByteArrayInputStream(mixedBuffer);
        AudioInputStream mixedAudioStream =
                new AudioInputStream(bais, format, mixedBuffer.length / format.getFrameSize());

        return mixedAudioStream;
    }
}
