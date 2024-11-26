package com.oreo.finalproject_5re5_be.member.config;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.oreo.finalproject_5re5_be.member.dto.request.MemberRegisterRequest;
import com.oreo.finalproject_5re5_be.member.dto.request.MemberTermCheckOrNotRequest;
import com.oreo.finalproject_5re5_be.member.entity.Member;
import com.oreo.finalproject_5re5_be.member.repository.MemberRepository;
import com.oreo.finalproject_5re5_be.member.service.MemberServiceImpl;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberSecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LoginAuthenticationSuccessHandler successHandler;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private MemberServiceImpl memberService;
    @MockBean
    private MemberRepository memberRepository;


    @BeforeEach
    void setUp() {
        assertNotNull(successHandler);
        assertNotNull(memberService);
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    void 로그인_성공() throws Exception {
        // 회원 약관 동의 정보 생성
        List<MemberTermCheckOrNotRequest> memberTermCheckOrNotRequests = retryableCreateMemberMemberTerms();
        // 회원 가입 정보 생성
        MemberRegisterRequest memberRegisterRequest = retryableCreateMemberMemberRegisterRequest(
                memberTermCheckOrNotRequests);
        // 가입 정보로 부터 회원 엔티티 생성
        Member foundMember = memberRegisterRequest.createMemberEntity();
        // 비밀번호 암호화 처리 후 저장
        String notEncodedPassword = foundMember.getPassword();
        String encodedPassword = passwordEncoder.encode(foundMember.getPassword());
        foundMember.setPassword(encodedPassword);

        // 반환할 UserDetails 객체 생성
        UserDetails foundMemberDetails = User.withUsername(foundMember.getId())
                                             .password(encodedPassword)
                                             .build();

        // 목객체 동작 지정
        when(memberRepository.findById(foundMember.getId())).thenReturn(foundMember); // 회원 조회시 가입 정보로 부터 만든 회원 엔티티 반환
        when(memberService.loadUserByUsername(foundMember.getId())).thenReturn(foundMemberDetails); // 회원 아이디(이름)으로 로드시 이전에 만들었던 UserDetails 반환

        // 로그인 요청
        mockMvc.perform(formLogin("/api/member/login")
                        .user(memberRegisterRequest.getId())
                        .password(memberRegisterRequest.getPassword()))  // 실제 인코딩 전 비밀번호 전달
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("로그인 실패 테스트, 로그인 실패시 로그인 페이지로 리디렉션")
    void 로그인_실패() throws Exception {
        // 회원 약관 동의 정보 생성
        List<MemberTermCheckOrNotRequest> memberTermCheckOrNotRequests = retryableCreateMemberMemberTerms();
        // 회원 가입 정보 생성
        MemberRegisterRequest memberRegisterRequest = retryableCreateMemberMemberRegisterRequest(
                memberTermCheckOrNotRequests);
        // 가입 정보로 부터 회원 엔티티 생성
        Member foundMember = memberRegisterRequest.createMemberEntity();
        // 비밀번호 암호화 처리 후 저장
        String encodedPassword = passwordEncoder.encode(foundMember.getPassword());
        foundMember.setPassword(encodedPassword);

        // 반환할 UserDetails 객체 생성
        UserDetails foundMemberDetails = User.withUsername(foundMember.getId())
                .password(encodedPassword)
                .build();

        // 목객체 동작 지정
        when(memberRepository.findById(foundMember.getId())).thenReturn(foundMember); // 회원 조회시 가입 정보로 부터 만든 회원 엔티티 반환
        when(memberService.loadUserByUsername(foundMember.getId())).thenReturn(foundMemberDetails); // 회원 아이디(이름)으로 로드시 이전에 만들었던 UserDetails 반환

        // 로그인 요청
        mockMvc.perform(formLogin("/api/member/login")
                        .user(memberRegisterRequest.getId())
                        .password("wrong password"))
                .andExpect(status().is4xxClientError());
    }

    // 인증되지 않은 비회원이 보호된 페이지에 접근하면 401 Unauthorized 응답 반환된다.
    @Test
    @DisplayName("인증되지 않은 비회원이 보호된 페이지에 접근하면 401 Unauthorized 응답 반환")
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


    // 로그아웃할 경우, 세션 무효화
    @Test
    @DisplayName("로그아웃 시 세션이 무효화되고 홈으로 리디렉션된다.")
    void 로그아웃() throws Exception {
        mockMvc.perform(get("/api/member/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    private MemberRegisterRequest retryableCreateMemberMemberRegisterRequest(
            List<MemberTermCheckOrNotRequest> memberTermCheckOrNotRequests) {
        var request = MemberRegisterRequest.builder()
                .id("qwerfde2312")
                .password("asdf12341234@")
                .email("asdf3214@gmail.com")
                .name("홍길동")
                .userRegDate(LocalDateTime.now())
                .chkValid('Y')
                .memberTermCheckOrNotRequests(memberTermCheckOrNotRequests)
                .normAddr("서울시 강남구")
                .passAddr("서초대로 59-32")
                .locaAddr("서초동")
                .detailAddr("서초동 123-456")
                .build();

        return request;
    }

    private List<MemberTermCheckOrNotRequest> retryableCreateMemberMemberTerms() {
        List<MemberTermCheckOrNotRequest> memberTermCheckOrNotRequests = new ArrayList<>();
        // 약관 동의 내용 설정
        memberTermCheckOrNotRequests = new ArrayList<>();
        memberTermCheckOrNotRequests.add(
                MemberTermCheckOrNotRequest.builder()
                        .termCondCode(1L)
                        .agreed('Y')
                        .build());

        memberTermCheckOrNotRequests.add(
                MemberTermCheckOrNotRequest.builder()
                        .termCondCode(2L)
                        .agreed('Y')
                        .build());

        memberTermCheckOrNotRequests.add(
                MemberTermCheckOrNotRequest.builder()
                        .termCondCode(3L)
                        .agreed('Y')
                        .build());

        memberTermCheckOrNotRequests.add(
                MemberTermCheckOrNotRequest.builder()
                        .termCondCode(4L)
                        .agreed('N')
                        .build());

        memberTermCheckOrNotRequests.add(
                MemberTermCheckOrNotRequest.builder()
                        .termCondCode(5L)
                        .agreed('N')
                        .build());

        return memberTermCheckOrNotRequests;
    }
}