package com.oreo.finalproject_5re5_be.member.service;

import static org.junit.jupiter.api.Assertions.*;

import com.oreo.finalproject_5re5_be.member.dto.request.MemberRegisterRequest;
import com.oreo.finalproject_5re5_be.member.dto.request.MemberTermRequest;
import com.oreo.finalproject_5re5_be.member.entity.Member;
import com.oreo.finalproject_5re5_be.member.entity.MemberState;
import com.oreo.finalproject_5re5_be.member.entity.MemberTermsHistory;
import com.oreo.finalproject_5re5_be.member.repository.MemberRepository;
import com.oreo.finalproject_5re5_be.member.repository.MemberStateRepository;
import com.oreo.finalproject_5re5_be.member.repository.MemberTermsHistoryRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties") // - 현재 에러가 발생함. h2와 MySQL의 pk 설정 차이
class MemberServiceImplSuccessTest {

    @Autowired
    private MemberServiceImpl userService;


    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberStateRepository memberStateRepository;
    @Autowired
    private MemberTermsHistoryRepository memberTermsHistoryRepository;


    @BeforeEach
    void setUp() {
        assertNotNull(userService);
    }

    // 1. 회원가입
    // - 성공
        // - 사용자로부터 정상적인 데이터를 입력받아서 회원가입이 완료되는 경우

    @DisplayName("회원가입 - 성공")
    @Test
    public void 회원가입_성공() {
        // 사용자로부터 유효성 검증이 완료된 회원 정보와 약관 동의 내용을 받음
        List<MemberTermRequest> memberTermRequests = retryableCreateMemberMemberTerms();
        MemberRegisterRequest request = retryableCreateMemberMemberRegisterRequest(memberTermRequests);

        // 회원가입 처리
        userService.RetryableCreateMember(request);

        // 정상처리 - 회원 정보, 회원 약관 내역, 회원 상태 등록 확인
        Member foundMember = memberRepository.findByEmail(request.getEmail());
        assertNotNull(foundMember);
        assertTrue(isSameMemberFields(foundMember, request));

        MemberTermsHistory foundMemberTermsHistory = memberTermsHistoryRepository.findByMemberSeq(foundMember.getSeq()).get(0);
        assertNotNull(foundMemberTermsHistory);
        assertTrue(isSameMemberTermsHistoryFields(foundMemberTermsHistory, memberTermRequests));

        MemberState foundMemberState = memberStateRepository.findByMemberSeq(foundMember.getSeq()).get(0);
        assertNotNull(foundMemberState);
        assertTrue(isSameMemberStateFields(foundMemberState));
    }




    private MemberRegisterRequest retryableCreateMemberMemberRegisterRequest(List<MemberTermRequest> memberTermRequests) {
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

    private List<MemberTermRequest> retryableCreateMemberMemberTerms() {
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

    private boolean isSameMemberTermsHistoryFields(MemberTermsHistory memberTermsHistory, List<MemberTermRequest> memberTermRequests) {
        // 약관 동의 내역 비교
        return memberTermsHistory.getChkTerm1().equals(memberTermRequests.get(0).getAgreed()) &&
                memberTermsHistory.getChkTerm2().equals(memberTermRequests.get(1).getAgreed()) &&
                memberTermsHistory.getChkTerm3().equals(memberTermRequests.get(2).getAgreed()) &&
                memberTermsHistory.getChkTerm4().equals(memberTermRequests.get(3).getAgreed()) &&
                memberTermsHistory.getChkTerm5().equals(memberTermRequests.get(4).getAgreed());
    }

    // 추후에 해당 부분 문제 해결 : 회원 상태 어떻게 비교할지 고민하기
    private boolean isSameMemberStateFields(MemberState memberState) {
        // 회원 상태 비교
        return true;
    }
}