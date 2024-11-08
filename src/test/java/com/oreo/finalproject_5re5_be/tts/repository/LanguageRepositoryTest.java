package com.oreo.finalproject_5re5_be.tts.repository;

import com.oreo.finalproject_5re5_be.tts.entity.Language;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class LanguageRepositoryTest {
    @Autowired
    private LanguageRepository languageRepository;

    /*
    languageRepository 생성 테스트
    1. 생성 테스트 - 전체 데이터 입력
    2. 생성 테스트 - 일부 데이터 입력
    3. 생성 테스트 - 데이터 미포함

    languageRepository 조회 테스트
    1. 조회 테스트 - 단건 데이터 조회
    2. 조회 테스트 - 다건 데이터 조회

    languageRepository 수정 테스트
    1. 수정 테스트 - 전체 데이터 수정
    2. 수정 테스트 - 일부 데이터 수정
    3. 수정 테스트 - 일부 데이터 수정 시 필수 값 미포함

    languageRepository 삭제 테스트
    1. 삭제 테스트 - 단건 데이터 삭제
     */

    // 1. 생성 테스트 - 전체 데이터 입력
    @Test
    @DisplayName("languageRepository 생성 테스트 - 전체 데이터 입력")
    public void createLanguageWithAllDataTest() {
        // given
        // 1. Language 객체 생성
        Language createLanguage = languageCreate();

        // when
        // 2. Language 객체 저장
        Language savedLanguage = languageRepository.save(createLanguage);

        // then
        // 3. 저장된 Language 객체 조회
        Optional<Language> optionalResearchedLanguage = languageRepository.findById(savedLanguage.getLangSeq());
        assertTrue(optionalResearchedLanguage.isPresent());

        // 4. 생성한 Language 객체와 조회한 Language 객체 비교
        Language researchedLanguage = optionalResearchedLanguage.get();
        assertEquals(createLanguage, researchedLanguage);
    }

    // 2. 생성 테스트 - 일부 데이터 입력
    @Test
    @DisplayName("languageRepository 생성 테스트 - 일부 데이터 입력")
    public void createLanguageWithPartialDataTest() {
        // given
        // 1. Language 객체 생성
        Language createLanguage = Language.builder()
                .langCode("en-US")
                .langName("English")
                .build();

        // when, then
        // 2. Language 객체 저장
        assertThrows(DataIntegrityViolationException.class, () -> languageRepository.save(createLanguage));
    }


    // 3. 생성 테스트 - 데이터 미포함
    @Test
    @DisplayName("languageRepository 생성 테스트 - 데이터 미포함")
    public void createLanguageWithoutDataTest() {
        // given
        // 1. Language 객체 생성
        Language createLanguage = Language.builder().build();

        // when, then
        // 2. Language 객체 저장
        assertThrows(DataIntegrityViolationException.class, () -> languageRepository.save(createLanguage));
    }

    // 1. 조회 테스트 - 단건 데이터 조회
    @Test
    @DisplayName("languageRepository 조회 테스트 - 단건 데이터 조회")
    public void readOneLanguageTest() {
        // given
        // 1. Language 객체 생성
        Language createLanguage = languageCreate();

        // 2. Language 객체 저장
        Language savedLanguage = languageRepository.save(createLanguage);

        // when
        // 3. 저장된 Language 객체 조회
        Optional<Language> optionalResearchedLanguage = languageRepository.findById(savedLanguage.getLangSeq());
        assertTrue(optionalResearchedLanguage.isPresent());

        // then
        // 4. 저장된 Language 객체와 조회한 Language 객체 비교
        Language researchedLanguage = optionalResearchedLanguage.get();
        assertEquals(createLanguage, researchedLanguage);
    }

    // 2. 조회 테스트 - 다건 데이터 조회
    @Test
    @DisplayName("languageRepository 조회 테스트 - 다건 데이터 조회")
    public void readManyLanguageTest() {
        // given
        int repeatCount = 10;
        List<Language> languageList = new ArrayList<>();

        // 1. Language 객체 생성
        for (int i = 0; i < repeatCount; i++) {
            Language createLanguage = languageCreate(i);

            // 2. Language 객체 저장
            Language savedLanguage = languageRepository.save(createLanguage);

            // 3. 저장된 Language 객체를 배열에 저장
            languageList.add(savedLanguage);
        }

        // when, then
        // 4. 저장된 Language 객체 조회
        for (Language language : languageList) {

            // 5. 저장된 Language 객체와 조회한 Language 객체 비교
            // 저장된 객체 조회
            Optional<Language> optionalResearchedLanguage = languageRepository.findById(language.getLangSeq());
            assertTrue(optionalResearchedLanguage.isPresent());

            // 조회한 객체와 저장된 객체 비교
            Language researchedLanguage = optionalResearchedLanguage.get();
            assertEquals(language, researchedLanguage);
        }
    }

    // 1. 수정 테스트 - 전체 데이터 수정
    @Test
    @DisplayName("languageRepository 수정 테스트 - 전체 데이터 수정")
    public void updateLanguageWithAllDataTest() {
        // given
        String updateLangCode = "en-US";
        String updateLangName = "English";
        String updateRegionCode = "en-US";
        String updateRegionName = "United States";
        char updateEnabled = 'N';

        // 1. Language 객체 생성
        Language createLanguage = languageCreate();

        // 2. Language 객체 저장
        Language savedLanguage = languageRepository.save(createLanguage);

        // when
        // 3. Language 수정하기
        Language updateLanguage = savedLanguage.toBuilder()
                .langCode("en-US")
                .langName("English")
                .regionCode("en-US")
                .regionName("United States")
                .enabled('N')
                .build();

        // 4. Language 객체 수정 (save)
        Language updatedLanguage = languageRepository.save(updateLanguage);

        // then
        // 5. 수정된 Language 객체 조회
        Optional<Language> optionalManipulatedLanguage = languageRepository.findById(updatedLanguage.getLangSeq());
        assertTrue(optionalManipulatedLanguage.isPresent());

        // 6. 수정된 Language 객체와 조회한 Language 객체 비교
        Language manipulatedLanguage = optionalManipulatedLanguage.get();
        assertEquals(updateLanguage, manipulatedLanguage);
    }

    // 2. 수정 테스트 - 일부 데이터 수정
    @Test
    @DisplayName("languageRepository 수정 테스트 - 일부 데이터 수정")
    public void updateLanguageWithPartialDataTest() {
        // given
        String updateLangCode = "en-US";
        String updateLangName = "English";

        // 1. Language 객체 생성
        Language createLanguage = languageCreate();

        // 2. Language 객체 저장
        Language savedLanguage = languageRepository.save(createLanguage);

        // when
        // 3. Language 수정하기
        Language updateLanguage = savedLanguage.toBuilder()
                .langCode("en-US")
                .langName("English")
                .build();

        // 4. Language 객체 수정 (save)
        Language updatedLanguage = languageRepository.save(updateLanguage);

        // then
        // 5. 수정된 Language 객체 조회
        Optional<Language> optionalManipulatedLanguage = languageRepository.findById(updatedLanguage.getLangSeq());
        assertTrue(optionalManipulatedLanguage.isPresent());

        // 6. 수정된 Language 객체와 조회한 Language 객체 비교
        Language manipulatedLanguage = optionalManipulatedLanguage.get();
        assertEquals(updateLanguage, manipulatedLanguage);
    }

    // 3. 수정 테스트 - 일부 데이터 수정 시 필수 값 미포함
    @Test
    @DisplayName("languageRepository 수정 테스트 - 일부 데이터 수정 시 필수 값 미포함")
    public void updateLanguageWithoutRequiredDataTest() {
        // given
        // 1. Language 객체 생성
        Language createLanguage = languageCreate();

        // 2. Language 객체 저장
        Language savedLanguage = languageRepository.save(createLanguage);
        Long savedLanguageSeq = savedLanguage.getLangSeq();

        // when
        // 3. Language 수정하기 (필수 값 미포함)
        Language updateLanguage = Language.builder()
                .langSeq(savedLanguageSeq)
                .langCode("en-US")
                .build();

        // then
        // 4. Language 객체 수정 (save)
        assertThrows(DataIntegrityViolationException.class, () -> languageRepository.save(updateLanguage));
    }

    // 1. 삭제 테스트 - 단건 데이터 삭제
    @Test
    @DisplayName("languageRepository 삭제 테스트 - 단건 데이터 삭제")
    public void deleteOneLanguageTest() {
        // given
        // 1. Language 객체 생성
        Language createLanguage = languageCreate();

        // 2. Language 객체 저장
        Language savedLanguage = languageRepository.save(createLanguage);
        Long savedLanguageSeq = savedLanguage.getLangSeq();

        // when
        // 3. Language 객체 삭제
        languageRepository.deleteById(savedLanguageSeq);

        // then
        // 4. Language 객체 삭제 확인
        Optional<Language> optionalDeletedLanguage = languageRepository.findById(savedLanguageSeq);
        assertTrue(optionalDeletedLanguage.isEmpty());
    }

    // Language 객체 생성 메서드
    private static Language languageCreate() {
        return Language.builder()
                .langCode("ko-KR")
                .langName("한국어")
                .regionCode("ko-KR")
                .regionName("대한민국")
                .enabled('Y')
                .build();
    }

    private static Language languageCreate(int cnt) {
        return Language.builder()
                .langCode("ko-KR" + cnt)
                .langName("한국어" + cnt)
                .regionCode("ko-KR" + cnt)
                .regionName("대한민국" + cnt)
                .enabled('Y')
                .build();
    }
}
