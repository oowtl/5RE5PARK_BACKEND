package com.oreo.finalproject_5re5_be.concat.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ConcatResultDetailsResponse {
    private Long resultSeq;
    private String audioUrl;
    private String extension;
    private Long fileLength;
    private String fileName;
    private String optionName; // ConcatOption 정보


}
