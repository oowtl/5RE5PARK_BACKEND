package com.oreo.finalproject_5re5_be.tts.dto.response;

import com.oreo.finalproject_5re5_be.tts.dto.external.common.TtsSentenceDto;
import com.oreo.finalproject_5re5_be.tts.entity.TtsProgressStatus;
import com.oreo.finalproject_5re5_be.tts.entity.TtsProgressStatusCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TtsProgressStatusDto {
    private TtsSentenceDto ttsSentence;
    private TtsProgressStatusCode status;
    private LocalDateTime createAt;

    public static TtsProgressStatusDto of(TtsProgressStatus ttsProgressStatus) {
        return TtsProgressStatusDto.builder()
                .ttsSentence(TtsSentenceDto.of(ttsProgressStatus.getTtsSentence()))
                .status(ttsProgressStatus.getProgressStatus())
                .createAt(ttsProgressStatus.getChanged_at())
                .build();
    }
}
