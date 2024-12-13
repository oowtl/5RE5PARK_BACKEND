package com.oreo.finalproject_5re5_be.tts.dto.request;

import com.oreo.finalproject_5re5_be.tts.dto.response.SentenceInfo;
import com.oreo.finalproject_5re5_be.tts.util.TtsSentenceComparator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TtsSentenceBatchRequest {

    @Valid
    @NotEmpty(message = "sentenceList is empty")
    private List<TtsSentenceBatchInfo> sentenceList;

    public List<TtsSentenceBatchInfo> sortSentenceList() {
        // 정렬
        List<TtsSentenceBatchInfo> sortedSentencelist =
                sentenceList.stream()
                        .sorted(new TtsSentenceComparator())
                        .collect(Collectors.toCollection(ArrayList::new));

        // 정렬 순서 수정
        for (int orderIndex = 0; orderIndex < sortedSentencelist.size(); orderIndex++) {
            // 기존 SentenceInfo 조회
            SentenceInfo sentenceInfo = sortedSentencelist.get(orderIndex).getSentence();
            // orderIndex 순서로 수정
            SentenceInfo withOrderSentence = SentenceInfo.withOrder(sentenceInfo, orderIndex);
            // orderIndex 순서로 수정된 SentenceInfo 로 TtsSentenceBatchInfo 생성
            TtsSentenceBatchInfo withOrderBatchInfo =
                    TtsSentenceBatchInfo.of(
                            sortedSentencelist.get(orderIndex).getBatchProcessType(), withOrderSentence);
            // orderIndex 순서로 수정된 TtsSentenceBatchInfo 로 변경
            sortedSentencelist.set(orderIndex, withOrderBatchInfo);
        }

        return sortedSentencelist;
    }
}
