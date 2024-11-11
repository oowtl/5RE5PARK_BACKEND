package com.oreo.finalproject_5re5_be.tts.repository;

import com.oreo.finalproject_5re5_be.tts.entity.SampleAudio;
import com.oreo.finalproject_5re5_be.tts.entity.Voice;
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
public class SampleAudioRepositoryTest {

    @Autowired
    private SampleAudioRepository sampleAudioRepository;

    @Autowired
    private VoiceRepository voiceRepository;

    /*
    SampleAudioRepository 생성 테스트
        1. 생성 테스트 - 전체 데이터 입력
        2. 생성 테스트 - 필수 필드만 입력
        3. 생성 테스트 - 필수 필드 누락

    SampleAudioRepository 조회 테스트
        1. 조회 테스트 - 단건 데이터 조회
        2. 조회 테스트 - 다건 데이터 조회
        3. 조회 테스트 - Voice 연관 조회

    SampleAudioRepository 수정 테스트
        1. 수정 테스트 - 전체 데이터 수정
        2. 수정 테스트 - 필드 부분 수정
        3. 수정 테스트 - 필수 필드를 null로 수정

    SampleAudioRepository 삭제 테스트
        1. 삭제 테스트 - 단건 삭제

    SampleAudioRepository 예외 및 경계 값 테스트
        1. 필드 길이 초과
        2. audioSize에 잘못된 형식 입력
    */

    // 1. 생성 테스트 - 전체 데이터 입력
    @Test
    @DisplayName("SampleAudioRepository 생성 테스트 - 전체 데이터 입력")
    public void createEntireDataTest() {
        // given
        // 1. Voice Entity 생성
        Voice voice = voiceRepository.save(createVoiceEntity());

        // 2. SampleAudio Entity 생성
        SampleAudio sampleAudio = createSampleAudioEntity(voice);

        // when
        // 3. SampleAudio Entity 저장
        SampleAudio savedSampleAudio = sampleAudioRepository.save(sampleAudio);
        assertNotNull(savedSampleAudio);

        // then
        // 4. 저장된 SampleAudio Entity 조회 및 검증
        Optional<SampleAudio> optionalManipulatedSampleAudio = sampleAudioRepository.findById(savedSampleAudio.getSampleAudioSeq());
        assertTrue(optionalManipulatedSampleAudio.isPresent());


        // 5. 저장된 SampleAudio Entity와 생성한 SampleAudio Entity 비교
        SampleAudio manipulatedSampleAudio = optionalManipulatedSampleAudio.get();
        assertEquals(sampleAudio, manipulatedSampleAudio);
    }

    // 2. 생성 테스트 - 필수 필드만 입력
    @Test
    @DisplayName("SampleAudioRepository 생성 테스트 - 필수 필드만 입력")
    public void createRequiredFieldsOnlyTest() {
        // given
        // 1. SampleAudio Entity 생성
        SampleAudio sampleAudio = SampleAudio.builder()
                .audioPath("/path/to/audio")
                .enabled('Y')
                .build();

        // when
        // 2. SampleAudio Entity 저장
        SampleAudio savedSampleAudio = sampleAudioRepository.save(sampleAudio);
        assertNotNull(savedSampleAudio);

        // then
        // 3. 저장된 SampleAudio Entity 조회 및 검증
        Long savedSampleAudioSeq = savedSampleAudio.getSampleAudioSeq();
        Optional<SampleAudio> optionalManipulatedSampleAudio = sampleAudioRepository.findById(savedSampleAudioSeq);
        assertTrue(optionalManipulatedSampleAudio.isPresent());

        // 4. 저장된 SampleAudio Entity와 생성한 SampleAudio Entity 비교
        SampleAudio manipulatedSampleAudio = optionalManipulatedSampleAudio.get();
        assertEquals(sampleAudio, manipulatedSampleAudio);
    }

    // 3. 생성 테스트 - 필수 필드 누락
    @Test
    @DisplayName("SampleAudioRepository 생성 테스트 - 필수 필드 누락")
    public void createWithoutRequiredFieldsTest() {
        // given
        // 1. 필수 필드 누락 SampleAudio Entity 생성
        SampleAudio sampleAudio = SampleAudio.builder()
                .audioName("Sample Audio")
                .build();

        // when, then
        // 2. 필수 필드 누락 SampleAudio Entity 저장 시도 시 예외 발생 여부 확인
        assertThrows(DataIntegrityViolationException.class, () -> sampleAudioRepository.save(sampleAudio));
    }

