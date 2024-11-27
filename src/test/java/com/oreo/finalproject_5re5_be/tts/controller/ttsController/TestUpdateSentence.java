package com.oreo.finalproject_5re5_be.tts.controller.ttsController;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oreo.finalproject_5re5_be.global.exception.EntityNotFoundException;
import com.oreo.finalproject_5re5_be.global.exception.ErrorCode;
import com.oreo.finalproject_5re5_be.project.entity.Project;
import com.oreo.finalproject_5re5_be.project.service.ProjectService;
import com.oreo.finalproject_5re5_be.tts.controller.TtsController;
import com.oreo.finalproject_5re5_be.tts.dto.request.TtsAttributeInfo;
import com.oreo.finalproject_5re5_be.tts.dto.request.TtsSentenceRequest;
import com.oreo.finalproject_5re5_be.tts.dto.response.TtsSentenceDto;
import com.oreo.finalproject_5re5_be.tts.entity.TtsSentence;
import com.oreo.finalproject_5re5_be.tts.entity.Voice;
import com.oreo.finalproject_5re5_be.tts.service.TtsMakeService;
import com.oreo.finalproject_5re5_be.tts.service.TtsSentenceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TtsController.class)
class TestUpdateSentence {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TtsSentenceService ttsSentenceService;

    @MockBean
    private TtsMakeService ttsMakeService;

    @MockBean
    private ProjectService projectService;

    @Autowired
    private ObjectMapper objectMapper;


    /*
    테스트 시나리오: TTS 문장 수정 요청 (PUT /sentence/{tsSeq})

    1. 성공 케이스
    - 조건:
    - projectSeq와 tsSeq가 유효하고 존재.
    - updateRequest의 필드들이 모두 유효한 값.
    - 결과:
    - 상태 코드 200 OK 반환.
    - 수정된 TtsSentenceDto 응답.

    2. 유효성 검증 실패
    2.1. PathVariable 유효성 검증 실패
    - projectSeq가 null 또는 1 미만.
    - tsSeq가 null 또는 1 미만.
    - 결과: 400 Bad Request 반환, 에러 메시지 포함.

    2.2. RequestBody 유효성 검증 실패
    - updateRequest가 null.
    - updateRequest의 필드 중 하나라도 유효성 조건(@NotNull 등)을 만족하지 않음.
    - 결과: 400 Bad Request 반환, 에러 메시지 포함.

    3. 존재하지 않는 리소스
    3.1. 존재하지 않는 projectSeq
    - projectSeq가 DB에 존재하지 않음.
    - 결과: 404 Not Found 반환, "Project not found with id: <projectSeq>" 에러 메시지.

    3.2. 존재하지 않는 tsSeq
    - tsSeq가 DB에 존재하지 않음.
    - 결과: 404 Not Found 반환, "TtsSentence not found with id: <tsSeq>" 에러 메시지.

    3.3. 존재하지 않는 voiceSeq
    - updateRequest.getVoiceSeq()에 해당하는 Voice가 존재하지 않음.
    - 결과: 404 Not Found 반환, "Voice not found with id: <voiceSeq>" 에러 메시지.

    3.4. 존재하지 않는 styleSeq
    - updateRequest.getStyleSeq()에 해당하는 Style이 존재하지 않음.
    - 결과: 404 Not Found 반환, "Style not found with id: <styleSeq>" 에러 메시지.

    4. 예외 처리
    - updateRequest가 null인 경우 IllegalArgumentException 발생.
    - DB 저장(save) 과정에서 예외 발생 (예: 데이터 무결성 위반).
    - 결과: 500 Internal Server Error 반환.

    5. 연관 데이터 처리
    - TtsSentence에 연관된 ttsAudioFile이 존재할 경우, null로 설정 후 성공적으로 저장.
    - 결과: 상태 코드 200 OK 반환, 수정된 데이터 확인.
    */

    // 1. 성공적인 문장 수정 요청 테스트
    @WithMockUser
    @Test
    @DisplayName("성공적인 문장 수정 요청 테스트")
    void updateSentence_successfulUpdate() throws Exception {
        // given
        Long projectSeq = 1L;
        Long tsSeq = 1L;
        Long voiceSeq = 1L;
        String updatedText = "Updated text";

        // 1. 요청 객체 생성
        TtsAttributeInfo attributeInfo = createAttributeInfo();
        TtsSentenceRequest requestBody = createSentenceRequest(attributeInfo);

        // 2. 응답 객체 생성
        Voice voice = createVoice(voiceSeq);
        Project project = createProject(projectSeq);
        TtsSentence sentence = createTtsSentence(project, tsSeq, voice, updatedText);
        TtsSentenceDto response = TtsSentenceDto.of(sentence);

        // mock 서비스 동작 설정
        when(ttsSentenceService.updateSentence(eq(projectSeq), eq(tsSeq),
            any(TtsSentenceRequest.class)))
            .thenReturn(response);

        // projectService.projectCheck 메서드가 호출되면 true 반환
        when(projectService.projectCheck(1L, 1L)).thenReturn(true);

        // mockHttpSession 생성
        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("memberSeq", 1L);

        // when
        mockMvc.perform(put("/api/project/{projectSeq}/tts/sentence/{tsSeq}", projectSeq, tsSeq)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
                .session(mockHttpSession)
                .with(csrf()))
            // then
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status", is(HttpStatus.OK.value())))
            .andExpect(jsonPath("$.response.sentence.text", is(sentence.getText())))
            .andExpect(jsonPath("$.response.sentence.order", is(sentence.getSortOrder())))
            .andDo(print());

    }

