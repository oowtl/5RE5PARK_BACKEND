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
    private Float fileLength;
    private String fileName;

    //format 관련 필드
    private String encoding;
    private Float sampleRate;
    private Integer sampleSizeBit;
    private Integer channel;
    private Integer frameSize;
    private Float frameRate;
    private Character isBigEndian;


}
