package com.oreo.finalproject_5re5_be.member.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.oreo.finalproject_5re5_be.member.dto.request.MemberRegisterRequest;
import com.oreo.finalproject_5re5_be.member.dto.request.MemberTermRequest;
import com.oreo.finalproject_5re5_be.member.dto.response.MemberRegisterResponse;
import com.oreo.finalproject_5re5_be.member.entity.Member;
import com.oreo.finalproject_5re5_be.member.service.MemberServiceImpl;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberServiceImpl memberService;


    @DisplayName("회원 가입 처리 성공")
    @Test
    public void 회원가입_성공() throws Exception {
        // Given
        List<MemberTermRequest> memberTerms = createMemberTerms();
        MemberRegisterRequest request = createMemberRegisterRequest(memberTerms);
        MemberRegisterResponse response = MemberRegisterResponse.of("회원가입이 완료되었습니다");

        // Mock memberService 호출
        Member savedMember = Member.builder()
                .seq(1L)
                .id("qwerfde2312")
                .password("asdf12341234@")
                .email("asdf3214@gmail.com")
                .name("홍길동")
                .birthDate("1990-01-01")
                .userRegDate(LocalDateTime.now())
                .chkValid('Y')
                .normAddr("서울시 강남구")
                .passAddr("서초대로 59-32")
                .locaAddr("서초동")
                .detailAddr("서초동 123-456")
                .build();

        given(memberService.RetryableCreateMember(request)).willReturn(savedMember);

        // When & Then
        mockMvc.perform(post("/api/member/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("회원가입이 완료되었습니다"));


    }


    @DisplayName("유효성 검증 실패로 인한 회원가입 실패")
    @Test
    public void 유효성_검증_실패_회원가입_실패() throws Exception {
        // Given
        List<MemberTermRequest> memberTerms = createMemberTerms();
        MemberRegisterRequest request = createMemberRegisterRequest(memberTerms);
        request.setId("잘못된 아이디");
        String expectedErrorMessage = "데이터 입력 형식이 잘못되었습니다. 상세 내용은 다음과 같습니다.\n아이디는 6~20자의 영문 및 숫자만 허용됩니다.\n";


        given(memberService.RetryableCreateMember(request)).willReturn(null);

        // When & Then
        mockMvc.perform(post("/api/member/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.content").value(expectedErrorMessage));
    }


    private List<MemberTermRequest> createMemberTerms() {
        List<MemberTermRequest> memberTermRequests = new ArrayList<>();
        // 약관 동의 내용 설정
        memberTermRequests = new ArrayList<>();
        memberTermRequests.add(
                MemberTermRequest.builder()
                        .termCondCode(1L)
                        .agreed('Y')
                        .isMandatory(true)
                        .build());

        memberTermRequests.add(
                MemberTermRequest.builder()
                        .termCondCode(2L)
                        .agreed('Y')
                        .isMandatory(true)
                        .build());

        memberTermRequests.add(
                MemberTermRequest.builder()
                        .termCondCode(3L)
                        .agreed('Y')
                        .isMandatory(true)
                        .build());

        memberTermRequests.add(
                MemberTermRequest.builder()
                        .termCondCode(4L)
                        .agreed('N')
                        .isMandatory(false)
                        .build());

        memberTermRequests.add(
                MemberTermRequest.builder()
                        .termCondCode(5L)
                        .agreed('N')
                        .isMandatory(false)
                        .build());

        return memberTermRequests;
    }

    private MemberRegisterRequest createMemberRegisterRequest(
            List<MemberTermRequest> memberTermRequests) {
        var request = MemberRegisterRequest.builder()
                .id("qwerfde2312")
                .password("asdf12341234@")
                .email("asdf3214@gmail.com")
                .name("홍길동")
                .birthDate("1990-01-01")
                .userRegDate(LocalDateTime.now())
                .chkValid('Y')
                .memberTermRequests(memberTermRequests)
                .normAddr("서울시 강남구")
                .passAddr("서초대로 59-32")
                .locaAddr("서초동")
                .detailAddr("서초동 123-456")
                .build();

        return request;
    }


}