package com.oreo.finalproject_5re5_be.concat.service;


import com.oreo.finalproject_5re5_be.concat.dto.request.AudioFormatRequest;
import com.oreo.finalproject_5re5_be.concat.entity.AudioFormat;
import com.oreo.finalproject_5re5_be.concat.repository.AudioFormatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AudioFormatService {

    private final AudioFormatRepository audioFormatRepository;

    // AudioFormat에 대한 정보 저장 (1개)
    public void saveAudioFormat(AudioFormatRequest request) {
        AudioFormat audioFormat = AudioFormat.builder()
                .encoding(request.getEncoding())
                .sampleRate(request.getSampleRate())
                .sampleSizeBit(request.getSampleSizeBit())
                .channel(request.getChannel())
                .frameSize(request.getFrameSize())
                .frameRate(request.getFrameRate())
                .isBigEndian(request.getIsBigEndian())
                .build();

        audioFormatRepository.save(audioFormat);

    }


}
