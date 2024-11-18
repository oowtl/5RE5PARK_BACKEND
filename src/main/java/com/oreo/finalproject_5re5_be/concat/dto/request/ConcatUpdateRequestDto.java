package com.oreo.finalproject_5re5_be.concat.dto.request;

import com.oreo.finalproject_5re5_be.concat.entity.ConcatOption;
import com.oreo.finalproject_5re5_be.member.entity.Member;
import lombok.*;

@ToString
@AllArgsConstructor
@Builder
@Getter
public class ConcatUpdateRequestDto {
    private final Member member;
    private final Long tabId;
    private final ConcatOption concatOption;
    private final float frontSilence;
    private final Character status;
}
