package com.oreo.finalproject_5re5_be.tts.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.oreo.finalproject_5re5_be.global.constant.BatchProcessType;
import com.oreo.finalproject_5re5_be.tts.dto.response.SentenceInfo;
import com.oreo.finalproject_5re5_be.tts.exception.InValidRequestException;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Getter
@Setter(AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class TtsSentenceBatchInfo {

    BatchProcessType batchProcessType;
    @NotNull
    SentenceInfo sentence;

    @JsonCreator
    public static TtsSentenceBatchInfo of(BatchProcessType batchProcessType,
        SentenceInfo sentenceInfo) {
        TtsSentenceBatchInfo ttsSentenceBatchInfo = new TtsSentenceBatchInfo();
        ttsSentenceBatchInfo.setBatchProcessType(batchProcessType);
        ttsSentenceBatchInfo.setSentence(sentenceInfo);
        return ttsSentenceBatchInfo;
    }


}