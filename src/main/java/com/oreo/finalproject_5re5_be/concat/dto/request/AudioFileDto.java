package com.oreo.finalproject_5re5_be.concat.dto.request;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Builder
@AllArgsConstructor
@Getter
public class AudioFileDto {
    private Long audioFileSeq;
    private String audioUrl;
    private String extension;
    private Long fileSize;
    private Long fileLength;
    private String fileName;
    private LocalDateTime createdDate;
}
