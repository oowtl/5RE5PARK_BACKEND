package com.oreo.finalproject_5re5_be.tts.dto.external;

import com.oreo.finalproject_5re5_be.tts.dto.external.common.TtsSentenceDto;
import com.oreo.finalproject_5re5_be.tts.entity.TtsSentence;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TtsMakeRequest {
    private TtsSentenceDto ttsSentence;
    private String fileName;

    public static TtsMakeRequest of(TtsSentence ttsSentence, String fileName) {
        return TtsMakeRequest.builder()
                .ttsSentence(TtsSentenceDto.of(ttsSentence))
                .fileName(fileName)
                .build();
    }
}
