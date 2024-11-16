package com.oreo.finalproject_5re5_be.tts.dto.request;

import com.oreo.finalproject_5re5_be.tts.dto.response.SentenceInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "TTS 문장 수정 요청")
@Getter
@AllArgsConstructor
@Builder
public class TtsSentenceUpdateRequest {
    @Schema(description = "TTS 문장 ID", example = "1")
    private Long tsSeq;

    @Schema(description = "목소리 ID", example = "1")
    @NotNull(message = "voiceSeq is required")
    private Long voiceSeq;

    @Schema(description = "스타일 ID", example = "1")
    private Long styleSeq;

    @Schema(description = "표시 순서", example = "1")
    @NotNull(message = "order is required")
    private Integer order;

    @Schema(description = "텍스트 내용", example = "안녕하세요")
    @NotBlank(message = "text is required")
    private String text;

    @Schema(description = "옵션 정보")
    @Valid
    private TtsAttributeInfo attribute;
}
