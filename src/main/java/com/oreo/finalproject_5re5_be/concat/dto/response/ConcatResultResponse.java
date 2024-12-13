package com.oreo.finalproject_5re5_be.concat.dto.response;

import com.oreo.finalproject_5re5_be.concat.dto.request.OriginAudioRequest;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ConcatResultResponse {
    private Long concatResultSeq;
    private String audioUrl;
    private String extension;
    private Float fileLength;
    private String fileName;
    private OriginAudioRequest bgmFile; // 사용된 BGM 파일
    private List<OriginAudioRequest> materialAudioFiles; // 재료 오디오 파일 목록
}
