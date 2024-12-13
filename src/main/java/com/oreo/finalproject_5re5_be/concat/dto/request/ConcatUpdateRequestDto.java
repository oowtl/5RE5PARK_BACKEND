package com.oreo.finalproject_5re5_be.concat.dto.request;

import java.util.List;
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
    private final float frontSilence;
    private final Character status;
    private final List<OriginAudioRequest> bgmFileList; // BgmFile 정보 리스트
}
