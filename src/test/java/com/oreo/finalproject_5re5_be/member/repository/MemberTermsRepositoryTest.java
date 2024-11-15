package com.oreo.finalproject_5re5_be.member.repository;

import static org.junit.jupiter.api.Assertions.*;

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

    @BeforeEach
    void setUp() {
        // 자동 주입 확인
        assertNotNull(memberTermsRepository);
        assertNotNull(memberTermConditionRepository);

        // 엔티티 초기화
        dummy.clear();
        memberTermConditionRepository.deleteAll();
        memberTermsRepository.deleteAll();

        // 더미 데이터 넣어 놓기
        // 약관 항목 10개 넣어둠
        createDummy();
        memberTermConditionRepository.saveAll(dummy);
        assertTrue(10 == memberTermConditionRepository.count());

    }

    @DisplayName("사용가능한 가장 최근 약관 조회")
    @Test
    void 사용_가능한_가장_최근_약관_조회() {

    }

    @DisplayName("사용가능한 약관 모두 조회")
    @Test
    void 사용_가능한_약관_모두_조회() {

    }

    @DisplayName("사용불가능한 약관 모두 조회")
    @Test
    void 사용_불가능한_약관_모두_조회() {

    }

    @DisplayName("약관 이름으로 조회")
    @Test
    void 약관_이름으로_조회() {

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

        dummy.add(MemberTermsCondition.builder()
                .condCode("TERMS006")
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

        dummy.add(MemberTermsCondition.builder()
                .condCode("TERMS007")
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

        dummy.add(MemberTermsCondition.builder()
                .condCode("TERMS008")
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

        dummy.add(MemberTermsCondition.builder()
                .condCode("TERMS009")
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

        dummy.add(MemberTermsCondition.builder()
                .condCode("TERMS010")
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
    }

}