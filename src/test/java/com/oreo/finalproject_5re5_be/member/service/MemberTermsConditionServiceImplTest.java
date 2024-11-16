package com.oreo.finalproject_5re5_be.member.service;

import static org.junit.jupiter.api.Assertions.*;

import com.oreo.finalproject_5re5_be.member.dto.request.MemberTermConditionRequest;
import com.oreo.finalproject_5re5_be.member.dto.request.MemberTermConditionUpdateRequest;
import com.oreo.finalproject_5re5_be.member.dto.response.MemberTermConditionResponse;
import com.oreo.finalproject_5re5_be.member.dto.response.MemberTermConditionResponses;
import com.oreo.finalproject_5re5_be.member.entity.MemberTermsCondition;
import com.oreo.finalproject_5re5_be.member.repository.MemberTermConditionRepository;
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
class MemberTermsConditionServiceImplTest {

    @Autowired
    private MemberTermsConditionServiceImpl memberTermsConditionService;

    @Autowired
    private MemberTermConditionRepository memberTermConditionRepository;

    private List<MemberTermsCondition> dummy = new ArrayList<>();


    @BeforeEach
    void setUp() {
        assertNotNull(memberTermsConditionService);
        assertNotNull(memberTermConditionRepository);

        // 초가화
        dummy.clear();
        memberTermConditionRepository.deleteAll();

        // 더미 생성 및 저장
        createDummy();
        memberTermConditionRepository.saveAll(dummy);
    }

    @DisplayName("단건 회원 약관 항목을 등록함")
    @Test
    void 단건_회원_약관_항목을_등록함() {
        // 기존에 등록된 더미 데이터 없애기
        memberTermConditionRepository.deleteAll();
        // 새로운 요청 더미 데이터 1개 생성
        MemberTermConditionRequest request = MemberTermConditionRequest.builder()
                .condCode("TC-001")
                .name("서비스 이용약관")
                .shortCont("서비스 이용에 관한 짧은 내용")
                .longCont("서비스 이용에 관한 자세한 내용")
                .chkUse('Y')
                .ord(1)
                .law1("개인정보보호법")
                .law2("전자상거래법")
                .law3("소비자보호법")
                .build();
        MemberTermsCondition expectedMemberTermCondition = MemberTermsCondition.builder()
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
                .build();

        // 서비스에 등록 요청
        MemberTermConditionResponse expectedResponse = new MemberTermConditionResponse(expectedMemberTermCondition);
        MemberTermConditionResponse response = memberTermsConditionService.create(request);

        // 등록된 데이터와 요청한 데이터가 같은지 확인
        assertTrue(isSameMemberTermConditionResponse(expectedResponse, response));
    }

    @DisplayName("여러개 회원 약관 항목을 등록함")
    @Test
    void 여러개_회원_약관_항목을_등록함() {
        // 기존에 등록된 더미 데이터 없애기
        memberTermConditionRepository.deleteAll();

        // 새로운 요청 더미 데이터 여러개 생성
        List<MemberTermConditionRequest> list = new ArrayList<>();
        for (MemberTermsCondition memberTermsCondition : dummy) {
            MemberTermConditionRequest request = MemberTermConditionRequest.builder()
                    .condCode(memberTermsCondition.getCondCode())
                    .name(memberTermsCondition.getName())
                    .shortCont(memberTermsCondition.getShortCont())
                    .longCont(memberTermsCondition.getLongCont())
                    .chkUse(memberTermsCondition.getChkUse())
                    .ord(memberTermsCondition.getOrd())
                    .law1(memberTermsCondition.getLaw1())
                    .law2(memberTermsCondition.getLaw2())
                    .law3(memberTermsCondition.getLaw3())
                    .build();
            list.add(request);
        }

        // 서비스에 등록 요청
        List<MemberTermConditionResponse> expectedCreatedDummy = new ArrayList<>();
        for (MemberTermsCondition memberTermsCondition : dummy) {
            MemberTermConditionResponse memberTermConditionResponse = new MemberTermConditionResponse(memberTermsCondition);
            expectedCreatedDummy.add(memberTermConditionResponse);
        }
        MemberTermConditionResponses expectedResponse = new MemberTermConditionResponses(expectedCreatedDummy);
        MemberTermConditionResponses actualResponse = memberTermsConditionService.create(list);

        // 등록된 데이터와 요청한 데이터가 같은지 확인
        assertEquals(expectedResponse.getMemberTermConditionResponses().size(), actualResponse.getMemberTermConditionResponses().size());
        for (int i = 0; i < expectedResponse.getMemberTermConditionResponses().size(); i++) {
            assertTrue(isSameMemberTermConditionResponse(expectedResponse.getMemberTermConditionResponses().get(i), actualResponse.getMemberTermConditionResponses().get(i)));
        }
    }

