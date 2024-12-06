package com.oreo.finalproject_5re5_be.concat.dto.response;

import com.oreo.finalproject_5re5_be.concat.dto.request.ConcatRowSaveRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;


@Getter
@AllArgsConstructor
@ToString
public class TabRowResponseDto {
    private ConcatTabResponseDto concatTab;
    private ConcatRowSaveRequestDto concatRows;
}
