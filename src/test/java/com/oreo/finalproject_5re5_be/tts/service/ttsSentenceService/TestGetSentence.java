package com.oreo.finalproject_5re5_be.tts.service.ttsSentenceService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.oreo.finalproject_5re5_be.global.exception.EntityNotFoundException;
import com.oreo.finalproject_5re5_be.tts.dto.response.TtsSentenceDto;
import com.oreo.finalproject_5re5_be.tts.entity.TtsSentence;
import com.oreo.finalproject_5re5_be.tts.repository.TtsSentenceRepository;
import com.oreo.finalproject_5re5_be.tts.service.TtsSentenceService;
import jakarta.validation.ConstraintViolationException;
import java.util.Optional;
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
class TestGetSentence {

    @Autowired private TtsSentenceService ttsSentenceService;

    @MockBean private TtsSentenceRepository ttsSentenceRepository;

    /*
    테스트 시나리오: getSentence 메서드

    1. **성공적인 조회**
    - 조건:
    - 유효한 `projectSeq`와 `tsSeq`가 제공됨.
    - 제공된 `tsSeq`에 해당하는 `TtsSentence` 엔티티가 데이터베이스에 존재함.
    - 기대 결과:
    - `tsSeq`에 해당하는 `TtsSentenceDto`를 반환.

    2. **TtsSentence를 찾을 수 없음**
    - 조건:
    - 유효한 `projectSeq`와 `tsSeq`가 제공됨.
    - 제공된 `tsSeq`에 해당하는 `TtsSentence` 엔티티가 데이터베이스에 존재하지 않음.
    - 기대 결과:
    - `EntityNotFoundException` 예외가 발생하며 적절한 에러 메시지를 포함.

    3. **null `tsSeq`**
    - 조건:
    - `tsSeq`가 `null`로 제공됨.
    - 기대 결과:
    - 입력값이 유효하지 않아 `IllegalArgumentException` 예외가 발생.

    4. **유효하지 않은 `tsSeq`**
    - 조건:
    - 존재하지 않거나 음수인 `tsSeq`가 제공됨.
    - 기대 결과:
    - 데이터베이스에 존재하지 않는 `tsSeq`로 인해 `EntityNotFoundException` 예외가 발생.
    */

    // 1. 성공적인 조회
    @Test
    @DisplayName("getSentence - 성공적인 조회")
    void getSentence_Success() {
        // given: 유효한 tsSeq를 설정하고 해당하는 Mock TtsSentence 객체 생성
        Long tsSeq = 1L;
        String ttsText = "Test Sentence";

        TtsSentence mockSentence =
                TtsSentence.builder()
                        .tsSeq(tsSeq) // tsSeq 설정
                        .text(ttsText) // 텍스트 설정
                        .build();

        // findById 메서드가 Optional로 Mock TtsSentence를 반환하도록 설정
        when(ttsSentenceRepository.findById(tsSeq)).thenReturn(Optional.of(mockSentence));

        // when: getSentence 메서드를 호출
        TtsSentenceDto result = ttsSentenceService.getSentence(1L, tsSeq);

        // then: 결과값 검증
        assertNotNull(result); // 반환값이 null이 아님
        assertEquals(tsSeq, result.getSentence().getTsSeq()); // 반환된 DTO의 tsSeq가 요청값과 동일
        assertEquals(ttsText, result.getSentence().getText()); // 반환된 텍스트가 예상된 값과 동일
    }

    // 2. TtsSentence를 찾을 수 없음
    @Test
    @DisplayName("getSentence - TtsSentence를 찾을 수 없음")
    void getSentence_NotFound() {
        // given: tsSeq를 설정하고 해당하는 엔티티가 존재하지 않도록 설정
        Long tsSeq = 999L;
        // findById 메서드가 Optional.empty를 반환하도록 설정
        when(ttsSentenceRepository.findById(tsSeq)).thenReturn(Optional.empty());

        // when, then: getSentence 호출 시 EntityNotFoundException 발생 여부 검증
        EntityNotFoundException exception =
                assertThrows(
                        EntityNotFoundException.class, () -> ttsSentenceService.getSentence(1L, tsSeq));

        // 예외 메시지 검증
        assertEquals("TtsSentence not found with id: " + tsSeq, exception.getMessage());
    }

    // 3. null tsSeq
    @Test
    @DisplayName("getSentence - tsSeq가 null일 때")
    void getSentence_NullTsSeq() {
        // given: tsSeq가 null로 설정됨
        Long tsSeq = null;

        // when, then: getSentence 호출 시 IllegalArgumentException 발생 여부 검증
        assertThrows(
                ConstraintViolationException.class, () -> ttsSentenceService.getSentence(1L, tsSeq));
    }

    // 4. 유효하지 않은 tsSeq
    @Test
    @DisplayName("getSentence - 유효하지 않은 tsSeq")
    void getSentence_InvalidTsSeq() {
        // given: 유효하지 않은 tsSeq 설정
        Long tsSeq = -1L;

        // when, then: getSentence 호출 시 EntityNotFoundException 발생 여부 검증
        EntityNotFoundException exception =
                assertThrows(
                        EntityNotFoundException.class, () -> ttsSentenceService.getSentence(1L, tsSeq));

        // 예외 메시지 검증
        assertEquals("TtsSentence not found with id: " + tsSeq, exception.getMessage());
    }
}