    @DisplayName("단건 회원 약관 항목을 조회함")
    @Test
    void 단건_회원_약관_항목을_조회함() {
        // 기존에 등록된 회원 약관 항목 중 하나 조회
        MemberTermsCondition selectedMemberTermsCondition = dummy.get(1);
        MemberTermConditionResponse expectedMemberTermConditionResponse = new MemberTermConditionResponse(selectedMemberTermsCondition);

        // 조회된 데이터와 기대하는 데이터가 같은지 확인
        MemberTermConditionResponse foundMemberTermConditionResponse = memberTermsConditionService.read(selectedMemberTermsCondition.getCondCode());

        assertTrue(isSameMemberTermConditionResponse(expectedMemberTermConditionResponse, foundMemberTermConditionResponse));
    }

    @DisplayName("모든 회원 약관 항목을 조회함")
    @Test
    void 여러개_회원_약관_항목을_조회함() {
        // 기존에 등록된 회원 약관 항목 모두 조회
        List<MemberTermConditionResponse> expectedMemberTermConditionResponses = new ArrayList<>();
        for (MemberTermsCondition memberTermsCondition : dummy) {
            MemberTermConditionResponse memberTermConditionResponse = new MemberTermConditionResponse(memberTermsCondition);
            expectedMemberTermConditionResponses.add(memberTermConditionResponse);
        }

        MemberTermConditionResponses foundMemberTermConditionResponses = memberTermsConditionService.readAll();

        // 조회된 데이터와 기대하는 데이터가 같은지 확인
        assertEquals(expectedMemberTermConditionResponses.size(), foundMemberTermConditionResponses.getMemberTermConditionResponses().size());
        for (int i = 0; i < expectedMemberTermConditionResponses.size(); i++) {
            assertTrue(isSameMemberTermConditionResponse(expectedMemberTermConditionResponses.get(i), foundMemberTermConditionResponses.getMemberTermConditionResponses().get(i)));
        }

    }

    @DisplayName("사용 가능한 회원 약관 항목을 조회함")
    @Test
    void 사용_가능한_회원_약관_항목을_조회함() {
        // 기존에 등록된 회원 약관 항목 중 사용 가능한 항목만 조회
        List<MemberTermsCondition> availableDummy = dummy.stream()
                                                         .filter(m -> m.getChkUse().equals('Y'))
                                                         .toList();
        List<MemberTermConditionResponse> expectedMemberTermConditionResponses = new ArrayList<>();
        for (MemberTermsCondition memberTermsCondition : availableDummy) {
            MemberTermConditionResponse memberTermConditionResponse = new MemberTermConditionResponse(memberTermsCondition);
            expectedMemberTermConditionResponses.add(memberTermConditionResponse);
        }

        // 조회된 데이터와 기대하는 데이터가 같은지 확인
        MemberTermConditionResponses foundMemberTermConditionResponses = memberTermsConditionService.readAvailable();
        assertEquals(expectedMemberTermConditionResponses.size(), foundMemberTermConditionResponses.getMemberTermConditionResponses().size());
        for (int i = 0; i < expectedMemberTermConditionResponses.size(); i++) {
            assertTrue(isSameMemberTermConditionResponse(expectedMemberTermConditionResponses.get(i), foundMemberTermConditionResponses.getMemberTermConditionResponses().get(i)));
        }
    }

    @DisplayName("사용 불가능한 회원 약관 항목을 조회함")
    @Test
    void 사용_불가능한_회원_약관_항목을_조회함() {
        // 기존에 등록된 회원 약관 항목 중 사용 불가능한 항목만 조회
        List<MemberTermsCondition> notAvailableDummy = dummy.stream()
                .filter(m -> m.getChkUse().equals('N'))
                .toList();
        List<MemberTermConditionResponse> expectedMemberTermConditionResponses = new ArrayList<>();
        for (MemberTermsCondition memberTermsCondition : notAvailableDummy) {
            MemberTermConditionResponse memberTermConditionResponse = new MemberTermConditionResponse(memberTermsCondition);
            expectedMemberTermConditionResponses.add(memberTermConditionResponse);
        }

        // 조회된 데이터와 기대하는 데이터가 같은지 확인
        MemberTermConditionResponses foundMemberTermConditionResponses = memberTermsConditionService.readNotAvailable();
        assertEquals(expectedMemberTermConditionResponses.size(), foundMemberTermConditionResponses.getMemberTermConditionResponses().size());
        for (int i = 0; i < expectedMemberTermConditionResponses.size(); i++) {
            assertTrue(isSameMemberTermConditionResponse(expectedMemberTermConditionResponses.get(i), foundMemberTermConditionResponses.getMemberTermConditionResponses().get(i)));
        }
    }

