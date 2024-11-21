package com.oreo.finalproject_5re5_be.tts.dto.response;

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
    private Long voiceSeq;
    private String name;
    private String gender;
    private Integer age;
    private Integer useCnt;
    private char isRecommend;

    public static VoiceDto of(Voice voice) {
        return VoiceDto.builder()
                .voiceSeq(voice.getVoiceSeq())
                .name(voice.getName())
                .gender(voice.getGender())
                .age(voice.getAge())
                .useCnt(voice.getUseCnt())
                .isRecommend(voice.getIsRecommend())
                .build();
    }
}
