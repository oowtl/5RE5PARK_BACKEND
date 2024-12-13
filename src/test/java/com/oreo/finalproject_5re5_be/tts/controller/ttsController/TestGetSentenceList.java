package com.oreo.finalproject_5re5_be.tts.controller.ttsController;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.oreo.finalproject_5re5_be.global.exception.EntityNotFoundException;
import com.oreo.finalproject_5re5_be.global.exception.ErrorCode;
import com.oreo.finalproject_5re5_be.project.entity.Project;
import com.oreo.finalproject_5re5_be.project.service.ProjectService;
import com.oreo.finalproject_5re5_be.tts.controller.TtsController;
import com.oreo.finalproject_5re5_be.tts.dto.response.TtsSentenceListDto;
import com.oreo.finalproject_5re5_be.tts.entity.TtsSentence;
import com.oreo.finalproject_5re5_be.tts.entity.Voice;
import com.oreo.finalproject_5re5_be.tts.service.TtsMakeService;
import com.oreo.finalproject_5re5_be.tts.service.TtsSentenceService;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TtsController.class)
class TestGetSentenceList {

    @Autowired private MockMvc mockMvc;

    @MockBean private TtsSentenceService ttsSentenceService;

    @MockBean private TtsMakeService ttsMakeService;

    @MockBean private ProjectService projectService;

    /*
    테스트 시나리오: getSentenceList 컨트롤러 메서드

    1. **성공적인 조회**
    - 조건:
    - 유효한 `projectSeq`가 제공됨.
    - `projectSeq`에 해당하는 프로젝트가 존재.
    - 해당 프로젝트에 연관된 `TtsSentence` 엔티티들이 존재.
    - 기대 결과:
    - HTTP 상태 코드 200 반환.
    - 응답 본문에 `TtsSentenceListDto` 객체 포함.
    - `TtsSentenceListDto`의 `sentences` 필드가 `TtsSentenceDto` 리스트를 포함.

    2. **프로젝트를 찾을 수 없음**
    - 조건:
    - 유효한 `projectSeq`가 제공되었으나, 데이터베이스에 해당 프로젝트가 존재하지 않음.
    - 기대 결과:
    - HTTP 상태 코드 404 반환.
    - 응답 본문에 `"Project not found with id: <projectSeq>"` 포함.

    3. **TtsSentence 리스트가 없음**
    - 조건:
    - 유효한 `projectSeq`가 제공되고, 프로젝트가 존재.
    - 해당 프로젝트에 연관된 `TtsSentence` 엔티티가 없음.
    - 기대 결과:
    - HTTP 상태 코드 200 반환.
    - 응답 본문에 비어 있는 `TtsSentenceListDto` 객체 포함.

    4. **유효성 검증 실패 - 잘못된 `projectSeq`**
    - 조건:
    - `projectSeq`가 1보다 작은 값으로 제공됨 (예: `0` 또는 `-1`).
    - 기대 결과:
    - HTTP 상태 코드 400 반환.
    - 응답 본문에 `"projectSeq is invalid"` 메시지 포함.

    5. **내부 서버 에러**
    - 조건:
    - 서비스 계층에서 예기치 못한 예외 발생 (예: 데이터베이스 연결 문제 등).
    - 기대 결과:
    - HTTP 상태 코드 500 반환.
    - 응답 본문에 `"Internal Server Error"` 포함.
    */

