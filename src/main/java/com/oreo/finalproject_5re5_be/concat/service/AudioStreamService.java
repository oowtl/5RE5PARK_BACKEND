package com.oreo.finalproject_5re5_be.concat.service;

import com.oreo.finalproject_5re5_be.concat.dto.request.ConcatRowRequest;
import com.oreo.finalproject_5re5_be.concat.dto.request.ConcatRowSaveRequestDto;
import com.oreo.finalproject_5re5_be.concat.dto.request.OriginAudioRequest;
import com.oreo.finalproject_5re5_be.concat.service.bgm.BgmProcessor;
import com.oreo.finalproject_5re5_be.concat.service.concatenator.AudioProperties;
import com.oreo.finalproject_5re5_be.global.component.S3Service;
import com.oreo.finalproject_5re5_be.global.component.audio.AudioExtensionConverter;
import com.oreo.finalproject_5re5_be.global.component.audio.AudioFormats;
import com.oreo.finalproject_5re5_be.global.component.audio.AudioResample;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Log4j2
@Service
public class AudioStreamService {

    private final AudioFormat defaultAudioFormat = AudioFormats.STEREO_FORMAT_SR441_B16; // 기본 포맷
    private final AudioResample audioResample = new AudioResample();

    public AudioInputStream createAudioInputStream(ByteArrayOutputStream buffer, AudioFormat format) {
        byte[] data = buffer.toByteArray();
        return new AudioInputStream(
                new ByteArrayInputStream(data), format, data.length / format.getFrameSize());
    }

    public AudioInputStream bufferAudioStream(AudioInputStream stream, AudioFormat format)
            throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        AudioSystem.write(stream, AudioFileFormat.Type.WAVE, buffer);
        byte[] data = buffer.toByteArray();
        return new AudioInputStream(
                new ByteArrayInputStream(data), format, data.length / format.getFrameSize());
    }

    public long getValidFrameLength(AudioInputStream audioStream) throws IOException {
        long frameLength = audioStream.getFrameLength();
        return frameLength > 0 ? frameLength : BgmProcessor.calculateTargetFrames(audioStream);
    }

    public List<AudioProperties> loadAudioFiles(ConcatRowSaveRequestDto selectedRows) {
        List<AudioProperties> audioPropertiesList = new ArrayList<>();
        for (ConcatRowRequest row : selectedRows.getConcatRowRequests()) {
            OriginAudioRequest originAudio = row.getOriginAudioRequest();

            try {
                AudioInputStream audioStream = S3Service.load(originAudio.getAudioUrl());

                // 리샘플링 처리
                audioStream = audioResample.formatting(audioStream); // 리샘플링 처리
                log.info("[loadAudioFiles] 리샘플링 완료된 오디오 포맷: {}", audioStream.getFormat());

                AudioProperties audioProperties = new AudioProperties(audioStream, row.getRowSilence());
                log.info(
                        "[loadAudioFiles] 생성된 AudioProperties: silenceInterval={}, frameLength={}",
                        row.getRowSilence(),
                        audioStream.getFrameLength());

                audioPropertiesList.add(audioProperties);
            } catch (Exception e) {
                log.error(
                        "Failed to load or process audio file from URL: {}", originAudio.getAudioUrl(), e);
            }
        }
        return audioPropertiesList;
    }

    public AudioInputStream loadAsBufferedStream(String s3Url) {
        try {
            URL url = new URL(s3Url);

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
            log.info(
                    "[loadAsBufferedStream] AudioInputStream 가져오기 성공. Format: {}",
                    audioInputStream.getFormat());

            // mp3 -> WAV 변환
            byte[] wavData = AudioExtensionConverter.mp3ToWav(audioInputStream);

            // Target Format으로 변환 (formatting)
            AudioInputStream wavStream =
                    new AudioInputStream(
                            new ByteArrayInputStream(wavData),
                            defaultAudioFormat,
                            wavData.length / defaultAudioFormat.getFrameSize());
            AudioInputStream formattedStream = audioResample.formatting(wavStream);
            log.info(
                    "[loadAsBufferedStream] formatting 변환 성공. Frame Length: {}",
                    formattedStream.getFrameLength());

            // 데이터를 메모리에 버퍼링
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            AudioSystem.write(formattedStream, AudioFileFormat.Type.WAVE, buffer);

            byte[] bufferedData = buffer.toByteArray();
            AudioFormat format = formattedStream.getFormat();

            AudioInputStream bufferedStream =
                    new AudioInputStream(
                            new ByteArrayInputStream(bufferedData),
                            format,
                            bufferedData.length / format.getFrameSize());

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
