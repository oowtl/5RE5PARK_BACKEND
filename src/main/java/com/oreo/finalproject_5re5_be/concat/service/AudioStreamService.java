package com.oreo.finalproject_5re5_be.concat.service;

import com.oreo.finalproject_5re5_be.concat.dto.request.SelectedConcatRowRequest;
import com.oreo.finalproject_5re5_be.concat.service.bgm.BgmProcessor;
import com.oreo.finalproject_5re5_be.concat.service.concatenator.AudioProperties;
import com.oreo.finalproject_5re5_be.global.component.S3Service;
import com.oreo.finalproject_5re5_be.global.component.audio.AudioExtensionConverter;
import com.oreo.finalproject_5re5_be.global.component.audio.AudioFormats;
import com.oreo.finalproject_5re5_be.global.component.audio.AudioResample;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Log4j2
@Service
public class AudioStreamService {

    private final AudioFormat defaultAudioFormat = AudioFormats.STEREO_FORMAT_SR441_B16; // 기본 포맷
    private final AudioResample audioResample = new AudioResample();

    public AudioInputStream createAudioInputStream(ByteArrayOutputStream buffer, AudioFormat format) {
        byte[] data = buffer.toByteArray();
        return new AudioInputStream(new ByteArrayInputStream(data), format, data.length / format.getFrameSize());
    }

    public AudioInputStream bufferAudioStream(AudioInputStream stream, AudioFormat format) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        AudioSystem.write(stream, AudioFileFormat.Type.WAVE, buffer);
        byte[] data = buffer.toByteArray();
        return new AudioInputStream(new ByteArrayInputStream(data), format, data.length / format.getFrameSize());
    }

    public long getValidFrameLength(AudioInputStream audioStream) throws IOException {
        long frameLength = audioStream.getFrameLength();
        return frameLength > 0 ? frameLength : BgmProcessor.calculateTargetFrames(audioStream);
    }

    public List<AudioProperties> loadAudioFiles(SelectedConcatRowRequest selectedRows) {
        List<AudioProperties> audioPropertiesList = new ArrayList<>();
        for (SelectedConcatRowRequest.Row row : selectedRows.getRows()) {
            log.info("[loadAudioFiles] SelectedConcatRowRequest의 Row에 박혀있는 URL: {}", row.getAudioUrl());
            try {
                AudioInputStream audioStream = S3Service.load(row.getAudioUrl());
                log.info("[loadAudioFiles] load 완료: ");

                audioStream = audioResample.formatting(audioStream); // 리샘플링 처리

                // 리샘플링 후 포맷 로그
                log.info("[loadAudioFiles] 리샘플링 완료하고 나온 포맷: {}", audioStream.getFormat());

                AudioProperties audioProperties = new AudioProperties(audioStream, row.getSilenceInterval());
                log.info("[loadAudioFiles] 생성된 AudioProperties: silenceInterval={}, frameLength={}",
                        row.getSilenceInterval(), audioStream.getFrameLength());

                audioPropertiesList.add(audioProperties);
            } catch (Exception e) {
                log.error("Failed to load or process audio file from URL: {}", row.getAudioUrl(), e);
            }
        }
        return audioPropertiesList;
    }

    public AudioInputStream loadAsBufferedStream(String s3Url) {
        try {
            URL url = new URL(s3Url);

            log.info("[loadAsBufferedStream] BGM으로 쓰일 S3 URL: {}", s3Url);

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
            log.info("[loadAsBufferedStream] AudioInputStream 가져오기 성공. Format: {}", audioInputStream.getFormat());

            // mp3 -> WAV 변환
            byte[] wavData = AudioExtensionConverter.mp3ToWav(audioInputStream);
            log.info("[loadAsBufferedStream] mp3ToWAV 변환 성공. Data Size: {} bytes", wavData.length);

            // Target Format으로 변환 (formatting)
            AudioInputStream wavStream = new AudioInputStream(new ByteArrayInputStream(wavData), defaultAudioFormat, wavData.length / defaultAudioFormat.getFrameSize());
            AudioInputStream formattedStream = audioResample.formatting(wavStream);
            log.info("[loadAsBufferedStream] formatting 변환 성공. Frame Length: {}", formattedStream.getFrameLength());

            // 데이터를 메모리에 버퍼링
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            AudioSystem.write(formattedStream, AudioFileFormat.Type.WAVE, buffer);
            log.info("[loadASBufferedStream] AudioInputStream 데이터를 ByteArrayOutputStream으로 버퍼링 성공. 크기: {} bytes", buffer.size());

            byte[] bufferedData = buffer.toByteArray();
            AudioFormat format = formattedStream.getFormat();
            log.info("[loadASBufferedStream] Buffered Data 생성 성공. 총 길이: {} bytes, FrameSize: {}", bufferedData.length, format.getFrameSize());

            AudioInputStream bufferedStream = new AudioInputStream(
                    new ByteArrayInputStream(bufferedData),
                    format,
                    bufferedData.length / format.getFrameSize()
            );

            log.info("[loadAsBufferedStream] Buffered AudioInputStream 생성 성공. Frame Length: {}", bufferedStream.getFrameLength());

            return bufferedStream;

        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("잘못된 S3 URL입니다.", e);
        } catch (UnsupportedAudioFileException e) {
            throw new IllegalArgumentException("지원되지 않는 오디오 파일 형식입니다.", e);
        } catch (IOException e) {
            log.error("Error processing S3 URL: {}", s3Url, e);
            throw new IllegalArgumentException("오디오 처리 실패", e);
        }
    }

}