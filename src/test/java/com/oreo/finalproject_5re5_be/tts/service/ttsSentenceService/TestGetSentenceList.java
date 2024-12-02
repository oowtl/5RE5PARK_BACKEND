package com.oreo.finalproject_5re5_be.tts.service.ttsSentenceService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.oreo.finalproject_5re5_be.global.exception.EntityNotFoundException;
import com.oreo.finalproject_5re5_be.project.entity.Project;
import com.oreo.finalproject_5re5_be.project.repository.ProjectRepository;
import com.oreo.finalproject_5re5_be.tts.dto.response.TtsSentenceListDto;
import com.oreo.finalproject_5re5_be.tts.entity.TtsSentence;
import com.oreo.finalproject_5re5_be.tts.repository.TtsSentenceRepository;
import com.oreo.finalproject_5re5_be.tts.service.TtsSentenceService;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
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
class TestGetSentenceList {

    @Autowired
    private TtsSentenceService ttsSentenceService;

    @MockBean
    private TtsSentenceRepository ttsSentenceRepository;

    @MockBean
    private ProjectRepository projectRepository;

    /*
    테스트 시나리오: getSentenceList 메서드

    1. **성공적인 조회**
    - 조건:
    - 유효한 `projectSeq`가 제공됨.
    - 제공된 `projectSeq`에 해당하는 `Project` 엔티티가 데이터베이스에 존재.
    - 해당 `Project`에 연관된 `TtsSentence` 리스트가 존재.
    - 기대 결과:
    - `TtsSentenceListDto` 객체 반환.
    - 반환된 DTO는 `TtsSentence` 리스트를 포함.

    2. **프로젝트를 찾을 수 없음**
    - 조건:
    - 유효한 `projectSeq`가 제공되었으나, 데이터베이스에 해당 `Project`가 존재하지 않음.
    - 기대 결과:
    - `EntityNotFoundException` 예외 발생.
    - 예외 메시지에 "Project not found with id: <projectSeq>" 포함.

    3. **TtsSentence 리스트가 없음**
    - 조건:
    - 유효한 `projectSeq`가 제공되었고, 데이터베이스에 해당 `Project`가 존재.
    - 해당 `Project`에 연관된 `TtsSentence`가 하나도 없음.
    - 기대 결과:
    - 비어 있는 `TtsSentenceListDto` 객체 반환.

    4. **`projectSeq`가 null**
    - 조건:
    - `projectSeq`가 null로 제공됨.
    - 기대 결과:
    - `IllegalArgumentException` 예외 발생.
    - 예외 메시지에 "projectSeq must not be null" 포함.
    */

    // 1. 성공적인 조회
    @Test
    @DisplayName("getSentenceList - 성공적인 조회")
    void getSentenceList_Success() {
        // given
        int repeatCount = 5;
        Long projectSeq = 1L;
        Project mockProject = Project.builder().proSeq(projectSeq).build();

        // TtsSentence 리스트 생성
        List<TtsSentence> ttsSentenceList = IntStream.range(0, repeatCount)
            .mapToObj(i -> TtsSentence.builder()
                .tsSeq((long) i)
                .text("Sentence " + i).project(mockProject)
                .build())
            .toList();

        when(projectRepository.findById(projectSeq)).thenReturn(Optional.of(mockProject));
        when(ttsSentenceRepository.findAllByProjectOrderBySortOrder(mockProject)).thenReturn(ttsSentenceList);

        // when
        TtsSentenceListDto result = ttsSentenceService.getSentenceList(projectSeq);

        // then
        assertNotNull(result); // 결과가 null이 아님
        assertEquals(repeatCount, result.getSentenceList().size()); // 반환된 리스트의 크기 검증
        assertEquals("Sentence 1",
            result.getSentenceList().get(1).getSentence().getText()); // 첫 번째 문장의 텍스트 검증
        assertEquals("Sentence 2",
            result.getSentenceList().get(2).getSentence().getText()); // 두 번째 문장의 텍스트 검증
    }

    // 2. 프로젝트를 찾을 수 없음
    @Test
    @DisplayName("getSentenceList - 프로젝트를 찾을 수 없음")
    void getSentenceList_ProjectNotFound() {
        // given
        Long projectSeq = 999L;
        when(projectRepository.findById(projectSeq)).thenReturn(Optional.empty());

        // when, then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
            () -> ttsSentenceService.getSentenceList(projectSeq));

        assertEquals("Project not found with id: " + projectSeq,
            exception.getMessage()); // 예외 메시지 검증
    }

    // 3. TtsSentence 리스트가 없음
    @Test
    @DisplayName("getSentenceList - TtsSentence 리스트가 없음")
    void getSentenceList_EmptyList() {
        // given
        Long projectSeq = 1L;
        Project mockProject = Project.builder().proSeq(projectSeq).build();

        when(projectRepository.findById(projectSeq)).thenReturn(Optional.of(mockProject));
        when(ttsSentenceRepository.findAllByProjectOrderBySortOrder(mockProject)).thenReturn(List.of()); // 빈 리스트 반환

        // when
        TtsSentenceListDto result = ttsSentenceService.getSentenceList(projectSeq);

        // then
        assertNotNull(result); // 결과가 null이 아님
        assertTrue(result.getSentenceList().isEmpty()); // 반환된 리스트가 비어 있음
    }

    // 4. projectSeq가 null
    @Test
    @DisplayName("getSentenceList - projectSeq가 null")
    void getSentenceList_NullProjectSeq() {
        // given
        Long projectSeq = null;

        // when, then
        assertThrows(ConstraintViolationException.class,
            () -> ttsSentenceService.getSentenceList(projectSeq));

    }

}
