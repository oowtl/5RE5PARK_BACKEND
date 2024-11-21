package com.oreo.finalproject_5re5_be.concat.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ConcatResultDetailsResponse {

    private Long concatTabSeq; //객체가 아닌 seq를 반환
    private Long concatOptionSeq; //객체가 아닌 seq를 반환
    private String audioUrl;
    private String extension;
    private Long fileLength;
    private String fileName;

    //format 관련 필드
    private String encoding;
    private Integer sampleRate;
    private Short sampleSizeBit;
    private Short channel;
    private Integer frameSize;
    private Short frameRate;
    private Character isBigEndian;


}
