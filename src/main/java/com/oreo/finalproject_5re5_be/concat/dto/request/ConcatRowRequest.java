package com.oreo.finalproject_5re5_be.concat.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ConcatRowRequest { // 화면을 저장하기 위해 SelectedConcatRowRequest와 달리 selected여부도 저장해야함
    private Long seq;

    @NotNull(message = "originAudioRequest 필드는 null 일 수 없습니다.")
    private OriginAudioRequest originAudioRequest; // 행마다 매칭되는 원본 오디오파일

    @Size(max = 255, message = "rowText 필드는 최대 255자까지 허용됩니다.")
    private String rowText;

    @Size(max = 1, message = "selected 필드는 Y/N 1글자만 허용됩니다.")
    @NotNull(message = "selected 필드는 null 일 수 없습니다.")
    private Character selected;

    @PositiveOrZero(message = "rowSilence는 0 혹은 양수여야 합니다.")
    @NotNull(message = "rowSilence 필드는 null 일 수 없습니다.")
    private Float rowSilence;

    @Positive(message = "rowIndex 필드는 양수여야 합니다.")
    @NotNull(message = "rowIndex 필드는 null 일 수 없습니다.")
    private Integer rowIndex; // 행 순서

    private Character status;
}
