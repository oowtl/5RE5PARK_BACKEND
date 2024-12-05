package com.oreo.finalproject_5re5_be.concat.dto.request;


import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BgmFunctionRequestDto {

    private List<SelectedConcatRowRequest.Row> rows; // 선택된 행들의 리스트
    private float initialSilence; // 맨 앞에 삽입될 무음 구간

    // bgm작업에 필요한 정보
    private String bgmFileUrl;
    // 결과 파일 S3에 저장에 필요한 정보
    private String concatResultFileName;
    
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Row {
        private String audioUrl; // S3 URL 또는 파일 경로
        private float silenceInterval; // 해당 행 뒤에 삽입될 무음 간격
    }

}
