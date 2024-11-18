package com.oreo.finalproject_5re5_be.tts.dto.request;

import com.oreo.finalproject_5re5_be.global.constant.BatchProcessType;
import com.oreo.finalproject_5re5_be.tts.dto.response.SentenceInfo;
import lombok.*;

@Getter
@Setter(AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class TtsSentenceBatchInfo {
    BatchProcessType batchProcessType;
    SentenceInfo sentence;

    public static TtsSentenceBatchInfo of(BatchProcessType batchProcessType, SentenceInfo sentenceInfo) {
        TtsSentenceBatchInfo ttsSentenceBatchInfo = new TtsSentenceBatchInfo();
        ttsSentenceBatchInfo.setBatchProcessType(batchProcessType);
        ttsSentenceBatchInfo.setSentence(sentenceInfo);
        return ttsSentenceBatchInfo;
    }
}