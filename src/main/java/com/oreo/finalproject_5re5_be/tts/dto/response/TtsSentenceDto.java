package com.oreo.finalproject_5re5_be.tts.dto.response;

import com.oreo.finalproject_5re5_be.tts.entity.TtsSentence;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TtsSentenceDto {
    private SentenceInfo sentence;

    public static TtsSentenceDto of(TtsSentence ttsSentence) {
        SentenceInfo sentence = SentenceInfo.of(ttsSentence);

        return TtsSentenceDto.builder().sentence(sentence).build();
    }
}
