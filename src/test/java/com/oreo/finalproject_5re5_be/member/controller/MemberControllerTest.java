package com.oreo.finalproject_5re5_be.member.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.oreo.finalproject_5re5_be.member.dto.request.MemberRegisterRequest;
import com.oreo.finalproject_5re5_be.member.dto.request.MemberTermRequest;
import com.oreo.finalproject_5re5_be.member.dto.response.MemberReadResponse;
import com.oreo.finalproject_5re5_be.member.entity.Member;
import com.oreo.finalproject_5re5_be.member.exception.MemberNotFoundException;
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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
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
        // 회원 가입에 필요한 데이터 생성
        List<MemberTermRequest> memberTerms = createMemberTerms();
        MemberRegisterRequest request = createMemberRegisterRequest(memberTerms);

        // 회원 엔티티 생성
        Member savedMember = Member.builder()
                .seq(1L)
                .id("qwerfde2312")
                .password("asdf12341234@")
                .email("asdf3214@gmail.com")
                .name("홍길동")
                .birthDate("1990-01-01")
                .memberRegDate(LocalDateTime.now())
                .chkValid('Y')
                .normAddr("서울시 강남구")
                .passAddr("서초대로 59-32")
                .locaAddr("서초동")
                .detailAddr("서초동 123-456")
                .build();

        // 서비스로 create 호출 시 savedMember 반환하게 세팅
        given(memberService.create(request)).willReturn(savedMember);

        // 컨트롤러로 요청 보내기
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
        // 회원 가입에 필요한 데이터 생성
        List<MemberTermRequest> memberTerms = createMemberTerms();
        MemberRegisterRequest request = createMemberRegisterRequest(memberTerms);
        // 잘못된 아이디로 설정
        request.setId("잘못된 아이디");
        // 기대하는 에러 메시지 설정
        String expectedErrorMessage = "데이터 입력 형식이 잘못되었습니다. 상세 내용은 다음과 같습니다.\n아이디는 6~20자의 영문 및 숫자만 허용됩니다.\n";

        // 컨트롤러로 요청 보내기
        mockMvc.perform(post("/api/member/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.content").value(expectedErrorMessage));
    }

    @DisplayName("회원 단순 조회 성공")
    @Test
    public void 회원_단순_조회_성공() throws Exception {
        // 회원 단순 조회에 필요한 데이터 생성
        MemberReadResponse memberReadResponse = MemberReadResponse.of("qwerfde2312", "qwedr123@gmail.com",
                                                                        "홍길동", "서울시 강남구",
                                                                    "서초대로 59-32");
        // 서비스 read 호출 시 MemberReadResponse 반환하게 세팅
        when(memberService.read("qwerfde2312")).thenReturn(memberReadResponse);

        // 컨트롤러로 요청 보내기
        mockMvc.perform(get("/api/member/read/qwerfde2312"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value("qwerfde2312"))
                .andExpect(jsonPath("$.email").value("qwedr123@gmail.com"))
                .andExpect(jsonPath("$.name").value("홍길동"))
                .andExpect(jsonPath("$.normAddr").value("서울시 강남구"))
                .andExpect(jsonPath("$.detailAddr").value("서초대로 59-32"));

    }

    @DisplayName("회원 단순 조회 실패")
    @Test
    public void 회원_단순_조회_실패() throws Exception {
        // 서비스 read 호출 시 null 반환하게 세팅
        MemberNotFoundException memberNotFoundException = new MemberNotFoundException();
        when(memberService.read("qwerfde2312")).thenThrow(memberNotFoundException);
        String expectedErrorMessage = "회원이 존재하지 않습니다.";

        // 컨트롤러로 요청 보내기
        mockMvc.perform(get("/api/member/read/qwerfde2312"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(expectedErrorMessage));;
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
        MemberRegisterRequest request = MemberRegisterRequest.builder()
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