package com.oreo.finalproject_5re5_be.concat.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ConcatResultDto {
    private Long concatResultSequence;
    private Long fileSize;
    private Float fileLength;
    private Integer seperated;

    private String audioUrl;
    private String fileName;
    private String extension;
    private String processId;
}
