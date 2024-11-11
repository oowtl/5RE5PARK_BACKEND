package com.oreo.finalproject_5re5_be.member.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.oreo.finalproject_5re5_be.member.dto.request.MemberRegisterRequest;
import com.oreo.finalproject_5re5_be.member.dto.request.MemberTermRequest;
import com.oreo.finalproject_5re5_be.member.entity.Member;
import com.oreo.finalproject_5re5_be.member.entity.MemberState;
import com.oreo.finalproject_5re5_be.member.entity.MemberTermsHistory;
import com.oreo.finalproject_5re5_be.member.exception.MemberDuplicatedEmailException;
import com.oreo.finalproject_5re5_be.member.exception.MemberDuplicatedIdException;
import com.oreo.finalproject_5re5_be.member.exception.MemberMandatoryTermNotAgreedException;
import com.oreo.finalproject_5re5_be.member.exception.MemberWrongCountTermCondition;
import com.oreo.finalproject_5re5_be.member.repository.MemberRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.TestPropertySource;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@EnableRetry
class MemberServiceImplFailTest {

    @Autowired
    private MemberServiceImpl memberService;

    @MockBean
    private MemberRepository memberRepository;

    private User user;

    @BeforeEach
    void setUp() {
        assertNotNull(memberService);
        user = new User("qwefghnm1212", "dqwesf1212@!", Collections.emptyList());
    }


    @DisplayName("회원가입 - 중복된 이메일")
    @Test
    public void 중복된_이메일_예외_발생() {
        List<MemberTermRequest> memberTermRequests = retryableCreateMemberMemberTerms();
        MemberRegisterRequest request = retryableCreateMemberMemberRegisterRequest(memberTermRequests);
        Member member = request.createMemberEntity();
        when(memberRepository.findByEmail(request.getEmail())).thenReturn(member);
        assertThrows(MemberDuplicatedEmailException.class, () -> memberService.create(request));
    }

    @DisplayName("회원가입 - 중복된 아이디")
    @Test
    public void 중복된_아이디_예외_발생() {
        List<MemberTermRequest> memberTermRequests = retryableCreateMemberMemberTerms();
        MemberRegisterRequest request = retryableCreateMemberMemberRegisterRequest(memberTermRequests);
        Member member = request.createMemberEntity();
        when(memberRepository.findById(request.getId())).thenReturn(member);
        assertThrows(MemberDuplicatedIdException.class, () -> memberService.create(request));
    }

    @DisplayName("회원가입 - 필수 약관 미동의")
    @Test
    public void 필수_약관_미동의_예외_발생() {
        List<MemberTermRequest> memberTermRequests = retryableCreateMemberMemberTerms();
        memberTermRequests.get(0).setAgreed('N');
        MemberRegisterRequest request = retryableCreateMemberMemberRegisterRequest(memberTermRequests);
        Member member = request.createMemberEntity();
        assertThrows(MemberMandatoryTermNotAgreedException.class, () -> memberService.create(request));
    }

    @DisplayName("회원가입 - 5개의 약관 항목 보다 많은 경우")
    @Test
    public void 약관_항목_초과_예외_발생() {
        List<MemberTermRequest> memberTermRequests = retryableCreateMemberMemberTerms();
        memberTermRequests.add(MemberTermRequest.builder()
                        .termCondCode(6L)
                        .agreed('Y')
                        .isMandatory(true)
                        .build());
        MemberRegisterRequest request = retryableCreateMemberMemberRegisterRequest(memberTermRequests);
        Member member = request.createMemberEntity();
        assertThrows(MemberWrongCountTermCondition.class, () -> memberService.create(request));
    }

    // 추후에 해당 부분 문제 해결 : RetryFailedException 발생함. 이거 어떻게 잡을지 고민하기
    @DisplayName("재시도 복구 로직 정상적으로 동작하는지 테스트")
    @Test
    public void 재시도_복구_동작_테스트() {

    }

    @DisplayName("스프링시큐리티에서 호출하는 loadUserByUsername() 성공 테스트 ")
    @Test
    public void 스프링시큐리티_회원_조회_성공() {
        List<MemberTermRequest> memberTermRequests = retryableCreateMemberMemberTerms();
        MemberRegisterRequest memberRegisterRequest = retryableCreateMemberMemberRegisterRequest(
                memberTermRequests);
        Member foundMember = memberRegisterRequest.createMemberEntity();
        when(memberRepository.findById("qwerfde2312")).thenReturn(foundMember);
        UserDetails foundMemberDetails = memberService.loadUserByUsername("qwerfde2312");
        assertEquals(foundMemberDetails.getUsername(), foundMember.getId());
    }

    @DisplayName("스프링시큐리티에서 호출하는 loadUserByUsername() 실패 테스트 ")
    @Test
    public void 스프링시큐리티_회원_조회_실패() {
        List<MemberTermRequest> memberTermRequests = retryableCreateMemberMemberTerms();
        MemberRegisterRequest memberRegisterRequest = retryableCreateMemberMemberRegisterRequest(
                memberTermRequests);
        Member foundMember = memberRegisterRequest.createMemberEntity();
        when(memberRepository.findById("qwerfde2312")).thenReturn(null);
        assertThrows(UsernameNotFoundException.class,
                () -> memberService.loadUserByUsername("qwerfde2312"));
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