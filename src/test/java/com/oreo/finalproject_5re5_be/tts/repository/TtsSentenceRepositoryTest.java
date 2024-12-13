package com.oreo.finalproject_5re5_be.tts.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.oreo.finalproject_5re5_be.project.entity.Project;
import com.oreo.finalproject_5re5_be.project.repository.ProjectRepository;
import com.oreo.finalproject_5re5_be.tts.entity.ServerCode;
import com.oreo.finalproject_5re5_be.tts.entity.TtsAudioFile;
import com.oreo.finalproject_5re5_be.tts.entity.TtsSentence;
import com.oreo.finalproject_5re5_be.tts.entity.Voice;
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
public class TtsSentenceRepositoryTest {

    @Autowired private TtsSentenceRepository ttsSentenceRepository;

    @Autowired private VoiceRepository voiceRepository;

    @Autowired private TtsAudioFileRepository ttsAudioFileRepository;

    @Autowired private ProjectRepository projectRepository;

    /*
    TtsSentenceRepository 생성 테스트
    1.	TtsSentenceRepository 생성 테스트 - 전체 필드 포함
    2.	TtsSentenceRepository 생성 테스트 - 필수 필드만 포함
    3.	TtsSentenceRepository 생성 테스트 - 필수 필드 누락
    4.	TtsSentenceRepository 생성 테스트 - 연관 엔티티 누락

    TtsSentenceRepository 조회 테스트
    1.	TtsSentenceRepository 조회 테스트 - 단건 조회
    2.	TtsSentenceRepository 조회 테스트 - 연관 데이터와 함께 조회
    3.	TtsSentenceRepository 조회 테스트 - 프로젝트 기반 조회

    TtsSentenceRepository 수정 테스트
    1.	TtsSentenceRepository 수정 테스트 - 전체 필드 수정
    2.	TtsSentenceRepository 수정 테스트 - 일부 필드 수정
    3.	TtsSentenceRepository 수정 테스트 - 필수 필드를 null로 수정하여 예외 발생
    4.	TtsSentenceRepository 수정 테스트 - 연관 엔티티 변경

    TtsSentenceRepository 삭제 테스트
    1.	TtsSentenceRepository 삭제 테스트 - 단건 삭제
    2.	TtsSentenceRepository 삭제 테스트 - 존재하지 않는 엔티티 삭제
     */

    // 1. 전체 필드 포함 생성 테스트
    @Test
    @DisplayName("TtsSentenceRepository 생성 테스트 - 전체 필드 포함")
    public void createWithAllFieldsTest() {
        // given
        // 1. Voice 엔티티 생성 및 저장
        Voice voice = voiceRepository.save(createVoice());

        // 2. Project 엔티티 생성 및 저장
        Project project = projectRepository.save(createProject());

        // 3. TtsAudioFile 엔티티 생성 및 저장
        TtsAudioFile audioFile = ttsAudioFileRepository.save(createTtsAudioFile());

        // 4. TtsSentence 엔티티 생성 (모든 필드 포함)
        TtsSentence sentence =
                TtsSentence.builder()
                        .text("Test sentence with all fields") // 텍스트 설정
                        .sortOrder(1) // 정렬 순서 설정
                        .volume(80) // 볼륨 설정
                        .speed(1.0f) // 속도 설정
                        .startPitch(1) // 시작 피치 설정
                        .emotion("5") // 감정 설정
                        .emotionStrength(3) // 감정 강도 설정
                        .sampleRate(44100) // 샘플레이트 설정
                        .alpha(2) // 알파 값 설정
                        .endPitch(0.5f) // 끝 피치 설정
                        .audioFormat("wav") // 오디오 형식 설정
                        .voice(voice) // Voice 연관 관계 설정
                        .project(project) // Project 연관 관계 설정
                        .ttsAudiofile(audioFile) // TtsAudioFile 연관 관계 설정
                        .build();

        // when
        // 5. TtsSentence 저장
        TtsSentence savedSentence = ttsSentenceRepository.save(sentence);

        // then
        // 6. 저장된 TtsSentence 검증
        assertNotNull(savedSentence);
        assertEquals(sentence.getText(), savedSentence.getText());
    }

