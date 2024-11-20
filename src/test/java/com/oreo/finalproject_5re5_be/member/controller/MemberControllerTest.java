package com.oreo.finalproject_5re5_be.member.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.oreo.finalproject_5re5_be.member.dto.request.MemberRegisterRequest;
import com.oreo.finalproject_5re5_be.member.dto.request.MemberRemoveRequest;
import com.oreo.finalproject_5re5_be.member.dto.request.MemberTermCheckOrNotRequest;
import com.oreo.finalproject_5re5_be.member.dto.request.MemberUpdateRequest;
import com.oreo.finalproject_5re5_be.member.dto.response.MemberReadResponse;
import com.oreo.finalproject_5re5_be.member.entity.Member;
import com.oreo.finalproject_5re5_be.member.exception.MemberNotFoundException;
import com.oreo.finalproject_5re5_be.member.service.MemberServiceImpl;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberServiceImpl memberService;


    @DisplayName("회원 가입 처리 성공")
    @Test
    @WithMockUser
    public void 회원가입_성공() throws Exception {
        // 회원 가입에 필요한 데이터 생성
        List<MemberTermCheckOrNotRequest> memberTerms = createMemberTerms();
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
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("회원가입이 완료되었습니다"));


    }


    @DisplayName("유효성 검증 실패로 인한 회원가입 실패")
    @Test
    @WithMockUser
    public void 유효성_검증_실패_회원가입_실패() throws Exception {
        // 회원 가입에 필요한 데이터 생성
        List<MemberTermCheckOrNotRequest> memberTerms = createMemberTerms();
        MemberRegisterRequest request = createMemberRegisterRequest(memberTerms);
        // 잘못된 아이디로 설정
        request.setId("잘못된 아이디");

        // 컨트롤러로 요청 보내기
        mockMvc.perform(post("/api/member/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf())
                )
                .andExpect(status().isBadRequest());
    }
//
//    @DisplayName("회원 단순 조회 성공")
//    @Test
//    @WithMockUser
//    public void 회원_단순_조회_성공() throws Exception {
//        // 회원 단순 조회에 필요한 데이터 생성
//        MemberReadResponse memberReadResponse = MemberReadResponse.of("qwerfde2312", "qwedr123@gmail.com",
//                "홍길동", "서울시 강남구",
//                "서초대로 59-32");
//        // 서비스 read 호출 시 MemberReadResponse 반환하게 세팅
//        when(memberService.read("qwerfde2312")).thenReturn(memberReadResponse);
//
//        // 컨트롤러로 요청 보내기
//        mockMvc.perform(get("/api/member/read/qwerfde2312"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.memberId").value("qwerfde2312"))
//                .andExpect(jsonPath("$.email").value("qwedr123@gmail.com"))
//                .andExpect(jsonPath("$.name").value("홍길동"))
//                .andExpect(jsonPath("$.normAddr").value("서울시 강남구"))
//                .andExpect(jsonPath("$.detailAddr").value("서초대로 59-32"));
//
//    }
//
//    @DisplayName("회원 단순 조회 실패")
//    @Test
//    @WithMockUser
//    public void 회원_단순_조회_실패() throws Exception {
//        // 서비스 read 호출 시 null 반환하게 세팅
//        MemberNotFoundException memberNotFoundException = new MemberNotFoundException();
//        when(memberService.read("qwerfde2312")).thenThrow(memberNotFoundException);
//
//        // 컨트롤러로 요청 보내기
//        mockMvc.perform(get("/api/member/read/qwerfde2312"))
//                .andExpect(status().isNotFound());
//    }

    @DisplayName("회원 수정 처리 성공")
    @Test
    @WithMockUser
    public void 회원_수정_처리() throws Exception {
        // 서비스에서 update() 호출 시 정상 처리되게 목킹
        Long memberSeq = 1L;
        MemberUpdateRequest request = MemberUpdateRequest.builder()
                .id("newdwads23123")
                .password("dwadaw123212!!!")
                .email("eqwfqws2131@gmail.com")
                .name("홍만동")
                .normAddr("서울시 양천구")
                .build();

        doNothing().when(memberService).update(memberSeq, request);

        // 컨트롤러에 요청 보내기
        mockMvc.perform(patch("/api/member/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(csrf())
        )
                .andExpect(status().isNoContent());

    }

    @DisplayName("회원 삭제 처리 성공")
    @Test
    @WithMockUser
    void 삭제_처리() throws Exception {
        // 서비스에서 remove() 호출시 정상 처리되게 목킹
        Long memberSeq = 1L;
        MemberRemoveRequest request = MemberRemoveRequest.builder()
                .code("MBD001") // 서비스 이용 불만족
                .detailCont("해당 서비스가 제가 생각했던 서비스가 아니네요. 실망했습니다")
                .build();

        // 서비스 목킹
        doNothing().when(memberService).remove(memberSeq, request);

        // 컨트롤러에 요청 보내기
        mockMvc.perform(delete("/api/member/" + memberSeq)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(csrf())
        )
                .andExpect(status().isNoContent());


    }

    private List<MemberTermCheckOrNotRequest> createMemberTerms() {
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

    private MemberRegisterRequest createMemberRegisterRequest(
            List<MemberTermCheckOrNotRequest> memberTermCheckOrNotRequests) {
        MemberRegisterRequest request = MemberRegisterRequest.builder()
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


}