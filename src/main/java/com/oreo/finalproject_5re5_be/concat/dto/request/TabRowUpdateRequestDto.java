package com.oreo.finalproject_5re5_be.concat.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TabRowUpdateRequestDto {
    private ConcatUpdateRequestDto concatTab;
    private ConcatRowSaveRequestDto concatRows;
}
