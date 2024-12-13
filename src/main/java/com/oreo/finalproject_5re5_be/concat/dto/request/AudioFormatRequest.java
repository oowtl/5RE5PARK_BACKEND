package com.oreo.finalproject_5re5_be.concat.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AudioFormatRequest {

    private String encoding;
    private Integer sampleRate;
    private Short sampleSizeBit;
    private Short channel;
    private Integer frameSize;
    private Short frameRate;
    private Character isBigEndian;
}
