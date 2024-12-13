package com.oreo.finalproject_5re5_be.tts.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.oreo.finalproject_5re5_be.global.exception.EntityNotFoundException;
import com.oreo.finalproject_5re5_be.tts.dto.response.VoiceListDto;
import com.oreo.finalproject_5re5_be.tts.entity.Language;
import com.oreo.finalproject_5re5_be.tts.entity.ServerCode;
import com.oreo.finalproject_5re5_be.tts.entity.Style;
import com.oreo.finalproject_5re5_be.tts.entity.Voice;
import com.oreo.finalproject_5re5_be.tts.repository.LanguageRepository;
import com.oreo.finalproject_5re5_be.tts.repository.StyleRepository;
import com.oreo.finalproject_5re5_be.tts.repository.VoiceRepository;
import jakarta.validation.ConstraintViolationException;
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
@TestPropertySource(locations = "classpath:application-ndb-test.properties")
class VoiceServiceTest {
    @Autowired VoiceService voiceService;

    @MockBean VoiceRepository voiceRepository;

    @MockBean LanguageRepository languageRepository;

    @MockBean StyleRepository styleRepository;

    /**
     * [ voice 조회 서비스 테스트 ] 1. 유효한 조건으로 조회 2. 유효하지 않는 조건으로 조회 2-1. 유효한 언어 코드, 유효하지 않는 스타일명으로 조회 2-2.
     * 유효하지 않는 언어 코드, 유효한 스타일명으로 조회 3. 조건이 null 값인 경우의 조회
     */

    // voice 조회 서비스 테스트 - 1. 유효한 조건으로 조회
    @Test
    @DisplayName("voice 조회 서비스 테스트 - 유효한 조건으로 조회")
    public void getVoiceListTest() {
        // 언어, 스타일, 보이스 반환 데이터 생성
        Long langSeq = 10L;
        Long styleSeq = 20L;
        Language language = createLanguageEntity(langSeq);
        Style style = createStyleEntity(styleSeq);

        Voice voice1 = createVoiceEntity(1L, language, style, 'y');
        Voice voice2 = createVoiceEntity(2L, language, style, 'y');
        Voice voice3 = createVoiceEntity(3L, language, style, 'y');
        List<Voice> voiceList = List.of(voice1, voice2, voice3);

        // 레파지토리 동작 설정
        when(languageRepository.findByLangCode(language.getLangCode()))
                .thenReturn(Optional.of(language));
        when(styleRepository.findByName(style.getName())).thenReturn(Optional.of(style));
        when(voiceRepository.findAllByLanguageAndStyleAndEnabled(language, style, 'y'))
                .thenReturn(voiceList);

        // 유효한 조건으로 보이스 목록 조회 테스트 및 검증
        VoiceListDto voiceListDto = voiceService.getVoiceList(language.getLangCode(), style.getName());
        assertEquals(voiceListDto.getVoiceList().size(), voiceList.size());
        assertEquals(voiceListDto.getVoiceList().get(0).getVoiceSeq(), voiceList.get(0).getVoiceSeq());
    }

    // voice 조회 서비스 테스트 - 2-1. 유효한 언어 코드, 유효하지 않는 스타일명으로 조회
    @Test
    @DisplayName("voice 조회 서비스 테스트 - 유효한 언어 코드, 유효하지 않는 스타일명으로 조회")
    public void getVoiceListTest_invalidStyleName() {
        // 언어, 스타일, 보이스 반환 데이터 생성
        Language language = createLanguageEntity(10L);
        Style style = createStyleEntity(20L);

        Voice voice1 = createVoiceEntity(1L, language, style, 'y');
        Voice voice2 = createVoiceEntity(2L, language, style, 'y');
        Voice voice3 = createVoiceEntity(3L, language, style, 'y');
        List<Voice> voiceList = List.of(voice1, voice2, voice3);

        // 레파지토리 동작 설정
        String invalidStyleName = "invalid-style-name";
        when(languageRepository.findByLangCode(language.getLangCode()))
                .thenReturn(Optional.of(language));
        when(styleRepository.findByName(invalidStyleName)).thenReturn(Optional.empty());
        when(voiceRepository.findAllByLanguageAndStyleAndEnabled(language, style, 'y'))
                .thenReturn(voiceList);

        // 유효하지 않은 스타일명으로 조회 시도했기 때문에 예외 발생
        assertThrows(
                EntityNotFoundException.class,
                () -> voiceService.getVoiceList(language.getLangCode(), invalidStyleName));
    }

    // voice 조회 서비스 테스트 - 2-2. 유효하지 않는 언어 코드, 유효한 스타일명으로 조회
    @Test
    @DisplayName("voice 조회 서비스 테스트 - 유효하지 않는 언어 코드, 유효한 스타일명으로 조회")
    public void getVoiceListTest_invalidLangCode() {
        // 언어, 스타일, 보이스 반환 데이터 생성
        Language language = createLanguageEntity(10L);
        Style style = createStyleEntity(20L);

        Voice voice1 = createVoiceEntity(11L, language, style, 'y');
        Voice voice2 = createVoiceEntity(22L, language, style, 'y');
        Voice voice3 = createVoiceEntity(33L, language, style, 'y');
        List<Voice> voiceList = List.of(voice1, voice2, voice3);

        // 레파지토리 동작 설정
        String invalidLangCode = "invalid-language-code";
        when(languageRepository.findByLangCode(invalidLangCode)).thenReturn(Optional.empty());
        when(styleRepository.findByName(style.getName())).thenReturn(Optional.of(style));
        when(voiceRepository.findAllByLanguageAndStyleAndEnabled(language, style, 'y'))
                .thenReturn(voiceList);

        // 유효하지 않은 언어코드로 조회 시도했기 때문에 예외 발생
        assertThrows(
                EntityNotFoundException.class,
                () -> voiceService.getVoiceList(invalidLangCode, style.getName()));
    }

    // voice 조회 서비스 테스트 - 2-2. 조건이 모두 null값일 경우
    @Test
    @DisplayName("voice 조회 서비스 테스트 - 조건이 모두 null값일 경우")
    public void getVoiceListTest_null() {
        // 언어코드 또는 스타일명이 null 값으로 전달되면 ConstraintViolationException 예외 발생
        assertThrows(ConstraintViolationException.class, () -> voiceService.getVoiceList(null, null));
    }

    // 스타일 엔티티 생성 메서드
    private Style createStyleEntity(Long seq) {
        return Style.builder()
                .styleSeq(seq)
                .name("style-name" + seq)
                .mood("style-mood" + seq)
                .description("style-desc" + seq)
                .contents("contents-test" + seq)
                .build();
    }

    private Language createLanguageEntity(Long seq) {
        return Language.builder()
                .langSeq(seq)
                .langCode("lang-code" + seq)
                .langName("lang-name" + seq)
                .regionName("region-name" + seq)
                .regionCode("region-code" + seq)
                .build();
    }

    // 목소리 엔티티 생성 메서드
    private Voice createVoiceEntity(Long seq, Language language, Style style, char enabled) {
        return Voice.builder()
                .voiceSeq(seq)
                .name("test-voice-name" + seq)
                .age(seq.intValue() + 10)
                .server(ServerCode.GOOGLE_CLOUD)
                .useCnt(seq.intValue())
                .isRecommend('N')
                .enabled(enabled)
                .language(language)
                .style(style)
                .build();
    }
}
