package com.oreo.finalproject_5re5_be.concat.dto.request;

import com.oreo.finalproject_5re5_be.concat.dto.ConcatOptionDto;
import lombok.*;

@ToString
@AllArgsConstructor
@Builder
@Getter
public class ConcatUpdateRequestDto {
    private final String memberId;
    private final Long tabId;
    private final ConcatOptionDto concatOption;
    private final float frontSilence;
    private final Character status;
}
