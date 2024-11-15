package com.oreo.finalproject_5re5_be.tts.dto.response;

import com.oreo.finalproject_5re5_be.tts.dto.request.TtsAttributeInfo;
import com.oreo.finalproject_5re5_be.tts.dto.request.TtsAudioFileInfo;
import com.oreo.finalproject_5re5_be.tts.entity.TtsSentence;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SentenceInfo {
    private Long tsSeq; // 행 id
    private Long voiceSeq;    // 목소리 id
    private Long styleSeq;    // 스타일 id
    private String text;     // 텍스트 내용
    private Integer order;       // 표시 순서

    private TtsAttributeInfo ttsAttributeInfo; // 옵션 정보
    private TtsAudioFileInfo ttsAudioFileInfo;     // TTS 오디오 정보

    public static SentenceInfo of(TtsSentence ttsSentence) {

        return SentenceInfo.builder()
                .tsSeq(ttsSentence.getTsSeq())
                .voiceSeq(ttsSentence.getVoice() != null ? ttsSentence.getVoice().getVoiceSeq() : null)
                .styleSeq(ttsSentence.getStyle() != null ? ttsSentence.getStyle().getStyleSeq() : null)
                .text(ttsSentence.getText())
                .order(ttsSentence.getSortOrder())
                .ttsAttributeInfo(TtsAttributeInfo.of(ttsSentence))
                .ttsAudioFileInfo(TtsAudioFileInfo.of(ttsSentence.getTtsAudiofile()))
                .build();
    }
}
