package com.oreo.finalproject_5re5_be.member.service;

import static org.junit.jupiter.api.Assertions.*;

import com.oreo.finalproject_5re5_be.member.dto.request.MemberRegisterRequest;
import com.oreo.finalproject_5re5_be.member.dto.request.MemberTermCheckOrNotRequest;
import com.oreo.finalproject_5re5_be.member.dto.request.MemberTermRequest;
import com.oreo.finalproject_5re5_be.member.entity.Code;
import com.oreo.finalproject_5re5_be.member.entity.Member;
import com.oreo.finalproject_5re5_be.member.entity.MemberState;
import com.oreo.finalproject_5re5_be.member.entity.MemberTerms;
import com.oreo.finalproject_5re5_be.member.entity.MemberTermsCondition;
import com.oreo.finalproject_5re5_be.member.entity.MemberTermsHistory;
import com.oreo.finalproject_5re5_be.member.repository.CodeRepository;
import com.oreo.finalproject_5re5_be.member.repository.MemberRepository;
import com.oreo.finalproject_5re5_be.member.repository.MemberStateRepository;
import com.oreo.finalproject_5re5_be.member.repository.MemberTermConditionRepository;
import com.oreo.finalproject_5re5_be.member.repository.MemberTermsHistoryRepository;
import com.oreo.finalproject_5re5_be.member.repository.MemberTermsRepository;
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
    private MemberServiceImpl memberService;

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberTermsHistoryRepository memberTermsHistoryRepository;

    @Autowired
    private MemberStateRepository memberStateRepository;

    @Autowired
    private MemberTermConditionRepository memberTermsConditionRepository;

    @Autowired
    private CodeRepository codeRepository;

    // 회원 가입 요청 데이터
    private MemberRegisterRequest memberRegisterRequest;

    // 코드 더미 데이터
    private List<Code> codeDummy = new ArrayList<>();

    // 회원 약관 항목 더미
    private List<MemberTermsCondition> memberTermsConditionDummy = new ArrayList<>();
    @Autowired
    private MemberTermsRepository memberTermsRepository;

    // 회원 상태 목 데이터

    // 회원 약관 목 데이터



    @BeforeEach
    void setUp() {
        // 자동 주입 확인
        assertNotNull(memberService);
        assertNotNull(mailSender);
        assertNotNull(memberRepository);
        assertNotNull(memberTermsHistoryRepository);
        assertNotNull(memberStateRepository);
        assertNotNull(memberTermsConditionRepository);
        assertNotNull(codeRepository);


        // 초기화
        // 더미 데이터 초기화
        codeDummy.clear();
        memberTermsConditionDummy.clear();


        // 데이터 베이스 초기화
        memberRepository.deleteAll();
        memberTermsHistoryRepository.deleteAll();
        memberStateRepository.deleteAll();
        memberTermsRepository.deleteAll();
        memberTermsConditionRepository.deleteAll();
        codeRepository.deleteAll();

    }

    // 1. 회원가입 성공 테스트
    @DisplayName("회원가입 - 성공")
    @Test
    public void 회원가입_성공() {
        // 회원 가입에 필요한 더미 데이터 생성
        // -1. 유효성 검증이 완료된 회원 가입 요청 데이터 생성
        List<MemberTermCheckOrNotRequest> terms = createMemberTermsRequest(); // 회원 약관 동의 내역 관련 더미
        memberRegisterRequest = createMemberRegisterRequest(terms); // 회원 가입 요청 데이터 생성

        // -2. 회원 약관 항목 최소 10개
        createMemberTermsRequestCondition();

        // -3. 위의 약관 항목들로 구성된 약관 1개
        createMemberTerm();

        // -4. 회원 상태 1개 - 신규등록회원 : MBS001
        createCodes();

        // 회원 가입 요청
        Member savedMember = memberService.create(memberRegisterRequest);

        // 기대 결과 비교 - 회원 정보, 회원 약관 내역, 회원 상태 조회
        System.out.println("savedMember = " + savedMember);

        // 회원 상태 조회
        memberStateRepository.findByMemberSeq(savedMember.getSeq()).forEach(e -> {
            System.out.println("memberState = " + e);
        });

        // 회원 약관 내역 조회
        memberTermsHistoryRepository.findByMemberSeq(savedMember.getSeq()).forEach(e -> {
            System.out.println("memberTermsHistory = " + e);
        });

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

    private void createMemberTermsRequestCondition() {
        memberTermsConditionDummy.add(MemberTermsCondition.builder()
                                .condCode("MTC001")
                                .name("서비스 이용약관")
                                .shortCont("서비스 이용에 관한 짧은 내용")
                                .longCont("서비스 이용에 관한 자세한 내용")
                                .chkUse('Y')
                                .ord(1)
                                .termCondDate(LocalDateTime.now())
                                .termCondUpDate(LocalDateTime.now())
                                .law1("개인정보보호법")
                                .law2("전자상거래법")
                                .law3("소비자보호법")
                                .build());

        memberTermsConditionDummy.add(MemberTermsCondition.builder()
                                .condCode("MTC002")
                                .name("개인정보 수집 및 이용")
                                .shortCont("개인정보 수집에 대한 짧은 설명")
                                .longCont("개인정보 수집 및 이용에 대한 자세한 설명")
                                .chkUse('Y')
                                .ord(2)
                                .termCondDate(LocalDateTime.now())
                                .termCondUpDate(LocalDateTime.now())
                                .law1("정보통신망법")
                                .law2("개인정보보호법")
                                .law3("없음")
                                .build());

        memberTermsConditionDummy.add(MemberTermsCondition.builder()
                                .condCode("MTC003")
                                .name("위치정보 이용 약관")
                                .shortCont("위치 정보 이용에 대한 짧은 설명")
                                .longCont("위치 정보 이용에 대한 자세한 설명")
                                .chkUse('Y')
                                .ord(3)
                                .termCondDate(LocalDateTime.now())
                                .termCondUpDate(LocalDateTime.now())
                                .law1("위치정보법")
                                .law2("없음")
                                .law3("없음")
                                .build());

        memberTermsConditionDummy.add(MemberTermsCondition.builder()
                                .condCode("MTC004")
                                .name("쿠키 사용 약관")
                                .shortCont("쿠키 사용에 대한 짧은 설명")
                                .longCont("쿠키 사용에 대한 자세한 설명")
                                .chkUse('Y')
                                .ord(4)
                                .termCondDate(LocalDateTime.now())
                                .termCondUpDate(LocalDateTime.now())
                                .law1("정보통신망법")
                                .law2("개인정보보호법")
                                .law3("없음")
                                .build());

        memberTermsConditionDummy.add(MemberTermsCondition.builder()
                                .condCode("MTC005")
                                .name("마케팅 정보 수신 동의")
                                .shortCont("마케팅 정보 수신에 대한 짧은 설명")
                                .longCont("마케팅 정보 수신에 대한 자세한 설명")
                                .chkUse('Y')
                                .ord(5)
                                .termCondDate(LocalDateTime.now())
                                .termCondUpDate(LocalDateTime.now())
                                .law1("개인정보보호법")
                                .law2("광고법")
                                .law3("없음")
                                .build());

        memberTermsConditionDummy.add(MemberTermsCondition.builder()
                                .condCode("MTC006")
                                .name("제3자 제공 동의")
                                .shortCont("제3자 제공 동의에 대한 짧은 설명")
                                .longCont("제3자 제공 동의에 대한 자세한 설명")
                                .chkUse('Y')
                                .ord(6)
                                .termCondDate(LocalDateTime.now())
                                .termCondUpDate(LocalDateTime.now())
                                .law1("개인정보보호법")
                                .law2("없음")
                                .law3("없음")
                                .build());

        memberTermsConditionDummy.add(MemberTermsCondition.builder()
                                .condCode("MTC007")
                                .name("구매 약관")
                                .shortCont("구매에 관한 짧은 설명")
                                .longCont("구매 약관에 대한 자세한 설명")
                                .chkUse('Y')
                                .ord(7)
                                .termCondDate(LocalDateTime.now())
                                .termCondUpDate(LocalDateTime.now())
                                .law1("전자상거래법")
                                .law2("소비자보호법")
                                .law3("없음")
                                .build());

        memberTermsConditionDummy.add(MemberTermsCondition.builder()
                                .condCode("MTC008")
                                .name("환불 약관")
                                .shortCont("환불 정책에 대한 짧은 설명")
                                .longCont("환불 약관에 대한 자세한 설명")
                                .chkUse('Y')
                                .ord(8)
                                .termCondDate(LocalDateTime.now())
                                .termCondUpDate(LocalDateTime.now())
                                .law1("전자상거래법")
                                .law2("소비자보호법")
                                .law3("없음")
                                .build());

        memberTermsConditionDummy.add(MemberTermsCondition.builder()
                                .condCode("MTC009")
                                .name("회원 탈퇴 약관")
                                .shortCont("회원 탈퇴 절차에 대한 짧은 설명")
                                .longCont("회원 탈퇴 절차에 대한 자세한 설명")
                                .chkUse('Y')
                                .ord(9)
                                .termCondDate(LocalDateTime.now())
                                .termCondUpDate(LocalDateTime.now())
                                .law1("정보통신망법")
                                .law2("개인정보보호법")
                                .law3("없음")
                                .build());

        memberTermsConditionDummy.add(MemberTermsCondition.builder()
                                .condCode("MTC010")
                                .name("서비스 변경 약관")
                                .shortCont("서비스 변경에 관한 짧은 설명")
                                .longCont("서비스 변경에 대한 자세한 설명")
                                .chkUse('Y')
                                .ord(10)
                                .termCondDate(LocalDateTime.now())
                                .termCondUpDate(LocalDateTime.now())
                                .law1("전자상거래법")
                                .law2("소비자보호법")
                                .law3("정보통신망법")
                                .build());

        List<MemberTermsCondition> savedMemberTermsCondition = memberTermsConditionRepository.saveAll(memberTermsConditionDummy);
        assertTrue(savedMemberTermsCondition.size() == memberTermsConditionDummy.size());
    }

    private void createCodes() {
        // 더미 데이터 생성
        codeDummy.add(Code.builder()
                .code("MBS001")
                .cateNum("M")
                .name("신규회원")
                .chkUse("Y")
                .ord(1)
                .build());

        codeDummy.add(Code.builder()
                .code("MBS002")
                .cateNum("M")
                .name("VIP회원")
                .chkUse("N")
                .ord(2)
                .build());

        codeDummy.add(Code.builder()
                .code("MBS003")
                .cateNum("M")
                .name("비활성회원")
                .chkUse("Y")
                .ord(3)
                .build());

        List<Code> savedCodes = codeRepository.saveAll(codeDummy);
        assertTrue(savedCodes.size() == codeDummy.size());
    }

    private MemberRegisterRequest createMemberRegisterRequest(
            List<MemberTermCheckOrNotRequest> memberTermCheckOrNotRequests) {
        MemberRegisterRequest request = MemberRegisterRequest.builder()
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
                .termSeq(1L)
                .build();

        return request;
    }

    private void createMemberTerm() {
        MemberTerms term1 = new MemberTerms();

        MemberTermsCondition termCond1 = memberTermsConditionRepository.findMemberTermsConditionByCondCode("MTC001");
        MemberTermsCondition termCond2 = memberTermsConditionRepository.findMemberTermsConditionByCondCode("MTC002");
        MemberTermsCondition termCond3 = memberTermsConditionRepository.findMemberTermsConditionByCondCode("MTC003");
        MemberTermsCondition termCond4 = memberTermsConditionRepository.findMemberTermsConditionByCondCode("MTC004");
        MemberTermsCondition termCond5 = memberTermsConditionRepository.findMemberTermsConditionByCondCode("MTC005");

        term1.setTermCond1(termCond1);
        term1.setTermCond2(termCond2);
        term1.setTermCond3(termCond3);
        term1.setTermCond4(termCond4);
        term1.setTermCond5(termCond5);

        // 약관에 필요한 기본값 세팅
        term1.setChkTerm1('Y');
        term1.setChkTerm2('Y');
        term1.setChkTerm3('Y');
        term1.setChkTerm4('N');
        term1.setChkTerm5('N');
        term1.setTermRegDate(LocalDateTime.now().minusDays(10)); // 당일 기준 10일 이전에 등록했다고 가정
        term1.setName("24년도회원약관A");
        term1.setChkUse('Y');

        MemberTerms saveMemberTerm = memberTermsRepository.save(term1);
        assertNotNull(saveMemberTerm);
    }

    private List<MemberTermCheckOrNotRequest> createMemberTermsRequest() {
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
                        .agreed('Y')
                        .isMandatory(false)
                        .build());

        memberTermCheckOrNotRequests.add(
                MemberTermCheckOrNotRequest.builder()
                        .termCondCode(5L)
                        .agreed('Y')
                        .isMandatory(false)
                        .build());

        return memberTermCheckOrNotRequests;
    }
}