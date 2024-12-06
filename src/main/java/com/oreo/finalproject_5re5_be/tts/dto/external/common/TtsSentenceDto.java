package com.oreo.finalproject_5re5_be.tts.dto.external.common;


import com.oreo.finalproject_5re5_be.tts.entity.TtsSentence;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TtsSentenceDto {
    private String text;
    private VoiceDto voice;
    private AudioOptionDto audioOption;

    public static TtsSentenceDto of(TtsSentence ttsSentence) {
        return TtsSentenceDto.builder()
                .text(ttsSentence.getText())
                .voice(VoiceDto.of(ttsSentence.getVoice()))
                .audioOption(AudioOptionDto.of(ttsSentence))
                .build();
    }
}
