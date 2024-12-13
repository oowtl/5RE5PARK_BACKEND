package com.oreo.finalproject_5re5_be.concat.dto;

import com.oreo.finalproject_5re5_be.concat.dto.request.OriginAudioRequest;
import java.util.List;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConcatResponseDto {
    private String audioUrl; // 결과 파일 URL
    private OriginAudioRequest bgmFile; // BGM 오디오 파일 정보
    private List<OriginAudioRequest> concatRowFiles; // ConcatRow에서 사용된 파일 정보 리스트
}