    // 2. 필수 필드만 포함하여 생성 테스트
    @Test
    @DisplayName("TtsSentenceRepository 생성 테스트 - 필수 필드만 포함")
    public void createWithRequiredFieldsOnlyTest() {
        // given
        // 1. Voice 엔티티 생성 및 저장
        Voice voice = voiceRepository.save(createVoice());
        // 2. Project 엔티티 생성 및 저장
        Project project = projectRepository.save(createProject());

        // 3. 필수 필드만 포함한 TtsSentence 엔티티 생성
        TtsSentence sentence =
                TtsSentence.builder()
                        .text("Required field only") // 텍스트 설정
                        .sortOrder(1) // 정렬 순서 설정
                        .voice(voice) // Voice 연관 관계 설정
                        .project(project) // Project 연관 관계 설정
                        .build();

        // when
        // 4. TtsSentence 저장
        TtsSentence savedSentence = ttsSentenceRepository.save(sentence);

        // then
        // 5. 저장된 TtsSentence 검증
        assertNotNull(savedSentence);
        assertEquals(sentence.getText(), savedSentence.getText());
    }

    // 3. 필수 필드 누락으로 생성 실패 테스트
    @Test
    @DisplayName("TtsSentenceRepository 생성 테스트 - 필수 필드 누락")
    public void createWithoutRequiredFieldsTest() {
        // 1. 필수 필드 누락한 TtsSentence 엔티티 생성
        TtsSentence sentence = TtsSentence.builder().build();

        // when, then
        // 2. 필수 필드 누락으로 인한 예외 발생 확인
        assertThrows(DataIntegrityViolationException.class, () -> ttsSentenceRepository.save(sentence));
    }

    // 4. 연관된 엔티티 누락으로 생성 실패 테스트
    @Test
    @DisplayName("TtsSentenceRepository 생성 테스트 - 연관 엔티티 누락")
    public void createWithoutAssociatedEntitiesTest() {
        // 1. 필수 연관 엔티티 누락한 TtsSentence 엔티티 생성
        TtsSentence sentence = TtsSentence.builder().text("Missing association").sortOrder(1).build();

        // when, then
        // 2. 연관 엔티티 누락으로 인한 예외 발생 확인
        assertThrows(DataIntegrityViolationException.class, () -> ttsSentenceRepository.save(sentence));
    }

    // 1. 단건 조회 테스트
    @Test
    @DisplayName("TtsSentenceRepository 조회 테스트 - 단건 조회")
    public void readSingleTtsSentenceTest() {
        // given
        // 1. Voice 엔티티 생성 및 저장
        Voice voice = voiceRepository.save(createVoice());

        // 2. Project 엔티티 생성 및 저장
        Project project = projectRepository.save(createProject());

        // 3. TtsSentence 생성 및 저장
        TtsSentence sentence = ttsSentenceRepository.save(createTtsSentence(voice, project));

        // when
        // 4. 단건 조회
        Optional<TtsSentence> foundSentence = ttsSentenceRepository.findById(sentence.getTsSeq());

        // then
        // 5. 조회된 TtsSentence 검증
        assertTrue(foundSentence.isPresent());
        assertEquals(sentence, foundSentence.get());
    }

    // 2. 연관 데이터와 함께 조회 테스트
    @Test
    @DisplayName("TtsSentenceRepository 조회 테스트 - 연관 데이터와 함께 조회")
    public void readWithVoiceAndProjectTest() {
        // given
        // 1. Voice 엔티티 생성 및 저장
        Voice voice = voiceRepository.save(createVoice());
        // 2. Project 엔티티 생성 및 저장
        Project project = projectRepository.save(createProject());
        // 3. TtsSentence 생성 및 저장
        TtsSentence sentence = ttsSentenceRepository.save(createTtsSentence(voice, project));

        // when
        // 4. 단건 조회
        Optional<TtsSentence> foundSentence = ttsSentenceRepository.findById(sentence.getTsSeq());

        // then
        // 5. 조회된 TtsSentence 검증
        assertTrue(foundSentence.isPresent());
        assertEquals(voice, foundSentence.get().getVoice()); // Voice 연관 데이터 검증
        assertEquals(project, foundSentence.get().getProject()); // Project 연관 데이터 검증
    }

