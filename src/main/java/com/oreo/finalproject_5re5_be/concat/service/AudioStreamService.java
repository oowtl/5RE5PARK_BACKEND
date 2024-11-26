package com.oreo.finalproject_5re5_be.concat.service;

import com.oreo.finalproject_5re5_be.concat.dto.request.SelectedConcatRowRequest;
import com.oreo.finalproject_5re5_be.concat.service.bgm.BgmProcessor;
import com.oreo.finalproject_5re5_be.concat.service.concatenator.AudioProperties;
import com.oreo.finalproject_5re5_be.global.component.S3Service;
import com.oreo.finalproject_5re5_be.global.component.audio.AudioResample;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AudioStreamService {

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
            AudioInputStream audioStream = S3Service.load(row.getAudioUrl());
            audioStream = audioResample.formatting(audioStream); // 리샘플링 처리
            audioPropertiesList.add(new AudioProperties(audioStream, row.getSilenceInterval()));
        }
        return audioPropertiesList;
    }

}