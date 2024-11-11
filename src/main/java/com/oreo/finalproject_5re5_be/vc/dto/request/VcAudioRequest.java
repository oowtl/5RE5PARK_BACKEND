package com.oreo.finalproject_5re5_be.vc.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class VcAudioRequest {
    private Long Seq1;
    private Long Seq2;
    private String name;
    private String fileUrl;
    private Integer length;
    private String  size;
    private String extension;
}