    // 3. 프로젝트 기반 조회 테스트
    @Test
    @DisplayName("TtsSentenceRepository 조회 테스트 - 프로젝트 기반 조회")
    public void readByProjectTest() {
        // given
        // 1. Project 엔티티 생성 및 저장
        Project project = projectRepository.save(createProject());

        // 2. Voice 엔티티 생성 및 저장
        Voice voice = voiceRepository.save(createVoice());

        // 3. 특정 Project에 속한 TtsSentence 생성 및 저장
        TtsSentence sentence = ttsSentenceRepository.save(createTtsSentence(voice, project));

        // when
        // 4. Project로 조회
        List<TtsSentence> sentences = ttsSentenceRepository.findAllByProjectOrderBySortOrder(project);

        // then
        // 5. 조회된 TtsSentence 검증
        assertFalse(sentences.isEmpty());
        assertTrue(sentences.contains(sentence));
    }

    // 1. 전체 필드 수정 테스트
    @Test
    @DisplayName("TtsSentenceRepository 수정 테스트 - 전체 필드 수정")
    public void updateAllFieldsTest() {
        // given
        String updatedText = "Updated text"; // 수정할 텍스트
        Integer updatedVolume = 70; // 수정할 볼륨
        Float updatedSpeed = 1.5f; // 수정할 속도
        Integer updatedStartPitch = 2; // 수정할 시작 피치
        String updatedEmotion = "4"; // 수정할 감정
        Float updatedEndPitch = 1.0f; // 수정할 끝 피치
        String updatedAudioFormat = "mp3"; // 수정할 오디오 형식

        // 1. Voice 엔티티 생성 및 저장
        Voice voice = voiceRepository.save(createVoice());

        // 2. Project 엔티티 생성 및 저장
        Project project = projectRepository.save(createProject());

        // 3. TtsSentence 생성 및 저장
        TtsSentence sentence = ttsSentenceRepository.save(createTtsSentence(voice, project));

        // 4. TtsSentence 필드 전체 수정
        TtsSentence updatedSentence =
                sentence.toBuilder()
                        .text(updatedText)
                        .volume(updatedVolume)
                        .speed(updatedSpeed)
                        .startPitch(updatedStartPitch)
                        .emotion(updatedEmotion)
                        .endPitch(updatedEndPitch)
                        .audioFormat(updatedAudioFormat)
                        .build();

        // when
        // 5. 수정된 TtsSentence 저장
        TtsSentence savedUpdatedSentence = ttsSentenceRepository.save(updatedSentence);

        // then
        // 6. 수정된 TtsSentence 검증
        assertEquals(updatedSentence, savedUpdatedSentence); // PK 확인
    }

    // 2. 일부 필드 수정 테스트
    @Test
    @DisplayName("TtsSentenceRepository 수정 테스트 - 일부 필드 수정")
    public void updateSomeFieldsTest() {
        // given
        Integer updatedVolume = 85;
        String updatedEmotion = "3";

        // 1. Voice 엔티티 생성 및 저장
        Voice voice = voiceRepository.save(createVoice());

        // 2. Project 엔티티 생성 및 저장
        Project project = projectRepository.save(createProject());

        // 3. TtsSentence 생성 및 저장
        TtsSentence sentence = ttsSentenceRepository.save(createTtsSentence(voice, project));

        // 4. 일부 필드만 수정
        TtsSentence updatedSentence =
                sentence.toBuilder()
                        .volume(updatedVolume) // 볼륨 수정
                        .emotion(updatedEmotion) // 감정 수정
                        .build();

        // when
        // 5. 수정된 TtsSentence 저장
        TtsSentence savedUpdatedSentence = ttsSentenceRepository.save(updatedSentence);

        // then
        // 6. 수정된 TtsSentence 검증
        assertEquals(updatedVolume, savedUpdatedSentence.getVolume()); // 볼륨 변경 확인
        assertEquals(updatedEmotion, savedUpdatedSentence.getEmotion()); // 감정 변경 확인
    }

