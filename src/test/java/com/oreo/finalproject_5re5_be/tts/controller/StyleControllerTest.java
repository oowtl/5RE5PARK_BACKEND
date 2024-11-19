package com.oreo.finalproject_5re5_be.tts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oreo.finalproject_5re5_be.tts.dto.response.StyleListDto;
import com.oreo.finalproject_5re5_be.tts.entity.Style;
import com.oreo.finalproject_5re5_be.tts.service.LanguageService;
import com.oreo.finalproject_5re5_be.tts.service.StyleService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@WebMvcTest(StyleController.class) // 컨트롤러 테스트에 필요한 빈만 로드
class StyleControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StyleService styleService;

    @Autowired
    private ObjectMapper objectMapper; // JSON 직렬화에 사용

    /*
     * [ 스타일 전체 조회 테스트 ]
     *  1. 여러 개 데이터 조회 -> 응답 상태 200, 결과로 StyleListDto 반환
     *  2. 빈 리스트 조회 -> 응답 상태 200, 결과로 빈 StyleListDto 반환
     * */

    // 1. 여러 개 데이터 조회 -> 응답 상태 200, 결과로 StyleListDto 반환
    @Test
    @DisplayName("스타일 전체 조회 테스트 - 여러 개 데이터 조회")
    public void getStyleListTest() throws Exception {
        // 3개 데이터가 들어간 스타일 리스트 생성
        Style style1 = createStyleEntity(0);
        Style style2 = createStyleEntity(2020);
        Style style3 = createStyleEntity(34432);
        StyleListDto styleListDto = StyleListDto.of(List.of(style1, style2, style3));

        // 스타일 전체 조회 서비스 동작 세팅
        when(styleService.getStyleList()).thenReturn(styleListDto);

        // 스타일 전체 조회 get 요청 테스트 및 검증
        mockMvc.perform(get("/api/style")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // HTTP 상태 코드 검증
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value())) // 응답 상태 코드 검증
                .andExpect(jsonPath("$.response.styleList.size()").value(3)) // 응답 배열 크기 검증
                .andExpect(jsonPath("$.response.styleList[0].desc").value(style1.getDescription())) // 첫 번째 항목 검증
                .andExpect(jsonPath("$.response.styleList[1].mood").value(style2.getMood())) // 두 번째 항목 검증
                .andExpect(jsonPath("$.response.styleList[2].name").value(style3.getName())); // 세 번째 항목 검증
    }

    // 2. 빈 리스트 조회 -> 응답 상태 200, 결과로 빈 StyleListDto 반환
    @Test
    @DisplayName("스타일 전체 조회 테스트 - 빈 리스트 조회")
    public void getEmptyStyleListTest() throws Exception {
        // 빈 리스트를 가진 응답 객체 생성
        StyleListDto styleListDto = StyleListDto.of(new ArrayList<>());

        // 스타일 전체 조회 서비스 동작 세팅
        when(styleService.getStyleList()).thenReturn(styleListDto);

        // 스타일 전체 조회 get 요청 테스트 및 검증
        mockMvc.perform(get("/api/style")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // HTTP 상태 코드 검증
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value())) // 응답 상태 코드 검증
                .andExpect(jsonPath("$.response.styleList.size()").value(0)) // 응답 배열 크기 검증
                .andExpect(jsonPath("$.response.styleList").isArray()) // 배열인지 확인
                .andExpect(jsonPath("$.response.styleList").isEmpty()); // 빈 배열인지 확인
    }

    // 스타일 엔티티 생성 메서드
    private Style createStyleEntity(int n) {
        return Style.builder()
                .styleSeq((long)n)
                .name("style-name"+n)
                .mood("style-mood"+n)
                .description("style-desc"+n)
                .isRecommend('n')
                .contents("contents-test"+n)
                .useCnt(n)
                .build();
    }
}