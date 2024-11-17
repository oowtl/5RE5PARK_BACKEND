package com.oreo.finalproject_5re5_be.member.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.oreo.finalproject_5re5_be.member.entity.MemberTerms;
import com.oreo.finalproject_5re5_be.member.entity.MemberTermsCondition;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberTermsRepositoryTest {

    @Autowired
    private MemberTermsRepository memberTermsRepository;

    @Autowired
    private MemberTermConditionRepository memberTermConditionRepository;

    private List<MemberTermsCondition> dummy = new ArrayList<>();

    private MemberTerms savedMemberTerms1;
    private MemberTerms savedMemberTerms2;
    private MemberTerms savedMemberTerms3;

    @BeforeEach
    void setUp() {
        // 자동 주입 확인
        assertNotNull(memberTermsRepository);
        assertNotNull(memberTermConditionRepository);

        // 엔티티 초기화
        dummy.clear();
        memberTermsRepository.deleteAll();
        memberTermConditionRepository.deleteAll();

        // 더미 데이터 넣어 놓기
        // 약관 항목 5개 넣어둠
        createDummy();
        memberTermConditionRepository.saveAll(dummy);
        assertTrue(5 == memberTermConditionRepository.count());

        // 약관 3개 만들기
        MemberTerms term1 = new MemberTerms();
        MemberTerms term2 = new MemberTerms();
        MemberTerms term3 = new MemberTerms();

        // TERMS001 ~ TERMS005까지로 약관 항목들 조회
        MemberTermsCondition termCond1 = memberTermConditionRepository.findMemberTermsConditionByCondCode("TERMS001");
        MemberTermsCondition termCond2 = memberTermConditionRepository.findMemberTermsConditionByCondCode("TERMS002");
        MemberTermsCondition termCond3 = memberTermConditionRepository.findMemberTermsConditionByCondCode("TERMS003");
        MemberTermsCondition termCond4 = memberTermConditionRepository.findMemberTermsConditionByCondCode("TERMS004");
        MemberTermsCondition termCond5 = memberTermConditionRepository.findMemberTermsConditionByCondCode("TERMS005");

        // 1번 약관 만들기 : Y - Y - N - N - Y
        // 약관에 약관 항목들 세팅
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
        term1.setTermCode("TERMS001");

        // 2번 약관 만들기 : Y - Y - Y - Y - Y
        // 2번 약관 사용 불가
        term2.setTermCond1(termCond1);
        term2.setTermCond2(termCond2);
        term2.setTermCond3(termCond3);
        term2.setTermCond4(termCond4);
        term2.setTermCond5(termCond5);

        // 약관에 필요한 기본값 세팅
        term2.setChkTerm1('Y');
        term2.setChkTerm2('Y');
        term2.setChkTerm3('Y');
        term2.setChkTerm4('Y');
        term2.setChkTerm5('Y');
        term2.setTermRegDate(LocalDateTime.now().minusDays(5)); // 당일 기준 5일 이전에 등록했다고 가정
        term2.setName("24년도회원약관B");
        term2.setChkUse('N');
        term2.setTermCode("TERMS002");

        // 3번 약관 만들기 : N - N - N - Y - Y
        term3.setTermCond1(termCond1);
        term3.setTermCond2(termCond2);
        term3.setTermCond3(termCond3);
        term3.setTermCond4(termCond4);
        term3.setTermCond5(termCond5);

        // 약관에 필요한 기본값 세팅
        term3.setChkTerm1('N');
        term3.setChkTerm2('N');
        term3.setChkTerm3('N');
        term3.setChkTerm4('Y');
        term3.setChkTerm5('Y');
        term3.setTermRegDate(LocalDateTime.now().minusDays(3)); // 당일 기준 3일 이전에 등록했다고 가정
        term3.setName("24년도회원약관C");
        term3.setChkUse('Y');
        term3.setTermCode("TERMS003");


        // 약관 저장
        savedMemberTerms1 = memberTermsRepository.save(term1);
        savedMemberTerms2 = memberTermsRepository.save(term2);
        savedMemberTerms3 = memberTermsRepository.save(term3);
        assertTrue(3 == memberTermsRepository.count());

    }

    @DisplayName("사용 가능한 가장 최근 약관 조회")
    @Test
    void 사용_가능한_가장_최근_약관_조회() {
        // 사용 가능한 가장 최근 약관 조회
        MemberTerms foundMemberTerms = memberTermsRepository.findTopByChkUseOrderByTermRegDateDesc();

        // 24년도 회원 약관 C가 조회 되어야함
        assertTrue(isSameMemberTerm(savedMemberTerms3, foundMemberTerms));
    }

    @DisplayName("사용가능한 약관 모두 조회")
    @Test
    void 사용_가능한_약관_모두_조회() {
        // 사용 가능한 모든 약관 조회
        List<MemberTerms> foundMemberTerms = memberTermsRepository.findAvailableMemberTerms();

        // 2개의 약관이 조회 되어야함(24년도 회원 약관 A, 24년도 회원 약관 C)
        assertTrue(2 == foundMemberTerms.size());
        assertTrue(isSameMemberTerm(savedMemberTerms1, foundMemberTerms.get(1)));
        assertTrue(isSameMemberTerm(savedMemberTerms3, foundMemberTerms.get(0)));
    }

    @DisplayName("사용불가능한 약관 모두 조회")
    @Test
    void 사용_불가능한_약관_모두_조회() {
        // 사용 불가능한 모든 약관 조회
        List<MemberTerms> foundMemberTerms = memberTermsRepository.findNotAvailableMemberTerms();

        // 1개의 약관이 조회 되어야함(24년도 회원 약관 B)
        assertTrue(1 == foundMemberTerms.size());
        assertTrue(isSameMemberTerm(savedMemberTerms2, foundMemberTerms.get(0)));
    }

    @DisplayName("약관 이름으로 조회")
    @Test
    void 약관_이름으로_조회() {
        // 약관 이름으로 조회 - 24년도 회원 약관 A
        MemberTerms foundMemberTerms = memberTermsRepository.findMemberTermsByName("24년도회원약관A");
        // 내용 비교
        assertTrue(isSameMemberTerm(savedMemberTerms1, foundMemberTerms));

    }

    private void createDummy() {
        dummy.add(MemberTermsCondition.builder()
                .condCode("TERMS001")
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

        dummy.add(MemberTermsCondition.builder()
                .condCode("TERMS002")
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

        dummy.add(MemberTermsCondition.builder()
                .condCode("TERMS003")
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

        dummy.add(MemberTermsCondition.builder()
                .condCode("TERMS004")
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

        dummy.add(MemberTermsCondition.builder()
                .condCode("TERMS005")
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

    }

    private boolean isSameMemberTerm(MemberTerms mt1, MemberTerms mt2) {
        return mt1.getChkTerm1().equals(mt2.getChkTerm1()) &&
                mt1.getChkTerm2().equals(mt2.getChkTerm2()) &&
                mt1.getChkTerm3().equals(mt2.getChkTerm3()) &&
                mt1.getChkTerm4().equals(mt2.getChkTerm4()) &&
                mt1.getChkTerm5().equals(mt2.getChkTerm5()) &&
                mt1.getName().equals(mt2.getName()) &&
                mt1.getChkUse().equals(mt2.getChkUse()) &&
                mt1.getTermCond1().equals(mt2.getTermCond1()) &&
                mt1.getTermCond2().equals(mt2.getTermCond2()) &&
                mt1.getTermCond3().equals(mt2.getTermCond3()) &&
                mt1.getTermCond4().equals(mt2.getTermCond4()) &&
                mt1.getTermCond5().equals(mt2.getTermCond5());
    }

}