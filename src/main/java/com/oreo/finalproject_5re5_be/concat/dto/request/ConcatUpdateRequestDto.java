package com.oreo.finalproject_5re5_be.concat.dto.request;

import com.oreo.finalproject_5re5_be.concat.dto.ConcatOptionDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@AllArgsConstructor
@Builder
@Getter
public class ConcatUpdateRequestDto {
    private final Long tabId;
    private final ConcatOptionDto concatOption;
    private final float frontSilence;
    private final Character status;

}
