package com.oreo.finalproject_5re5_be.concat.dto;

import com.oreo.finalproject_5re5_be.concat.entity.ConcatOption;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ConcatOptionDto {
    private Long optionSequence;
    private String optionName;

    public static ConcatOptionDto of(ConcatOption concatOption) {
        return new ConcatOptionDto(concatOption.getOptionSeq(), concatOption.getOptionName());
    }
}
