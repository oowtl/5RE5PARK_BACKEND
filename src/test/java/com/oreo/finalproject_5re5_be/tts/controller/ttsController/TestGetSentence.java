package com.oreo.finalproject_5re5_be.tts.controller.ttsController;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.oreo.finalproject_5re5_be.global.exception.EntityNotFoundException;
import com.oreo.finalproject_5re5_be.global.exception.ErrorCode;
import com.oreo.finalproject_5re5_be.project.entity.Project;
import com.oreo.finalproject_5re5_be.project.service.ProjectService;
import com.oreo.finalproject_5re5_be.tts.controller.TtsController;
import com.oreo.finalproject_5re5_be.tts.dto.response.TtsSentenceDto;
import com.oreo.finalproject_5re5_be.tts.entity.TtsSentence;
import com.oreo.finalproject_5re5_be.tts.entity.Voice;
import com.oreo.finalproject_5re5_be.tts.service.TtsMakeService;
import com.oreo.finalproject_5re5_be.tts.service.TtsSentenceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TtsController.class)
class TestGetSentence {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TtsSentenceService ttsSentenceService;

    @MockBean
    private TtsMakeService ttsMakeService;

    @MockBean
    private ProjectService projectService;

    /*
    테스트 시나리오: 컨트롤러 메서드 `getSentence`

    1. **성공적인 조회**
    - 조건:
    - 유효한 `projectSeq`와 `tsSeq`가 제공됨.
    - 제공된 `tsSeq`에 해당하는 `TtsSentence`가 존재하며, `TtsSentenceDto`로 변환이 성공.
    - 기대 결과:
    - HTTP 상태 코드 200 반환.
    - 응답에 `TtsSentenceDto` 객체가 포함됨.

    2. **유효성 검증 실패**
    2.1. **`projectSeq` 유효성 검증 실패**
    - 조건:
    - `projectSeq`가 1보다 작은 값이 제공됨.
    - 기대 결과:
    - HTTP 상태 코드 400 반환.
    - 에러 메시지에 "projectSeq is invalid" 포함.

    2.2. **`tsSeq` 유효성 검증 실패**
    - 조건:
    - `tsSeq`가 1보다 작은 값이 제공됨.
    - 기대 결과:
    - HTTP 상태 코드 400 반환.
    - 에러 메시지에 "tsSeq is invalid" 포함.

    3. **`TtsSentence`를 찾을 수 없음**
    - 조건:
    - 유효한 `projectSeq`와 `tsSeq`가 제공되었으나, `tsSeq`에 해당하는 `TtsSentence`가 데이터베이스에 존재하지 않음.
    - 기대 결과:
    - HTTP 상태 코드 404 반환.
    - 에러 메시지에 "TtsSentence not found with id: <tsSeq>" 포함.

    4. **내부 서버 에러**
    - 조건:
    - `TtsSentenceService.getSentence` 호출 중 예외 발생 (예: 데이터베이스 연결 문제 등).
    - 기대 결과:
    - HTTP 상태 코드 500 반환.
    - 에러 메시지에 "Internal Server Error" 포함.
    */
    // 1. 성공적인 조회
    @Test
    @DisplayName("getSentence - 성공적인 조회")
    @WithMockUser
    void getSentence_Success() throws Exception {
        // given: 유효한 projectSeq와 tsSeq 설정
        Long projectSeq = 1L;
        Long tsSeq = 1L;
        Long voiceSeq = 1L;
        String updatedText = "Updated Text";

        Project project = createProject(projectSeq);
        Voice voice = createVoice(voiceSeq);

        // TtsSentenceDto 객체 생성
        TtsSentence sentence = createTtsSentence(project, tsSeq, voice, updatedText);
        TtsSentenceDto response = TtsSentenceDto.of(sentence);

        Mockito.when(ttsSentenceService.getSentence(projectSeq, tsSeq)).thenReturn(response);

        // when: 컨트롤러 호출
        mockMvc.perform(get("/api/project/{projectSeq}/tts/sentence/{tsSeq}", projectSeq, tsSeq)
                .contentType(MediaType.APPLICATION_JSON))
            // then: 응답 검증
            .andExpect(status().isOk()) // HTTP 상태 200 확인
            .andExpect(jsonPath("$.status", is(HttpStatus.OK.value()))) // 응답 상태 코드 검증
            .andExpect(
                jsonPath("$.response.sentence.tsSeq", is(tsSeq.intValue()))) // 반환된 데이터의 tsSeq 검증
            .andExpect(jsonPath("$.response.sentence.text", is(updatedText))); // 반환된 데이터의 텍스트 검증
    }

