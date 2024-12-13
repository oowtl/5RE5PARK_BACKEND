package com.oreo.finalproject_5re5_be.tts.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.oreo.finalproject_5re5_be.global.exception.EntityNotFoundException;
import com.oreo.finalproject_5re5_be.tts.dto.response.StyleListDto;
import com.oreo.finalproject_5re5_be.tts.entity.Language;
import com.oreo.finalproject_5re5_be.tts.entity.Style;
import com.oreo.finalproject_5re5_be.tts.repository.LanguageRepository;
import com.oreo.finalproject_5re5_be.tts.repository.StyleRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class StyleServiceTest {
    @Autowired StyleService styleService;

    @MockBean StyleRepository styleRepository;

    @MockBean LanguageRepository languageRepository;

    /**
     * [ 스타일 조회 서비스 테스트 ] 1. 여러 개의 데이터가 있는 목록 조회 테스트 2. 빈 목록 조회 테스트
     *
     * <p>[ 언어 기반 목소리가 있는 스타일만 조회 서비스 테스트 ] 1. 유효한 언어코드로 조회 2. 유효하지 않는 언어코드로 조회
     */
    @Test
    @DisplayName("스타일 조회 테스트 - 여러 개의 데이터가 있는 목록")
    public void getStyleListTest() {
        // 3개 데이터가 들어간 리스트 생성
        int cnt = 3;
        List<Style> styleList = new ArrayList<>();
        for (int i = 0; i < cnt; i++) {
            styleList.add(createStyleEntity(i));
        }

        // style 레파지토리 findAll 메서드의 동작 세팅
        when(styleRepository.findAll()).thenReturn(styleList);

        // 스타일 전체 조회 서비스 실행 및 검증
        StyleListDto styleListDto = styleService.getStyleList();
        assertEquals(styleListDto.getStyleList().get(0).getContents(), styleList.get(0).getContents());
        assertEquals(styleListDto.getStyleList().get(1).getMood(), styleList.get(1).getMood());
        assertEquals(styleListDto.getStyleList().get(2).getDesc(), styleList.get(2).getDescription());
    }

    @Test
    @DisplayName("스타일 조회 테스트 - 빈 목록")
    public void getEmptyStyleListTest() {
        // 빈 리스트 생성
        List<Style> styleList = new ArrayList<>();

        // style 레파지토리 findAll 메서드의 동작 세팅
        when(styleRepository.findAll()).thenReturn(styleList);

        // 스타일 전체 조회 서비스 실행 및 검증
        StyleListDto styleListDto = styleService.getStyleList();
        assertTrue(styleListDto.getStyleList().isEmpty()); // 비어있어야함
    }

    // 언어 기반 목소리가 있는 스타일만 조회 서비스 테스트
    // 1. 유효한 언어코드로 조회
    @Test
    @DisplayName("언어 기반 목소리가 있는 스타일만 조회 테스트 - 유효한 언어코드로 조회")
    public void getStyleByValidLangTest() {
        // 1. given: language, style 엔티티 1개 생성
        Long langSeq = 10L;
        Long styleSeq = 1000L;
        Language language = createLanguageEntity(langSeq.intValue());
        Style style = createStyleEntity(styleSeq.intValue());

        // 2. when
        // 2-1. 언어 코드로 조회할 때 언어 정보가 조회되도록 세팅
        when(languageRepository.findByLangCode(language.getLangCode()))
                .thenReturn(Optional.of(language));
        // 2-2. 언어 식별번호로 조회할 때 스타일 정보가 조회되도록 세팅
        when(styleRepository.findListBylangSeq(langSeq)).thenReturn(List.of(style));

        // 3. then: 서비스 호출시 한 개의 스타일 정보가 담긴 리스트가 StyleListDto 객체에 담겨져 리턴되어야 함
        StyleListDto styleListDto = styleService.getStyleListByLang(language.getLangCode());
        assertNotNull(styleListDto);
        assertEquals(styleListDto.getStyleList().size(), 1);
        assertEquals(styleListDto.getStyleList().get(0).getName(), style.getName());
    }

    // 2. 유효하지 않는 언어코드로 조회
    @Test
    @DisplayName("언어 기반 목소리가 있는 스타일만 조회 테스트 - 유효한 언어코드로 조회")
    public void getStyleByinvalidLangTest() {
        // 1. given: 존재하지 않는 언어 코드 초기화
        String notExistLangCode = "not-exist-lang-code";

        // 2. when: 존재하지 않는 언어 코드로 조회시 빈 Optional 객체를 반환하도록 세팅
        when(languageRepository.findByLangCode(notExistLangCode)).thenReturn(Optional.empty());

        // 3. when: 존재하지 않는 언어 코드로 조회하면 EntityNotFoundException 발생해야함
        assertThrows(
                EntityNotFoundException.class, () -> styleService.getStyleListByLang(notExistLangCode));
    }

    // 스타일 엔티티 생성 메서드
    private Style createStyleEntity(int n) {
        return Style.builder()
                .styleSeq((long) n)
                .name("style-name" + n)
                .mood("style-mood" + n)
                .description("style-desc" + n)
                .contents("contents-test" + n)
                .build();
    }

    // 언어 엔티티 생성 메서드
    private Language createLanguageEntity(int n) {
        return Language.builder()
                .langSeq((long) n)
                .langCode("lang-code" + n)
                .langName("lang-name" + n)
                .regionName("region-name" + n)
                .regionCode("region-code" + n)
                .build();
    }
}
