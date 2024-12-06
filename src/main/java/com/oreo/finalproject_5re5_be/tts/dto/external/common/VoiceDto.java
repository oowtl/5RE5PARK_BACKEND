package com.oreo.finalproject_5re5_be.tts.dto.external.common;

import com.oreo.finalproject_5re5_be.tts.entity.Voice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoiceDto {
    private String langCode;
    private String name;
    private String gender;

    public static VoiceDto of(Voice voice) {
        return VoiceDto.builder()
                .langCode(voice.getLanguage().getLangCode())
                .name(voice.getName())
                .gender(voice.getGender())
                .build();
    }
}
