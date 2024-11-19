package com.oreo.finalproject_5re5_be.tts.dto.response;

import com.oreo.finalproject_5re5_be.tts.entity.Language;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LanguageListDto {
    private List<LanguageDto> languageList;


    public static LanguageListDto of(List<Language> languageList) {
        List<LanguageDto> list = languageList.stream()
                .map(LanguageDto::of)
                .toList();

        return LanguageListDto.builder()
                .languageList(list)
                .build();
    }
}
