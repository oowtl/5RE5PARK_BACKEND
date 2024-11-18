package com.oreo.finalproject_5re5_be.member.service;

import static org.junit.jupiter.api.Assertions.*;

import com.oreo.finalproject_5re5_be.member.dto.request.MemberRegisterRequest;
import com.oreo.finalproject_5re5_be.member.dto.request.MemberTermCheckOrNotRequest;
import com.oreo.finalproject_5re5_be.member.entity.Member;
import com.oreo.finalproject_5re5_be.member.entity.MemberState;
import com.oreo.finalproject_5re5_be.member.entity.MemberTermsHistory;
import com.oreo.finalproject_5re5_be.member.repository.MemberRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties") // - 현재 에러가 발생함. h2와 MySQL의 pk 설정 차이
class MemberServiceImplTest {

    @Autowired
    private MemberServiceImpl userService;

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private MemberRepository memberRepository;


    @BeforeEach
    void setUp() {
        assertNotNull(userService);
        assertNotNull(mailSender);
    }

    // 1. 회원가입 성공 테스트
    @DisplayName("회원가입 - 성공")
    @Test
    public void 회원가입_성공() {
        // 사용자로부터 유효성 검증이 완료된 회원 정보와 약관 동의 내용을 받음
        List<MemberTermCheckOrNotRequest> memberTermCheckOrNotRequests = retryableCreateMemberMemberTerms();
        MemberRegisterRequest request = retryableCreateMemberMemberRegisterRequest(
                memberTermCheckOrNotRequests);

        // 회원가입 처리
        userService.create(request);

        // 정상처리 - 회원 정보, 회원 약관 내역, 회원 상태 등록 확인
        Member foundMember = memberRepository.findByEmail(request.getEmail());
        assertNotNull(foundMember);
        assertTrue(isSameMemberFields(foundMember, request));

        // 아래 부분도 회원 약관과 상태 개발 구현 완료되면 주석 해제
//        MemberTermsHistory foundMemberTermsHistory = memberTermsHistoryRepository.findByMemberSeq(foundMember.getSeq()).get(0);
//        assertNotNull(foundMemberTermsHistory);
//        assertTrue(isSameMemberTermsHistoryFields(foundMemberTermsHistory, memberTermRequests));
//
//        MemberState foundMemberState = memberStateRepository.findByMemberSeq(foundMember.getSeq()).get(0);
//        assertNotNull(foundMemberState);
//        assertTrue(isSameMemberStateFields(foundMemberState));
    }



    private MemberRegisterRequest retryableCreateMemberMemberRegisterRequest(List<MemberTermCheckOrNotRequest> memberTermCheckOrNotRequests) {
        var request = MemberRegisterRequest.builder()
                .id("qwerfde2312")
                .password("asdf12341234@")
                .email("asdf3214@gmail.com")
                .name("홍길동")
                .birthDate("1990-01-01")
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
                        .isMandatory(true)
                        .build());

        memberTermCheckOrNotRequests.add(
                MemberTermCheckOrNotRequest.builder()
                        .termCondCode(2L)
                        .agreed('Y')
                        .isMandatory(true)
                        .build());

        memberTermCheckOrNotRequests.add(
                MemberTermCheckOrNotRequest.builder()
                        .termCondCode(3L)
                        .agreed('Y')
                        .isMandatory(true)
                        .build());

        memberTermCheckOrNotRequests.add(
                MemberTermCheckOrNotRequest.builder()
                        .termCondCode(4L)
                        .agreed('N')
                        .isMandatory(false)
                        .build());

        memberTermCheckOrNotRequests.add(
                MemberTermCheckOrNotRequest.builder()
                        .termCondCode(5L)
                        .agreed('N')
                        .isMandatory(false)
                        .build());

        return memberTermCheckOrNotRequests;
    }

    private boolean isSameMemberFields(Member member, MemberRegisterRequest request) {
        // 아이디, 이름, 이메일 등 회원 정보 비교
        return member.getId().equals(request.getId()) &&
                member.getEmail().equals(request.getEmail()) &&
                member.getName().equals(request.getName()) &&
                member.getNormAddr().equals(request.getNormAddr()) &&
                member.getBirthDate().equals(request.getBirthDate()) &&
                member.getLocaAddr().equals(request.getLocaAddr()) &&
                member.getDetailAddr().equals(request.getDetailAddr()) &&
                member.getPassAddr().equals(request.getPassAddr());
    }

    private boolean isSameMemberTermsHistoryFields(MemberTermsHistory memberTermsHistory, List<MemberTermCheckOrNotRequest> memberTermCheckOrNotRequests) {
        // 약관 동의 내역 비교
        return memberTermsHistory.getChkTerm1().equals(memberTermCheckOrNotRequests.get(0).getAgreed()) &&
                memberTermsHistory.getChkTerm2().equals(memberTermCheckOrNotRequests.get(1).getAgreed()) &&
                memberTermsHistory.getChkTerm3().equals(memberTermCheckOrNotRequests.get(2).getAgreed()) &&
                memberTermsHistory.getChkTerm4().equals(memberTermCheckOrNotRequests.get(3).getAgreed()) &&
                memberTermsHistory.getChkTerm5().equals(memberTermCheckOrNotRequests.get(4).getAgreed());
    }

    // 추후에 해당 부분 문제 해결 : 회원 상태 어떻게 비교할지 고민하기
    private boolean isSameMemberStateFields(MemberState memberState) {
        // 회원 상태 비교
        return true;
    }
}