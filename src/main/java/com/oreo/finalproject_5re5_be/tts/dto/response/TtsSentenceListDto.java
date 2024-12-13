package com.oreo.finalproject_5re5_be.tts.dto.response;

import com.oreo.finalproject_5re5_be.tts.entity.TtsSentence;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TtsSentenceListDto {

    private List<TtsSentenceDto> sentenceList;

    public static TtsSentenceListDto of(List<TtsSentence> ttsSentenceList) {
        List<TtsSentenceDto> sentenceList = ttsSentenceList.stream().map(TtsSentenceDto::of).toList();

        return TtsSentenceListDto.builder().sentenceList(sentenceList).build();
    }
}
