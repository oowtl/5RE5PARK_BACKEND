package com.oreo.finalproject_5re5_be.concat.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;

public class ConcatTabRequest {
    @NotNull(message = "seq 필드는 null 일 수 없습니다.")
    private Long seq;

    @NotNull(message = "concatRows 필드는 null 일 수 없습니다.")
    private List<ConcatRowRequest> concatRows;

    @PositiveOrZero(message = "rowIndex 필드는 0또는 양수여야 합니다.")
    @NotNull(message = "frontSilence 필드는 null 일 수 없습니다.")
    private Float frontSilence;

    private String BgmFileUrl; //없을 수도 있음
}
