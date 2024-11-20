package com.oreo.finalproject_5re5_be.tts.dto.response;

import com.oreo.finalproject_5re5_be.tts.entity.Style;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StyleDto {
    private String name;
    private String mood;
    private String contents;
    private String desc;
    private Integer useCnt;
    private char recm;

    public static StyleDto of(Style style) {
        return StyleDto.builder()
                .name(style.getName())
                .mood(style.getMood())
                .contents(style.getContents())
                .desc(style.getDescription())
                .useCnt(style.getUseCnt())
                .recm(style.getIsRecommend())
                .build();
    }
}