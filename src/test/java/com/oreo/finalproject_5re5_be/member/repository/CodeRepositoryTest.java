package com.oreo.finalproject_5re5_be.member.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.oreo.finalproject_5re5_be.code.entity.Code;
import com.oreo.finalproject_5re5_be.code.repository.CodeRepository;
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
class CodeRepositoryTest {


    @Autowired
    private CodeRepository codeRepository;

    private List<Code> dummy = new ArrayList<>();

    @BeforeEach
    void setUp() {
        // 자동주입 확인
        assertNotNull(codeRepository);

        // 초기화
        codeRepository.deleteAll();

        // 더미 데이터 생성 및 저장
        createDummy();
        codeRepository.saveAll(dummy);

        // 더미 데이터 정렬시키기
        dummy.sort((c1, c2) -> c1.getOrd().compareTo(c2.getOrd()));
    }

    @DisplayName("코드 번호로 특정 코드 조회")
    @Test
    void 코드_번호_특정_코드_조회() {
        // 더미 데이터 생성 및 저장
        // 해당 데이터 코드 번호로 조회
        Code foundCode = codeRepository.findCodeByCode("MS001");
        Code expectedCode = dummy.get(0);

        // 결과 동일한지 비교
        assertTrue(isSameCode(expectedCode, foundCode));

    }

    @DisplayName("각 파트별 사용 가능한 코드 조회")
    @Test
    void 각_파트별_사용_가능한_코드_조회() {
        // 회원 파트 여러 더미 데이터 생성 및 저장
        // 해당 파트의 사용 가능한 코드 조회
        List<Code> foundCodes = codeRepository.findAvailableCodesByCateNum("M");
        List<Code> expectedCodes = dummy.stream()
                                        .filter(c -> c.getCateNum().equals("M") && c.getChkUse().equals("Y"))
                                        .toList();

        // 결과 동일한지 비교
        assertTrue(foundCodes.size() == expectedCodes.size());
        for (int i = 0; i < foundCodes.size(); i++) {
            assertTrue(isSameCode(expectedCodes.get(i), foundCodes.get(i)));
        }

    }

    @DisplayName("각 파트별 모든 코드 조회")
    @Test
    void 각_파트별_모든_코드_조회() {
        // 회원 파트 여러 더미 데이터 생성 및 저장
        // 해당 파트의 모든 코드 조회
        List<Code> foundCodes = codeRepository.findCodesByCateNum("M");

        // 결과 동일한지 비교
        assertTrue(foundCodes.size() == dummy.size());


        for (int i = 0; i < foundCodes.size(); i++) {
            assertTrue(isSameCode(dummy.get(i), foundCodes.get(i)));
        }

    }

    private void createDummy() {
        // 더미 데이터 생성
        Code code1 = Code.builder()
                .code("MS001")
                .cateNum("M")
                .name("신규회원")
                .chkUse("Y")
                .ord(1)
                .build();

        Code code2 = Code.builder()
                .code("MS002")
                .cateNum("M")
                .name("VIP회원")
                .chkUse("N")
                .ord(2)
                .build();

        Code code3 = Code.builder()
                .code("MS003")
                .cateNum("M")
                .name("비활성회원")
                .chkUse("Y")
                .ord(3)
                .build();

        dummy.add(code1);
        dummy.add(code2);
        dummy.add(code3);
    }

    private boolean isSameCode(Code expected, Code actual) {
        // 코드 동일한지 비교
        return expected.getCode().equals(actual.getCode())
                && expected.getCateNum().equals(actual.getCateNum())
                && expected.getName().equals(actual.getName())
                && expected.getChkUse().equals(actual.getChkUse())
                && expected.getOrd().equals(actual.getOrd());
    }

}