package com.oreo.finalproject_5re5_be.concat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Builder
@AllArgsConstructor
public class ConcatTabResponseDto {
    private final Long tabId;
    private final float frontSilence;
    private final Character status;
}
