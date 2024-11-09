package com.oreo.finalproject_5re5_be.member.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.oreo.finalproject_5re5_be.member.dto.request.MemberRegisterRequest;
import com.oreo.finalproject_5re5_be.member.dto.request.MemberTerm;
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
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplFailTest {

    @InjectMocks
    private UserServiceImpl memberService;

    @Mock
    private MemberRepository memberRepository;


    @BeforeEach
    void setUp() {
        assertNotNull(memberService);
    }


    @DisplayName("회원가입 - 중복된 이메일")
    @Test
    public void 중복된_이메일_예외_발생() {
        List<MemberTerm> memberTerms = createMemberTerms();
        MemberRegisterRequest request = createMemberRegisterRequest(memberTerms);
        Member member = request.createMemberEntity();
        when(memberRepository.findByEmail(request.getEmail())).thenReturn(member);
        assertThrows(MemberDuplicatedEmailException.class, () -> memberService.create(request));
    }

    @DisplayName("회원가입 - 중복된 아이디")
    @Test
    public void 중복된_아이디_예외_발생() {
        List<MemberTerm> memberTerms = createMemberTerms();
        MemberRegisterRequest request = createMemberRegisterRequest(memberTerms);
        Member member = request.createMemberEntity();
        when(memberRepository.findById(request.getId())).thenReturn(member);
        assertThrows(MemberDuplicatedIdException.class, () -> memberService.create(request));
    }

    @DisplayName("회원가입 - 필수 약관 미동의")
    @Test
    public void 필수_약관_미동의_예외_발생() {
        List<MemberTerm> memberTerms = createMemberTerms();
        memberTerms.get(0).setAgreed('N');
        MemberRegisterRequest request = createMemberRegisterRequest(memberTerms);
        Member member = request.createMemberEntity();
        assertThrows(MemberMandatoryTermNotAgreedException.class, () -> memberService.create(request));
    }

    @DisplayName("회원가입 - 5개의 약관 항목 보다 많은 경우")
    @Test
    public void 약관_항목_초과_예외_발생() {
        List<MemberTerm> memberTerms = createMemberTerms();
        memberTerms.add(MemberTerm.builder()
                        .termCondCode(6L)
                        .agreed('Y')
                        .isMandatory(true)
                        .build());
        MemberRegisterRequest request = createMemberRegisterRequest(memberTerms);
        Member member = request.createMemberEntity();
        assertThrows(MemberWrongCountTermCondition.class, () -> request.createMemberTermsHistoryEntity(member));
    }



    private MemberRegisterRequest createMemberRegisterRequest(List<MemberTerm> memberTerms) {
        var request = MemberRegisterRequest.builder()
                .id("qwerfde2312")
                .password("asdf12341234@")
                .email("asdf3214@gmail.com")
                .name("홍길동")
                .birthDate("1990-01-01")
                .userRegDate(LocalDateTime.now())
                .chkValid('Y')
                .memberTerms(memberTerms)
                .normAddr("서울시 강남구")
                .passAddr("서초대로 59-32")
                .locaAddr("서초동")
                .detailAddr("서초동 123-456")
                .build();

        return request;
    }

    private List<MemberTerm> createMemberTerms() {
        List<MemberTerm> memberTerms = new ArrayList<>();
        // 약관 동의 내용 설정
        memberTerms = new ArrayList<>();
        memberTerms.add(
                MemberTerm.builder()
                        .termCondCode(1L)
                        .agreed('Y')
                        .isMandatory(true)
                        .build());

        memberTerms.add(
                MemberTerm.builder()
                        .termCondCode(2L)
                        .agreed('Y')
                        .isMandatory(true)
                        .build());

        memberTerms.add(
                MemberTerm.builder()
                        .termCondCode(3L)
                        .agreed('Y')
                        .isMandatory(true)
                        .build());

        memberTerms.add(
                MemberTerm.builder()
                        .termCondCode(4L)
                        .agreed('N')
                        .isMandatory(false)
                        .build());

        memberTerms.add(
                MemberTerm.builder()
                        .termCondCode(5L)
                        .agreed('N')
                        .isMandatory(false)
                        .build());

        return memberTerms;
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

    private boolean isSameMemberTermsHistoryFields(MemberTermsHistory memberTermsHistory, List<MemberTerm> memberTerms) {
        // 약관 동의 내역 비교
        return memberTermsHistory.getChkTerm1().equals(memberTerms.get(0).getAgreed()) &&
                memberTermsHistory.getChkTerm2().equals(memberTerms.get(1).getAgreed()) &&
                memberTermsHistory.getChkTerm3().equals(memberTerms.get(2).getAgreed()) &&
                memberTermsHistory.getChkTerm4().equals(memberTerms.get(3).getAgreed()) &&
                memberTermsHistory.getChkTerm5().equals(memberTerms.get(4).getAgreed());
    }

    private boolean isSameMemberStateFields(MemberState memberState) {
        // 회원 상태 비교
        return true;
    }
}