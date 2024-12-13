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
class MemberTermConditionRepositoryTest {

    @Autowired private MemberTermConditionRepository memberTermConditionRepository;

    private List<MemberTermsCondition> dummy = new ArrayList<>();

    @BeforeEach
    void setUp() {
        assertNotNull(memberTermConditionRepository);
        // 초가화
        dummy.clear();
        memberTermConditionRepository.deleteAll();

        // 더미 생성 및 저장
        createDummy();
        memberTermConditionRepository.saveAll(dummy);
    }

    @DisplayName("약관 항목 코드 번호로 조회")
    @Test
    void 약관_항목_코드_번호_조회_성공() {
        // 특정 약관 항목 코드로 조회
        String condCode = "TC-001";
        MemberTermsCondition expectedMemberTermsCondition = dummy.get(0);
        MemberTermsCondition foundMemberTermsCondition =
                memberTermConditionRepository.findMemberTermsConditionByCondCode(condCode);
        // 기대하는 결과와 조횐한 결과가 같은지 비교
        assertTrue(isSameMemberTermsCondition(expectedMemberTermsCondition, foundMemberTermsCondition));
    }

    @DisplayName("사용 가능한 약관 정렬 순서대로 조회")
    @Test
    void 사용_가능한_약관_정렬_순서대로_조회_성공() {
        // 사용 가능한 약관을 저장되어 있는 정렬 순서에 따라 조회
        List<MemberTermsCondition> foundAvailableMemberTermsCondtions =
                memberTermConditionRepository.findAvailableMemberTermsConditions();

        // 위의 더미와 같은지 비교
        int expectedSize = dummy.size();
        assertEquals(expectedSize, foundAvailableMemberTermsCondtions.size());

        // 반복 순회하면서 내용 비교
        for (int i = 0; i < expectedSize; i++) {
            MemberTermsCondition expected = dummy.get(i);
            MemberTermsCondition found = foundAvailableMemberTermsCondtions.get(i);
            assertTrue(isSameMemberTermsCondition(expected, found));
        }
    }

    private boolean isSameMemberTermsCondition(
            MemberTermsCondition expected, MemberTermsCondition actual) {
        return expected.getCondCode().equals(actual.getCondCode())
                && expected.getName().equals(actual.getName())
                && expected.getShortCont().equals(actual.getShortCont())
                && expected.getLongCont().equals(actual.getLongCont())
                && expected.getChkUse().equals(actual.getChkUse())
                && expected.getOrd().equals(actual.getOrd())
                && expected.getLaw1().equals(actual.getLaw1())
                && expected.getLaw2().equals(actual.getLaw2())
                && expected.getLaw3().equals(actual.getLaw3());
    }

    private void createDummy() {
        dummy.add(
                MemberTermsCondition.builder()
                        .condCode("TC-001")
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

        dummy.add(
                MemberTermsCondition.builder()
                        .condCode("TC-002")
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

        dummy.add(
                MemberTermsCondition.builder()
                        .condCode("TC-003")
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

        dummy.add(
                MemberTermsCondition.builder()
                        .condCode("TC-004")
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

        dummy.add(
                MemberTermsCondition.builder()
                        .condCode("TC-005")
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

        dummy.add(
                MemberTermsCondition.builder()
                        .condCode("TC-006")
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

        dummy.add(
                MemberTermsCondition.builder()
                        .condCode("TC-007")
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

        dummy.add(
                MemberTermsCondition.builder()
                        .condCode("TC-008")
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

        dummy.add(
                MemberTermsCondition.builder()
                        .condCode("TC-009")
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

        dummy.add(
                MemberTermsCondition.builder()
                        .condCode("TC-010")
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
