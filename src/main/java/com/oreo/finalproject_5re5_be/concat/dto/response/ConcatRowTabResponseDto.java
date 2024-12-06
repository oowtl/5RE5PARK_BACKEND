package com.oreo.finalproject_5re5_be.concat.dto.response;

import com.oreo.finalproject_5re5_be.concat.dto.RowAudioFileDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ConcatRowTabResponseDto {
    private ConcatTabResponseDto concatTab;
    private List<RowAudioFileDto> audioFiles;

}
