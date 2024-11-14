package com.oreo.finalproject_5re5_be.tts.dto.request;

import com.oreo.finalproject_5re5_be.tts.entity.TtsSentence;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

/*
 * TtsAttributeInfo
 * 기본값은 TTS 요청 부분에서 참조한다.
 */
@Schema(description = "TTS 문장의 옵션정보")
@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
public class TtsAttributeInfo {

    @Schema(description = "음성 크기", example = "100")
    @NotNull(message = "volume 는 필수 입력값입니다.")
    @Min(value = 0, message = "volume 는 0이상 부터 설정할 수 있습니다.")
    @Max(value = 100, message = "volume 는 100이하 까지 설정할 수 있습니다.")
    private Integer volume;            // 음성 크기

    @Schema(description = "음성 속도", example = "1.0")
    @Positive(message = "speed 는 0이상 부터 설정할 수 있습니다.") // 양수 허용
    private Float speed;           // 음성 속도

    @Schema(description = "시작 음성 높낮이", example = "0")
    @Min(value = -20, message = "stPitch 는 -20이상 부터 설정할 수 있습니다.")
    @Max(value = 20, message = "stPitch 는 20이하 까지 설정할 수 있습니다.")
    private Integer stPitch;           // 시작 음성 높낮이

    @Schema(description = "감정 구분", example = "neutral")
    private String emotion;        // 감정 구분

    @Schema(description = "감정 강도", example = "100")
    private Integer emotionStrength;   // 감정 강도

    @Schema(description = "샘플 속도", example = "16000")
    private Integer sampleRate;        // 샘플 속도

    @Schema(description = "음색", example = "0")
    private Integer alpha;             // 음색

    @Schema(description = "마지막 음성 높낮이", example = "0")
    private Float endPitch;        // 마지막 음성 높낮이

    @Schema(description = "오디오 파일 포맷", example = "wav")
    private String audioFormat;    // 오디오 파일 포맷

    public static TtsAttributeInfo of(Integer volume, Float speed, Integer stPitch, String emotion, Integer emotionStrength, Integer sampleRate, Integer alpha, Float endPitch, String audioFormat) {
        return TtsAttributeInfo.builder()
                .volume(volume)
                .speed(speed)
                .stPitch(stPitch)
                .emotion(emotion)
                .emotionStrength(emotionStrength)
                .sampleRate(sampleRate)
                .alpha(alpha)
                .endPitch(endPitch)
                .audioFormat(audioFormat)
                .build();
    }

    public static TtsAttributeInfo of(TtsSentence ttsSentence) {
        // null check
        if (ttsSentence == null) {
            return null;
        }

        return TtsAttributeInfo.builder()
                .volume(ttsSentence.getVolume())
                .speed(ttsSentence.getSpeed())
                .stPitch(ttsSentence.getStartPitch())
                .emotion(ttsSentence.getEmotion())
                .emotionStrength(ttsSentence.getEmotionStrength())
                .sampleRate(ttsSentence.getSampleRate())
                .alpha(ttsSentence.getAlpha())
                .endPitch(ttsSentence.getEndPitch())
                .audioFormat(ttsSentence.getAudioFormat())
                .build();
    }
}