    // 1. 성공적인 조회
    @Test
    @WithMockUser
    @DisplayName("getSentenceList - 성공적인 조회")
    void getSentenceList_Success() throws Exception {
        // given
        int repeatCount = 10;
        Long projectSeq = 1L;
        Long voiceSeq = 1L;

        Project project = createProject(projectSeq);
        Voice voice = createVoice(voiceSeq);

        List<TtsSentence> ttsSentenceList =
                IntStream.range(0, repeatCount)
                        .mapToObj(i -> createTtsSentence(project, (long) i, voice, "Test sentence " + i, i))
                        .toList();

        TtsSentenceListDto response = TtsSentenceListDto.of(ttsSentenceList);

        when(ttsSentenceService.getSentenceList(projectSeq)).thenReturn(response);

        // when, then
        mockMvc
                .perform(
                        get("/api/project/{projectSeq}/tts", projectSeq)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(HttpStatus.OK.value())))
                .andExpect(jsonPath("$.response.sentenceList[0].sentence.tsSeq", is(0)))
                .andExpect(jsonPath("$.response.sentenceList[1].sentence.text", is("Test sentence 1")));
    }

    // 2. 프로젝트를 찾을 수 없음
    @Test
    @WithMockUser
    @DisplayName("getSentenceList - 프로젝트를 찾을 수 없음")
    void getSentenceList_ProjectNotFound() throws Exception {
        // given
        Long projectSeq = 999L;

        when(ttsSentenceService.getSentenceList(projectSeq)).thenThrow(new EntityNotFoundException());

        // when, then
        mockMvc
                .perform(
                        get("/api/project/{projectSeq}/tts", projectSeq)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(ErrorCode.ENTITY_NOT_FOUND.getStatus()))
                .andExpect(jsonPath("$.status", is(ErrorCode.ENTITY_NOT_FOUND.getStatus())))
                .andExpect(jsonPath("$.response.message", is(ErrorCode.ENTITY_NOT_FOUND.getMessage())));
    }

    // 3. TtsSentence 리스트가 없음
    @Test
    @WithMockUser
    @DisplayName("getSentenceList - TtsSentence 리스트가 없음")
    void getSentenceList_EmptyList() throws Exception {
        // given
        Long projectSeq = 1L;

        TtsSentenceListDto response = TtsSentenceListDto.of(Collections.emptyList());

        when(ttsSentenceService.getSentenceList(projectSeq)).thenReturn(response);

        // when, then
        mockMvc
                .perform(
                        get("/api/project/{projectSeq}/tts", projectSeq)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(HttpStatus.OK.value())))
                .andExpect(jsonPath("$.response.sentenceList").isEmpty());
    }

    // 4. 유효성 검증 실패 - 잘못된 projectSeq
    @Test
    @WithMockUser
    @DisplayName("getSentenceList - 유효성 검증 실패")
    void getSentenceList_InvalidProjectSeq() throws Exception {
        // given
        Long invalidProjectSeq = 0L;

        // when, then
        mockMvc
                .perform(
                        get("/api/project/{projectSeq}/tts", invalidProjectSeq)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(ErrorCode.INVALID_INPUT_VALUE.getStatus()))
                .andExpect(jsonPath("$.response.message", is(ErrorCode.INVALID_INPUT_VALUE.getMessage())));
    }

    // 5. 내부 서버 에러
    @Test
    @WithMockUser
    @DisplayName("getSentenceList - 내부 서버 에러")
    void getSentenceList_InternalServerError() throws Exception {
        // given
        Long projectSeq = 1L;

        when(ttsSentenceService.getSentenceList(projectSeq))
                .thenThrow(new RuntimeException("Unexpected error"));

        // when, then
        mockMvc
                .perform(
                        get("/api/project/{projectSeq}/tts", projectSeq)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(ErrorCode.INTERNAL_SERVER_ERROR.getStatus()))
                .andExpect(
                        jsonPath("$.response.message", is(ErrorCode.INTERNAL_SERVER_ERROR.getMessage())));
    }

    private static Voice createVoice(Long voiceSeq) {
        return Voice.builder()
                .voiceSeq(voiceSeq) // 목소리 ID 설정
                .name("Valid voice") // 목소리 이름 설정
                .build();
    }

    private static Project createProject(Long projectSeq) {
        return Project.builder()
                .proSeq(projectSeq) // 프로젝트 ID 설정
                .proName("Valid project") // 프로젝트 이름 설정
                .build();
    }

    private static TtsSentence createTtsSentence(
            Project project, Long tsSeq, Voice voice, String text, int order) {
        return TtsSentence.builder()
                .tsSeq(tsSeq)
                .text(text)
                .sortOrder(order)
                .volume(50)
                .speed(1.0f)
                .voice(voice)
                .project(project)
                .build();
    }
}
