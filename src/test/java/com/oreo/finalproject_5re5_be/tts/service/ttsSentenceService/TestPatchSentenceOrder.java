package com.oreo.finalproject_5re5_be.tts.service.ttsSentenceService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.oreo.finalproject_5re5_be.global.exception.EntityNotFoundException;
import com.oreo.finalproject_5re5_be.project.repository.ProjectRepository;
import com.oreo.finalproject_5re5_be.tts.dto.response.TtsSentenceDto;
import com.oreo.finalproject_5re5_be.tts.entity.TtsSentence;
import com.oreo.finalproject_5re5_be.tts.repository.StyleRepository;
import com.oreo.finalproject_5re5_be.tts.repository.TtsProgressStatusRepository;
import com.oreo.finalproject_5re5_be.tts.repository.TtsSentenceRepository;
import com.oreo.finalproject_5re5_be.tts.repository.VoiceRepository;
import com.oreo.finalproject_5re5_be.tts.service.TtsSentenceService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class TestPatchSentenceOrder {

    @Autowired private TtsSentenceService ttsSentenceService;

    @MockBean private TtsSentenceRepository ttsSentenceRepository;

    @MockBean private ProjectRepository projectRepository;

    @MockBean private VoiceRepository voiceRepository;

    @MockBean private StyleRepository styleRepository;

    @MockBean private TtsProgressStatusRepository ttsProgressStatusRepository;

    /*
    테스트 시나리오: patchSentenceOrder 메서드

    1. **성공적인 순서 변경**
    - 조건:
    - 유효한 `projectSeq`, `tsSeq`, 그리고 `order`가 제공됨.
    - `tsSeq`에 해당하는 `TtsSentence` 엔티티가 데이터베이스에 존재.
    - 기대 결과:
    - `ttsSentenceRepository.findById` 메서드가 호출됨.
    - `ttsSentenceRepository.save` 메서드가 호출됨.
    - 저장된 `TtsSentence` 엔티티가 DTO로 변환되어 반환됨.

    2. **TtsSentence를 찾을 수 없음**
    - 조건:
    - 유효한 `projectSeq`와 `tsSeq`가 제공되었으나, `tsSeq`에 해당하는 `TtsSentence` 엔티티가 존재하지 않음.
    - 기대 결과:
    - `EntityNotFoundException` 예외 발생.

    3. **null `order`**
    - 조건:
    - `order`가 `null`로 제공됨.
    - 기대 결과:
    - `ConstraintViolationException` 예외 발생.

    4. **유효하지 않은 `order` 값**
    - 조건:
    - `order` 값이 음수 등 유효하지 않은 값으로 제공됨.
    - 기대 결과:
    - `ConstraintViolationException` 예외 발생.
    */

    // 1. 성공적인 순서 변경
    @Test
    @DisplayName("patchSentenceOrder - 성공적인 순서 변경")
    void patchSentenceOrder_Success() {
        // given
        Long tsSeq = 1L;
        Integer oldOrder = 1;
        Integer newOrder = 2;

        TtsSentence existingSentence = TtsSentence.builder().tsSeq(tsSeq).sortOrder(oldOrder).build();

        TtsSentence updatedSentence = existingSentence.toBuilder().sortOrder(newOrder).build();

        when(ttsSentenceRepository.findById(tsSeq)).thenReturn(java.util.Optional.of(existingSentence));
        when(ttsSentenceRepository.save(any(TtsSentence.class))).thenReturn(updatedSentence);

        // when
        TtsSentenceDto result = ttsSentenceService.patchSentenceOrder(1L, tsSeq, newOrder);

        // then
        assertNotNull(result); // 반환값이 null이 아님
        assertEquals(newOrder, result.getSentence().getOrder()); // 업데이트된 순서 확인
    }

    // 2. TtsSentence를 찾을 수 없음
    @Test
    @DisplayName("patchSentenceOrder - TtsSentence를 찾을 수 없음")
    void patchSentenceOrder_NotFound() {
        // given
        Long tsSeq = 999L;
        Integer newOrder = 2;

        when(ttsSentenceRepository.findById(tsSeq)).thenReturn(java.util.Optional.empty());

        // when, then
        assertThrows(
                EntityNotFoundException.class,
                () -> ttsSentenceService.patchSentenceOrder(1L, tsSeq, newOrder));
    }

    // 3. null order
    @Test
    @DisplayName("patchSentenceOrder - order가 null일 때")
    void patchSentenceOrder_NullOrder() {
        // given
        Long tsSeq = 1L;

        // when, then
        assertThrows(
                ConstraintViolationException.class,
                () -> ttsSentenceService.patchSentenceOrder(1L, tsSeq, null));
    }

    // 4. 유효하지 않은 order 값
    @Test
    @DisplayName("patchSentenceOrder - 유효하지 않은 order 값")
    void patchSentenceOrder_InvalidOrder() {
        // given
        Long tsSeq = 1L;
        Integer invalidOrder = -1; // 음수 값

        // when, then
        assertThrows(
                ConstraintViolationException.class,
                () -> ttsSentenceService.patchSentenceOrder(1L, tsSeq, invalidOrder));
    }
}
