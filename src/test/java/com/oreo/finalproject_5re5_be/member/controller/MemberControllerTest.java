package com.oreo.finalproject_5re5_be.member.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.oreo.finalproject_5re5_be.member.service.MemberServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MemberController.class)
class MemberControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberServiceImpl memberService;

    @DisplayName("회원 가입 처리 성공")
    @Test
    public void 회원가입_성공() {
    }

    @DisplayName("유효성 검증 실패로 인한 회원가입 실패")
    @Test
    public void 유효성_검증_실패_회원가입_실패() {

    }




}