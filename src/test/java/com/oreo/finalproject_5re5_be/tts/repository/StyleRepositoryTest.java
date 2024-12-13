package com.oreo.finalproject_5re5_be.tts.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.oreo.finalproject_5re5_be.tts.entity.Language;
import com.oreo.finalproject_5re5_be.tts.entity.ServerCode;
import com.oreo.finalproject_5re5_be.tts.entity.Style;
import com.oreo.finalproject_5re5_be.tts.entity.Voice;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class StyleRepositoryTest {

    @Autowired private StyleRepository styleRepository;
    @Autowired private LanguageRepository languageRepository;
    @Autowired private VoiceRepository voiceRepository;

    /*
    StyleRepository 생성 테스트
    1. 생성 테스트 - 전체 데이터 입력
    2. 생성 테스트 - 일부 데이터 입력 (필수 값 포함)
    3. 생성 테스트 - 일부 데이터 입력 (필수 값 미포함)

    StyleRepository 조회 테스트
    1. 조회 테스트 - 단건 데이터 조회
    2. 조회 테스트 - 다건 데이터 조회

    StyleRepository 수정 테스트
    1. 수정 테스트 - 전체 데이터 수정
    2. 수정 테스트 - 일부 데이터 수정 (필수 값 변경 포함)
    3. 수정 테스트 - 일부 데이터 수정 (필수 값 변경 제외)
    4. 수정 테스트 - 일부 데이터 수정 (필수 값 제외 변경)

    StyleRepository 삭제 테스트
    1. 삭제 테스트 - 데이터 삭제


    language 기반 보이스가 존재하는 style 조회 테스트
    1. 조회 테스트 - 단건 데이터 조회
    2. 조회 테스트 - 다건 데이터 조회
     */

    // StyleRepository 생성 테스트
    // 1. 생성 테스트 - 전체 데이터 입력
    @Test
    @DisplayName("StyleRepository 생성 테스트 - 전체 데이터 입력")
    public void createEntireDataTest() {
        // given
        // 1. Style Entity 객체 생성
        Style createStyle = createStyleEntity();

        // when
        // 2. Style Entity 객체 저장
        Style savedStyle = styleRepository.save(createStyle);
        // 조회용 seq 저장
        Long savedStyleSeq = savedStyle.getStyleSeq();

        // then
        // 3. Style Entity 객체 조회
        Optional<Style> optionalResearchedStyle = styleRepository.findById(savedStyleSeq);
        assertTrue(optionalResearchedStyle.isPresent());

        // 4. 조회한 Style Entity 객체의 값 검증
        Style researchedStyle = optionalResearchedStyle.get();
        assertEquals(researchedStyle, savedStyle);
    }

    // 2. 생성 테스트 - 일부 데이터 입력 (필수 값 포함)
    @Test
    @DisplayName("StyleRepository 생성 테스트 - 일부 데이터 입력 (필수 값 포함)")
    public void createPartialDataWithRequiredTest() {
        // given
        // 1. Style Entity 객체 생성
        Style createStyle = Style.builder().name("styleName").mood("styleMood").build();

        // when
        // 2. Style Entity 객체 저장
        Style savedStyle = styleRepository.save(createStyle);
        // 조회용 seq 저장
        Long savedStyleSeq = savedStyle.getStyleSeq();

        // then
        // 3. Style Entity 객체 조회
        Optional<Style> optionalResearchedStyle = styleRepository.findById(savedStyleSeq);
        assertTrue(optionalResearchedStyle.isPresent());

        // 4. 조회한 Style Entity 객체의 값 검증
        Style researchedStyle = optionalResearchedStyle.get();
        assertEquals(researchedStyle, savedStyle);
    }

    // 3. 생성 테스트 - 일부 데이터 입력 (필수 값 미포함)
    @Test
    @DisplayName("StyleRepository 생성 테스트 - 일부 데이터 입력 (필수 값 미포함)")
    public void createPartialDataWithoutRequiredTest() {
        // given
        // 1. Style Entity 객체 생성
        Style createStyle =
                Style.builder()
                        .mood("styleMood")
                        .contents("styleContents")
                        .description("styleDescription")
                        .build();

        // when, then
        // 2. Style Entity 객체 저장
        assertThrows(DataIntegrityViolationException.class, () -> styleRepository.save(createStyle));
    }

    // 1. 조회 테스트 - 단건 데이터 조회
    @Test
    @DisplayName("StyleRepository 조회 테스트 - 단건 데이터 조회")
    public void searchOneDataTest() {
        // given
        // 1. Style Entity 객체 생성
        Style createStyle = createStyleEntity();

        // 2. Style Entity 객체 저장
        Style savedStyle = styleRepository.save(createStyle);
        // 조회용 seq 저장
        Long savedStyleSeq = savedStyle.getStyleSeq();

        // when
        // 3. Style Entity 객체 조회
        Optional<Style> optionalResearchedStyle = styleRepository.findById(savedStyleSeq);
        assertTrue(optionalResearchedStyle.isPresent());

        // then
        Style researchedStyle = optionalResearchedStyle.get();
        assertEquals(researchedStyle, savedStyle);
    }

    // 2. 조회 테스트 - 다건 데이터 조회
    @Test
    @DisplayName("StyleRepository 조회 테스트 - 다건 데이터 조회")
    public void searchManyDataTest() {
        // given
        int repeatCnt = 10;
        List<Style> sytleList = new ArrayList<Style>();

        // 1. Style Entity 객체 생성 및 저장
        for (int i = 0; i < repeatCnt; i++) {
            Style createStyle = createStyleEntity(i);
            styleRepository.save(createStyle);

            // 조회용 리스트에 저장
            sytleList.add(createStyle);
        }

        // when, then
        // 2. Style Entity 객체 전체 조회
        for (Style style : sytleList) {
            // 3. Style Entity 조회 및 존재 여부 검증
            Optional<Style> optionalResearchedStyle = styleRepository.findById(style.getStyleSeq());
            assertTrue(optionalResearchedStyle.isPresent());

            Style researchedStyle = optionalResearchedStyle.get();
            assertEquals(researchedStyle, style);
        }
    }

    // 1. 수정 테스트 - 전체 데이터 수정
    @Test
    @DisplayName("StyleRepository 수정 테스트 - 전체 데이터 수정")
    public void updateEntireDataTest() {
        // given
        String updateName = "updateStyleName";
        String updateMood = "updateStyleMood";
        String updateContents = "updateStyleContents";
        String updateDescription = "updateStyleDescription";
        ServerCode updateServer = ServerCode.GOOGLE_CLOUD;
        char updateEnabled = 'n';

        // 1. Style Entity 객체 생성
        Style createStyle = createStyleEntity();

        // 2. Style Entity 객체 저장
        Style savedStyle = styleRepository.save(createStyle);
        // 조회용 seq 저장
        Long savedStyleSeq = savedStyle.getStyleSeq();

        // when
        // 4. 조회한 seq 로 Style Entity 객체 수정
        Style updateStyle =
                Style.builder()
                        .styleSeq(savedStyleSeq)
                        .name(updateName)
                        .mood(updateMood)
                        .contents(updateContents)
                        .description(updateDescription)
                        .server(updateServer)
                        .enabled(updateEnabled)
                        .build();

        // 5. 수정된 Style Entity 객체 저장
        Style manipulatedStyle = styleRepository.save(updateStyle);

        // then
        // 6. 수정된 Style Entity 객체 조회
        Optional<Style> optionalResearchedStyle =
                styleRepository.findById(manipulatedStyle.getStyleSeq());
        assertTrue(optionalResearchedStyle.isPresent());

        // 7. 수정된 Style Entity 객체의 값 검증
        Style researchedStyle = optionalResearchedStyle.get();
        assertEquals(updateStyle, researchedStyle);
    }

    // 2. 수정 테스트 - 일부 데이터 수정 (필수 값 변경 포함)
    @Test
    @DisplayName("StyleRepository 수정 테스트 - 일부 데이터 수정 (필수 값 변경 포함)")
    public void updatePartialDataWithRequiredTest() {
        // given
        String updateName = "updateStyleName";
        String updateMood = "updateStyleMood";

        // 1. Style Entity 객체 생성
        Style createStyle = createStyleEntity();

        // 2. Style Entity 객체 저장
        Style savedStyle = styleRepository.save(createStyle);
        assertEquals(createStyle, savedStyle);

        // 조회용 seq 저장
        Long savedStyleSeq = savedStyle.getStyleSeq();

        // when
        // 3. 조회한 seq 로 Style Entity 객체 수정
        Style updateStyle =
                Style.builder().styleSeq(savedStyleSeq).name(updateName).mood(updateMood).build();

        // 4. 수정된 Style Entity 객체 저장
        Style manipulatedStyle = styleRepository.save(updateStyle);
        assertEquals(updateStyle, manipulatedStyle);

        // then
        // 5. 수정된 Style Entity 객체 조회
        Optional<Style> optionalResearchedStyle =
                styleRepository.findById(manipulatedStyle.getStyleSeq());
        assertTrue(optionalResearchedStyle.isPresent());

        // 6. 수정된 Style Entity 객체의 값 검증
        Style researchedStyle = optionalResearchedStyle.get();
        assertEquals(updateStyle, researchedStyle);
    }

    // 3. 수정 테스트 - 일부 데이터 수정 (필수 값 변경 제외)
    @Test
    @DisplayName("StyleRepository 수정 테스트 - 일부 데이터 수정 (필수 값 변경 제외)")
    public void updatePartialDataWithoutRequiredTest() {
        // given
        String updateMood = "updateStyleMood";
        String updateContents = "updateStyleContents";
        String updateDescription = "updateStyleDescription";

        // 1. Style Entity 객체 생성
        Style createStyle = createStyleEntity();

        // 2. Style Entity 객체 저장
        Style savedStyle = styleRepository.save(createStyle);
        assertEquals(createStyle, savedStyle);

        // 조회용 seq 저장
        Long savedStyleSeq = savedStyle.getStyleSeq();

        // when
        // 3. 조회한 seq 로 Style Entity 객체 수정
        Style updateStyle =
                savedStyle.toBuilder()
                        .mood(updateMood)
                        .contents(updateContents)
                        .description(updateDescription)
                        .build();

        // 4. 수정된 Style Entity 객체 저장
        Style manipulatedStyle = styleRepository.save(updateStyle);
        assertEquals(updateStyle, manipulatedStyle);

        // then
        // 5. 수정된 Style Entity 객체 조회
        Optional<Style> optionalResearchedStyle =
                styleRepository.findById(manipulatedStyle.getStyleSeq());
        assertTrue(optionalResearchedStyle.isPresent());

        // 6. 수정된 Style Entity 객체의 값 검증
        Style researchedStyle = optionalResearchedStyle.get();
        assertEquals(updateStyle, researchedStyle);
    }

    // 4. 수정 테스트 - 데이터 수정 실패 (존재하지 않는 데이터)
    @Test
    @DisplayName("StyleRepository 수정 테스트 - 일부 데이터 수정 (필수 값 제외 변경)")
    public void updateFailTest() {
        // given
        String updateStyleMood = "updateStyleMood";
        // 1. Style Entity 객체 생성
        Style createStyle = createStyleEntity();

        // 2. Style Entity 객체 저장
        Style savedStyle = styleRepository.save(createStyle);
        assertEquals(createStyle, savedStyle);

        // 수정용 seq 저장
        Long savedStyleSeq = savedStyle.getStyleSeq();

        // when, then
        // 3. 존재하지 않는 seq 로 Style Entity 객체 수정
        Style updateStyle = Style.builder().styleSeq(savedStyleSeq).mood(updateStyleMood).build();

        // 4. 수정된 Style Entity 객체 저장
        assertThrows(DataIntegrityViolationException.class, () -> styleRepository.save(updateStyle));
    }

    // 1. 삭제 테스트 - 데이터 삭제
    @Test
    @DisplayName("StyleRepository 삭제 테스트 - 데이터 삭제")
    public void deleteDataTest() {
        // given
        // 1. Style Entity 객체 생성
        Style createStyle = createStyleEntity();

        // 2. Style Entity 객체 저장
        Style savedStyle = styleRepository.save(createStyle);
        assertEquals(createStyle, savedStyle);

        // 삭제용 seq 저장
        Long savedStyleSeq = savedStyle.getStyleSeq();

        // when
        // 3. 저장한 Style Entity 객체 삭제
        styleRepository.deleteById(savedStyleSeq);

        // then
        // 4. 삭제한 Style Entity 객체 조회
        Optional<Style> optionalResearchedStyle = styleRepository.findById(savedStyleSeq);
        assertTrue(optionalResearchedStyle.isEmpty());
    }

    // language 기반 보이스가 존재하는 style 조회 테스트
    // 1. 조회 테스트 - 단건 데이터 조회
    @Test
    @DisplayName(" language 기반 보이스가 존재하는 style 조회 테스트 - 단건 데이터 조회")
    public void findListByLangSeqTest() {
        // given: language, style, voice 엔티티 생성
        Language language = languageRepository.save(createLanguageEntity());
        assertNotNull(language.getLangSeq());
        Style style = styleRepository.save(createStyleEntity());
        assertNotNull(style.getStyleSeq());
        Voice voice = voiceRepository.save(createVoiceEntity(language, style));
        assertNotNull(voice.getVoiceSeq());

        // when: langSeq로 조회시
        List<Style> findStyleList = styleRepository.findListBylangSeq(language.getLangSeq());

        // then: 한 개의 style 엔티티가 조회되어야 한다
        assertEquals(findStyleList.size(), 1);
        assertEquals(findStyleList.get(0).getStyleSeq(), style.getStyleSeq());
    }

    // 2. 조회 테스트 - 다건 데이터 조회
    @Test
    @DisplayName(" language 기반 보이스가 존재하는 style 조회 테스트 - 다건 데이터 조회")
    public void findListByLangSeqTest_several() {
        // 1. given
        // 1-1. language 엔티티 2개 추가
        Language language1 = languageRepository.save(createLanguageEntity(1));
        assertNotNull(language1.getLangSeq());
        Language language2 = languageRepository.save(createLanguageEntity(2));
        assertNotNull(language2.getLangSeq());

        // 1-2. style 엔티티 3개 추가
        Style style1 = styleRepository.save(createStyleEntity(10));
        assertNotNull(style1.getStyleSeq());
        Style style2 = styleRepository.save(createStyleEntity(20));
        assertNotNull(style2.getStyleSeq());
        Style style3 = styleRepository.save(createStyleEntity(30));
        assertNotNull(style3.getStyleSeq());

        // 1-3. 1번 language와 1번 style을 가진 voice 엔티티 1개 추가
        Voice voice1 = voiceRepository.save(createVoiceEntity(language1, style1, 1000));
        assertNotNull(voice1.getVoiceSeq());

        // 1-4. 1번 language와 2번 style을 가진 voice 엔티티 2개 추가
        Voice voice2 = voiceRepository.save(createVoiceEntity(language1, style2, 2000));
        assertNotNull(voice2.getVoiceSeq());
        Voice voice3 = voiceRepository.save(createVoiceEntity(language1, style2, 3000));
        assertNotNull(voice3.getVoiceSeq());

        // 2. when, then
        // 2-1. 1번 language 식별 번호로 조회한 결과 2개의 엔티티가 조회되어야 한다.
        List<Style> styleListByLang1 = styleRepository.findListBylangSeq(language1.getLangSeq());
        assertEquals(styleListByLang1.size(), 2);
        assertTrue(styleListByLang1.containsAll(List.of(style1, style2)));

        // 2-2. 2번 language 식별 번호로 조회한 결과 0개의 엔티티가 조회되어야 한다.
        List<Style> styleListByLang2 = styleRepository.findListBylangSeq(language2.getLangSeq());
        assertEquals(styleListByLang2.size(), 0);
    }

    // Style Entity 객체 생성 메서드
    private Style createStyleEntity() {
        return Style.builder()
                .name("styleName")
                .mood("styleMood")
                .contents("styleContents")
                .description("styleDescription")
                .server(ServerCode.NAVER_CLOVA)
                .enabled('y')
                .build();
    }

    private Style createStyleEntity(int cnt) {
        return Style.builder()
                .name("styleName" + cnt)
                .mood("styleMood" + cnt)
                .contents("styleContents" + cnt)
                .description("styleDescription" + cnt)
                .server(ServerCode.GOOGLE_CLOUD)
                .enabled('y')
                .build();
    }

    private Language createLanguageEntity() {
        return createLanguageEntity(0);
    }

    private Language createLanguageEntity(int i) {
        return Language.builder()
                .langCode("ts-Test" + i)
                .langName("test-lang_name" + i)
                .regionCode("ts-region" + i)
                .regionName("ts-region-name" + i)
                .build();
    }

    private Voice createVoiceEntity(Language language, Style style) {
        return createVoiceEntity(language, style, 0);
    }

    private Voice createVoiceEntity(Language language, Style style, int i) {
        return Voice.builder()
                .style(style)
                .language(language)
                .name("test-voice-name" + i)
                .age(0)
                .isRecommend('n')
                .enabled('y')
                .useCnt(0)
                .gender("female" + i)
                .server(ServerCode.GOOGLE_CLOUD)
                .description("test-description" + i)
                .build();
    }
}
