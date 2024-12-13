package com.oreo.finalproject_5re5_be.global.config;

import com.oreo.finalproject_5re5_be.concat.dto.request.AudioFileRequestDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class MultipartFileAccepter implements Converter<MultipartFile, AudioFileRequestDto> {

    @SuppressWarnings("NullableProblems")
    @Override
    public AudioFileRequestDto convert(MultipartFile source) {
        return AudioFileRequestDto.builder().audioFile(source).build();
    }
}
