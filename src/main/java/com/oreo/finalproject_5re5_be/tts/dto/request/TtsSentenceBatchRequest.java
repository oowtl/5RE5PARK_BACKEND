package com.oreo.finalproject_5re5_be.tts.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TtsSentenceBatchRequest {
    @NotEmpty(message = "sentenceList is empty")
    private List<TtsSentenceBatchInfo> sentenceList;
}
