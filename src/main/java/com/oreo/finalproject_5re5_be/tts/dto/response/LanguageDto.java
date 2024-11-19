package com.oreo.finalproject_5re5_be.tts.dto.response;

import com.oreo.finalproject_5re5_be.tts.entity.Language;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LanguageDto {
    private String languageCode;
    private String languageName;
    private String regionCode;
    private String regionName;

    public static LanguageDto of(Language language) {
        return LanguageDto.builder()
                .languageCode(language.getLangCode())
                .languageName(language.getLangName())
                .regionCode(language.getRegionCode())
                .regionName(language.getRegionName())
                .build();
    }

}