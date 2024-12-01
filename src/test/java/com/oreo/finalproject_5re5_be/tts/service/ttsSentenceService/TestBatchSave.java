package com.oreo.finalproject_5re5_be.tts.service.ttsSentenceService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.oreo.finalproject_5re5_be.global.constant.BatchProcessType;
import com.oreo.finalproject_5re5_be.global.exception.EntityNotFoundException;
import com.oreo.finalproject_5re5_be.project.entity.Project;
import com.oreo.finalproject_5re5_be.project.repository.ProjectRepository;
import com.oreo.finalproject_5re5_be.tts.dto.request.TtsAttributeInfo;
import com.oreo.finalproject_5re5_be.tts.dto.request.TtsSentenceBatchInfo;
import com.oreo.finalproject_5re5_be.tts.dto.request.TtsSentenceBatchRequest;
import com.oreo.finalproject_5re5_be.tts.dto.response.SentenceInfo;
import com.oreo.finalproject_5re5_be.tts.dto.response.TtsSentenceListDto;
import com.oreo.finalproject_5re5_be.tts.entity.Style;
import com.oreo.finalproject_5re5_be.tts.entity.TtsProgressStatus;
import com.oreo.finalproject_5re5_be.tts.entity.TtsProgressStatusCode;
import com.oreo.finalproject_5re5_be.tts.entity.TtsSentence;
import com.oreo.finalproject_5re5_be.tts.entity.Voice;
import com.oreo.finalproject_5re5_be.tts.exception.TtsSentenceInValidInput;
import com.oreo.finalproject_5re5_be.tts.repository.StyleRepository;
import com.oreo.finalproject_5re5_be.tts.repository.TtsProgressStatusRepository;
import com.oreo.finalproject_5re5_be.tts.repository.TtsSentenceRepository;
import com.oreo.finalproject_5re5_be.tts.repository.VoiceRepository;
import com.oreo.finalproject_5re5_be.tts.service.TtsSentenceService;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class TestBatchSave {

    @Autowired
    private TtsSentenceService ttsSentenceService;

    @MockBean
    private TtsSentenceRepository ttsSentenceRepository;

    @MockBean
    private ProjectRepository projectRepository;

    @MockBean
    private VoiceRepository voiceRepository;

    @MockBean
    private StyleRepository styleRepository;

    @MockBean
    private TtsProgressStatusRepository ttsProgressStatusRepository;

    /*
    1. 성공 케이스 테스트
    - 조건:
    - 유효한 projectSeq와 sentenceList가 제공됨.
    - sentenceList 내 BatchProcessType이 CREATE 또는 UPDATE로 설정.
    - 기대 결과:
    - TtsSentenceListDto 반환.

    2. 유효성 검증 실패 테스트 - sentenceList가 null
    - 조건:
    - BatchRequest의 sentenceList가 null로 제공됨.
    - 기대 결과:
    - TtsSentenceInValidInput 예외 발생.


    3. 유효성 검증 실패 테스트 - sentenceInfo가 null
    - 조건:
    - sentenceList 내 sentenceInfo가 null인 데이터 포함.
    - 기대 결과:
    - TtsSentenceInValidInput 예외 발생.

    4. 잘못된 BatchProcessType 테스트
    - 조건:
    - BatchProcessType이 null로 제공됨.
    - 기대 결과:
    - TtsSentenceInValidInput 예외 발생.

    5. 리소스 존재하지 않음 테스트 - projectSeq가 존재하지 않음
    - 조건:
    - ProjectRepository에서 projectSeq에 해당하는 Project가 없음.
    - 기대 결과:
    - TtsSentenceInValidInput 예외 발생.

    6. 빈 sentenceList 테스트
    - 조건:
    - BatchRequest의 sentenceList가 빈 리스트로 제공됨.
    - 기대 결과:
    - 빈 TtsSentenceDto 리스트 반환.

    정렬 테스트

    7. 정렬 테스트
    - 조건:
    - sentenceList 내 sentenceInfo의 order가 정렬되어 있지 않음.
    - 기대 결과:
    - TtsSentenceListDto 반환.

    8. 정렬 테스트 - order 중복
    - 조건:
    - sentenceList 내 sentenceInfo의 order가 중복된 것이 있음
    - 기대 결과:
    - TsSeq 기준 정렬 후 TtsSentenceListDto 반환.
    - TsSeq 기준 정렬 불가 시, 임의로 정렬 후 TtsSentenceListDto 반환.

    9. 정렬 테스트 - order 미설정
    - 조건:
    - sentenceList 내 sentenceInfo의 order가 설정되지 않음.
    - 기대 결과:
    - TsSeq 기준 정렬 후 TtsSentenceListDto 반환.
    - TsSeq 기준 정렬 불가 시, 임의로 정렬 후 TtsSentenceListDto 반환.

    10. batchProcessType이 DELETE 인 경우 성공 테스트
    - 조건 성공 테스트 input 에서 batchProcessType을 DELETE로 넣어서 테스트
    - 기대 결과:
    - DELETE 에 해당하는 TtsSentence는 삭제되어서 저장된다.
    - 삭제된 행이 적용된 TtsSentenceListDto 반환.
    */

    // 1. 성공 케이스 테스트
    @Test
    @DisplayName("batchSaveSentence - 성공 케이스")
    void batchSaveSentence_Success() {
        // given: 유효한 projectSeq와 sentenceList 생성
        Long projectSeq = 1L; // 유효한 projectSeq 설정

        Project project = Project.builder().proSeq(projectSeq).build();
        Voice voice = Voice.builder().voiceSeq(1L).build();
        Style style = Style.builder().styleSeq(1L).build();

        TtsSentenceBatchRequest batchRequest = createBatchRequest(); // 유효한 batchRequest 생성

        TtsSentence ttsSentence = TtsSentence.builder()
            .tsSeq(1L)
            .text("Test text")
            .sortOrder(1)
            .volume(100)
            .speed(1.0f)
            .startPitch(0)
            .emotion("normal")
            .emotionStrength(0)
            .sampleRate(16000)
            .alpha(0)
            .endPitch(0.0f)
            .audioFormat("wav")
            .project(mock(Project.class))
            .voice(mock(Voice.class))
            .build();

        TtsProgressStatus ttsProgressStatus = TtsProgressStatus.builder()
            .ttsSentence(ttsSentence)
            .progressStatus(TtsProgressStatusCode.CREATED)
            .build();

        // 프로젝트가 존재한다고 설정
        when(projectRepository.findById(projectSeq)).thenReturn(Optional.of(project));
        // voice이 존재한다고 설정
        when(voiceRepository.findById(anyLong())).thenReturn(Optional.of(voice));
        // style이 존재한다고 설정
        when(styleRepository.findById(anyLong())).thenReturn(Optional.of(style));
        // ttsSentece 가 존재한다고 설정
        when(ttsSentenceRepository.findById(anyLong())).thenReturn(Optional.of(ttsSentence));
        // 각 문장에 대해 TtsSentenceDto 반환
        when(ttsSentenceRepository.save(any())).thenReturn(ttsSentence);
        // ttsProgressStatus 가 존재한다고 설정
        when(ttsProgressStatusRepository.save(any())).thenReturn(ttsProgressStatus);

        // when: batchSaveSentence 메서드 호출
        TtsSentenceListDto result = ttsSentenceService.batchSaveSentence(projectSeq, batchRequest);

        // then: 반환된 결과 검증
        assertNotNull(result); // 결과가 null이 아님
//        assertFalse(result.getSentenceList().isEmpty()); // 리스트가 비어 있지 않음
    }

    // 2. 유효성 검증 실패 테스트 - sentenceList가 null
    @Test
    @DisplayName("batchSaveSentence - sentenceList가 null")
    void batchSaveSentence_SentenceListNull() {
        // given: sentenceList가 null인 batchRequest 생성
        TtsSentenceBatchRequest batchRequest = new TtsSentenceBatchRequest(
            null); // sentenceList가 null로 설정

        // when, then: 예외 발생 검증
        assertThrows(ConstraintViolationException.class,
            () -> ttsSentenceService.batchSaveSentence(1L, batchRequest));
    }

    // 3. 유효성 검증 실패 테스트 - sentenceInfo가 null
    @Test
    @DisplayName("batchSaveSentence - sentenceInfo가 null")
    void batchSaveSentence_SentenceInfoNull() {
        // given: sentenceInfo가 null인 batchRequest 생성
        TtsSentenceBatchInfo batchInfo = new TtsSentenceBatchInfo(BatchProcessType.CREATE, null);
        TtsSentenceBatchRequest batchRequest = new TtsSentenceBatchRequest(List.of(batchInfo));

        // when, then: 예외 발생 검증
//        assertThrows(TtsSentenceInValidInput.class,
        assertThrows(ConstraintViolationException.class,
            () -> ttsSentenceService.batchSaveSentence(1L, batchRequest));
    }

    // 4. 잘못된 BatchProcessType 테스트
    @Test
    @DisplayName("batchSaveSentence - 잘못된 BatchProcessType")
    void batchSaveSentence_InvalidBatchProcessType() {
        // given: BatchProcessType이 null인 batchRequest 생성
        TtsSentenceBatchInfo batchInfo = new TtsSentenceBatchInfo(null, mock(SentenceInfo.class));
        TtsSentenceBatchRequest batchRequest = new TtsSentenceBatchRequest(List.of(batchInfo));

        // when, then: 예외 발생 검증
        assertThrows(TtsSentenceInValidInput.class,
            () -> ttsSentenceService.batchSaveSentence(1L, batchRequest));
    }

    // 5. 리소스 존재하지 않음 테스트 - projectSeq가 존재하지 않음
    @Test
    @DisplayName("batchSaveSentence - projectSeq가 존재하지 않음")
    void batchSaveSentence_ProjectSeqNotFound() {
        // given: 존재하지 않는 projectSeq 설정
        Long projectSeq = 999999L;
        TtsSentenceBatchRequest batchRequest = createBatchRequest();

        // projectRepository가 empty 반환하도록 설정
        when(projectRepository.findById(projectSeq)).thenReturn(Optional.empty());

        // when, then: 예외 발생 검증
        assertThrows(EntityNotFoundException.class,
            () -> ttsSentenceService.batchSaveSentence(projectSeq, batchRequest));
    }

    // 6. 빈 sentenceList 테스트
    @Test
    @DisplayName("batchSaveSentence - 빈 sentenceList")
    void batchSaveSentence_EmptySentenceList() {
        // given: 빈 sentenceList가 포함된 batchRequest 생성
        Long projectSeq = 1L;
        TtsSentenceBatchRequest batchRequest = new TtsSentenceBatchRequest(List.of());

        // when, then: batchSaveSentence 메서드 호출
        assertThrows(ConstraintViolationException.class,
            () -> ttsSentenceService.batchSaveSentence(projectSeq, batchRequest));
    }

    // 7. 정렬 테스트
    @Test
    @DisplayName("batchSaveSentence - 정렬 테스트")
    void batchSaveSentence_SortOrder() {
        // given: 정렬되지 않은 sentenceList가 포함된 batchRequest 생성

        // 정렬되지 않은 sentenceList 생성
        TtsSentenceBatchRequest request = new TtsSentenceBatchRequest(List.of(
            createBatchInfoWithOrder(BatchProcessType.CREATE, 3),
            createBatchInfoWithOrder(BatchProcessType.UPDATE, 1),
            createBatchInfoWithOrder(BatchProcessType.CREATE, 2),
            createBatchInfoWithOrder(BatchProcessType.UPDATE, 5),
            createBatchInfoWithOrder(BatchProcessType.CREATE, 4)
        ));

        // when
        // 정렬된 sentenceList 생성
        List<TtsSentenceBatchInfo> sortedList = request.sortSentenceList();

        // then: 반환된 결과 검증
        assertEquals(0, sortedList.get(0).getSentence().getOrder());
        assertEquals(1, sortedList.get(1).getSentence().getOrder());
        assertEquals(2, sortedList.get(2).getSentence().getOrder());
        assertEquals(3, sortedList.get(3).getSentence().getOrder());
        assertEquals(4, sortedList.get(4).getSentence().getOrder());
    }

    // 8. 정렬 테스트 - order 중복
    @Test
    @DisplayName("batchSaveSentence - 정렬 테스트 - order 중복")
    void batchSaveSentence_SortOrder_Duplicate() {
        // given: 중복된 order가 포함된 sentenceList가 포함된 batchRequest 생성
        TtsSentenceBatchRequest request = new TtsSentenceBatchRequest(List.of(
            createBatchInfoWithOrder(BatchProcessType.CREATE, 3),
            createBatchInfoWithOrder(BatchProcessType.UPDATE, 1),
            createBatchInfoWithOrder(BatchProcessType.CREATE, 2),
            createBatchInfoWithOrder(BatchProcessType.UPDATE, 2),
            createBatchInfoWithOrder(BatchProcessType.CREATE, 3)
        ));

        // when
        // 정렬된 sentenceList 생성
        List<TtsSentenceBatchInfo> sortedList = request.sortSentenceList();

        // then: 반환된 결과 검증
        assertEquals(0, sortedList.get(0).getSentence().getOrder());
        assertEquals(1, sortedList.get(1).getSentence().getOrder());
        assertEquals(2, sortedList.get(2).getSentence().getOrder());
        assertEquals(3, sortedList.get(3).getSentence().getOrder());
        assertEquals(4, sortedList.get(4).getSentence().getOrder());
    }

    // 9. 정렬 테스트 - order 미설정
    @Test
    @DisplayName("batchSaveSentence - 정렬 테스트 - order 미설정")
    void batchSaveSentence_SortOrder_NotSet() {
        // given: order가 설정되지 않은 sentenceList가 포함된 batchRequest 생성
        TtsSentenceBatchRequest request = new TtsSentenceBatchRequest(List.of(
            createBatchInfoWithOrder(BatchProcessType.CREATE, null),
            createBatchInfoWithOrder(BatchProcessType.UPDATE, 1),
            createBatchInfoWithOrder(BatchProcessType.CREATE, null),
            createBatchInfoWithOrder(BatchProcessType.UPDATE, 5),
            createBatchInfoWithOrder(BatchProcessType.CREATE, 4)
        ));

        // when
        // 정렬된 sentenceList 생성
        List<TtsSentenceBatchInfo> sortedList = request.sortSentenceList();

        // then: 반환된 결과 검증
        assertEquals(0, sortedList.get(0).getSentence().getOrder());
        assertEquals(1, sortedList.get(1).getSentence().getOrder());
        assertEquals(2, sortedList.get(2).getSentence().getOrder());
        assertEquals(3, sortedList.get(3).getSentence().getOrder());
        assertEquals(4, sortedList.get(4).getSentence().getOrder());
    }

    // 10. batchProcessType이 DELETE 인 경우
    @Test
    @DisplayName("batchSaveSentence - batchProcessType이 DELETE 인 경우")
    void batchSaveSentence_DeleteBatchProcessType() {
        // given: 유효한 projectSeq와 sentenceList 생성
        Long projectSeq = 1L; // 유효한 projectSeq 설정

        Project project = Project.builder().proSeq(projectSeq).build();
        Voice voice = Voice.builder().voiceSeq(1L).build();
        Style style = Style.builder().styleSeq(1L).build();


        // List<TtsSentenceBatchInfo> 생성
        List<TtsSentenceBatchInfo> reqList = List.of(
            createBatchInfoWithOrder(BatchProcessType.DELETE, 3),
            createBatchInfoWithOrder(BatchProcessType.UPDATE, 1),
            createBatchInfoWithOrder(BatchProcessType.CREATE, 2),
            createBatchInfoWithOrder(BatchProcessType.UPDATE, 5),
            createBatchInfoWithOrder(BatchProcessType.CREATE, 4)
        );

        // batchRequest 생성
        TtsSentenceBatchRequest batchRequest = new TtsSentenceBatchRequest(reqList);

        TtsSentence ttsSentence = TtsSentence.builder()
            .tsSeq(1L)
            .text("Test text")
            .sortOrder(1)
            .volume(100)
            .speed(1.0f)
            .startPitch(0)
            .emotion("normal")
            .emotionStrength(0)
            .sampleRate(16000)
            .alpha(0)
            .endPitch(0.0f)
            .audioFormat("wav")
            .project(mock(Project.class))
            .voice(mock(Voice.class))
            .build();

        TtsProgressStatus ttsProgressStatus = TtsProgressStatus.builder()
            .ttsSentence(ttsSentence)
            .progressStatus(TtsProgressStatusCode.CREATED)
            .build();

        // 프로젝트가 존재한다고 설정
        when(projectRepository.findById(projectSeq)).thenReturn(Optional.of(project));
        // voice이 존재한다고 설정
        when(voiceRepository.findById(anyLong())).thenReturn(Optional.of(voice));
        // style이 존재한다고 설정
        when(styleRepository.findById(anyLong())).thenReturn(Optional.of(style));
        // ttsSentece 가 존재한다고 설정
        when(ttsSentenceRepository.findById(any())).thenReturn(Optional.of(ttsSentence));
        // 각 문장에 대해 TtsSentenceDto 반환
        when(ttsSentenceRepository.save(any())).thenReturn(ttsSentence);
        // 삭제 = 호출 횟수 검증
        doNothing().when(ttsSentenceRepository).delete(any());

        // ttsProgressStatus 가 존재한다고 설정
        when(ttsProgressStatusRepository.save(any())).thenReturn(ttsProgressStatus);

        // when
        // 정렬된 sentenceList 생성
        TtsSentenceListDto batchResponse = ttsSentenceService.batchSaveSentence(projectSeq, batchRequest);

        // then: 반환된 결과 검증
        verify(ttsSentenceRepository, times(1)).delete(any());
//        assertEquals(reqList.size() - 1, batchResponse.getSentenceList().size()); // delete 로 인한 갯수 감소반영 확인
    }

    /*
    데이터 생성 메서드
     */

    // 유효한 TtsSentenceBatchRequest 생성
    private TtsSentenceBatchRequest createBatchRequest() {

        SentenceInfo updateSentenceInfo1 = SentenceInfo.builder()
            .tsSeq(1L)
            .voiceSeq(1L)
            .styleSeq(1L)
            .order(1)
            .text("Test text")
            .ttsAttributeInfo(createAttribute())
            .build();

        SentenceInfo updateSentenceInfo2 = SentenceInfo.builder()
            .tsSeq(1L)
            .voiceSeq(1L)
            .styleSeq(1L)
            .order(2)
            .text("Test text")
            .ttsAttributeInfo(createAttribute())
            .build();

        SentenceInfo createSentenceInfo3 = SentenceInfo.builder()
            .voiceSeq(1L)
            .styleSeq(1L)
            .order(3)
            .text("Test text")
            .ttsAttributeInfo(createAttribute())
            .build();

        SentenceInfo createSentenceInfo4 = SentenceInfo.builder()
            .voiceSeq(1L)
            .styleSeq(1L)
            .order(4)
            .text("Test text")
            .ttsAttributeInfo(createAttribute())
            .build();

        TtsSentenceBatchInfo batchInfo1 = new TtsSentenceBatchInfo(BatchProcessType.UPDATE,
            updateSentenceInfo1);
        TtsSentenceBatchInfo batchInfo2 = new TtsSentenceBatchInfo(BatchProcessType.UPDATE,
            updateSentenceInfo2);
        TtsSentenceBatchInfo batchInfo3 = new TtsSentenceBatchInfo(BatchProcessType.CREATE,
            createSentenceInfo3);
        TtsSentenceBatchInfo batchInfo4 = new TtsSentenceBatchInfo(BatchProcessType.CREATE,
            createSentenceInfo4);
        return new TtsSentenceBatchRequest(List.of(batchInfo1, batchInfo2, batchInfo3, batchInfo4));
    }

    private TtsSentenceBatchInfo createBatchInfoWithOrder(BatchProcessType batchProcessType,
        Integer orderIndex) {
        SentenceInfo sentenceInfo = SentenceInfo.builder()
            .voiceSeq(1L)
            .styleSeq(1L)
            .order(orderIndex)
            .text("Test text")
            .ttsAttributeInfo(createAttribute())
            .build();
        return new TtsSentenceBatchInfo(batchProcessType, sentenceInfo);
    }

    private TtsAttributeInfo createAttribute() {
        return TtsAttributeInfo.of(100, 1.0f, 0, "normal", 0, 16000, 0, 0.0f, "wav");
    }
}
