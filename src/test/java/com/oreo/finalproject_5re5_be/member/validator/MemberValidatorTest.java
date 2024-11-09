package com.oreo.finalproject_5re5_be.member.validator;

import static org.junit.jupiter.api.Assertions.*;

import com.oreo.finalproject_5re5_be.member.dto.request.MemberRegisterRequest;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

@SpringBootTest
class MemberValidatorTest {


    @Autowired
    private MemberValidator memberValidator;

    private MemberRegisterRequest memberRegisterRequest;
    private Errors errors;

    @DisplayName("유효성 검증 성공")
    @Test
    public void 유효한_회원_데이터_통과() {
        // 유효한 아이디 데이터 생성
        memberRegisterRequest = MemberRegisterRequest.builder()
                .id("qwerfde2312")
                .password("asdf12341234@")
                .email("asdf3214@gmail.com")
                .name("홍길동")
                .birthDate("1990-01-01")
                .userRegDate(LocalDateTime.now())
                .chkValid('Y')
                .normAddr("서울시 강남구")
                .passAddr("서초대로 59-32")
                .locaAddr("서초동")
                .detailAddr("서초동 123-456")
                .build();
        // 에러 저장 객체 생성
        errors = new BeanPropertyBindingResult(memberRegisterRequest, "userRegisterRequest");

        // 유효성 검증 실행
        memberValidator.validate(memberRegisterRequest, errors);

        // 에러 존재 여부 확인 - false
        assertFalse(errors.hasErrors());
    }


    @DisplayName("필수값 누락")
    @Test
    public void 필수값_누락() {
        // 비밀번호가 누락된 데이터 생성
        memberRegisterRequest = MemberRegisterRequest.builder()
                .id("qwerfde2312")
                .password(null)
                .email("asdf3214@gmail.com")
                .name("홍길동")
                .birthDate("1990-01-01")
                .userRegDate(LocalDateTime.now())
                .chkValid('Y')
                .normAddr("서울시 강남구")
                .passAddr("서초대로 59-32")
                .locaAddr("서초동")
                .detailAddr("서초동 123-456")
                .build();

        // 에러 저장 객체 생성
        errors = new BeanPropertyBindingResult(memberRegisterRequest, "userRegisterRequest");

        // 유효성 검증 실행
        memberValidator.validate(memberRegisterRequest, errors);

        // 에러 존재 여부 확인 - true
        assertTrue(errors.hasErrors());
    }


    @DisplayName("아이디 유효성 검증 : 아이디 유효성 검증: 6~20자, 영문 및 숫자만 허용")
    @Test
    public void 아이디_유효성_검증_처리_실패() {
        // 유효하지 않은 아이디 데이터 생성
        memberRegisterRequest = MemberRegisterRequest.builder()
                .id("q안녕312")
                .password("asdf12341234@")
                .email("asdf3214@gmail.com")
                .name("홍길동")
                .birthDate("1990-01-01")
                .userRegDate(LocalDateTime.now())
                .chkValid('Y')
                .normAddr("서울시 강남구")
                .passAddr("서초대로 59-32")
                .locaAddr("서초동")
                .detailAddr("서초동 123-456")
                .build();

        // 에러 저장 객체 생성
        errors = new BeanPropertyBindingResult(memberRegisterRequest, "userRegisterRequest");

        // 유효성 검증 실행
        memberValidator.validate(memberRegisterRequest, errors);

        // 에러 존재 여부 확인 - true
        assertTrue(errors.hasErrors());
    }



    @DisplayName("비밀번호 유효성 검증: 8~20자, 특수문자 포함, 동일 문자 4회 이상 연속 불가")
    @Test
    public void 비밀번호_유효성_검증_처리_실패() {
        // 유효하지 않은 아이디 데이터 생성
        memberRegisterRequest = MemberRegisterRequest.builder()
                .id("잘못된 아이디")
                .password("asdf12341234@")
                .email("asdf3214@gmail.com")
                .name("홍길동")
                .birthDate("1990-01-01")
                .userRegDate(LocalDateTime.now())
                .chkValid('Y')
                .normAddr("서울시 강남구")
                .passAddr("서초대로 59-32")
                .locaAddr("서초동")
                .detailAddr("서초동 123-456")
                .build();

        // 에러 저장 객체 생성
        errors = new BeanPropertyBindingResult(memberRegisterRequest, "userRegisterRequest");

        // 유효성 검증 실행
        memberValidator.validate(memberRegisterRequest, errors);

        // 에러 존재 여부 확인 - true
        assertTrue(errors.hasErrors());
    }


