package com.oreo.finalproject_5re5_be.tts.dto.request;

import com.oreo.finalproject_5re5_be.global.constant.BatchProcessType;
import com.oreo.finalproject_5re5_be.tts.dto.response.SentenceInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TtsSentenceBatchInfo {
    BatchProcessType batchProcessType;
    SentenceInfo sentence;

    public static TtsSentenceBatchInfo of(BatchProcessType batchProcessType, SentenceInfo sentenceInfo) {
        return TtsSentenceBatchInfo.builder()
                .batchProcessType(batchProcessType)
                .sentence(sentenceInfo)
                .build();
    }
}