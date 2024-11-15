package com.oreo.finalproject_5re5_be.tts.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Schema(description = "TTS 문장 생성 요청")
@Getter
@ToString
@AllArgsConstructor
@Builder
public class TtsSentenceCreateRequest {
    @Schema(description = "스타일 ID", example = "1")
    private Long styleSeq; // 스타일 id

    @Schema(description = "목소리 ID", example = "1")
    @NotNull(message = "voiceSeq is required")
    private Long voiceSeq; // 목소리 id

    @Schema(description = "텍스트 내용", example = "안녕하세요")
    @NotBlank(message = "text is required")
    private String text;  // 텍스트 내용

    @Schema(description = "표시 순서", example = "1")
    private Integer order;    // 표시 순서

    @Schema(description = "옵션 정보")
    @Valid
    private TtsAttributeInfo attribute; // 옵션 정보

    public static TtsSentenceCreateRequest of(Long styleSeq, Long voiceSeq, String text, Integer order, TtsAttributeInfo attribute) {
        return TtsSentenceCreateRequest.builder()
                .styleSeq(styleSeq)
                .voiceSeq(voiceSeq)
                .text(text)
                .order(order)
                .attribute(attribute)
                .build();
    }


}