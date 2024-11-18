package com.oreo.finalproject_5re5_be.member.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.oreo.finalproject_5re5_be.member.entity.Member;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    private Member member1;
    private Member member2;

    @BeforeEach
    void setUp() {
        // 자동주입 확인
        assertNotNull(memberRepository);

        // 초기화
        memberRepository.deleteAll();

        // 더미 데이터 생성 및 저장
        createDummy();
    }

    @Test
    @DisplayName("변경된 이메일이 다른 회원의 이메일과 중복되는 경우 true 반환")
    void 변경된_이메일_다른_회원_이메일_중복되는_경우_true_반환() {
        // 등록된 두 더미 회원을 조회함
        Member foundMember1 = memberRepository.findById(member1.getSeq()).get();
        Member foundMember2 = memberRepository.findById(member2.getSeq()).get();

        assertNotNull(foundMember1);
        assertNotNull(foundMember2);

        // 한 회원의 이메일을 다른 회원의 이메일로 변경함
        // 그때 repository 의 existsByEmailNotContainingMemberSeq 메서드를 이용하여 중복 유무 확인
        boolean isDuplicated = memberRepository.existsByEmailNotContainingMemberSeq(foundMember1.getSeq(), foundMember2.getEmail());
        assertTrue(isDuplicated);
    }

    @Test
    @DisplayName("변경된 아이디가 다른 회원의 아이디와 중복되는 경우 true 반환")
    void 변경된_아이디_다른_회원_아이디_중복되는_경우_true_반환() {
        // 등록된 두 더미 회원을 조회함
        Member foundMember1 = memberRepository.findById(member1.getSeq()).get();
        Member foundMember2 = memberRepository.findById(member2.getSeq()).get();

        assertNotNull(foundMember1);
        assertNotNull(foundMember2);
        // 한 회원의 아이디를 다른 회원의 아이디로 변경함
        // 그때 repository 의 existsByEmailNotContainingMemberSeq 메서드를 이용하여 중복 유무 확인
        boolean isDuplicated = memberRepository.existsByIdNotContainingMemberSeq(foundMember1.getSeq(), foundMember2.getId());
        assertTrue(isDuplicated);
    }



    private void createDummy() {
        member1 = Member.builder()
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

        member2 = Member.builder()
                        .id("qwerfde2312")
                        .password("asdf12341234@")
                        .email("wdwadaw2323@naver.com")
                        .name("홍만동")
                        .normAddr("서울시 강남구")
                        .locaAddr("서울시")
                        .detailAddr("서초동 123-456")
                        .passAddr("서초대로 59-32")
                        .chkValid('Y')
                        .build();

        Member savedMember1 = memberRepository.save(member1);
        assertNotNull(savedMember1);

        Member savedMember2 = memberRepository.save(member2);
        assertNotNull(savedMember2);
    }
}