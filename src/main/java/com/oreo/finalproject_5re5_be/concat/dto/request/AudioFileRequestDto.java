package com.oreo.finalproject_5re5_be.concat.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
@AllArgsConstructor
public class AudioFileRequestDto {
    private MultipartFile audioFile;
    private String fileName;

    public AudioFileRequestDto(String fileName) {
        this.fileName = fileName;
        this.audioFile = null;
    }

}
