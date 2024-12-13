package com.oreo.finalproject_5re5_be.tts.util;

import com.oreo.finalproject_5re5_be.tts.dto.request.TtsSentenceBatchInfo;
import com.oreo.finalproject_5re5_be.tts.dto.response.SentenceInfo;
import java.util.Comparator;

public class TtsSentenceComparator implements Comparator<TtsSentenceBatchInfo> {

    @Override
    public int compare(TtsSentenceBatchInfo firstInfo, TtsSentenceBatchInfo secondInfo) {

        SentenceInfo firstSentence = firstInfo.getSentence();
        SentenceInfo secondSentence = secondInfo.getSentence();

        // 1. 양쪽의 정렬 순서가 없다면 그대로 둔다.
        if (firstSentence.getOrder() == null && secondSentence.getOrder() == null) {
            return 0;
        }

        // 2. 한쪽 정렬 순서가 없다면 없는 쪽이 뒤로 간다.
        // 2.1 첫번째 정렬 순서가 없다면 첫번째가 뒤로 간다.
        if (firstSentence.getOrder() == null) {
            return 1;
        }
        // 2.2 두번째 정렬 순서가 없다면 두번째가 뒤로 간다.
        if (secondSentence.getOrder() == null) {
            return -1;
        }

        // 3. 양쪽의 정렬 순서가 존재하는 경우
        // 3.1 양쪽의 정렬 순서가 같다면 seq 로 비교한다.
        // 3.2 양쪽의 정렬 순서가 다르다면 정렬 순서로 비교한다.
        int result = firstSentence.getOrder().compareTo(secondSentence.getOrder());
        return result == 0 ? ttsSeqCompare(firstSentence, secondSentence) : result;
    }

    public int ttsSeqCompare(SentenceInfo firstSentence, SentenceInfo secondSentence) {
        // 1. 양쪽의 시퀀스가 없다면 그대로 둔다.
        if (firstSentence.getTsSeq() == null && secondSentence.getTsSeq() == null) {
            return 0;
        }

        // 2. 한쪽 시퀀스가 없다면 없는 쪽이 뒤로 간다.
        // 2.1 첫번째 시퀀스가 없다면 첫번째가 뒤로 간다.
        if (firstSentence.getTsSeq() == null) {
            return 1;
        }

        // 2.2 두번째 시퀀스가 없다면 두번째가 뒤로 간다.
        if (secondSentence.getTsSeq() == null) {
            return -1;
        }

        // 3. 양쪽의 시퀀스가 존재하는 경우 (그대로 비교)
        return firstSentence.getTsSeq().compareTo(secondSentence.getTsSeq());
    }
}
