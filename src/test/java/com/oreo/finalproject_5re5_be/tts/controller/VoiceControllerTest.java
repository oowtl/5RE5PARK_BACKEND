package com.oreo.finalproject_5re5_be.tts.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oreo.finalproject_5re5_be.global.exception.EntityNotFoundException;
import com.oreo.finalproject_5re5_be.global.exception.ErrorCode;
import com.oreo.finalproject_5re5_be.tts.dto.response.VoiceListDto;
import com.oreo.finalproject_5re5_be.tts.entity.Language;
import com.oreo.finalproject_5re5_be.tts.entity.ServerCode;
import com.oreo.finalproject_5re5_be.tts.entity.Style;
import com.oreo.finalproject_5re5_be.tts.entity.Voice;
import com.oreo.finalproject_5re5_be.tts.service.VoiceService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WithMockUser
@WebMvcTest(VoiceController.class)
class VoiceControllerTest {
    @Autowired private MockMvc mockMvc;

    @MockBean private VoiceService voiceService;

    @Autowired private ObjectMapper objectMapper;

    /**
     * [ 보이스 조건 조회 테스트 ] 1. 유효한 조건으로 조회 -> 응답 상태 200, response에 VoiceListDto 반환 2. 유효한 조건이지만 만족하는 보이스가
     * 없을 경우 -> 응답 상태 200, response에 빈 리스트 반환 4. 유효하지 않은 조건으로 조회 -> 응답 상태 400, ErrorResponse 반환
     */

    // 보이스 조건 조회 테스트 - 1. 유효한 조건으로 조회
    @Test
    @DisplayName("보이스 조건 조회 테스트 - 유효한 조건으로 조회 ")
    public void getVoiceListByLangAndStyle() throws Exception {
        // 언어, 스타일, 보이스 반환 데이터 생성
        Language language = createLanguageEntity(10L);
        Style style = createStyleEntity(20L);

        Voice voice = createVoiceEntity(1L, language, style, 'y');
        VoiceListDto voiceList = VoiceListDto.of(List.of(voice));

        // 보이스 조건 조회 서비스 동작 설정
        when(voiceService.getVoiceList(language.getLangCode(), style.getName())).thenReturn(voiceList);

        // 스타일 조건 조회 get 요청 테스트 및 검증
        mockMvc
                .perform(
                        get("/api/voice")
                                .param("languagecode", language.getLangCode())
                                .param("stylename", style.getName())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.response.voiceList").exists())
                .andExpect(jsonPath("$.response.voiceList.size()").value(voiceList.getVoiceList().size()))
                .andExpect(
                        jsonPath("$.response.voiceList[0].voiceSeq")
                                .value(voiceList.getVoiceList().get(0).getVoiceSeq()));
    }

    // 보이스 조건 조회 테스트 - 2. 유효한 조건이지만 만족하는 보이스가 없을 경우
    @Test
    @DisplayName("보이스 조건 조회 테스트 - 유효한 조건이지만 만족하는 보이스가 없을 경우")
    public void getVoiceListByLangAndStyle_emptyList() throws Exception {
        // 유효한 언어 코드, 언어 이름 초기화
        String validLangCode = "valid-language-code";
        String validStyleName = "valid-style-name";

        // 빈 리스트를 가진 DTO 생성
        VoiceListDto voiceList = VoiceListDto.of(new ArrayList<>());

        // 보이스 조건 조회 서비스 동작 설정
        when(voiceService.getVoiceList(validLangCode, validStyleName)).thenReturn(voiceList);

        // 스타일 조건 조회 get 요청 테스트 및 검증
        mockMvc
                .perform(
                        get("/api/voice")
                                .param("languagecode", validLangCode)
                                .param("stylename", validStyleName)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.response.voiceList").exists()) // 존재하는지 확인
                .andExpect(jsonPath("$.response.voiceList").isArray()) // 배열인지 확인
                .andExpect(jsonPath("$.response.voiceList").isEmpty()); // 비어있는지 확인
    }

    // 보이스 조건 조회 테스트 - 3. 유효하지 않은 조건으로 조회
    @Test
    @DisplayName("보이스 조건 조회 테스트 - 유효하지 않은 조건으로 조회")
    public void getVoiceListByLangAndStyle_null() throws Exception {
        // 유효하지 않는 언어 코드, 유효한 언어 이름 초기화
        String invalidLangCode = "invalid-language-code";
        String validStyleName = "valid-style-name";

        // 없는 조건 값으로 조회해 EntityNotFoundException을 발생하는 동작으로 설정
        EntityNotFoundException throwException =
                new EntityNotFoundException("invalid language code, langCode:" + invalidLangCode);
        when(voiceService.getVoiceList(invalidLangCode, validStyleName)).thenThrow(throwException);

        // 스타일 조건 조회 get 요청 테스트 및 검증
        mockMvc
                .perform(
                        get("/api/voice")
                                .param("languagecode", invalidLangCode)
                                .param("stylename", validStyleName)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(ErrorCode.ENTITY_NOT_FOUND.getStatus()))
                .andExpect(jsonPath("$.status").value(throwException.getErrorCode().getStatus()))
                .andExpect(jsonPath("$.response.message").value(throwException.getMessage()));
    }

    // 스타일 엔티티 생성 메서드
    private Style createStyleEntity(Long seq) {
        return Style.builder()
                .styleSeq(seq)
                .name("style-name" + seq)
                .mood("style-mood" + seq)
                .description("style-desc" + seq)
                .contents("contents-test" + seq)
                .build();
    }

    // 언어 엔티티 생성 메서드
    private Language createLanguageEntity(Long seq) {
        return Language.builder()
                .langSeq(seq)
                .langCode("lang-code" + seq)
                .langName("lang-name" + seq)
                .regionName("region-name" + seq)
                .regionCode("region-code" + seq)
                .build();
    }

    // 목소리 엔티티 생성 메서드
    private Voice createVoiceEntity(Long seq, Language language, Style style, char enabled) {
        return Voice.builder()
                .voiceSeq(seq)
                .name("test-voice-name" + seq)
                .age(seq.intValue() + 10)
                .server(ServerCode.GOOGLE_CLOUD)
                .enabled(enabled)
                .language(language)
                .style(style)
                .isRecommend('N')
                .useCnt(seq.intValue())
                .build();
    }
}
