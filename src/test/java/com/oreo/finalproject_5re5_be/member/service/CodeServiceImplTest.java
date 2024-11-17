package com.oreo.finalproject_5re5_be.member.service;

import static org.junit.jupiter.api.Assertions.*;

import com.oreo.finalproject_5re5_be.member.repository.CodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CodeServiceImplTest {

    @Mock
    private CodeRepository codeRepository;

    @InjectMocks
    private CodeServiceImpl codeService;

    @BeforeEach
    void setUp() {
        // 자동 주입 확인
        assertNotNull(codeRepository);
        assertNotNull(codeService);
    }

    // 코드 등록
    @DisplayName("코드 등록 처리")
    @Test
    void 코드_생성_처리() {
        // 유효성 검증이 완료된 더미 데이터 생성
        // 기대 결과 생성

        // 리포지토리 목킹
        // - 중복여부 false 설정
        // - 저장된 엔티티 반환

        // 서비스 메서드 호출
        // 기대한 결과와 반환된 결과 비교

    }

    @DisplayName("중복된 코드명 예외 처리")
    @Test
    void 중복된_코드명_예외_처리() {
        // 더미 데이터 생성

        // 리포지토리 목킹
        // - 중복여부 true 설정

        // 서비스 메서드 호출
        // 예외 발생 확인

    }

    // 코드 조회
    @DisplayName("모든 코드 조회")
    @Test
    void 모든_코드_조회() {
        // 기대한 결과 생성

        // 리포지토리 목킹
        // - 여러 코드 엔티티 반환 설정

        // 서비스 메서드 호출
        // 기대한 결과와 반환된 결과 비교
    }

    @DisplayName("시퀀스로 조회")
    @Test
    void 시퀀스로_조회() {
        // 더미 데이터 생성
        // 기대한 결과 생성

        // 리포지토리 목킹
        // - 시퀀스로 조회한 코드 엔티티 반환 설정

        // 서비스 메서드 호출
        // 기대한 결과와 반환된 결과 비교

    }


    @DisplayName("코드 번호로 특정 코드 조회")
    @Test
    void 코드_번호로_특정_코드_조회() {

    }

    @DisplayName("각 파트별 사용 가능한 코드 조회")
    @Test
    void 각_파트별_사용_가능한_코드_조회() {

    }

    @DisplayName("각 파트별 모든 코드 조회")
    @Test
    void 각_파트별_모든_코드_조회() {

    }


    // 코드 수정
    @DisplayName("코드 수정 처리")
    @Test
    void 코드_수정_처리() {

    }


    // 코드 삭제

    @DisplayName("코드 삭제 처리")
    @Test
    void 코드_삭제_처리() {

    }

}