    // 1. 조회 테스트 - 단건 데이터 조회
    @Test
    @DisplayName("SampleAudioRepository 조회 테스트 - 단건 데이터 조회")
    public void readOneSampleAudioTest() {
        // given
        // 1. Voice Entity 생성
        Voice voice = voiceRepository.save(createVoiceEntity());

        // 2. SampleAudio Entity 생성
        SampleAudio sampleAudio = sampleAudioRepository.save(createSampleAudioEntity(voice));

        // when
        // 3. SampleAudio Entity 조회
        Long sampleAudioSeq = sampleAudio.getSampleAudioSeq();
        Optional<SampleAudio> optionalResearchedSampleAudio = sampleAudioRepository.findById(sampleAudioSeq);
        assertTrue(optionalResearchedSampleAudio.isPresent());

        // then
        // 4. 조회한 SampleAudio Entity와 생성한 SampleAudio Entity 비교
        SampleAudio researchedSampleAudio = optionalResearchedSampleAudio.get();
        assertEquals(sampleAudio, researchedSampleAudio);
    }

    // 2. 조회 테스트 - 다건 데이터 조회
    @Test
    @DisplayName("SampleAudioRepository 조회 테스트 - 다건 데이터 조회")
    public void readMultipleSampleAudiosTest() {
        // given
        List<SampleAudio> sampleAudios = new ArrayList<>();

        // 1. Voice Entity 생성 및 SampleAudio Entity 생성 및 저장
        for (int i = 0; i < 5; i++) {
            Voice voice = voiceRepository.save(createVoiceEntity());
            SampleAudio sampleAudio = sampleAudioRepository.save(createSampleAudioEntity(voice, i));

            // 2. 생성한 SampleAudio Entity 저장
            sampleAudios.add(sampleAudio);
        }

        // when
        // 3. 모든 SampleAudio Entity 조회
        List<SampleAudio> allSampleAudios = sampleAudioRepository.findAll();
        assertFalse(allSampleAudios.isEmpty());

        // then
        // 4. 조회한 SampleAudio Entity와 생성한 SampleAudio Entity 비교
        assertTrue(allSampleAudios.containsAll(sampleAudios));
    }

    // 3. 조회 테스트 - Voice 연관 조회
    @Test
    @DisplayName("SampleAudioRepository 조회 테스트 - Voice 연관 조회")
    public void readSampleAudioWithVoiceTest() {
        // given
        // 1. Voice Entity 생성 및 저장
        Voice voice = voiceRepository.save(createVoiceEntity());

        // 2. SampleAudio Entity 생성 및 저장
        SampleAudio sampleAudio = sampleAudioRepository.save(createSampleAudioEntity(voice));

        // when
        // 3. SampleAudio Entity 조회 및 검증
        Optional<SampleAudio> optionalResearchedSampleAudio = sampleAudioRepository.findById(sampleAudio.getSampleAudioSeq());
        assertTrue(optionalResearchedSampleAudio.isPresent());

        // then
        // 4. Voice Entity 조회 및 검증
        assertNotNull(optionalResearchedSampleAudio.get().getVoice());
        assertEquals(voice, optionalResearchedSampleAudio.get().getVoice());
    }

    // 1. 수정 테스트 - 전체 데이터 수정
    @Test
    @DisplayName("SampleAudioRepository 수정 테스트 - 전체 데이터 수정")
    public void updateEntireDataTest() {
        // given
        // 1. Voice Entity 및 SampleAudio Entity 생성 및 저장
        Voice voice = voiceRepository.save(createVoiceEntity());
        SampleAudio sampleAudio = sampleAudioRepository.save(createSampleAudioEntity(voice));

        // 2. SampleAudio Entity 수정
        SampleAudio updatedSampleAudio = sampleAudio.toBuilder()
                .audioPath("/new/path/to/audio")
                .audioName("New Sample Audio")
                .audioExtension("mp3")
                .audioSize("5MB")
                .audioTime("3:45")
                .script("Updated script")
                .enabled('N')
                .build();

        // when
        // 3. 수정된 SampleAudio Entity 저장
        SampleAudio manipulatedSampleAudio = sampleAudioRepository.save(updatedSampleAudio);

        // then
        // 4. 저장된 SampleAudio Entity 조회 및 검증
        assertEquals(updatedSampleAudio, manipulatedSampleAudio);
    }

