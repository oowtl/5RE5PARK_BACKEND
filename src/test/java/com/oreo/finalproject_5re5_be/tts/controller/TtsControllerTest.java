package com.oreo.finalproject_5re5_be.tts.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oreo.finalproject_5re5_be.global.exception.EntityNotFoundException;
import com.oreo.finalproject_5re5_be.global.exception.ErrorCode;
import com.oreo.finalproject_5re5_be.project.entity.Project;
import com.oreo.finalproject_5re5_be.tts.dto.request.TtsAttributeInfo;
import com.oreo.finalproject_5re5_be.tts.dto.request.TtsSentenceRequest;
import com.oreo.finalproject_5re5_be.tts.dto.response.TtsSentenceDto;
import com.oreo.finalproject_5re5_be.tts.dto.response.TtsSentenceListDto;
import com.oreo.finalproject_5re5_be.tts.entity.Style;
import com.oreo.finalproject_5re5_be.tts.entity.TtsAudioFile;
import com.oreo.finalproject_5re5_be.tts.entity.TtsSentence;
import com.oreo.finalproject_5re5_be.tts.entity.Voice;
import com.oreo.finalproject_5re5_be.tts.service.TtsMakeService;
import com.oreo.finalproject_5re5_be.tts.service.TtsSentenceService;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
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
class TtsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TtsSentenceService ttsSentenceService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TtsMakeService ttsMakeService;

    /*
    TtsController 테스트 클래스
    - TtsController의 addSentence 메서드에 대한 테스트 케이스를 작성
    - 다양한 유효성 검증과 에러 상황을 포함하여 테스트 진행

    Test Scenarios

    1. 성공적인 문장 생성 요청 테스트
    - 유효한 projectSeq와 TtsSentenceRequest 입력 시
    - HTTP 상태 200과 올바른 JSON 응답을 반환하는지 확인

    2. 텍스트 필드 누락으로 인한 유효성 검증 에러 테스트
    - text 필드가 누락된 TtsSentenceRequest 입력 시
    - HTTP 상태 400과 텍스트 필드 관련 에러 메시지 반환을 확인

    3. voiceSeq 필드 누락으로 인한 유효성 검증 에러 테스트
    - voiceSeq 필드가 누락된 TtsSentenceRequest 입력 시
    - HTTP 상태 400과 voiceSeq 관련 에러 메시지 반환을 확인

    4. 잘못된 projectSeq로 인한 유효성 검증 에러 테스트
    - 유효하지 않은 projectSeq 입력 시
    - HTTP 상태 400과 projectSeq 관련 에러 메시지 반환을 확인

    5. voiceSeq로 Voice 엔티티를 찾을 수 없는 경우
    - 유효한 projectSeq와 없는 voiceSeq 입력 시
    - HTTP 상태 400과 voiceSeq 관련 에러 메시지 반환을 확인

    6. styleSeq로 Style 엔티티를 찾을 수 없는 경우
    - 유효한 projectSeq와 없는 styleSeq 입력 시
    - HTTP 상태 400과 styleSeq 관련 에러 메시지 반환을 확인

    7. 잘못된 속성 값으로 인한 유효성 검증 에러 테스트
    - 유효 범위를 벗어난 속성 값 (예: volume)을 가진 TtsSentenceRequest 입력 시
    - HTTP 상태 400과 속성 관련 에러 메시지 반환을 확인

    8. 내부 서버 에러 테스트
    - 유효한 입력값이지만 메서드 내부에서 예외 발생 시
    - HTTP 상태 500과 예상치 못한 에러 메시지 반환을 확인
 */

    // 1. 성공적인 문장 생성 요청 테스트
    @WithMockUser
    @Test
    @DisplayName("성공적인 문장 생성 요청 테스트")
    void registerSentence_successfulCreation() throws Exception {
        // given - 유효한 프로젝트 ID와 문장 생성 요청 객체 초기화
        Long projectSeq = 1L;
        Long voiceSeq = 1L;

        // 1. 요청 객체 생성
        // 속성 정보 객체 생성
        TtsAttributeInfo attributeInfo = createAttributeInfo();
        // 문장 생성 요청 객체 생성
        TtsSentenceRequest requestBody = createSentenceRequest(attributeInfo);

        // 2. 응답 객체 생성
        // voice 객체 생성
        Voice voice = createVoice(voiceSeq);
        // project 객체 생성
        Project project = createProject(projectSeq);
        // TtsSentence 객체 생성
        TtsSentence ttsSentence = createTtsSentence(voice, project);
        // TtsSentenceDto 객체 생성
        TtsSentenceDto response = TtsSentenceDto.of(ttsSentence);

        // 3. TtsSentenceService의 addSentence 메서드에 대한 모의 동작 설정
        when(ttsSentenceService.addSentence(eq(projectSeq),
            any(TtsSentenceRequest.class))).thenReturn(response); // 응답 객체 반환

        // when
        // 4. 컨트롤러의 addSentence 메서드에 요청을 전송하여 테스트 수행
        mockMvc.perform(post("/api/project/{projectSeq}/tts/sentence", projectSeq) // URL 설정
                .contentType(MediaType.APPLICATION_JSON)                // 요청 본문 타입 설정
                .content(
                    objectMapper.writeValueAsString(requestBody))      // 요청 본문으로 JSON 데이터를 직렬화하여 전송
                .with(csrf()))                                          // CSRF 토큰 추가
            // Then - 예상 응답 상태와 JSON 구조 확인
            .andExpect(status().isCreated())                                   // HTTP 상태 201 확인
            .andExpect(jsonPath("$.status", is(HttpStatus.CREATED.value())))// 응답의 상태가 200인지 확인
            .andExpect(
                jsonPath("$.response.sentence").exists());         // JSON 응답에 sentence 필드가 존재하는지 확인
    }

    // 2. 텍스트 필드 누락으로 인한 유효성 검증 에러 테스트
    @Test
    @WithMockUser
    @DisplayName("유효성 검증 에러 - 텍스트 필드 누락")
    void registerSentence_validationErrorForMissingTextField() throws Exception {
        // given - 유효한 프로젝트 ID와 텍스트 필드가 누락된 요청 객체 초기화
        Long projectSeq = 1L;

        // 1. 요청 객체 생성
        // 속성 정보 객체 생성
        TtsAttributeInfo attributeInfo = createAttributeInfo();

        // 텍스트 필드가 없는 요청 객체 생성
        TtsSentenceRequest requestBody = TtsSentenceRequest.builder()
            .voiceSeq(1L) // 유효한 voiceSeq 설정
            .styleSeq(1L) // 유효한 styleSeq 설정
            .text(null)   // 텍스트 필드 누락
            .order(1)     // 표시 순서 설정
            .attribute(attributeInfo) // 빈 속성 정보 설정
            .build();

        // when
        // 2. 컨트롤러의 addSentence 메서드에 요청을 전송하여 테스트 수행
        mockMvc.perform(post("/api/project/{projectSeq}/tts/sentence", projectSeq) // URL 설정
                .contentType(MediaType.APPLICATION_JSON)              // 요청 본문 타입 설정
                .content(objectMapper.writeValueAsString(requestBody)) // 요청 본문으로 JSON 데이터를 직렬화하여 전송
                .with(csrf()))
            // Then - 예상 응답 상태와 오류 메시지 확인
            .andExpect(status().is(ErrorCode.INVALID_INPUT_VALUE.getStatus())).andExpect(
                jsonPath("$.response.message", is(ErrorCode.INVALID_INPUT_VALUE.getMessage())));
    }

    // 3. voiceSeq 필드 누락으로 인한 유효성 검증 에러 테스트
    @Test
    @DisplayName("유효성 검증 에러 - voiceSeq 필드 누락")
    @WithMockUser
    void registerSentence_validationErrorForMissingVoiceSeqField() throws Exception {
        // Given - 유효한 프로젝트 ID와 voiceSeq 필드가 누락된 요청 객체 초기화
        Long projectSeq = 1L;

        TtsAttributeInfo attributeInfo = createAttributeInfo();

        // 텍스트 필드가 없는 요청 객체 생성
        TtsSentenceRequest requestBody = TtsSentenceRequest.builder()
            .voiceSeq(null) // voiceSeq 필드 누락
            .styleSeq(1L) // 유효한 styleSeq 설정
            .text("sample text")   // 텍스트 필드 누락
            .order(1)     // 표시 순서 설정
            .attribute(attributeInfo) // 빈 속성 정보 설정
            .build();

        // When - 컨트롤러의 addSentence 메서드에 요청을 전송하여 테스트 수행
        mockMvc.perform(post("/api/project/{projectSeq}/tts/sentence", projectSeq) // URL 설정
                .contentType(MediaType.APPLICATION_JSON)              // 요청 본문 타입 설정
                .content(objectMapper.writeValueAsString(requestBody)) // 요청 본문으로 JSON 데이터를 직렬화하여 전송
                .with(csrf()))
            // Then - 예상 응답 상태와 오류 메시지 확인
            .andExpect(status().is(
                ErrorCode.INVALID_INPUT_VALUE.getStatus()))                            // HTTP 상태 400 확인
            .andExpect(
                jsonPath("$.response.message", is(ErrorCode.INVALID_INPUT_VALUE.getMessage())));
    }

    // 4. 잘못된 projectSeq로 인한 유효성 검증 에러 테스트
    @Test
    @DisplayName("유효성 검증 에러 - 잘못된 projectSeq")
    @WithMockUser
    void registerSentence_validationErrorForInvalidProjectSeq() throws Exception {
        // Given - 잘못된 projectSeq 설정
        Long invalidProjectSeq = -100L;

        // 1. 요청 객체 생성
        // 속성 정보 객체 생성
        TtsAttributeInfo attributeInfo = createAttributeInfo();
        // 문장 생성 요청 객체 생성
        TtsSentenceRequest requestBody = createSentenceRequest(attributeInfo);

        // When - 서비스에서 예외 발생을 설정
        when(ttsSentenceService.addSentence(eq(invalidProjectSeq),
            any(TtsSentenceRequest.class))).thenThrow(
            new IllegalArgumentException("projectSeq is invalid"));

        // Then - 요청 전송 및 응답 검증
        mockMvc.perform(
                post("/api/project/{projectSeq}/tts/sentence", invalidProjectSeq).contentType(
                        MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(requestBody))
                    .with(csrf()))
            .andExpect(status().is(
                ErrorCode.INVALID_INPUT_VALUE.getStatus()))                            // HTTP 상태 400 확인
            .andExpect(
                jsonPath("$.response.message", is(ErrorCode.INVALID_INPUT_VALUE.getMessage())));
    }

    // 5. voiceSeq 로 Voice 엔티티를 찾을 수 없는 경우
    @Test
    @DisplayName("유효성 검증 에러 - voiceSeq 로 Voice 엔티티를 찾을 수 없는 경우")
    @WithMockUser
    void registerSentence_notFoundVoiceEntity() throws Exception {
        // Given
        Long projectSeq = 1L;

        // 속성 정보 객체 생성
        TtsAttributeInfo attributeInfo = createAttributeInfo();

        // 문장 생성 요청 객체 생성
        TtsSentenceRequest requestBody = TtsSentenceRequest.builder().voiceSeq(-1L)
            .text("Valid text").attribute(attributeInfo).build();

        // When - 서비스에서 예외 발생을 설정
        when(ttsSentenceService.addSentence(eq(projectSeq),
            any(TtsSentenceRequest.class))).thenThrow(new EntityNotFoundException());

        // Then - 요청 전송 및 응답 검증
        mockMvc.perform(post("/api/project/{projectSeq}/tts/sentence", projectSeq).contentType(
                    MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(requestBody))
                .with(csrf())).andExpect(status().is(ErrorCode.ENTITY_NOT_FOUND.getStatus()))
            .andExpect(jsonPath("$.response.message", is(ErrorCode.ENTITY_NOT_FOUND.getMessage())));
    }

    // 6. styleSeq로 Style 엔티티를 찾을 수 없는 경우
    @Test
    @DisplayName("유효성 검증 에러 - 잘못된 styleSeq")
    @WithMockUser
    void registerSentence_notFoundStyleEntity() throws Exception {
        // Given - 유효한 projectSeq와 잘못된 styleSeq 설정
        Long projectSeq = 1L;
        TtsSentenceRequest request = TtsSentenceRequest.builder().voiceSeq(1L).styleSeq(-1L)
            .text("Valid text").build();

        // When - 서비스에서 예외 발생을 설정
        when(ttsSentenceService.addSentence(eq(projectSeq),
            any(TtsSentenceRequest.class))).thenThrow(new EntityNotFoundException());

        // Then - 요청 전송 및 응답 검증
        mockMvc.perform(post("/api/project/{projectSeq}/tts/sentence", projectSeq).contentType(
                    MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request))
                .with(csrf())).andExpect(status().is(ErrorCode.ENTITY_NOT_FOUND.getStatus()))
            .andExpect(jsonPath("$.response.message", is(ErrorCode.ENTITY_NOT_FOUND.getMessage())));
    }

    // 7. 잘못된 속성 값으로 인한 유효성 검증 에러 테스트
    @Test
    @DisplayName("유효성 검증 에러 - 잘못된 속성 값")
    @WithMockUser
    void registerSentence_validationErrorForInvalidAttributeFields() throws Exception {
        // Given - 유효한 projectSeq와 잘못된 속성 값 설정
        Long projectSeq = 1L;

        // 속성 정보 객체 생성
        TtsAttributeInfo attributeInfo = TtsAttributeInfo.builder().volume(200) // 유효 범위 초과 설정
            .speed(1.0f) // 유효한 speed 설정
            .stPitch(0)  // 유효한 stPitch 설정
            .emotion("neutral") // 유효한 emotion 설정
            .emotionStrength(100) // 유효한 emotionStrength 설정
            .sampleRate(16000) // 유효한 sampleRate 설정
            .alpha(0) // 유효한 alpha 설정
            .endPitch(0.0f) // 유효한 endPitch 설정
            .audioFormat("wav") // 유효한 audioFormat 설정
            .build(); // 유효한 속성 정보 초기화

        TtsSentenceRequest requestBody = TtsSentenceRequest.builder().voiceSeq(1L)
            .text("Valid text").attribute(attributeInfo) // 유효 범위 초과 설정
            .build();

        // When - 서비스에서 예외 발생을 설정
        // Then - 요청 전송 및 응답 검증
        mockMvc.perform(post("/api/project/{projectSeq}/tts/sentence", projectSeq).contentType(
                    MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(requestBody))
                .with(csrf())).andExpect(status().is(ErrorCode.INVALID_INPUT_VALUE.getStatus()))
            .andExpect(
                jsonPath("$.response.message", is(ErrorCode.INVALID_INPUT_VALUE.getMessage())));
    }

    // 8. 내부 서버 에러 테스트
    @Test
    @DisplayName("내부 서버 에러 테스트")
    @WithMockUser
    void registerSentence_internalServerError() throws Exception {
        // Given - 유효한 projectSeq와 요청 객체 설정
        Long projectSeq = 1L;
        TtsSentenceRequest requestBody = TtsSentenceRequest.builder().voiceSeq(1L)
            .text("Valid text").build();

        // When - 서비스에서 런타임 예외 발생을 설정
        when(ttsSentenceService.addSentence(eq(projectSeq),
            any(TtsSentenceRequest.class))).thenThrow(new RuntimeException("Unexpected error"));

        // Then - 요청 전송 및 응답 검증
        mockMvc.perform(post("/api/project/{projectSeq}/tts/sentence", projectSeq).contentType(
                MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(requestBody))
            .with(csrf())).andExpect(status().isInternalServerError()).andExpect(
            jsonPath("$.response.message", is(ErrorCode.INTERNAL_SERVER_ERROR.getMessage())));
    }

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

        // when
        mockMvc.perform(put("/api/project/{projectSeq}/tts/sentence/{tsSeq}", projectSeq, tsSeq)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
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

        // when, then
        mockMvc.perform(
                put("/api/project/{projectSeq}/tts/sentence/{tsSeq}", invalidProjectSeq, invalidTsSeq)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestBody))
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

        // when, then
        mockMvc.perform(
                put("/api/project/{projectSeq}/tts/sentence/{tsSeq}", nonExistentProjectSeq, tsSeq)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestBody))
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

        // when, then
        mockMvc.perform(put("/api/project/{projectSeq}/tts/sentence/{tsSeq}", projectSeq, tsSeq)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
                .with(csrf()))
            .andExpect(status().is(ErrorCode.INTERNAL_SERVER_ERROR.getStatus()))
            .andExpect(jsonPath("$.status", is(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())))
            .andExpect(
                jsonPath("$.response.message", is(ErrorCode.INTERNAL_SERVER_ERROR.getMessage())));
    }

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

        List<TtsSentence> ttsSentenceList = IntStream.range(0, repeatCount)
            .mapToObj(i -> createTtsSentence(project, (long) i, voice, "Test sentence " + i, i))
            .toList();

        TtsSentenceListDto response = TtsSentenceListDto.of(ttsSentenceList);

        when(ttsSentenceService.getSentenceList(projectSeq)).thenReturn(response);

        // when, then
        mockMvc.perform(get("/api/project/{projectSeq}/tts", projectSeq)
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

        when(ttsSentenceService.getSentenceList(projectSeq))
            .thenThrow(new EntityNotFoundException());

        // when, then
        mockMvc.perform(get("/api/project/{projectSeq}/tts", projectSeq)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is(ErrorCode.ENTITY_NOT_FOUND.getStatus()))
            .andExpect(jsonPath("$.status", is(ErrorCode.ENTITY_NOT_FOUND.getStatus())))
            .andExpect(
                jsonPath("$.response.message", is(ErrorCode.ENTITY_NOT_FOUND.getMessage())));
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
        mockMvc.perform(get("/api/project/{projectSeq}/tts", projectSeq)
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
        mockMvc.perform(get("/api/project/{projectSeq}/tts", invalidProjectSeq)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is(ErrorCode.INVALID_INPUT_VALUE.getStatus()))
            .andExpect(
                jsonPath("$.response.message", is(ErrorCode.INVALID_INPUT_VALUE.getMessage())));
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
        mockMvc.perform(get("/api/project/{projectSeq}/tts", projectSeq)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is(ErrorCode.INTERNAL_SERVER_ERROR.getStatus()))
            .andExpect(
                jsonPath("$.response.message", is(ErrorCode.INTERNAL_SERVER_ERROR.getMessage())));
    }

    /*
     *  [ tts 생성 컨트롤러 테스트 ]
     *  1. 존재하는 tts 행 seq로 TTS 생성 요청
     *  2. 존재하지 않는 tts 행 seq로 TTS 생성 요청
     *  3. 잘못된 행 seq 값으로 요청
     * */
    @WithMockUser
    @Test
    @DisplayName("tts 생성 컨트롤러 테스트 - 존재하는 tts 행 seq로 TTS 생성")
    public void makeTtsTest() throws Exception {
        // 1. tts sentence seq 설정
        Long projectSeq = 1L;
        Long tsSeq = 1L;
        Long voiceSeq =1L;

        // 2. 응답 객체 생성
        // TtsSentence 객체 생성
        TtsSentence ttsSentence = createTtsSentence(tsSeq, createProject(projectSeq), createVoice(voiceSeq), createTtsAudioFile());
        // TtsSentenceDto 객체 생성
        TtsSentenceDto response = TtsSentenceDto.of(ttsSentence);

        // 3. TtsMakeService의 makeTts 메서드에 대한 모의 동작 설정
        Mockito.when(ttsMakeService.makeTts(eq(tsSeq))).thenReturn(response); // 응답 객체 반환

        // 4. maketts 컨트롤러 메서드에 요청을 전송하여 테스트
        mockMvc.perform(get("/api/project/{projectSeq}/tts/sentence/{tsSeq}/maketts", projectSeq, tsSeq)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                // 5. 응답 상태와 데이터 확인
                .andExpect(status().isCreated())                                             // HTTP 상태 201 확인
                .andExpect(jsonPath("$.status", is(HttpStatus.CREATED.value())))    // 응답 데이터의 상태값이 201인지 확인
                .andExpect(jsonPath("$.response.sentence").exists());               // JSON 응답에 sentence 필드가 존재하는지
    }

    @WithMockUser
    @Test
    @DisplayName("tts 생성 컨트롤러 테스트 - 존재하는 tts 행 seq로 TTS 생성")
    public void makeTtsTestNotExistSentence() throws Exception {
        // 1. tts sentence seq 설정
        Long tsSeq = 1L;
        Long projectSeq = 1L;

        // 3. TtsMakeService의 makeTts 메서드 결과로 EntityNotFoundException 예외를 발생하도록 설정
        String errorMassage = "존재하지 않는 TTS 행입니다. id:"+tsSeq;
        Mockito.when(ttsMakeService.makeTts(eq(tsSeq))).thenThrow(new EntityNotFoundException(errorMassage));

        // 4. maketts 컨트롤러 메서드에 요청을 전송하여 테스트
        mockMvc.perform(get("/api/project/{projectSeq}/tts/sentence/{tsSeq}/maketts", projectSeq, tsSeq)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                // 5. 응답 상태와 데이터 확인
                .andExpect(status().is(ErrorCode.ENTITY_NOT_FOUND.getStatus()))
                .andExpect(jsonPath("$.status", is(ErrorCode.ENTITY_NOT_FOUND.getStatus())))
                .andExpect(jsonPath("$.response.message", is(errorMassage)));
    }


    @WithMockUser
    @Test
    @DisplayName("tts 생성 컨트롤러 테스트 -잘못된 행 seq 값으로 요청")
    public void makeTtsTestInvalidSentenceSeq() throws Exception {
        // 1. tts sentence seq 설정
        Long tsSeq = -1L;
        Long projectSeq = 1L;

        // 2. maketts 컨트롤러 메서드에 요청을 전송하여 테스트
        mockMvc.perform(get("/api/project/{projectSeq}/tts/sentence/{tsSeq}/maketts", projectSeq, tsSeq)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                // 3. 응답 상태가 bad request여야 함
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())));
    }

    /*
    테스트 시나리오: deleteSentence 컨트롤러 메서드

    1. **성공적인 삭제**
    - 조건:
    - 유효한 `projectSeq`와 `tsSeq`가 제공됨.
    - 제공된 `tsSeq`에 해당하는 `TtsSentence` 엔티티가 존재하며 삭제 성공.
    - 기대 결과:
    - HTTP 상태 코드 200 반환.
    - 응답 메시지 `"삭제 성공"` 포함.

    2. **TtsSentence를 찾을 수 없음**
    - 조건:
    - 유효한 `projectSeq`와 `tsSeq`가 제공되었으나, 데이터베이스에 해당 `tsSeq`에 대한 `TtsSentence` 엔티티가 존재하지 않음.
    - 기대 결과:
    - HTTP 상태 코드 404 반환.
    - 응답 메시지 `"Entity not found"` 포함.

    3. **유효성 검증 실패 - 잘못된 PathVariable**
    - 조건:
    - `projectSeq`가 1보다 작은 값으로 제공됨 (예: `0` 또는 `-1`).
    - 또는 `tsSeq`가 1보다 작은 값으로 제공됨 (예: `0` 또는 `-1`).
    - 기대 결과:
    - HTTP 상태 코드 400 반환.
    - 응답 본문에 `"projectSeq is invalid"` 또는 `"tsSeq is invalid"` 메시지 포함.

    4. **내부 서버 에러**
    - 조건:
    - 서비스 계층에서 예기치 못한 예외 발생 (예: 데이터베이스 연결 문제 등).
    - 기대 결과:
    - HTTP 상태 코드 500 반환.
    - 응답 메시지 `"Internal Server Error"` 포함.

    */

    // 1. 성공적인 삭제
    @Test
    @DisplayName("deleteSentence - 성공적인 삭제")
    @WithMockUser
    void deleteSentence_Success() throws Exception {
        // given
        Long projectSeq = 1L;
        Long tsSeq = 1L;

        Mockito.when(ttsSentenceService.deleteSentence(projectSeq, tsSeq)).thenReturn(true);

        // when, then
        mockMvc.perform(delete("/api/project/{projectSeq}/tts/sentence/{tsSeq}", projectSeq, tsSeq)
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status", is(HttpStatus.OK.value())))
            .andExpect(jsonPath("$.response", is("삭제 성공")));
    }

    // 2. TtsSentence를 찾을 수 없음
    @Test
    @DisplayName("deleteSentence - TtsSentence를 찾을 수 없음")
    @WithMockUser
    void deleteSentence_NotFound() throws Exception {
        // given
        Long projectSeq = 1L;
        Long tsSeq = 999L;

//        Mockito.when(ttsSentenceService.deleteSentence(eq(projectSeq), eq(tsSeq)))
        Mockito.when(ttsSentenceService.deleteSentence(projectSeq, tsSeq))
            .thenThrow(new EntityNotFoundException());

        // when, then
        mockMvc.perform(delete("/api/project/{projectSeq}/tts/sentence/{tsSeq}", projectSeq, tsSeq)
                .with(csrf()))
            .andExpect(status().is(ErrorCode.ENTITY_NOT_FOUND.getStatus()))
            .andExpect(jsonPath("$.status", is(ErrorCode.ENTITY_NOT_FOUND.getStatus())))
            .andExpect(jsonPath("$.response.message", is(ErrorCode.ENTITY_NOT_FOUND.getMessage())));
    }

    // 3. 유효성 검증 실패 - 잘못된 PathVariable
    @Test
    @DisplayName("deleteSentence - 잘못된 PathVariable")
    @WithMockUser
    void deleteSentence_InvalidPathVariable() throws Exception {
        // given
        Long invalidProjectSeq = 0L;
        Long invalidTsSeq = -1L;

        // when, then
        mockMvc.perform(delete("/api/project/{projectSeq}/tts/sentence/{tsSeq}", invalidProjectSeq, invalidTsSeq)
                .with(csrf()))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
            .andExpect(jsonPath("$.response").exists());
    }

    // 4. 내부 서버 에러
    @Test
    @DisplayName("deleteSentence - 내부 서버 에러")
    @WithMockUser
    void deleteSentence_InternalServerError() throws Exception {
        // given
        Long projectSeq = 1L;
        Long tsSeq = 1L;

        Mockito.when(ttsSentenceService.deleteSentence(projectSeq, tsSeq))
            .thenThrow(new RuntimeException("Unexpected error"));

        // when, then
        mockMvc.perform(delete("/api/project/{projectSeq}/tts/sentence/{tsSeq}", projectSeq, tsSeq)
                .with(csrf()))
            .andExpect(status().is(ErrorCode.INTERNAL_SERVER_ERROR.getStatus()))
            .andExpect(jsonPath("$.status", is(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())))
            .andExpect(jsonPath("$.response.message", is(ErrorCode.INTERNAL_SERVER_ERROR.getMessage())));
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

    private static Style createStyle(Long styleSeq) {
        return Style.builder().styleSeq(styleSeq) // 스타일 ID 설정
                .name("Valid style") // 스타일 이름 설정
                .build();
    }

    private static TtsAudioFile createTtsAudioFile() {
        return TtsAudioFile.builder()
                .audioName("project-1-tts-1")
                .audioPath("/tts/123123_porject-1-tts-1.wav")
                .audioExtension(".wav")
                .audioSize("194.3KB")
                .audioTime(100)
                .audioPlayYn('y')
                .downloadCount(0)
                .downloadYn('y')
                .build();
    }

    private static TtsSentence createTtsSentence(Voice voice, Project project) {
        return TtsSentence.builder().text("Sample TtsSentence").sortOrder(1).volume(50).speed(1.0f)
            .voice(voice).project(project).build();
    }

    private static TtsSentence createTtsSentence(Project project, Long tsSeq, Voice voice,
        String text) {
        return TtsSentence.builder().tsSeq(tsSeq).text(text).sortOrder(1).volume(50).speed(1.0f)
            .voice(voice).project(project).build();
    }

    private static TtsSentence createTtsSentence(Project project, Long tsSeq, Voice voice,
        String text, int order) {
        return TtsSentence.builder().tsSeq(tsSeq).text(text).sortOrder(order).volume(50).speed(1.0f)
            .voice(voice).project(project).build();
    }

    private static TtsSentence createTtsSentence(Long tsSeq, Project project, Voice voice, TtsAudioFile ttsAudioFile) {
        return TtsSentence.builder()
                .tsSeq(tsSeq).project(project).voice(voice).ttsAudiofile(ttsAudioFile)
                .build();
    }
}