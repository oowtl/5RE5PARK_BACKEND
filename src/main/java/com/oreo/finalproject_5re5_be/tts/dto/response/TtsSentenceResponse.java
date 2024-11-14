package com.oreo.finalproject_5re5_be.tts.dto.response;

import com.oreo.finalproject_5re5_be.tts.dto.request.TtsAttributeInfo;
import com.oreo.finalproject_5re5_be.tts.dto.request.TtsAudioFileInfo;
import com.oreo.finalproject_5re5_be.tts.entity.TtsSentence;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TtsSentenceResponse {
    private Long tsSeq; // 행 id
    private Long styleSeq;    // 스타일 id
    private String text;     // 텍스트 내용
    private Integer order;       // 표시 순서

    private TtsAttributeInfo ttsAttributeInfo; // 옵션 정보
    private TtsAudioFileInfo ttsAudioFileInfo;     // TTS 오디오 정보

    public static TtsSentenceResponse of(TtsSentence ttsSentence) {
        TtsSentenceResponse ttsSentenceResponse = new TtsSentenceResponse();

        ttsSentenceResponse.tsSeq = ttsSentence.getTsSeq();
        ttsSentenceResponse.styleSeq = ttsSentence.getStyle().getStyleSeq();
        ttsSentenceResponse.text = ttsSentence.getText();
        ttsSentenceResponse.order = ttsSentence.getSortOrder();
        ttsSentenceResponse.ttsAttributeInfo = TtsAttributeInfo.of(ttsSentence);
        ttsSentenceResponse.ttsAudioFileInfo = TtsAudioFileInfo.of(ttsSentence.getTtsAudiofile());

        return ttsSentenceResponse;
    }

}
