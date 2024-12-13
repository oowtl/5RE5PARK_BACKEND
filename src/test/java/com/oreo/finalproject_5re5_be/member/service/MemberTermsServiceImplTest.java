package com.oreo.finalproject_5re5_be.member.service;

import static org.junit.jupiter.api.Assertions.*;

import com.oreo.finalproject_5re5_be.member.dto.request.MemberTermRequest;
import com.oreo.finalproject_5re5_be.member.dto.request.MemberTermUpdateRequest;
import com.oreo.finalproject_5re5_be.member.dto.response.MemberTermResponse;
import com.oreo.finalproject_5re5_be.member.entity.MemberTermsCondition;
import com.oreo.finalproject_5re5_be.member.exception.MemberTermsNotFoundException;
import com.oreo.finalproject_5re5_be.member.repository.MemberTermConditionRepository;
import com.oreo.finalproject_5re5_be.member.repository.MemberTermsRepository;
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
class MemberTermsServiceImplTest {

    @Autowired private MemberTermsServiceImpl memberTermsService;

    @Autowired private MemberTermsRepository memberTermsRepository;

    @Autowired private MemberTermConditionRepository memberTermConditionRepository;

    private List<MemberTermsCondition> dummy = new ArrayList<>();

    @BeforeEach
    void setUp() {
        // 자동 주입 확인
        assertNotNull(memberTermsService);
        assertNotNull(memberTermsRepository);
        assertNotNull(memberTermConditionRepository);

        // 초기화
        dummy.clear();
        memberTermsRepository.deleteAll();
        memberTermConditionRepository.deleteAll();

        // 더미 데이터 넣어 놓기
        createMemberTermsConditionDummy();
        memberTermConditionRepository.saveAll(dummy);
        assertTrue(5 == memberTermConditionRepository.count());
    }

    @DisplayName("회원 약관 생성 통합 테스트")
    @Test
    void 약관_생성_통합_테스트() {
        // 요청 데이터 생성
        MemberTermRequest request =
                MemberTermRequest.builder()
                        .name("24년도회원약관A")
                        .memberTermConditionCodes(
                                List.of(
                                        dummy.get(0).getCondCode(),
                                        dummy.get(1).getCondCode(),
                                        dummy.get(2).getCondCode(),
                                        dummy.get(3).getCondCode(),
                                        dummy.get(4).getCondCode()))
                        .memberTermConditionMandatoryOrNot(List.of('Y', 'Y', 'Y', 'Y', 'Y'))
                        .chkUse('Y')
                        .termCode("TERMS001")
                        .build();

        // 서비스 호출
        MemberTermResponse response = memberTermsService.create(request);

        // 결과확인
        assertNotNull(response);
        System.out.println("response = " + response);
    }

    @DisplayName("회원 약관 조회 통합 테스트")
    @Test
    void 약관_조회_통합_테스트() {
        // 요청 데이터 생성
        MemberTermRequest request =
                MemberTermRequest.builder()
                        .name("24년도회원약관A")
                        .memberTermConditionCodes(
                                List.of(
                                        dummy.get(0).getCondCode(),
                                        dummy.get(1).getCondCode(),
                                        dummy.get(2).getCondCode(),
                                        dummy.get(3).getCondCode(),
                                        dummy.get(4).getCondCode()))
                        .memberTermConditionMandatoryOrNot(List.of('Y', 'Y', 'Y', 'Y', 'Y'))
                        .chkUse('Y')
                        .termCode("TERMS001")
                        .build();

        // 데이터 등록
        MemberTermResponse response = memberTermsService.create(request);

        // 조회
        MemberTermResponse foundMemberTermResponse = memberTermsService.read(response.getTermSeq());
        assertNotNull(foundMemberTermResponse);
        System.out.println("foundMemberTermResponse = " + foundMemberTermResponse);
    }

    @DisplayName("회원 약관 수정 통합 테스트")
    @Test
    void 약관_수정_통합_테스트() {
        // 요청 데이터 생성
        MemberTermRequest request =
                MemberTermRequest.builder()
                        .name("24년도회원약관A")
                        .memberTermConditionCodes(
                                List.of(
                                        dummy.get(0).getCondCode(),
                                        dummy.get(1).getCondCode(),
                                        dummy.get(2).getCondCode(),
                                        dummy.get(3).getCondCode(),
                                        dummy.get(4).getCondCode()))
                        .memberTermConditionMandatoryOrNot(List.of('Y', 'Y', 'Y', 'Y', 'Y'))
                        .chkUse('Y')
                        .termCode("TERMS001")
                        .build();

        // 데이터 등록
        MemberTermResponse response = memberTermsService.create(request);

        // 수정
        MemberTermUpdateRequest updateRequest =
                MemberTermUpdateRequest.builder()
                        .chkUse('N')
                        .memberTermConditionMandatoryOrNot(List.of('N', 'N', 'N', 'N', 'N'))
                        .build();

        memberTermsService.update(response.getTermSeq(), updateRequest);

        // 조회
        MemberTermResponse foundMemberTermResponse = memberTermsService.read(response.getTermSeq());
        assertNotNull(foundMemberTermResponse);
        System.out.println("foundMemberTermResponse = " + foundMemberTermResponse);
    }

    @DisplayName("회원 약관 삭제 통합 테스트")
    @Test
    void 약관_삭제_통합_테스트() {
        // 요청 데이터 생성
        MemberTermRequest request =
                MemberTermRequest.builder()
                        .name("24년도회원약관A")
                        .memberTermConditionCodes(
                                List.of(
                                        dummy.get(0).getCondCode(),
                                        dummy.get(1).getCondCode(),
                                        dummy.get(2).getCondCode(),
                                        dummy.get(3).getCondCode(),
                                        dummy.get(4).getCondCode()))
                        .memberTermConditionMandatoryOrNot(List.of('Y', 'Y', 'Y', 'Y', 'Y'))
                        .chkUse('Y')
                        .termCode("TERMS001")
                        .build();

        // 데이터 등록
        MemberTermResponse response = memberTermsService.create(request);

        // 삭제
        memberTermsService.remove(response.getTermSeq());

        // 조회
        assertThrows(
                MemberTermsNotFoundException.class,
                () -> {
                    memberTermsService.read(response.getTermSeq());
                });
    }

    private void createMemberTermsConditionDummy() {
        dummy.add(
                MemberTermsCondition.builder()
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

        dummy.add(
                MemberTermsCondition.builder()
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

        dummy.add(
                MemberTermsCondition.builder()
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

        dummy.add(
                MemberTermsCondition.builder()
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

        dummy.add(
                MemberTermsCondition.builder()
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
}
