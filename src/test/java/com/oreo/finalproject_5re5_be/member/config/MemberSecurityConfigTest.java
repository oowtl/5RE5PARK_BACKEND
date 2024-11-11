package com.oreo.finalproject_5re5_be.member.config;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class MemberSecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LoginAuthenticationSuccessHandler successHandler;


    @BeforeEach
    void setUp() {
        assertNotNull(successHandler);
    }

    // 인증되지 않은 비회원이 보호된 페이지에 접근하면 401 Unauthorized 응답 반환된다.
    @Test
    @DisplayName("")
    void 비회원_인증_요구_페이지_접근() throws Exception {
        // 로그인 처리
        // 리소스 요청. 추후에 project/** 경로로 요청하기
    }

    // 인증된 회원이 보호된 페이지에 접근하면 200 OK 응답 반환
    @Test
    @DisplayName("인증된 회원이 보호된 페이지에 접근하면 200 OK 응답 반환 ")
    void 회원_인증_요구_페이지_접근() throws Exception {
        // 로그인 처리
        // 리소스 요청. 추후에 project/** 경로로 요청하기
    }

    // 로그인 페이지 요청 시 200 OK 응답 반환
    @Test
    @DisplayName("로그인 페이지 요청 시 200 OK 응답 반환")
    void 로그인_페이지_요청() throws Exception {
        // 프론트엔드 로그인 페이지 요청 경로 정해지면 테스트하기
    }

    // 로그인 실패시 로그인 페이지로 리디렉션
    @Test
    @DisplayName("로그인 실패 시 로그인 페이지로 리디렉션된다.")
    void 로그인_실패_로그인_페이지_리다이렉션() throws Exception {
        // 프론트엔드 로그인 페이지 요청 경로 정해지면 테스트하기
    }

    // 로그아웃할 경우, 세션 무효화
    @Test
    @DisplayName("로그아웃 시 세션이 무효화되고 홈으로 리디렉션된다.")
    void 로그아웃() throws Exception {
        mockMvc.perform(get("/api/member/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }
}