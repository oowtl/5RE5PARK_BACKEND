package com.oreo.finalproject_5re5_be.member.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.oreo.finalproject_5re5_be.member.entity.Code;
import com.oreo.finalproject_5re5_be.member.entity.Member;
import com.oreo.finalproject_5re5_be.member.entity.MemberChangeHistory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
class MemberChangeHistoryRepositoryTest {

    @Autowired
    private MemberChangeHistoryRepository memberChangeHistoryRepository;

    @Autowired
    private CodeRepository codeRepository;

    @Autowired
    private MemberRepository memberRepository;

    private List<MemberChangeHistory> dummy = new ArrayList<>();

    @BeforeEach
    void setUp() {
        // 자동 주입 확인
        assertNotNull(memberChangeHistoryRepository);
        assertNotNull(memberRepository);
        assertNotNull(codeRepository);

        // 초기화
        dummy.clear();
        memberRepository.deleteAll();
        codeRepository.deleteAll();
        memberChangeHistoryRepository.deleteAll();

        // 더미 데이터 생성 및 저장
        createCode();
        createMember();
        createDummy();

    }

    @Test
    @DisplayName("회원 가장 최근 변경 이력 조회")
    void 회원_가장_최근_변경_이력_조회() {
        // 변경 이력 여러개를 등록함
        List<MemberChangeHistory> memberChangeHistories = memberChangeHistoryRepository.saveAll(dummy);
        assertEquals(dummy.size(), memberChangeHistories.size());


        // 가장 최근 이력을 조회하는 쿼리 실행
        MemberChangeHistory foundLatestHistory = memberChangeHistoryRepository.findLatestHistoryByIdAndCode(1L,"MF001" ).get();

        // 결과 비교
        assertNotNull(foundLatestHistory);
        assertEquals(dummy.get(2).getAftVal(), foundLatestHistory.getAftVal());
        System.out.println("foundLatestHistory = " + foundLatestHistory);
    }

    private void createCode() {
        Code code = Code.builder()
                        .codeSeq(1L)
                        .cateNum("MB")
                        .code("MF001")
                        .name("회원아이디필드")
                        .ord(1)
                        .chkUse("Y")
                        .comt("회원 아이디 필드입니다.")
                        .build();
        Code savedCode = codeRepository.save(code);
        assertNotNull(savedCode);
    }

    private void createMember() {
        Member member = Member.builder()
                            .id("qwerfde2312")
                            .password("asdf12341234@")
                            .email("qwefghnm1212@gmail.com")
                            .name("홍길동")
                            .normAddr("서울시 강남구")
                            .locaAddr("서울시")
                            .detailAddr("서초동 123-456")
                            .passAddr("서초대로 59-32")
                            .chkValid('Y')
                            .build();
        Member savedMember = memberRepository.save(member);
        assertNotNull(savedMember);
    }

    private void createDummy() {
        // 현재 시간과 최대 시간 세팅
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.MAX;

        // DATETIME 형식으로 변환하기 위한 포맷터 생성
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 포맷팅된 문자열로 변환
        String formattedDateTime = now.format(formatter);
        String formattedEnd = end.format(formatter);

        // 코드 조회
        Code code = codeRepository.findCodeByCode("MF001");

        // 더미 데이터 생성
        dummy.add(MemberChangeHistory.builder()
                                    .member(memberRepository.findById(1L).get())
                                    .chngFieldCode(codeRepository.findById(1L).get())
                                    .befVal("qwerfde2312")
                                    .aftVal("(1)new qwerfde2312")
                                    .chngFieldCode(code)
                                    .applDate(formattedDateTime)
                                    .endDate(formattedEnd)
                                    .build());

        dummy.add(MemberChangeHistory.builder()
                                    .member(memberRepository.findById(1L).get())
                                    .chngFieldCode(codeRepository.findById(1L).get())
                                    .befVal("(1)new qwerfde2312")
                                    .aftVal("(2)new qwerfde2312")
                                    .chngFieldCode(code)
                                    .applDate(formattedDateTime)
                                    .endDate(formattedEnd)
                                    .build());

        dummy.add(MemberChangeHistory.builder()
                                    .member(memberRepository.findById(1L).get())
                                    .chngFieldCode(codeRepository.findById(1L).get())
                                    .befVal("(2)new qwerfde2312")
                                    .aftVal("(3)new qwerfde2312")
                                    .chngFieldCode(code)
                                    .applDate(formattedDateTime)
                                    .endDate(formattedEnd)
                                    .build());
    }
}