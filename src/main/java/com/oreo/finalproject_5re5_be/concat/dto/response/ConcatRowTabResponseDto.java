package com.oreo.finalproject_5re5_be.concat.dto.response;

import com.oreo.finalproject_5re5_be.concat.dto.RowAudioFileDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ConcatRowTabResponseDto {
    private ConcatTabResponseDto concatTab;
    private List<RowAudioFileDto> audioFiles;
}