    // 3. 필수 필드를 null로 수정하여 예외 발생 테스트
    @Test
    @DisplayName("TtsSentenceRepository 수정 테스트 - 필수 필드를 null로 수정하여 예외 발생")
    public void updateRequiredFieldToNullTest() {
        // given
        // 1. Voice 엔티티 생성 및 저장
        Voice voice = voiceRepository.save(createVoice());

        // 2. Project 엔티티 생성 및 저장
        Project project = projectRepository.save(createProject());

        // 3. TtsSentence 생성 및 저장
        TtsSentence sentence = ttsSentenceRepository.save(createTtsSentence(voice, project));

        // 4. 필수 필드 null로 수정
        TtsSentence updatedSentence =
                sentence.toBuilder()
                        .text(null) // 텍스트를 null로 설정
                        .build();

        // when, then
        // 5. 예외 발생 확인
        assertThrows(
                DataIntegrityViolationException.class, () -> ttsSentenceRepository.save(updatedSentence));
    }

    // 4. 연관 엔티티 변경 테스트
    @Test
    @DisplayName("TtsSentenceRepository 수정 테스트 - 연관 엔티티 변경")
    public void updateRelatedEntitiesTest() {
        // given
        // 1. Voice 및 Project 엔티티 생성 및 저장
        Voice originalVoice = voiceRepository.save(createVoice());

        // 2. Project 생성 및 저장
        Project originalProject = projectRepository.save(createProject());

        // 3. TtsSentence 생성 및 저장
        TtsSentence sentence =
                ttsSentenceRepository.save(createTtsSentence(originalVoice, originalProject));

        // 4. 새로운 Voice 및 Project 엔티티 생성 및 저장
        Voice newVoice = voiceRepository.save(createVoice());
        Project newProject = projectRepository.save(createProject());

        // 5. 연관 엔티티 변경
        TtsSentence updatedSentence = sentence.toBuilder().voice(newVoice).project(newProject).build();

        // when
        // 6. 변경된 TtsSentence 저장
        TtsSentence savedUpdatedSentence = ttsSentenceRepository.save(updatedSentence);

        // then
        // 7. 변경된 연관 엔티티 확인
        assertEquals(newVoice, savedUpdatedSentence.getVoice()); // Voice 변경 확인
        assertEquals(newProject, savedUpdatedSentence.getProject()); // Project 변경 확인
    }

    // 1. 단건 삭제 테스트
    @Test
    @DisplayName("TtsSentenceRepository 삭제 테스트 - 단건 삭제")
    public void deleteSingleTtsSentenceTest() {
        // given
        // 1. Voice 엔티티 생성 및 저장
        Voice voice = voiceRepository.save(createVoice());

        // 2. Project 엔티티 생성 및 저장
        Project project = projectRepository.save(createProject());

        // 3. TtsSentence 생성 및 저장
        TtsSentence sentence = ttsSentenceRepository.save(createTtsSentence(voice, project));

        // when
        // 4. TtsSentence 삭제
        ttsSentenceRepository.delete(sentence);

        // then
        // 5. 삭제된 TtsSentence 조회
        Optional<TtsSentence> deletedSentence = ttsSentenceRepository.findById(sentence.getTsSeq());
        assertTrue(deletedSentence.isEmpty());
    }

    // 2. 존재하지 않는 엔티티 삭제 테스트
    @Test
    @DisplayName("TtsSentenceRepository 삭제 테스트 - 존재하지 않는 엔티티 삭제")
    public void deleteNonExistingEntityTest() {
        // 존재하지 않는 ID로 삭제 시도
        ttsSentenceRepository.deleteById(999L); // 예외 발생 없이 동작
    }

    // 헬퍼 메서드 - Voice 생성
    private Voice createVoice() {
        return Voice.builder()
                .name("Sample Voice")
                .gender("Male")
                .age(30)
                .description("Sample Description")
                .enabled('Y')
                .server(ServerCode.GOOGLE_CLOUD)
                .build();
    }

    // 헬퍼 메서드 - Project 생성
    private Project createProject() {
        //        return new Project();
        return Project.builder().build();
    }

    // 헬퍼 메서드 - TtsAudioFile 생성
    private TtsAudioFile createTtsAudioFile() {
        return TtsAudioFile.builder()
                .audioPath("/path/to/audio")
                .audioName("SampleAudio.wav")
                .audioSize("1024MB")
                .audioExtension("wav")
                .build();
    }

    // 헬퍼 메서드 - TtsSentence 생성
    private TtsSentence createTtsSentence(Voice voice, Project project) {
        return TtsSentence.builder()
                .text("Sample TtsSentence")
                .sortOrder(1)
                .volume(50)
                .speed(1.0f)
                .voice(voice)
                .project(project)
                .build();
    }
}
