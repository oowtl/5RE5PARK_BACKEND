package com.oreo.finalproject_5re5_be.tts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oreo.finalproject_5re5_be.project.entity.Project;
import com.oreo.finalproject_5re5_be.tts.dto.request.TtsAttributeInfo;
import com.oreo.finalproject_5re5_be.tts.dto.request.TtsSentenceCreateRequest;
import com.oreo.finalproject_5re5_be.tts.dto.response.TtsSentenceDto;
import com.oreo.finalproject_5re5_be.tts.entity.TtsSentence;
import com.oreo.finalproject_5re5_be.tts.entity.Voice;
import com.oreo.finalproject_5re5_be.global.exception.EntityNotFoundException;
import com.oreo.finalproject_5re5_be.global.exception.ErrorCode;
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

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TtsController.class)
class TtsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TtsSentenceService ttsSentenceService;

    @Autowired
    private ObjectMapper objectMapper;

    /*
    TtsController 테스트 클래스
    - TtsController의 addSentence 메서드에 대한 테스트 케이스를 작성
    - 다양한 유효성 검증과 에러 상황을 포함하여 테스트 진행

    Test Scenarios

    1. 성공적인 문장 생성 요청 테스트
    - 유효한 projectSeq와 TtsSentenceCreateRequest 입력 시
    - HTTP 상태 200과 올바른 JSON 응답을 반환하는지 확인

    2. 텍스트 필드 누락으로 인한 유효성 검증 에러 테스트
    - text 필드가 누락된 TtsSentenceCreateRequest 입력 시
    - HTTP 상태 400과 텍스트 필드 관련 에러 메시지 반환을 확인

    3. voiceSeq 필드 누락으로 인한 유효성 검증 에러 테스트
    - voiceSeq 필드가 누락된 TtsSentenceCreateRequest 입력 시
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
    - 유효 범위를 벗어난 속성 값 (예: volume)을 가진 TtsSentenceCreateRequest 입력 시
    - HTTP 상태 400과 속성 관련 에러 메시지 반환을 확인

    8. 내부 서버 에러 테스트
    - 유효한 입력값이지만 메서드 내부에서 예외 발생 시
    - HTTP 상태 500과 예상치 못한 에러 메시지 반환을 확인
 */

    // 1. 성공적인 문장 생성 요청 테스트
    @WithMockUser
    @Test
    @DisplayName("성공적인 문장 생성 요청 테스트")
    public void registerSentence_successfulCreation() throws Exception {
        // given - 유효한 프로젝트 ID와 문장 생성 요청 객체 초기화
        Long projectSeq = 1L;

        // 1. 요청 객체 생성
        // 속성 정보 객체 생성
        TtsAttributeInfo attributeInfo = createAttributeInfo();
        // 문장 생성 요청 객체 생성
        TtsSentenceCreateRequest requestBody = createSentenceCreateRequest(attributeInfo);

        // 2. 응답 객체 생성
        // voice 객체 생성
        Voice voice = createVoice();
        // project 객체 생성
        Project project = createProject();
        // TtsSentence 객체 생성
        TtsSentence ttsSentence = createTtsSentence(voice, project);
        // TtsSentenceDto 객체 생성
        TtsSentenceDto response = TtsSentenceDto.of(ttsSentence);

        // 3. TtsSentenceService의 addSentence 메서드에 대한 모의 동작 설정
        Mockito.when(ttsSentenceService.addSentence(eq(projectSeq), any(TtsSentenceCreateRequest.class))).thenReturn(response); // 응답 객체 반환

        // when
        // 4. 컨트롤러의 addSentence 메서드에 요청을 전송하여 테스트 수행
        mockMvc.perform(post("/api/project/{projectSeq}/tts/sentence", projectSeq) // URL 설정
                        .contentType(MediaType.APPLICATION_JSON)                // 요청 본문 타입 설정
                        .content(objectMapper.writeValueAsString(requestBody))      // 요청 본문으로 JSON 데이터를 직렬화하여 전송
                        .with(csrf()))                                          // CSRF 토큰 추가
                // Then - 예상 응답 상태와 JSON 구조 확인
                .andExpect(status().isCreated())                                   // HTTP 상태 201 확인
                .andExpect(jsonPath("$.status", is(HttpStatus.CREATED.value())))// 응답의 상태가 200인지 확인
                .andExpect(jsonPath("$.response.sentence").exists());         // JSON 응답에 sentence 필드가 존재하는지 확인
    }

    // 2. 텍스트 필드 누락으로 인한 유효성 검증 에러 테스트
    @Test
    @WithMockUser
    @DisplayName("유효성 검증 에러 - 텍스트 필드 누락")
    public void registerSentence_validationErrorForMissingTextField() throws Exception {
        // given - 유효한 프로젝트 ID와 텍스트 필드가 누락된 요청 객체 초기화
        Long projectSeq = 1L;

        // 1. 요청 객체 생성
        // 속성 정보 객체 생성
        TtsAttributeInfo attributeInfo = createAttributeInfo();

        // 텍스트 필드가 없는 요청 객체 생성
        TtsSentenceCreateRequest requestBody = TtsSentenceCreateRequest.builder().voiceSeq(1L) // 유효한 voiceSeq 설정
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
                .andExpect(status().is(ErrorCode.INVALID_INPUT_VALUE.getStatus())).andExpect(jsonPath("$.response.message", is(ErrorCode.INVALID_INPUT_VALUE.getMessage())));
    }

    // 3. voiceSeq 필드 누락으로 인한 유효성 검증 에러 테스트
    @Test
    @DisplayName("유효성 검증 에러 - voiceSeq 필드 누락")
    @WithMockUser
    public void registerSentence_validationErrorForMissingVoiceSeqField() throws Exception {
        // Given - 유효한 프로젝트 ID와 voiceSeq 필드가 누락된 요청 객체 초기화
        Long projectSeq = 1L;

        TtsAttributeInfo attributeInfo = createAttributeInfo();

        // 텍스트 필드가 없는 요청 객체 생성
        TtsSentenceCreateRequest requestBody = TtsSentenceCreateRequest.builder().voiceSeq(null) // voiceSeq 필드 누락
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
                .andExpect(status().is(ErrorCode.INVALID_INPUT_VALUE.getStatus()))                            // HTTP 상태 400 확인
                .andExpect(jsonPath("$.response.message", is(ErrorCode.INVALID_INPUT_VALUE.getMessage())));
    }

    // 4. 잘못된 projectSeq로 인한 유효성 검증 에러 테스트
    @Test
    @DisplayName("유효성 검증 에러 - 잘못된 projectSeq")
    @WithMockUser
    public void registerSentence_validationErrorForInvalidProjectSeq() throws Exception {
        // Given - 잘못된 projectSeq 설정
        Long invalidProjectSeq = -100L;

        // 1. 요청 객체 생성
        // 속성 정보 객체 생성
        TtsAttributeInfo attributeInfo = createAttributeInfo();
        // 문장 생성 요청 객체 생성
        TtsSentenceCreateRequest requestBody = createSentenceCreateRequest(attributeInfo);


        // When - 서비스에서 예외 발생을 설정
        Mockito.when(ttsSentenceService.addSentence(eq(invalidProjectSeq), any(TtsSentenceCreateRequest.class))).thenThrow(new IllegalArgumentException("projectSeq is invalid"));

        // Then - 요청 전송 및 응답 검증
        mockMvc.perform(post("/api/project/{projectSeq}/tts/sentence", invalidProjectSeq).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(requestBody)).with(csrf()))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.response.message", is("projectSeq is invalid")));
                .andExpect(status().is(ErrorCode.INVALID_INPUT_VALUE.getStatus()))                            // HTTP 상태 400 확인
                .andExpect(jsonPath("$.response.message", is(ErrorCode.INVALID_INPUT_VALUE.getMessage())));
    }

    // 5. voiceSeq 로 Voice 엔티티를 찾을 수 없는 경우
    @Test
    @DisplayName("유효성 검증 에러 - voiceSeq 로 Voice 엔티티를 찾을 수 없는 경우")
    @WithMockUser
    public void registerSentence_notFoundVoiceEntity() throws Exception {
        // Given
        Long projectSeq = 1L;

        // 속성 정보 객체 생성
        TtsAttributeInfo attributeInfo = createAttributeInfo();

        // 문장 생성 요청 객체 생성
        TtsSentenceCreateRequest requestBody = TtsSentenceCreateRequest.builder().voiceSeq(-1L).text("Valid text").attribute(attributeInfo).build();

        // When - 서비스에서 예외 발생을 설정
        Mockito.when(ttsSentenceService.addSentence(eq(projectSeq), any(TtsSentenceCreateRequest.class))).thenThrow(new EntityNotFoundException());

        // Then - 요청 전송 및 응답 검증
        mockMvc.perform(post("/api/project/{projectSeq}/tts/sentence", projectSeq).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(requestBody)).with(csrf())).andExpect(status().is(ErrorCode.ENTITY_NOT_FOUND.getStatus())).andExpect(jsonPath("$.response.message", is(ErrorCode.ENTITY_NOT_FOUND.getMessage())));
    }

    // 6. styleSeq로 Style 엔티티를 찾을 수 없는 경우
    @Test
    @DisplayName("유효성 검증 에러 - 잘못된 styleSeq")
    @WithMockUser
    public void registerSentence_notFoundStyleEntity() throws Exception {
        // Given - 유효한 projectSeq와 잘못된 styleSeq 설정
        Long projectSeq = 1L;
        TtsSentenceCreateRequest request = TtsSentenceCreateRequest.builder().voiceSeq(1L).styleSeq(-1L).text("Valid text").build();

        // When - 서비스에서 예외 발생을 설정
        Mockito.when(ttsSentenceService.addSentence(eq(projectSeq), any(TtsSentenceCreateRequest.class))).thenThrow(new EntityNotFoundException());

        // Then - 요청 전송 및 응답 검증
        mockMvc.perform(post("/api/project/{projectSeq}/tts/sentence", projectSeq).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)).with(csrf())).andExpect(status().is(ErrorCode.ENTITY_NOT_FOUND.getStatus())).andExpect(jsonPath("$.response.message", is(ErrorCode.ENTITY_NOT_FOUND.getMessage())));
    }

    // 7. 잘못된 속성 값으로 인한 유효성 검증 에러 테스트
    @Test
    @DisplayName("유효성 검증 에러 - 잘못된 속성 값")
    @WithMockUser
    public void registerSentence_validationErrorForInvalidAttributeFields() throws Exception {
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

        TtsSentenceCreateRequest requestBody = TtsSentenceCreateRequest.builder().voiceSeq(1L).text("Valid text").attribute(attributeInfo) // 유효 범위 초과 설정
                .build();

        // When - 서비스에서 예외 발생을 설정
        // Then - 요청 전송 및 응답 검증
        mockMvc.perform(post("/api/project/{projectSeq}/tts/sentence", projectSeq).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(requestBody)).with(csrf())).andExpect(status().is(ErrorCode.INVALID_INPUT_VALUE.getStatus())).andExpect(jsonPath("$.response.message", is(ErrorCode.INVALID_INPUT_VALUE.getMessage())));
    }

    // 8. 내부 서버 에러 테스트
    @Test
    @DisplayName("내부 서버 에러 테스트")
    @WithMockUser
    public void registerSentence_internalServerError() throws Exception {
        // Given - 유효한 projectSeq와 요청 객체 설정
        Long projectSeq = 1L;
        TtsSentenceCreateRequest requestBody = TtsSentenceCreateRequest.builder().voiceSeq(1L).text("Valid text").build();

        // When - 서비스에서 런타임 예외 발생을 설정
        Mockito.when(ttsSentenceService.addSentence(eq(projectSeq), any(TtsSentenceCreateRequest.class))).thenThrow(new RuntimeException("Unexpected error"));

        // Then - 요청 전송 및 응답 검증
        mockMvc.perform(post("/api/project/{projectSeq}/tts/sentence", projectSeq).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(requestBody)).with(csrf())).andExpect(status().isInternalServerError()).andExpect(jsonPath("$.response.message", is(ErrorCode.INTERNAL_SERVER_ERROR.getMessage())));
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

    private static TtsSentenceCreateRequest createSentenceCreateRequest(TtsAttributeInfo attributeInfo) {
        return TtsSentenceCreateRequest.builder().voiceSeq(1L) // 유효한 voiceSeq 설정
                .styleSeq(1L) // 유효한 styleSeq 설정
                .text("Valid text") // 유효한 텍스트 설정
                .order(1) // 표시 순서 설정
                .attribute(attributeInfo) // 유효한 속성 정보 설정
                .build(); // 유효한 요청 객체 초기화
    }

    private static Voice createVoice() {
        return Voice.builder().voiceSeq(1L) // 목소리 ID 설정
                .name("Valid voice") // 목소리 이름 설정
                .build();
    }

    private static Project createProject() {
        return Project.builder().proSeq(1L) // 프로젝트 ID 설정
                .proName("Valid project") // 프로젝트 이름 설정
                .build();
    }

    private static TtsSentence createTtsSentence(Voice voice, Project project) {
        return TtsSentence.builder().text("Sample TtsSentence").sortOrder(1).volume(50).speed(1.0f).voice(voice).project(project).build();
    }
}