    @DisplayName("이름 유효성 검증: 특수문자, 숫자 불가")
    @Test
    public void 이름_유효성_검증_처리_실패() {
        // 유효하지 않은 이름 데이터 생성
        memberRegisterRequest = MemberRegisterRequest.builder()
                .id("잘못된 아이디")
                .password("asdf12341234@")
                .email("asdf3214@gmail.com")
                .name("홍길동!@#")
                .birthDate("1990-01-01")
                .userRegDate(LocalDateTime.now())
                .chkValid('Y')
                .normAddr("서울시 강남구")
                .passAddr("서초대로 59-32")
                .locaAddr("서초동")
                .detailAddr("서초동 123-456")
                .build();

        // 에러 저장 객체 생성
        errors = new BeanPropertyBindingResult(memberRegisterRequest, "userRegisterRequest");

        // 유효성 검증 실행
        memberValidator.validate(memberRegisterRequest, errors);

        // 에러 존재 여부 확인 - true
        assertTrue(errors.hasErrors());
    }


    @DisplayName("생년월일 유효성 검증: 'YYYY-MM-DD' 형식")
    @Test
    public void 생년월일_유효성_검증_처리_실패() {
        // 유효하지 않은 생년월일 데이터 생성
        memberRegisterRequest = MemberRegisterRequest.builder()
                .id("잘못된 아이디")
                .password("asdf12341234@")
                .email("asdf3214@gmail.com")
                .name("홍길동!@#")
                .birthDate("199--0-01-01")
                .userRegDate(LocalDateTime.now())
                .chkValid('Y')
                .normAddr("서울시 강남구")
                .passAddr("서초대로 59-32")
                .locaAddr("서초동")
                .detailAddr("서초동 123-456")
                .build();

        // 에러 저장 객체 생성
        errors = new BeanPropertyBindingResult(memberRegisterRequest, "userRegisterRequest");

        // 유효성 검증 실행
        memberValidator.validate(memberRegisterRequest, errors);

        // 에러 존재 여부 확인 - true
        assertTrue(errors.hasErrors());
    }


    @DisplayName("이메일 유효성 검증: 유효한 이메일 형식")
    @Test
    public void 이메일_유효성_검증_처리_실패() {
        // 유효하지 않은 이메일 데이터 생성
        memberRegisterRequest = MemberRegisterRequest.builder()
                .id("잘못된 아이디")
                .password("asdf12341234@")
                .email("asdf3214ㅇㄷ2gmail.com")
                .name("홍길동!@#")
                .birthDate("199--0-01-01")
                .userRegDate(LocalDateTime.now())
                .chkValid('Y')
                .normAddr("서울시 강남구")
                .passAddr("서초대로 59-32")
                .locaAddr("서초동")
                .detailAddr("서초동 123-456")
                .build();

        // 에러 저장 객체 생성
        errors = new BeanPropertyBindingResult(memberRegisterRequest, "userRegisterRequest");

        // 유효성 검증 실행
        memberValidator.validate(memberRegisterRequest, errors);

        // 에러 존재 여부 확인 - true
        assertTrue(errors.hasErrors());
    }


    @DisplayName("휴대전화 번호 유효성 검증: 숫자만 입력, 11자")
    @Test
    public void 휴대전화_번호_유효성_검증_처리_실패() {
        // 유효하지 않은 휴대전화 데이터 생성
        memberRegisterRequest = MemberRegisterRequest.builder()
                .id("잘못된 아이디")
                .password("asdf12341234@")
                .email("asdf3214ㅇㄷ2gmail.com")
                .name("홍길동!@#")
                .birthDate("199--0-01-01")
                .userRegDate(LocalDateTime.now())
                .chkValid('Y')
                .normAddr("서울시 강남구")
                .passAddr("서초대로 59-32")
                .locaAddr("서초동")
                .detailAddr("서초동 123-456")
                .build();

        // 에러 저장 객체 생성
        errors = new BeanPropertyBindingResult(memberRegisterRequest, "userRegisterRequest");

        // 유효성 검증 실행
        memberValidator.validate(memberRegisterRequest, errors);

        // 에러 존재 여부 확인 - true
        assertTrue(errors.hasErrors());
    }


}