    // 2. 수정 테스트 - 필드 부분 수정
    @Test
    @DisplayName("SampleAudioRepository 수정 테스트 - 필드 부분 수정")
    public void updatePartialDataTest() {
        // given
        String updatedAudioName = "Updated Audio Name";

        // 1. Voice Entity 및 SampleAudio Entity 생성 및 저장
        Voice voice = voiceRepository.save(createVoiceEntity());
        SampleAudio sampleAudio = sampleAudioRepository.save(createSampleAudioEntity(voice));

        // 2. audioName 필드만 수정
        SampleAudio updatedSampleAudio = sampleAudio.toBuilder()
                .audioName(updatedAudioName)
                .build();

        // when
        // 3. 수정된 SampleAudio Entity 저장
        SampleAudio savedUpdatedSampleAudio = sampleAudioRepository.save(updatedSampleAudio);

        // then
        // 4. 저장된 SampleAudio Entity 조회 및 검증
        assertEquals(updatedAudioName, savedUpdatedSampleAudio.getAudioName());
        assertEquals(sampleAudio.getAudioPath(), savedUpdatedSampleAudio.getAudioPath());
    }

    // 3. 수정 테스트 - 필수 필드를 null로 수정
    @Test
    @DisplayName("SampleAudioRepository 수정 테스트 - 필수 필드를 null로 수정")
    public void updateWithNullRequiredFieldTest() {
        // given
        // 1. Voice Entity 및 SampleAudio Entity 생성 및 저장
        Voice voice = voiceRepository.save(createVoiceEntity());
        SampleAudio sampleAudio = sampleAudioRepository.save(createSampleAudioEntity(voice));

        // 2. 필수 필드를 null로 설정
        SampleAudio updatedSampleAudio = sampleAudio.toBuilder()
                .audioPath(null)
                .build();

        // when, then
        assertThrows(DataIntegrityViolationException.class, () -> sampleAudioRepository.save(updatedSampleAudio));
    }

    // 1. 삭제 테스트 - 단건 삭제
    @Test
    @DisplayName("SampleAudioRepository 삭제 테스트 - 단건 삭제")
    public void deleteOneSampleAudioTest() {
        // given
        // 1. Voice Entity 및 SampleAudio Entity 생성 및 저장
        Voice voice = voiceRepository.save(createVoiceEntity());
        SampleAudio sampleAudio = sampleAudioRepository.save(createSampleAudioEntity(voice));

        // when
        // 2. SampleAudio Entity 삭제
        sampleAudioRepository.delete(sampleAudio);

        // then
        // 3. 삭제된 SampleAudio Entity 조회 시 null 반환
        Optional<SampleAudio> optionalSampleAudio = sampleAudioRepository.findById(sampleAudio.getSampleAudioSeq());
        assertTrue(optionalSampleAudio.isEmpty());
    }

    // Voice Entity 생성 메서드
    private Voice createVoiceEntity() {
        return Voice.builder()
                .name("Sample Voice")
                .gender("Male")
                .age(30)
                .description("Sample Voice Description")
                .enabled('Y')
                .server("Test Server")
                .build();
    }

    // SampleAudio Entity 생성 메서드
    private SampleAudio createSampleAudioEntity(Voice voice) {
        return SampleAudio.builder()
                .audioPath("/path/to/audio")
                .audioName("Sample Audio")
                .audioExtension("wav")
                .audioSize("10MB")
                .audioTime("2:34")
                .script("Sample script text")
                .enabled('Y')
                .voice(voice)
                .build();
    }

    private SampleAudio createSampleAudioEntity(Voice voice, int index) {
        return SampleAudio.builder()
                .audioPath("/path/to/audio" + index)
                .audioName("Sample Audio " + index)
                .audioExtension("wav")
                .audioSize("10MB")
                .audioTime("2:34")
                .script("Sample script text " + index)
                .enabled('Y')
                .voice(voice)
                .build();
    }
}