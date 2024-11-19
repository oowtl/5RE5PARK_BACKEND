package com.oreo.finalproject_5re5_be.tts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oreo.finalproject_5re5_be.tts.dto.response.LanguageListDto;
import com.oreo.finalproject_5re5_be.tts.entity.Language;
import com.oreo.finalproject_5re5_be.tts.service.LanguageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@WebMvcTest(LanguageController.class) // 컨트롤러 테스트에 필요한 빈만 로드
class LanguageControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LanguageService languageService;

    @Autowired
    private ObjectMapper objectMapper; // JSON 직렬화에 사용


    /*
    * [ 언어 전체 조회 테스트 ]
    *  1. 여러 개 데이터 조회 -> 응답 상태 200, 결과로 LanguageListResponse 반환
    *  2. 빈 리스트 조회 -> 응답 상태 200, 결과로 빈 LanguageListResponse 반환
    * */
    @Test
    @DisplayName("언어 전체 조회 테스트 - 여러 개 데이터 조회")
    public void getLanguageListTest() throws Exception {
        // 3개의 언어 정보가 담긴 리스트 생성
        Language language1 = createLanguageEntity(1);
        Language language2 = createLanguageEntity(2);
        Language language3 = createLanguageEntity(3);

        List<Language> languageList = List.of(language1, language2, language3);
        // 언어 전체 조회 응답 형태로 변환
        LanguageListDto languageListDto = LanguageListDto.of(languageList);

        // 서비스 동작 세팅
        when(languageService.getLanguageList()).thenReturn(languageListDto);

        // 언어 전체 조회 GET 요청 실행 및 검증
        mockMvc.perform(get("/api/language")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // HTTP 상태 코드 검증
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value())) // 응답 상태 코드 검증
                .andExpect(jsonPath("$.response.languageList.size()").value(3)) // 응답 배열 크기 검증
                .andExpect(jsonPath("$.response.languageList[0].languageCode").value(language1.getLangCode())) // 첫 번째 항목 검증
                .andExpect(jsonPath("$.response.languageList[1].languageName").value(language2.getLangName())) // 두 번째 항목 검증
                .andExpect(jsonPath("$.response.languageList[2].regionCode").value(language3.getRegionCode())); // 세 번째 항목 검증
    }

    @Test
    @DisplayName("언어 전체 조회 테스트 - 빈 리스트 조회")
    public void getEmptyLanguageListTest() throws Exception {
        // 빈 언어 리스트 생성
        List<Language> languageList = new ArrayList<>();
        // 언어 전체 조회 응답 형태로 변환
        LanguageListDto languageListDto = LanguageListDto.of(languageList);

        // 서비스 동작 세팅
        when(languageService.getLanguageList()).thenReturn(languageListDto);

        // 언어 전체 조회 GET 요청 실행 및 검증
        mockMvc.perform(get("/api/language")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // HTTP 상태 코드 검증
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value())) // 응답 상태 코드 검증
                .andExpect(jsonPath("$.response.languageList").value(languageList)); // 언어 리스트 검증
    }

    // Language 엔티티 생성 메서드
    private Language createLanguageEntity(int n) {
        return Language.builder()
                .langSeq((long)n)
                .langCode("lang-code"+n)
                .langName("lang-name"+n)
                .regionName("region-name"+n)
                .regionCode("region-code"+n)
                .build();
    }
}