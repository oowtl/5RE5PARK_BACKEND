package com.oreo.finalproject_5re5_be.tts.service;

import com.oreo.finalproject_5re5_be.tts.dto.response.LanguageListDto;
import com.oreo.finalproject_5re5_be.tts.entity.Language;
import com.oreo.finalproject_5re5_be.tts.repository.LanguageRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class LanguageServiceTest {

    @Autowired
    LanguageService languageService;

    @MockBean
    LanguageRepository languageRepository;

    /**
     *  [ 언어 전체 조회 테스트 ]
     *  1. 한 개 데이터 조회
     *  2. 여러 개 데이터 조회
     */
    @Test
    @DisplayName("언어 전체 조회 테스트 - 한 개 데이터 조회")
    public void findLanguageListTest() {
        // 반환 받을 객체 생성
        Language language = createLanguageEntity();
        List<Language> languageList = List.of(language);

        // 레파지토리의 언어 조회 결과 세팅
        when(languageRepository.findAll()).thenReturn(languageList);

        // 서비스 수행 및 검증
        LanguageListDto getLanguageListRes = languageService.getLanguageList();
        assertEquals(getLanguageListRes.getLanguageList().get(0).getLanguageCode(), languageList.get(0).getLangCode());
        assertEquals(getLanguageListRes.getLanguageList().get(0).getLanguageName(), languageList.get(0).getLangName());
        assertEquals(getLanguageListRes.getLanguageList().get(0).getRegionName(), languageList.get(0).getRegionName());
    }

    @Test
    @DisplayName("언어 전체 조회 테스트 - 여러 개 데이터 조회")
    public void findLanguageListTest_several() {

        // 3개의 언어 정보가 담긴 리스트 생성
        Language language1 = createLanguageEntity(1);
        Language language2 = createLanguageEntity(2);
        Language language3 = createLanguageEntity(3);

        List<Language> languageList = List.of(language1, language2, language3);

        // 레파지토리의 언어 조회 결과 세팅
        when(languageRepository.findAll()).thenReturn(languageList);

        // 서비스 수행 및 검증
        LanguageListDto getLanguageListRes = languageService.getLanguageList();
        assertEquals(getLanguageListRes.getLanguageList().get(0).getLanguageCode(), languageList.get(0).getLangCode());
        assertEquals(getLanguageListRes.getLanguageList().get(1).getLanguageName(), languageList.get(1).getLangName());
        assertEquals(getLanguageListRes.getLanguageList().get(2).getRegionName(), languageList.get(2).getRegionName());
    }

    private Language createLanguageEntity(){
        return createLanguageEntity(0);
    }
    private Language createLanguageEntity(int n) {
        return Language.builder()
                .langSeq((long)n)
                .langCode("lang-code"+n)
                .langName("lang-name"+n)
                .regionName("region-name"+n)
                .regionCode("region-code"+n)
                .build();
    }

}