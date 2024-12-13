package com.oreo.finalproject_5re5_be.tts.dto.response;

import com.oreo.finalproject_5re5_be.tts.entity.Voice;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoiceListDto {
    private List<VoiceDto> voiceList;

    public static VoiceListDto of(List<Voice> voiceList) {
        List<VoiceDto> voiceDtoList = voiceList.stream().map(VoiceDto::of).toList();

        return VoiceListDto.builder().voiceList(voiceDtoList).build();
    }
}
