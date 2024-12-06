package com.oreo.finalproject_5re5_be.tts.dto.external.common;

import com.oreo.finalproject_5re5_be.tts.entity.TtsSentence;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AudioOptionDto {
    private Float speed;
    private Float pitch;
    private Integer volume;

    public static AudioOptionDto of(TtsSentence ttsSentence) {
        return AudioOptionDto.builder()
                .speed(ttsSentence.getSpeed())
                .pitch(ttsSentence.getEndPitch())
                .volume(ttsSentence.getVolume())
                .build();
    }
}
