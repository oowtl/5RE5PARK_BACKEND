package com.oreo.finalproject_5re5_be.tts.dto.request;

import com.oreo.finalproject_5re5_be.tts.entity.TtsAudioFile;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class TtsAudioFileInfo {
    private Long ttsAudioSeq; // TTS 오디오 파일 식별번호
    private String audioUrl; // 오디오 파일 경로
    private Character downloadYn; // 다운로드 가능 여부
    private Character audioPlayYn; // 재생 가능 여부

    public static TtsAudioFileInfo of(TtsAudioFile ttsAudioFile) {
        // null check
        if (ttsAudioFile == null) {
            return null;
        }

        return TtsAudioFileInfo.builder()
                .ttsAudioSeq(ttsAudioFile.getTtsAudioSeq())
                .audioUrl(ttsAudioFile.getAudioPath())
                .downloadYn(ttsAudioFile.getDownloadYn())
                .audioPlayYn(ttsAudioFile.getAudioPlayYn())
                .build();
    }
}
