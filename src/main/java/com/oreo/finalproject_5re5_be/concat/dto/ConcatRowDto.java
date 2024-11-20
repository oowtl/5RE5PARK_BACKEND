package com.oreo.finalproject_5re5_be.concat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Builder
public class ConcatRowDto {
    private Long concatRowSequence;
    private Long projectSequence;
    private String rowText;
    private Character selected;
    private Character status;
    private Float silence;
    private Integer rowIndex;
}
