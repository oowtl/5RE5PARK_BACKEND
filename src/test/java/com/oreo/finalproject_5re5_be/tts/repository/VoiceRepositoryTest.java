package com.oreo.finalproject_5re5_be.tts.repository;

import com.oreo.finalproject_5re5_be.tts.entity.Language;
import com.oreo.finalproject_5re5_be.tts.entity.Style;
import com.oreo.finalproject_5re5_be.tts.entity.Voice;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.Mockito.mock;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class VoiceRepositoryTest {
    @Autowired
    private VoiceRepository voiceRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private StyleRepository styleRepository;

    private static String TEST_NAME = "TEST_NAME";

    /*
    VoiceRepository 생성 테스트
    1. 생성 테스트 - 전체 데이터 입력
    2. 생성 테스트 - 일부 데이터 입력 (필수 값 포함)
    3. 생성 테스트 - 일부 데이터 입력 (필수 값 미포함)
    4. 생성 테스트 - 전체 데이터 미입력 (null)

    VoiceRepository 조회 테스트
    1. 조회 테스트 - 단건 데이터 조회
    2. 조회 테스트 - 조건에 맞는 단건 데이터 조회
    3. 조회 테스트 - 다건 조회
    4. 조회 테스트 - 조건에 맞는 다건 데이터 조회

    VoiceRepository 수정 테스트
    1. 수정 테스트 - 전체 데이터 수정 (연관관계 포함)
    2. 수정 테스트 - 전체 데이터 수정 (연관관계 미포함)
    3. 수정 테스트 - 일부 데이터 수정 (연관관계 포함)
    4. 수정 테스트 - 일부 데이터 수정 (연관관계 미포함)
    5. 수정 테스트 - 일부 데이터 null 으로 수정 (server)

    VoiceRepository 삭제 테스트
    1. 삭제 테스트 - 전체 데이터 삭제
     */


    // 1. 생성 테스트 - 전체 데이터 입력
    @Test
    @DisplayName("VoiceRepository 생성 테스트 - 전체 데이터 입력")
    public void createEntireDataTest() {
        // given
        // 1. Language 객체 생성 및 저장
        Language createLanguage = languageCreate();
        Language language = languageRepository.save(createLanguage);

        // 2. Style 객체 생성 및 저장
        Style createStyle = createStyleEntity();
        Style style = styleRepository.save(createStyle);

        // 3. Voice 객체 생성 (연관 관계 매핑 포함)
        Voice createVoice = createVoiceWithoutMappings();
        createVoice.toBuilder()
                .language(language)
                .style(style)
                .build();

        // when
        // 4. Voice 객체 저장
        Voice savedVoice = voiceRepository.save(createVoice);
        assertNotNull(savedVoice);

        // then
        // 4. 저장된 Voice 객체 조회 및 검증
        Optional<Voice> optionalResearchedVoice = voiceRepository.findById(savedVoice.getVoiceSeq());
        assertTrue(optionalResearchedVoice.isPresent());

        // 5. 생성한 Voice 객체와 조회한 Voice 객체 비교
        Voice researchedVoice = optionalResearchedVoice.get();
        assertEquals(createVoice, researchedVoice);
    }

    // 2. 생성 테스트 - 일부 데이터 입력 (필수 값 포함)
    @Test
    @DisplayName("VoiceRepository 생성 테스트 - 일부 데이터 입력 (필수 값 포함)")
    public void createPartialDataWithRequiredTest() {
        // given
        // 1. Language 객체 생성 및 저장
        Language createLanguage = languageCreate();
        Language language = languageRepository.save(createLanguage);

        // 2. Style 객체 생성 및 저장
        Style createStyle = createStyleEntity();
        Style style = styleRepository.save(createStyle);

        // 3. Voice 객체 생성 및 저장 (연관 관계 매핑 포함)
        Voice createVoice = Voice.builder()
                .name("TEST_NAME") // name 을 제외한 나머지 비 필수 값 제외
                .server("googleCloudTTS") // 필수 값
                .language(language)
                .style(style)
                .build();

        // when
        // 4. Voice 객체 저장
        Voice savedVoice = voiceRepository.save(createVoice);
        assertNotNull(savedVoice);

        // then
        // 5. 저장된 Voice 객체 조회 및 검증
        Optional<Voice> optionalResearchedVoice = voiceRepository.findById(savedVoice.getVoiceSeq());
        assertTrue(optionalResearchedVoice.isPresent());

        // 6. 생성한 Voice 객체와 조회한 Voice 객체 비교
        Voice researchedVoice = optionalResearchedVoice.get();
        assertEquals(createVoice, researchedVoice);
    }

    // 3. 생성 테스트 - 일부 데이터 입력 (필수 값 미포함)
    @Test
    @DisplayName("VoiceRepository 생성 테스트 - 일부 데이터 입력 (필수 값 미포함)")
    public void createPartialDataWithoutRequiredTest() {
        // given
        // 1. Language 객체 생성 및 저장
        Language createLanguage = languageCreate();
        Language language = languageRepository.save(createLanguage);

        // 2. Style 객체 생성 및 저장
        Style createStyle = createStyleEntity();
        Style style = styleRepository.save(createStyle);

        // 3. Voice 객체 생성 및 저장 (연관 관계 매핑 포함)
        Voice createVoice = Voice.builder()
                .name("TEST_NAME") // name 을 제외한 나머지 비 필수 값 제외
                .language(language)
                .style(style)
                .build();

        // when, then
        // 4. 필수 값이 없을 때, DataIntegrityViolationException 이 발생하는지 확인
        assertThrows(DataIntegrityViolationException.class, () -> voiceRepository.save(createVoice));
    }

    // 4. 생성 테스트 - 전체 데이터 미입력 (null)
    @Test
    @DisplayName("VoiceRepository 생성 테스트 - 전체 데이터 미입력 (null)")
    public void createNoDataTest() {
        // given
        // 1. Language 객체 생성 및 저장
        Language createLanguage = languageCreate();
        Language language = languageRepository.save(createLanguage);

        // 2. Style 객체 생성 및 저장
        Style createStyle = createStyleEntity();
        Style style = styleRepository.save(createStyle);

        // 3. Voice 객체 생성 (데이터 없음 - 전체 null)
        Voice createVoice = Voice.builder()
                .build();

        // when, then
        // 4. 전체 값이 null 일 때, DataIntegrityViolationException 이 발생하는지 확인
        assertThrows(DataIntegrityViolationException.class, () -> voiceRepository.save(createVoice));
    }

    //5. 생성 테스트 - 연관 관계 매핑 없이 생성
    @Test
    @DisplayName("VoiceRepository 생성 테스트 - 연관 관계 매핑 없이 생성")
    public void createVoiceWithoutMappingsTest() {
        // given
        // 1. Voice 객체 생성
        Voice createVoice = createVoiceWithoutMappings();

        // when
        // 2. Voice 객체 저장
        Voice savedVoice = voiceRepository.save(createVoice);
        assertNotNull(savedVoice);

        // then
        // 3. 저장된 Voice 객체 조회 및 검증
        Optional<Voice> optionalResearchedVoice = voiceRepository.findById(savedVoice.getVoiceSeq());
        assertTrue(optionalResearchedVoice.isPresent());

        // 4. 생성한 Voice 객체와 조회한 Voice 객체 비교
        Voice researchedVoice = optionalResearchedVoice.get();
        assertEquals(createVoice, researchedVoice);
    }

    // 1. 조회 테스트 - 단건 데이터 조회
    @Test
    @DisplayName("VoiceRepository 조회 테스트 - 단건 데이터 조회")
    public void readOneVoiceTest() {
        // given
        // 1. Language 객체 생성 및 저장
        Language createLanguage = languageCreate();
        Language language = languageRepository.save(createLanguage);

        // 2. Style 객체 생성 및 저장
        Style createStyle = createStyleEntity();
        Style style = styleRepository.save(createStyle);

        // 3. Voice 객체 생성 및 저장 (연관 관계 매핑 포함)
        Voice createVoice = createVoiceWithoutMappings();
        createVoice.toBuilder()
                .language(language)
                .style(style)
                .build();

        // 4. Voice 객체 저장
        Voice savedVoice = voiceRepository.save(createVoice);

        // when
        // 5. 저장된 Voice 객체 조회
        Optional<Voice> optionalResearchedVoice = voiceRepository.findById(savedVoice.getVoiceSeq());
        assertTrue(optionalResearchedVoice.isPresent());

        // then
        // 6. 저장된 Voice 객체와 조회한 Voice 객체 비교
        Voice researchedVoice = optionalResearchedVoice.get();
        assertEquals(createVoice, researchedVoice);
    }


    // 2. 조회 테스트 - 조건에 맞는 단건 데이터 조회
    @Test
    @DisplayName("VoiceRepository 조회 테스트 - 조건에 맞는 단건 데이터 조회")
    public void readOneVoiceByConditionTest() {
        // given
        // 1. Language 객체 생성 및 저장
        Language createLanguage = languageCreate();
        Language language = languageRepository.save(createLanguage);

        // 2. Style 객체 생성 및 저장
        Style createStyle = createStyleEntity();
        Style style = styleRepository.save(createStyle);

        // 3. Voice 객체 생성 및 저장 (연관 관계 매핑 포함)
        Voice createVoice = createVoiceWithoutMappings();
        createVoice.toBuilder()
                .language(language)
                .style(style)
                .build();

        // 4. Voice 객체 저장
        Voice savedVoice = voiceRepository.save(createVoice);

        // when
        // 5. 저장된 Voice 객체의 name 으로 조회 및 검증
        List<Voice> researchVoiceListByName = voiceRepository.findAllByName(savedVoice.getName());
        assertFalse(researchVoiceListByName.isEmpty());

        // then
        // 6. 저장된 Voice 객체와 조회한 Voice 객체 비교
        assertTrue(researchVoiceListByName.contains(savedVoice));
    }

    // 3. 조회 테스트 - 다건 조회
    @Test
    @DisplayName("VoiceRepository 조회 테스트 - 다건 조회")
    public void researchManyVoiceTest() {
        // given
        List<Voice> savedVoiceList = new ArrayList<>();

        // 1. Voice 객체 생성 및 저장
        for (int i = 0; i < 10; i++) {
            // Voice 객체 생성
            Voice createVoice = createVoiceWithoutMappings(i);

            // Voice 객체 저장 및 저장된 Voice 객체 리스트에 추가
            Voice savedVoice = voiceRepository.save(createVoice);
            savedVoiceList.add(savedVoice);
        }

        // when
        // 2. 저장된 Voice 객체 다건 조회
        List<Voice> voiceList = voiceRepository.findAll();

        // then
        // 3. 조회 결과 검증 - 빈 리스트 반환 시 실패
        assertFalse(voiceList.isEmpty());

        // 4. 저장된 Voice 객체 리스트와 조회한 Voice 객체 리스트 비교
        assertTrue(voiceList.containsAll(savedVoiceList));
    }

    // 4. 조회 테스트 - 조건에 맞는 다건 데이터 조회
    @Test
    @DisplayName("VoiceRepository 조회 테스트 - 조건에 맞는 다건 데이터 조회")
    public void researchManyVoiceByConditionTest() {
        // given
        List<Voice> savedVoiceList = new ArrayList<>();

        // 1. Voice 객체 생성 및 저장
        for (int i = 0; i < 10; i++) {
            // Voice 객체 생성
            Voice createVoice = createVoiceWithoutMappings(i);

            // Voice 객체 저장 및 저장된 Voice 객체 리스트에 추가
            Voice savedVoice = voiceRepository.save(createVoice);
            savedVoiceList.add(savedVoice);
        }

        // when
        // 2. 저장된 Voice 객체의 name(TEST_NAME) 으로 다건 조회
        List<Voice> researchVoiceListByName = voiceRepository.findAllByNameContaining(TEST_NAME);

        // then
        // 3. 조회 결과 검증 - 빈 리스트 반환 시 실패
        assertFalse(researchVoiceListByName.isEmpty());

        // 4. 저장된 Voice 객체 리스트와 조회한 Voice 객체 리스트 비교
        assertTrue(researchVoiceListByName.containsAll(savedVoiceList));
    }

    // 1. 수정 테스트 - 전체 데이터 수정 (연관관계 포함)
    @Test
    @DisplayName("VoiceRepository 수정 테스트 - 전체 데이터 수정 (연관관계 포함)")
    public void updateEntireDataWithMappingsTest() {
        // given
        String updateName = "UPDATED_NAME";
        String updateGender = "FEMALE";
        Integer updateAge = 60;
        String updateDescription = "UPDATED_DESCRIPTION";
        char updateEnabled = 'N';
        String updateServer = "clovaTTS";

        // 1. Language 객체 생성 및 저장
        Language createLanguage = languageCreate();
        Language language = languageRepository.save(createLanguage);

        // 2. Style 객체 생성 및 저장
        Style createStyle = createStyleEntity();
        Style style = styleRepository.save(createStyle);

        // 3. Voice 객체 생성 (연관 관계 매핑 포함)
        Voice createVoice = createVoiceWithoutMappings();
        createVoice.toBuilder()
                .language(language)
                .style(style)
                .build();

        // 4. Voice 객체 저장
        Voice savedVoice = voiceRepository.save(createVoice);
        assertNotNull(savedVoice);

        // 5. 새로운 Language 객체 생성 및 저장
        Language newLanguage = languageCreate();
        Language savedNewLanguage = languageRepository.save(newLanguage);

        // 6. 새로운 Style 객체 생성 및 저장
        Style newStyle = createStyleEntity();
        Style savedNewStyle = styleRepository.save(newStyle);

        // 7. Voice 객체 수정 및 저장
        Long createVoiceSeq = savedVoice.getVoiceSeq();
        Voice updateVoice = Voice.builder()
                .voiceSeq(createVoiceSeq)
                .name(updateName)
                .gender(updateGender)
                .age(updateAge)
                .description(updateDescription)
                .enabled(updateEnabled)
                .server(updateServer)
                .language(savedNewLanguage)
                .style(savedNewStyle)
                .build();

        // when
        // 8. 수정된 Voice 객체 저장
        Voice manipulatedVoice = voiceRepository.save(updateVoice);

        // 9. 저장된 Voice 객체 조회 및 검증
        assertEquals(manipulatedVoice, updateVoice);
        assertEquals(manipulatedVoice.getVoiceSeq(), createVoiceSeq);
        assertEquals(manipulatedVoice.getName(), updateName);
        assertEquals(manipulatedVoice.getGender(), updateGender);
        assertEquals(manipulatedVoice.getAge(), updateAge);
        assertEquals(manipulatedVoice.getDescription(), updateDescription);
        assertEquals(manipulatedVoice.getEnabled(), updateEnabled);
        assertEquals(manipulatedVoice.getServer(), updateServer);
        assertEquals(manipulatedVoice.getLanguage(), savedNewLanguage);
        assertEquals(manipulatedVoice.getStyle(), savedNewStyle);
    }

    // 2. 수정 테스트 - 전체 데이터 수정 (연관관계 미포함)
    @Test
    @DisplayName("VoiceRepository 수정 테스트 - 전체 데이터 수정 (연관관계 미포함)")
    public void updateEntireDataWithoutMappingsTest() {
        // given
        String updateName = "UPDATED_NAME";
        String updateGender = "FEMALE";
        String updateDescription = "UPDATED_DESCRIPTION";
        char updateEnabled = 'N';

        // 1. Voice 객체 생성 및 저장
        Voice createVoice = createVoiceWithoutMappings();

        Voice savedVoice = voiceRepository.save(createVoice);
        assertNotNull(savedVoice);

        // when
        // 2. Voice 객체 수정 및 저장
        Long createVoiceSeq = savedVoice.getVoiceSeq();
        Voice updateVoice = Voice.builder()
                .voiceSeq(createVoiceSeq)
                .name(updateName)
                .gender(updateGender)
                .description(updateDescription)
                .enabled(updateEnabled)
                .server(createVoice.getServer()) // server 는 변경하지 않음
                .build();

        Voice manipulatedVoice = voiceRepository.save(updateVoice);
        assertNotNull(manipulatedVoice);

        // then
        // 3. 저장된 Voice 객체 조회 및 검증
        assertEquals(manipulatedVoice, updateVoice);
        assertEquals(manipulatedVoice.getVoiceSeq(), createVoiceSeq);
        assertEquals(manipulatedVoice.getName(), updateName);
        assertEquals(manipulatedVoice.getGender(), updateGender);
        assertEquals(manipulatedVoice.getDescription(), updateDescription);
        assertEquals(manipulatedVoice.getEnabled(), updateEnabled);
    }

    // 3. 수정 테스트 - 일부 데이터 수정 (연관관계 포함)
    @Test
    @DisplayName("VoiceRepository 수정 테스트 - 일부 데이터 수정 (연관관계 포함)")
    public void updatePartialDataWithMappingsTest() {
        // given
        // 일부 수정 데이터 : name
        String updateName = "UPDATED_NAME";

        // 1. Language 객체 생성 및 저장
        Language createLanguage = languageCreate();
        Language language = languageRepository.save(createLanguage);

        // 2. Style 객체 생성 및 저장
        Style createStyle = createStyleEntity();
        Style style = styleRepository.save(createStyle);

        // 3. Voice 객체 생성 (연관 관계 매핑 포함)
        Voice createVoice = createVoiceWithoutMappings();
        createVoice.toBuilder()
                .language(language)
                .style(style)
                .build();

        // 4. Voice 객체 저장
        Voice savedVoice = voiceRepository.save(createVoice);
        assertNotNull(savedVoice);

        // when
        // 5. Language 객체 수정 및 저장
        Language newLanguage = languageCreate();
        Language savedNewLanguage = languageRepository.save(newLanguage);

        // 6. Style 객체 수정 및 저장
        Style newStyle = createStyleEntity();
        Style savedNewStyle = styleRepository.save(newStyle);

        // 7. Voice 객체 수정 및 저장, 저장 검증
        Long savedVoiceSeq = savedVoice.getVoiceSeq();
        String savedGender = savedVoice.getGender();
        Integer savedAge = savedVoice.getAge();
        String savedDescription = savedVoice.getDescription();
        char savedEnabled = savedVoice.getEnabled();
        String savedServer = savedVoice.getServer();

        Voice updateVoice = Voice.builder()
                .voiceSeq(savedVoiceSeq)
                .name(updateName)
                .gender(savedGender)
                .age(savedAge)
                .description(savedDescription)
                .enabled(savedEnabled)
                .server(savedServer)
                .language(savedNewLanguage)
                .style(savedNewStyle)
                .build();

        Voice manipulatedVoice = voiceRepository.save(updateVoice);
        assertNotNull(manipulatedVoice);

        // then
        // 8. 저장된 Voice 객체 조회 및 검증
        assertEquals(manipulatedVoice, updateVoice);
        assertEquals(manipulatedVoice.getVoiceSeq(), savedVoiceSeq);
        assertEquals(manipulatedVoice.getName(), updateName);
        assertEquals(manipulatedVoice.getLanguage(), savedNewLanguage);
        assertEquals(manipulatedVoice.getStyle(), savedNewStyle);

        // 기존에 생성된 voice 와 비교
        assertEquals(manipulatedVoice.getServer(), savedServer);
        assertEquals(manipulatedVoice.getGender(), savedVoice.getGender());
        assertEquals(manipulatedVoice.getDescription(), savedVoice.getDescription());
        assertEquals(manipulatedVoice.getEnabled(), savedVoice.getEnabled());
    }

    // 4. 수정 테스트 - 일부 데이터 수정 (연관관계 미포함)
    @Test
    @DisplayName("VoiceRepository 수정 테스트 - 일부 데이터 수정 (연관관계 미포함)")
    public void updatePartialDataWithoutMappingsTest() {
        // given
        // 일부 수정 데이터 : name
        String updateName = "UPDATED_NAME";

        // 1. Voice 객체 생성 및 저장
        Voice createVoice = createVoiceWithoutMappings();

        Voice savedVoice = voiceRepository.save(createVoice);
        assertNotNull(savedVoice);

        // when
        // 2. Voice 객체 수정 및 저장
        Long savedVoiceSeq = savedVoice.getVoiceSeq();
        String savedGender = savedVoice.getGender();
        Integer savedAge = savedVoice.getAge();
        String savedDescription = savedVoice.getDescription();
        char savedEnabled = savedVoice.getEnabled();
        String savedServer = savedVoice.getServer();

        Voice updateVoice = Voice.builder()
                .voiceSeq(savedVoiceSeq)
                .name(updateName)
                .gender(savedGender)
                .age(savedAge)
                .description(savedDescription)
                .enabled(savedEnabled)
                .server(savedServer)
                .build();

        Voice manipulatedVoice = voiceRepository.save(updateVoice);
        assertNotNull(manipulatedVoice);

        // then
        // 3. 저장된 Voice 객체 조회 및 검증
        assertEquals(manipulatedVoice, updateVoice);
        assertEquals(manipulatedVoice.getVoiceSeq(), savedVoiceSeq);
        assertEquals(manipulatedVoice.getName(), updateName);
        assertEquals(manipulatedVoice.getGender(), savedGender);
        assertEquals(manipulatedVoice.getAge(), savedAge);
        assertEquals(manipulatedVoice.getDescription(), savedDescription);
        assertEquals(manipulatedVoice.getEnabled(), savedEnabled);
        assertEquals(manipulatedVoice.getServer(), savedServer);
    }

    // 5. 수정 테스트 - 일부 데이터 null 으로 수정 (server)
    @Test
    @DisplayName("VoiceRepository 수정 테스트 - 일부 데이터 null 으로 수정 (server)")
    public void updateAllDataToNullTest() {
        // given

        // 1. Voice 객체 생성 및 저장
        Voice createVoice = createVoiceWithoutMappings();

        Voice savedVoice = voiceRepository.save(createVoice);
        assertNotNull(savedVoice);

        // when
        // 2. Voice 객체 수정 및 저장
        Long savedVoiceSeq = savedVoice.getVoiceSeq();

        Voice updateVoice = Voice.builder()
                .voiceSeq(savedVoiceSeq)
                .name(null)
                .gender(null)
                .age(null)
                .description(null)
                .enabled('\u0000')
                .server(null)
                .build();

        // then
        // 3. Voice 객체 수정 시, null 값이 들어가면 DataIntegrityViolationException 이 발생하는지 확인
        assertThrows(DataIntegrityViolationException.class, () -> voiceRepository.save(updateVoice));
    }

    // 1. 삭제 테스트 - 전체 데이터 삭제
    @Test
    @DisplayName("VoiceRepository 삭제 테스트 - 전체 데이터 삭제")
    public void deleteAllVoiceTest() {
        // given
        // 1. Voice 객체 생성 및 저장
        Voice createVoice = createVoiceWithoutMappings();

        Voice savedVoice = voiceRepository.save(createVoice);
        assertNotNull(savedVoice);

        // when
        // 2. Voice 객체 삭제
        voiceRepository.delete(savedVoice);

        // then
        // 3. Voice 객체 삭제 후 조회 및 검증
        Long savedVoiceSeq = savedVoice.getVoiceSeq();

        Optional<Voice> deletedVoice = voiceRepository.findById(savedVoiceSeq);
        assertTrue(deletedVoice.isEmpty());
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

    // Style Entity 객체 생성 메서드
    private static Style createStyleEntity() {
        return Style.builder()
                .name("styleName")
                .mood("styleMood")
                .contents("styleContents")
                .description("styleDescription")
                .useCnt(0)
                .isRecommend('N')
                .build();
    }

    // Voice Entity 객체 생성 메서드
    private static Voice createVoiceWithoutMappings() {
        return Voice.builder()
                .name(TEST_NAME)
                .gender("MALE")
                .age(25)
                .description("TEST description")
                .enabled('Y')
                .server("googleCloudTTS")
                .build();
    }

    private static Voice createVoiceWithoutMappings(int cnt) {
        return Voice.builder()
                .name(TEST_NAME + cnt)
                .gender("MALE" + cnt)
                .age(25)
                .description("TEST description" + cnt)
                .enabled('Y')
                .server("googleCloudTTS")
                .build();
    }
}