    // 2.1. PathVariable 유효성 검증 실패 - projectSeq 또는 tsSeq가 1 미만
    @WithMockUser
    @Test
    @DisplayName("유효성 검증 실패 - PathVariable이 1 미만")
    void updateSentence_validationErrorForInvalidPathVariable() throws Exception {
        // given
        Long invalidProjectSeq = 0L;
        Long invalidTsSeq = 0L;
        TtsAttributeInfo attributeInfo = createAttributeInfo();
        TtsSentenceRequest requestBody = createSentenceRequest(attributeInfo);

        // projectService.projectCheck 메서드가 호출되면 true 반환
        when(projectService.projectCheck(1L, 1L)).thenReturn(true);

        // mockHttpSession 생성
        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("memberSeq", 1L);

        // when, then
        mockMvc.perform(
                put("/api/project/{projectSeq}/tts/sentence/{tsSeq}", invalidProjectSeq, invalidTsSeq)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestBody))
                    .session(mockHttpSession)
                    .with(csrf()))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())));
    }

    // 2.2. RequestBody 유효성 검증 실패 - 필드 누락
    @WithMockUser
    @Test
    @DisplayName("유효성 검증 실패 - RequestBody 필드 누락")
    void updateSentence_validationErrorForInvalidRequestBody() throws Exception {
        // given
        Long projectSeq = 1L;
        Long tsSeq = 1L;
        TtsSentenceRequest invalidRequestBody = TtsSentenceRequest.builder()
            .build(); // 필드가 없는 요청 객체

        // when, then
        mockMvc.perform(put("/api/project/{projectSeq}/tts/sentence/{tsSeq}", projectSeq, tsSeq)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequestBody))
                .with(csrf()))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())));
    }

    // 3.1. 존재하지 않는 projectSeq
    @WithMockUser
    @Test
    @DisplayName("존재하지 않는 projectSeq로 인한 예외")
    void updateSentence_notFoundProjectSeq() throws Exception {
        // given
        Long nonExistentProjectSeq = 99999L;
        Long tsSeq = 1L;
        TtsAttributeInfo attributeInfo = createAttributeInfo();
        TtsSentenceRequest requestBody = createSentenceRequest(attributeInfo);

        // mock 서비스 동작 설정
        when(ttsSentenceService.updateSentence(eq(nonExistentProjectSeq), eq(tsSeq),
            any(TtsSentenceRequest.class)))
            .thenThrow(
                new EntityNotFoundException("Project not found with id: " + nonExistentProjectSeq));

        // projectService.projectCheck 메서드가 호출되면 true 반환
        when(projectService.projectCheck(1L, 1L)).thenReturn(true);

        // mockHttpSession 생성
        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("memberSeq", 1L);

        // when, then
        mockMvc.perform(
                put("/api/project/{projectSeq}/tts/sentence/{tsSeq}", nonExistentProjectSeq, tsSeq)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestBody))
                    .session(mockHttpSession)
                    .with(csrf()))
            .andExpect(status().is(ErrorCode.ENTITY_NOT_FOUND.getStatus()))
            .andExpect(jsonPath("$.status", is(ErrorCode.ENTITY_NOT_FOUND.getStatus())))
            .andExpect(jsonPath("$.response.message",
                is("Project not found with id: " + nonExistentProjectSeq)));
    }

    // 4. 예외 처리 - RuntimeException 발생
    @WithMockUser
    @Test
    @DisplayName("예외 처리 - RuntimeException 발생")
    void updateSentence_internalServerError() throws Exception {
        // given
        Long projectSeq = 1L;
        Long tsSeq = 1L;

        TtsAttributeInfo attributeInfo = createAttributeInfo();
        TtsSentenceRequest requestBody = createSentenceRequest(attributeInfo);

        // mock 서비스 동작 설정
        when(ttsSentenceService.updateSentence(eq(projectSeq), eq(tsSeq),
            any(TtsSentenceRequest.class)))
            .thenThrow(new RuntimeException("Unexpected error"));

        // projectService.projectCheck 메서드가 호출되면 true 반환
        when(projectService.projectCheck(1L, 1L)).thenReturn(true);

        // mockHttpSession 생성
        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("memberSeq", 1L);

        // when, then
        mockMvc.perform(put("/api/project/{projectSeq}/tts/sentence/{tsSeq}", projectSeq, tsSeq)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
                .session(mockHttpSession)
                .with(csrf()))
            .andExpect(status().is(ErrorCode.INTERNAL_SERVER_ERROR.getStatus()))
            .andExpect(jsonPath("$.status", is(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())))
            .andExpect(
                jsonPath("$.response.message", is(ErrorCode.INTERNAL_SERVER_ERROR.getMessage())));
    }

    private static TtsAttributeInfo createAttributeInfo() {
        return TtsAttributeInfo.builder().volume(100) // 유효한 volume 설정
            .speed(1.0f) // 유효한 speed 설정
            .stPitch(0)  // 유효한 stPitch 설정
            .emotion("neutral") // 유효한 emotion 설정
            .emotionStrength(100) // 유효한 emotionStrength 설정
            .sampleRate(16000) // 유효한 sampleRate 설정
            .alpha(0) // 유효한 alpha 설정
            .endPitch(0.0f) // 유효한 endPitch 설정
            .audioFormat("wav") // 유효한 audioFormat 설정
            .build(); // 유효한 속성 정보 초기화
    }

    private static TtsSentenceRequest createSentenceRequest(TtsAttributeInfo attributeInfo) {
        return TtsSentenceRequest.builder().voiceSeq(1L) // 유효한 voiceSeq 설정
            .styleSeq(1L) // 유효한 styleSeq 설정
            .text("Valid text") // 유효한 텍스트 설정
            .order(1) // 표시 순서 설정
            .attribute(attributeInfo) // 유효한 속성 정보 설정
            .build(); // 유효한 요청 객체 초기화
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
