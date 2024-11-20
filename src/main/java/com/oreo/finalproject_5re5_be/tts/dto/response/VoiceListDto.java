package com.oreo.finalproject_5re5_be.tts.dto.response;

import com.oreo.finalproject_5re5_be.tts.entity.Voice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoiceListDto {
    private List<VoiceDto> voiceList;

    public static VoiceListDto of(List<Voice> voiceList) {
        List<VoiceDto> voiceDtoList = voiceList.stream()
                .map(VoiceDto::of)
                .toList();

        return VoiceListDto.builder()
                .voiceList(voiceDtoList)
                .build();
    }
}
