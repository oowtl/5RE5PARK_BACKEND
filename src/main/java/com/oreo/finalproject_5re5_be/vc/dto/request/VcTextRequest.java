package com.oreo.finalproject_5re5_be.vc.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class VcTextRequest {
    private Long SrcSeq;
    private String text;
}
