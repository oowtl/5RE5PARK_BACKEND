package com.oreo.finalproject_5re5_be.concat.dto.response;

import com.oreo.finalproject_5re5_be.concat.dto.ConcatOptionDto;
import lombok.*;

@ToString
@Getter
@Builder
@AllArgsConstructor
public class ConcatTabResponseDto {
    private final Long tabId;
    private final ConcatOptionDto concatOptionDto;
    private final float frontSilence;
    private final Character status;
}
