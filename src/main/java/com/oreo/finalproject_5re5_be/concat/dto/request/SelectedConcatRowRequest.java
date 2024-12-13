package com.oreo.finalproject_5re5_be.concat.dto.request;

import java.util.List;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SelectedConcatRowRequest {

    private List<Row> rows; // 선택된 행들의 리스트
    private float initialSilence; // 맨 앞에 삽입될 무음 구간

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
