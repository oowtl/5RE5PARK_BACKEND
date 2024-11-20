package com.oreo.finalproject_5re5_be.tts.dto.response;

import com.oreo.finalproject_5re5_be.tts.entity.Style;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StyleListDto {
    private List<StyleDto> styleList;

    public static StyleListDto of(List<Style> styleList) {
        List<StyleDto> list = styleList.stream()
                .map(StyleDto::of)
                .toList();

        return StyleListDto.builder()
                .styleList(list)
                .build();
    }
}