    // 2.1 projectSeq 유효성 검증 실패
    @Test
    @DisplayName("getSentence - projectSeq 유효성 검증 실패")
    @WithMockUser
    void getSentence_InvalidProjectSeq() throws Exception {
        // given: 유효하지 않은 projectSeq 설정
        Long projectSeq = 0L;
        Long tsSeq = 1L;

        // when: 컨트롤러 호출
        mockMvc.perform(get("/api/project/{projectSeq}/tts/sentence/{tsSeq}", projectSeq, tsSeq)
                .contentType(MediaType.APPLICATION_JSON))
            // then: 응답 검증
            .andExpect(status().is(ErrorCode.INVALID_INPUT_VALUE.getStatus())) // HTTP 상태 400 확인
            .andExpect(
                jsonPath("$.status", is(ErrorCode.INVALID_INPUT_VALUE.getStatus()))) // 응답 상태 코드 검증
            .andExpect(jsonPath("$.response.message",
                is(ErrorCode.INVALID_INPUT_VALUE.getMessage()))); // 에러 메시지 검증
    }

    // 2.2 tsSeq 유효성 검증 실패
    @Test
    @DisplayName("getSentence - tsSeq 유효성 검증 실패")
    @WithMockUser
    void getSentence_InvalidTsSeq() throws Exception {
        // given: 유효하지 않은 tsSeq 설정
        Long projectSeq = 1L;
        Long tsSeq = 0L;

        // when: 컨트롤러 호출
        mockMvc.perform(get("/api/project/{projectSeq}/tts/sentence/{tsSeq}", projectSeq, tsSeq)
                .contentType(MediaType.APPLICATION_JSON))
            // then: 응답 검증
            .andExpect(status().is(ErrorCode.INVALID_INPUT_VALUE.getStatus())) // HTTP 상태 400 확인
            .andExpect(
                jsonPath("$.status", is(ErrorCode.INVALID_INPUT_VALUE.getStatus()))) // 응답 상태 코드 검증
            .andExpect(jsonPath("$.response.message",
                is(ErrorCode.INVALID_INPUT_VALUE.getMessage()))); // 에러 메시지 검증
    }

    // 3. TtsSentence를 찾을 수 없음
    @Test
    @DisplayName("getSentence - TtsSentence를 찾을 수 없음")
    @WithMockUser
    void getSentence_NotFound() throws Exception {
        // given: 유효한 projectSeq와 tsSeq 설정, 데이터베이스에 존재하지 않는 tsSeq
        Long projectSeq = 1L;
        Long tsSeq = 999L;

        Mockito.when(ttsSentenceService.getSentence(projectSeq, tsSeq))
            .thenThrow(new EntityNotFoundException("TtsSentence not found with id: " + tsSeq));

        // when: 컨트롤러 호출
        mockMvc.perform(get("/api/project/{projectSeq}/tts/sentence/{tsSeq}", projectSeq, tsSeq)
                .contentType(MediaType.APPLICATION_JSON))
            // then: 응답 검증
            .andExpect(status().is(ErrorCode.ENTITY_NOT_FOUND.getStatus())) // HTTP 상태 404 확인
            .andExpect(
                jsonPath("$.status", is(ErrorCode.ENTITY_NOT_FOUND.getStatus()))) // 응답 상태 코드 검증
            .andExpect(jsonPath("$.response.message",
                is("TtsSentence not found with id: " + tsSeq))); // 에러 메시지 검증
    }

    // 4. 내부 서버 에러
    @Test
    @DisplayName("getSentence - 내부 서버 에러")
    @WithMockUser
    void getSentence_InternalServerError() throws Exception {
        // given: 유효한 projectSeq와 tsSeq 설정, 내부 서버 에러 시뮬레이션
        Long projectSeq = 1L;
        Long tsSeq = 1L;

        Mockito.when(ttsSentenceService.getSentence(projectSeq, tsSeq))
            .thenThrow(new RuntimeException("Unexpected server error"));

        // when: 컨트롤러 호출
        mockMvc.perform(get("/api/project/{projectSeq}/tts/sentence/{tsSeq}", projectSeq, tsSeq)
                .contentType(MediaType.APPLICATION_JSON))
            // then: 응답 검증
            .andExpect(status().is(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())) // HTTP 상태 500 확인
            .andExpect(jsonPath("$.status",
                is(ErrorCode.INTERNAL_SERVER_ERROR.getStatus()))) // 응답 상태 코드 검증
            .andExpect(jsonPath("$.response.message",
                is(ErrorCode.INTERNAL_SERVER_ERROR.getMessage()))); // 에러 메시지 검증
    }

    private static Voice createVoice(Long voiceSeq) {
        return Voice.builder().voiceSeq(voiceSeq) // 목소리 ID 설정
            .name("Valid voice") // 목소리 이름 설정
            .build();
    }

    private static Project createProject(Long projectSeq) {
        return Project.builder().proSeq(projectSeq) // 프로젝트 ID 설정
            .proName("Valid project") // 프로젝트 이름 설정
            .build();
    }

    private static TtsSentence createTtsSentence(Project project, Long tsSeq, Voice voice,
        String text) {
        return TtsSentence.builder().tsSeq(tsSeq).text(text).sortOrder(1).volume(50).speed(1.0f)
            .voice(voice).project(project).build();
    }

}