    @DisplayName("단건 회원 약관 항목을 수정함")
    @Test
    void 단건_회원_약관_항목을_수정함() {
        // 기존에 등록된 회원 약관 항목 중 하나 조회
        MemberTermsCondition selectedMemberTermsCondition = dummy.get(0);
        MemberTermsCondition foundMemberTermsCondition = memberTermConditionRepository.findMemberTermsConditionByCondCode(
                selectedMemberTermsCondition.getCondCode());
        assertNotNull(foundMemberTermsCondition);

        // 해당 항목의 시퀀스 조회
        String termCondCode = foundMemberTermsCondition.getCondCode();

        // 수정 요청 더미 데이터 생성
        MemberTermConditionUpdateRequest request = MemberTermConditionUpdateRequest.builder()
                .shortCont("업데이트된 서비스 이용에 관한 짧은 내용 수정")
                .longCont("업데이트된 서비스 이용에 관한 자세한 내용 수정")
                .chkUse('N')
                .ord(10)
                .build();

        // 서비스에 수정 요청
        memberTermsConditionService.update(termCondCode, request);

        // 조회된 데이터와 기대하는 데이터가 같은지 확인
        MemberTermsCondition updatedMemberTermCondition = memberTermConditionRepository.findMemberTermsConditionByCondCode(termCondCode);
        assertEquals(request.getShortCont(), updatedMemberTermCondition.getShortCont());
        assertEquals(request.getLongCont(), updatedMemberTermCondition.getLongCont());
        assertEquals(request.getChkUse(), updatedMemberTermCondition.getChkUse());
        assertEquals(request.getOrd(), updatedMemberTermCondition.getOrd());
    }

    @DisplayName("단건 회원 약관 항목을 삭제함")
    @Test
    void 단건_회원_약관_항목을_삭제함() {
        // 기존에 등록된 회원 약관 항목 중 하나 조회
        MemberTermsCondition selectedMemberTermsCondition = dummy.get(0);
        MemberTermsCondition foundMemberTermsCondition = memberTermConditionRepository.findMemberTermsConditionByCondCode(
                selectedMemberTermsCondition.getCondCode());
        assertNotNull(foundMemberTermsCondition);

        // 서비스에 삭제 요청
        memberTermsConditionService.remove(selectedMemberTermsCondition.getCondCode());

        // 조회된 데이터가 삭제되었는지 확인
        foundMemberTermsCondition = memberTermConditionRepository.findMemberTermsConditionByCondCode(
                selectedMemberTermsCondition.getCondCode());
        assertNull(foundMemberTermsCondition);
    }

    private boolean isSameMemberTermConditionResponse(MemberTermConditionResponse memberTermConditionResponse1, MemberTermConditionResponse memberTermConditionResponse2) {
        return memberTermConditionResponse1.getCondCode().equals(memberTermConditionResponse2.getCondCode())
                && memberTermConditionResponse1.getShortCont().equals(memberTermConditionResponse2.getShortCont())
                && memberTermConditionResponse1.getLongCont().equals(memberTermConditionResponse2.getLongCont())
                && memberTermConditionResponse1.getChkUse().equals(memberTermConditionResponse2.getChkUse())
                && memberTermConditionResponse1.getOrd().equals(memberTermConditionResponse2.getOrd())
                && memberTermConditionResponse1.getLaw1().equals(memberTermConditionResponse2.getLaw1())
                && memberTermConditionResponse1.getLaw2().equals(memberTermConditionResponse2.getLaw2())
                && memberTermConditionResponse1.getLaw3().equals(memberTermConditionResponse2.getLaw3());

    }


    private void createDummy() {
        dummy.add(MemberTermsCondition.builder()
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

        dummy.add(MemberTermsCondition.builder()
                .condCode("TC-002")
                .name("개인정보 수집 및 이용")
                .shortCont("개인정보 수집에 대한 짧은 설명")
                .longCont("개인정보 수집 및 이용에 대한 자세한 설명")
                .chkUse('N')
                .ord(2)
                .termCondDate(LocalDateTime.now())
                .termCondUpDate(LocalDateTime.now())
                .law1("정보통신망법")
                .law2("개인정보보호법")
                .law3("없음")
                .build());

        dummy.add(MemberTermsCondition.builder()
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

        dummy.add(MemberTermsCondition.builder()
                .condCode("TC-004")
                .name("쿠키 사용 약관")
                .shortCont("쿠키 사용에 대한 짧은 설명")
                .longCont("쿠키 사용에 대한 자세한 설명")
                .chkUse('N')
                .ord(4)
                .termCondDate(LocalDateTime.now())
                .termCondUpDate(LocalDateTime.now())
                .law1("정보통신망법")
                .law2("개인정보보호법")
                .law3("없음")
                .build());

        dummy.add(MemberTermsCondition.builder()
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

        dummy.add(MemberTermsCondition.builder()
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

        dummy.add(MemberTermsCondition.builder()
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

        dummy.add(MemberTermsCondition.builder()
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

        dummy.add(MemberTermsCondition.builder()
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

        dummy.add(MemberTermsCondition.builder()
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