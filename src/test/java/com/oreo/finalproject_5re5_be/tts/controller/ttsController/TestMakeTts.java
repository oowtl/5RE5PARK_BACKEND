package com.oreo.finalproject_5re5_be.tts.controller.ttsController;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.oreo.finalproject_5re5_be.global.exception.EntityNotFoundException;
import com.oreo.finalproject_5re5_be.global.exception.ErrorCode;
import com.oreo.finalproject_5re5_be.project.entity.Project;
import com.oreo.finalproject_5re5_be.project.service.ProjectService;
import com.oreo.finalproject_5re5_be.tts.controller.TtsController;
import com.oreo.finalproject_5re5_be.tts.dto.response.TtsSentenceDto;
import com.oreo.finalproject_5re5_be.tts.entity.TtsAudioFile;
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
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TtsController.class)
class TestMakeTts {

    @Autowired private MockMvc mockMvc;

    @MockBean private TtsSentenceService ttsSentenceService;

    @MockBean private TtsMakeService ttsMakeService;

    @MockBean private ProjectService projectService;

    /*
     *  [ tts 생성 컨트롤러 테스트 ]
     *  1. 존재하는 tts 행 seq로 TTS 생성 요청
     *  2. 존재하지 않는 tts 행 seq로 TTS 생성 요청
     *  3. 잘못된 행 seq 값으로 요청
     * */
    @WithMockUser
    @Test
    @DisplayName("tts 생성 컨트롤러 테스트 - 존재하는 tts 행 seq로 TTS 생성")
    void makeTtsTest() throws Exception {
        // 1. tts sentence seq 설정
        Long projectSeq = 1L;
        Long tsSeq = 1L;
        Long voiceSeq = 1L;

        // 2. 응답 객체 생성
        // TtsSentence 객체 생성
        TtsSentence ttsSentence =
                createTtsSentence(
                        tsSeq, createProject(projectSeq), createVoice(voiceSeq), createTtsAudioFile());
        // TtsSentenceDto 객체 생성
        TtsSentenceDto response = TtsSentenceDto.of(ttsSentence);

        // 3. TtsMakeService의 makeTts 메서드에 대한 모의 동작 설정
        Mockito.when(ttsMakeService.makeTts(eq(tsSeq))).thenReturn(response); // 응답 객체 반환

        // 3.1 회원 조회에 관한 모의 동작 설정
        Mockito.when(projectService.projectCheck(eq(projectSeq), anyLong())).thenReturn(true);

        // 3.2 mock
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("memberSeq", 1L);

        // 4. maketts 컨트롤러 메서드에 요청을 전송하여 테스트
        mockMvc
                .perform(
                        get("/api/project/{projectSeq}/tts/sentence/{tsSeq}/maketts", projectSeq, tsSeq)
                                .contentType(MediaType.APPLICATION_JSON)
                                .session(session)
                                .with(csrf()))
                // 5. 응답 상태와 데이터 확인
                .andExpect(status().isCreated()) // HTTP 상태 201 확인
                .andExpect(jsonPath("$.status", is(HttpStatus.CREATED.value()))) // 응답 데이터의 상태값이 201인지 확인
                .andExpect(jsonPath("$.response.sentence").exists()); // JSON 응답에 sentence 필드가 존재하는지
    }

    @WithMockUser
    @Test
    @DisplayName("tts 생성 컨트롤러 테스트 - 존재하는 tts 행 seq로 TTS 생성")
    void makeTtsTestNotExistSentence() throws Exception {
        // 1. tts sentence seq 설정
        Long tsSeq = 1L;
        Long projectSeq = 1L;

        // 3. TtsMakeService의 makeTts 메서드 결과로 EntityNotFoundException 예외를 발생하도록 설정
        String errorMassage = "존재하지 않는 TTS 행입니다. id:" + tsSeq;
        Mockito.when(ttsMakeService.makeTts(eq(tsSeq)))
                .thenThrow(new EntityNotFoundException(errorMassage));

        // 3.1 회원 조회에 관한 모의 동작 설정
        Mockito.when(projectService.projectCheck(eq(projectSeq), anyLong())).thenReturn(true);

        // 3.2 mock session 설정
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("memberSeq", 1L);

        // 4. maketts 컨트롤러 메서드에 요청을 전송하여 테스트
        mockMvc
                .perform(
                        get("/api/project/{projectSeq}/tts/sentence/{tsSeq}/maketts", projectSeq, tsSeq)
                                .contentType(MediaType.APPLICATION_JSON)
                                .session(session)
                                .with(csrf()))
                // 5. 응답 상태와 데이터 확인
                .andExpect(status().is(ErrorCode.ENTITY_NOT_FOUND.getStatus()))
                .andExpect(jsonPath("$.status", is(ErrorCode.ENTITY_NOT_FOUND.getStatus())))
                .andExpect(jsonPath("$.response.message", is(errorMassage)));
    }

    @WithMockUser
    @Test
    @DisplayName("tts 생성 컨트롤러 테스트 -잘못된 행 seq 값으로 요청")
    void makeTtsTestInvalidSentenceSeq() throws Exception {
        // 1. tts sentence seq 설정
        Long tsSeq = -1L;
        Long projectSeq = 1L;

        // 모의 동작 설정
        // 2.1 회원 조회에 관한 모의 동작 설정
        Mockito.when(projectService.projectCheck(eq(projectSeq), anyLong())).thenReturn(true);

        // 2.2 mock session 설정
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("memberSeq", 1L);

        // 3. maketts 컨트롤러 메서드에 요청을 전송하여 테스트
        mockMvc
                .perform(
                        get("/api/project/{projectSeq}/tts/sentence/{tsSeq}/maketts", projectSeq, tsSeq)
                                .contentType(MediaType.APPLICATION_JSON)
                                .session(session)
                                .with(csrf()))
                // 3. 응답 상태가 bad request여야 함
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())));
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

    private static TtsSentence createTtsSentence(
            Long tsSeq, Project project, Voice voice, TtsAudioFile ttsAudioFile) {
        return TtsSentence.builder()
                .tsSeq(tsSeq)
                .project(project)
                .voice(voice)
                .ttsAudiofile(ttsAudioFile)
                .build();
    }
}
