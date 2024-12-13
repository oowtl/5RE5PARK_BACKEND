package com.oreo.finalproject_5re5_be.tts.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.oreo.finalproject_5re5_be.tts.entity.Language;
import com.oreo.finalproject_5re5_be.tts.entity.ServerCode;
import com.oreo.finalproject_5re5_be.tts.entity.Style;
import com.oreo.finalproject_5re5_be.tts.entity.Voice;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class VoiceRepositoryFindTest {
    @Autowired private VoiceRepository voiceRepository;
    @Autowired private LanguageRepository languageRepository;
    @Autowired private StyleRepository styleRepository;

    /*
     * [ voice 조회 테스트 ]
     * 1. 유효한 언어, 스타일 정보로 조회
     * 2. 없는 언어 정보, 유효한 스타일 정보로 조회
     * 3. 유효한 언어 정보, 없는 스타일 정보로 조회
     * 4. 조건 null 값으로 조회
     * */

    // 1. 유효한 언어, 스타일 정보로 조회
    @Test
    @DisplayName("voice 조회 테스트 - 유효한 언어, 스타일 정보로 조회")
    public void voicefindByLangAndStyleTest() {
        // 언어, 스타일 정보 생성
        Language savedLanguage1 = languageRepository.save(createLanguage(0));
        Language savedLanguage2 = languageRepository.save(createLanguage(1));
        Style savedStyle1 = styleRepository.save(createStyle(0));
        Style savedStyle2 = styleRepository.save(createStyle(2));

        // 목소리 데이터 4개 삽입
        List<Voice> voiceList =
                List.of(
                        createVoice(savedLanguage1, savedStyle1, 'y', 0),
                        createVoice(savedLanguage1, savedStyle2, 'y', 1),
                        createVoice(savedLanguage2, savedStyle2, 'y', 2),
                        createVoice(savedLanguage2, savedStyle2, 'y', 3));
        List<Voice> savedVoiceList = voiceRepository.saveAll(voiceList);

        // 언어1, 스타일1 을 만족하는 목소리 조회 결과(list) 검증
        List<Voice> findVoiceList =
                voiceRepository.findAllByLanguageAndStyleAndEnabled(savedLanguage1, savedStyle1, 'y');
        assertEquals(findVoiceList.size(), 1);
        assertEquals(findVoiceList.get(0), savedVoiceList.get(0));

        // 언어2, 스타일2 을 만족하는 결과 리스트 검증
        findVoiceList =
                voiceRepository.findAllByLanguageAndStyleAndEnabled(savedLanguage2, savedStyle2, 'y');
        assertEquals(findVoiceList.size(), 2);
        assertTrue(findVoiceList.contains(savedVoiceList.get(2)));
        assertTrue(findVoiceList.contains(savedVoiceList.get(3)));
    }

    // 2. 없는 언어 정보, 유효한 스타일 정보로 조회
    @Test
    @DisplayName("voice 조회 테스트 - 유효한 언어, 스타일 정보로 조회")
    public void voicefindByLangAndStyleTest_invalidLang() {
        // 존재하지 않는 언어 정보 생성
        Long invalidLang = 9999L;
        Language invalidLanguage = createLanguage(-999);
        invalidLanguage.toBuilder().langSeq(invalidLang).build();
        assertTrue(languageRepository.findById(invalidLang).isEmpty());

        // 유효한 언어, 스타일 정보 생성
        Language savedLanguage = languageRepository.save(createLanguage(1212));
        Style savedStyle = styleRepository.save(createStyle(121));
        assertNotNull(savedStyle);

        // 목소리 데이터 2개 삽입
        List<Voice> voiceList =
                List.of(
                        createVoice(savedLanguage, savedStyle, 'y', 0),
                        createVoice(savedLanguage, savedStyle, 'y', 1));
        List<Voice> savedVoiceList = voiceRepository.saveAll(voiceList);
        assertNotNull(savedVoiceList);

        // 존재하지 않는 언어 정보, 유효한 스타일 정보로 조회시 예외 발생
        assertThrows(
                InvalidDataAccessApiUsageException.class,
                () ->
                        voiceRepository.findAllByLanguageAndStyleAndEnabled(invalidLanguage, savedStyle, 'y'));
    }

    // 3. 유효한 언어 정보, 없는 스타일 정보로 조회
    @Test
    @DisplayName("voice 조회 테스트 - 유효한 언어 정보, 존재하지 않는 스타일 정보로 조회")
    public void voicefindByLangAndStyleTest_invalidStyle() {
        // 존재하지 않는 스타일 정보 생성
        Long invalidId = 9999L;
        Style invalidStyle = createStyle(-999);
        invalidStyle.toBuilder().styleSeq(invalidId).build();
        assertTrue(languageRepository.findById(invalidId).isEmpty());

        // 유효한 언어, 스타일 정보 생성
        Language savedLanguage = languageRepository.save(createLanguage(11));
        Style savedStyle = styleRepository.save(createStyle(22));
        assertNotNull(savedStyle);

        // 목소리 데이터 1개 삽입
        Voice savedVoice = voiceRepository.save(createVoice(savedLanguage, savedStyle, 'y', 1));
        assertNotNull(savedVoice.getVoiceSeq());

        // 존재하지 않는 언어 정보, 유효한 스타일 정보로 조회시 예외 발생
        assertThrows(
                InvalidDataAccessApiUsageException.class,
                () ->
                        voiceRepository.findAllByLanguageAndStyleAndEnabled(savedLanguage, invalidStyle, 'y'));
    }

    // 4. null 값으로 조회
    @Test
    @DisplayName("voice 조회 테스트 - null 값으로 조회")
    public void voicefindByLangAndStyleTest_null() {
        // 유효한 언어, 스타일 정보 생성
        Language savedLanguage = languageRepository.save(createLanguage(11));
        Style savedStyle = styleRepository.save(createStyle(22));
        assertNotNull(savedStyle);

        // 목소리 데이터 1개 삽입
        Voice savedVoice = voiceRepository.save(createVoice(savedLanguage, savedStyle, 'y', 1));
        assertNotNull(savedVoice.getVoiceSeq());

        // 언어, 스타일 정보를 null 값으로 전달하여 조회시 빈 리스트 반환하는지 검증
        List<Voice> findVoiceList =
                voiceRepository.findAllByLanguageAndStyleAndEnabled(null, null, 'y');
        assertTrue(findVoiceList.isEmpty());
    }

    // 목소리 엔티티 생성 메서드
    private Voice createVoice(Language language, Style style, char enabled, int i) {
        return Voice.builder()
                .name("test-voice-name" + i)
                .age(i + 10)
                .server(ServerCode.GOOGLE_CLOUD)
                .enabled(enabled)
                .language(language)
                .style(style)
                .build();
    }

    // 언어 엔티티 생성 메서드
    private Language createLanguage(int i) {
        return Language.builder()
                .langName("test-lang-name" + i)
                .langCode("test-lang-code" + i)
                .regionCode("test-region-code" + i)
                .regionName("test-region-name" + i)
                .enabled('y')
                .build();
    }

    // 스티일 엔티티 생성 메서드
    private Style createStyle(int i) {
        return Style.builder()
                .name("test-style-name" + i)
                .mood("test-style-mood" + i)
                .description("test-style-dest" + i)
                .contents("test-style-contents" + i)
                .build();
    }
}
