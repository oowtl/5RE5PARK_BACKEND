package com.oreo.finalproject_5re5_be.tts.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.oreo.finalproject_5re5_be.project.entity.Project;
import com.oreo.finalproject_5re5_be.project.repository.ProjectRepository;
import com.oreo.finalproject_5re5_be.tts.entity.*;
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
class TtsProgressStatusRepositoryTest {
    @Autowired private TtsProgressStatusRepository ttsProgStatRepository;
    @Autowired private TtsSentenceRepository ttsSentenceRepository;
    @Autowired private VoiceRepository voiceRepository;

    @Autowired private ProjectRepository projectRepository;

    /*
     *  [TtsProgressStatus 생성 테스트]
     *  1. 모든 필드 값 입력 후 추가 => 성공
     *  2. 일부 필드 값 입력 후 추가 - 필수 입력 미포함 => 실패
     *  3. 필드 값 입력 없이 추가 => 실패
     * */

    @Test
    @DisplayName("TtsProgressStatus 생성 테스트 - 모든 필드 입력 후 추가")
    public void insertTtsProgressStatus() {
        // voice 엔티티 생성 및 저장
        Voice savedVoice = voiceRepository.save(createVoice());

        // Project 엔티티 생성 및 저장
        Project savedProject = projectRepository.save(createProject());

        // ttsSentence 엔티티 생성 및 저장
        TtsSentence ttsSentence = createTtsSentence(savedVoice, savedProject);
        TtsSentence savedTtsSentence = ttsSentenceRepository.save(ttsSentence);

        // 모든 데이터가 입력된 TtsProgressStatus 엔티티 생성
        TtsProgressStatus ttsProgressStatus =
                TtsProgressStatus.builder()
                        .progressStatus(TtsProgressStatusCode.CREATED)
                        .ttsSentence(savedTtsSentence)
                        .build();

        // 엔티티 DB에 저장
        TtsProgressStatus savedTtsProgressStatus = ttsProgStatRepository.save(ttsProgressStatus);
        Long savedTpsSeq = savedTtsProgressStatus.getTpsSeq();

        // 삽입된 엔티티의 PK로 조회한 엔티티와 삽입한 엔티티의 값이 같아야 함
        Optional<TtsProgressStatus> foundTpsStatus = ttsProgStatRepository.findById(savedTpsSeq);
        assertTrue(foundTpsStatus.isPresent());

        TtsProgressStatus getTpsStatus = foundTpsStatus.get();
        assertEquals(ttsProgressStatus, getTpsStatus);
    }

    @Test
    @DisplayName("TtsProgressStatus 생성 테스트 - 일부 필드 입력 후 추가(필수 입력 미포함)")
    public void insertTtsProgressStatusWithoutRequired() {
        // voice 엔티티 생성 및 저장
        Voice savedVoice = voiceRepository.save(createVoice());

        // Project 엔티티 생성 및 저장
        Project savedProject = projectRepository.save(createProject());

        // ttsSentence 엔티티 생성 및 저장
        TtsSentence ttsSentence = createTtsSentence(savedVoice, savedProject);
        TtsSentence savedTtsSentence = ttsSentenceRepository.save(ttsSentence);

        // 필수 필드 누락된 TtsProgressStatus 엔티티 생성
        TtsProgressStatus ttsProgressStatus =
                TtsProgressStatus.builder().ttsSentence(savedTtsSentence).build();

        // 필수 필드 누락된 엔티티 DB에 저장시 예외 발생
        assertThrows(
                DataIntegrityViolationException.class, () -> ttsProgStatRepository.save(ttsProgressStatus));
    }

    @Test
    @DisplayName("TtsProgressStatus 생성 테스트 - 필드 입력 없이 추가")
    public void insertTtsProgressStatusWithEmpty() {
        // 빈 TtsProgressStatus 엔티티 생성
        TtsProgressStatus ttsProgressStatus = TtsProgressStatus.builder().build();

        // 빈 엔티티 저장시 예외 발생
        // 엔티티 DB에 저장시 예외 발생
        assertThrows(
                DataIntegrityViolationException.class, () -> ttsProgStatRepository.save(ttsProgressStatus));
    }

    /*
     *  [TtsProgressStatus 조회 테스트]
     *  1. 한 개 엔티티 저장 후 조회
     *  2. 여러 개 엔티티 저장 후 조회
     * */

    @Test
    @DisplayName("TtsProgressStatus 조회 테스트 - 한 개 엔티티 저장 후 조회")
    public void selectTtsProgressStatus() {
        // voice 엔티티 생성 및 저장
        Voice savedVoice = voiceRepository.save(createVoice());

        // Project 엔티티 생성 및 저장
        Project savedProject = projectRepository.save(createProject());

        // ttsSentence 엔티티 생성 및 저장
        TtsSentence ttsSentence = createTtsSentence(savedVoice, savedProject);
        TtsSentence savedTtsSentence = ttsSentenceRepository.save(ttsSentence);

        // 모든 데이터가 입력된 TtsProgressStatus 엔티티 생성
        TtsProgressStatus ttsProgressStatus =
                TtsProgressStatus.builder()
                        .progressStatus(TtsProgressStatusCode.CREATED)
                        .ttsSentence(savedTtsSentence)
                        .build();

        // 엔티티 DB에 저장
        TtsProgressStatus savedTtsProgressStatus = ttsProgStatRepository.save(ttsProgressStatus);
        Long savedTpsSeq = savedTtsProgressStatus.getTpsSeq();

        // 삽입된 엔티티의 PK로 조회한 엔티티와 삽입한 엔티티의 값이 같아야 함
        Optional<TtsProgressStatus> foundTpsStatus = ttsProgStatRepository.findById(savedTpsSeq);
        assertTrue(foundTpsStatus.isPresent());

        TtsProgressStatus getTpsStatus = foundTpsStatus.get();
        assertEquals(ttsProgressStatus, getTpsStatus);
    }

    @Test
    @DisplayName("TtsProgressStatus 조회 테스트 - 여러 개 엔티티 저장 후 조회")
    public void selectTtsProgressStatusList() {
        // voice 엔티티 생성 및 저장
        Voice savedVoice = voiceRepository.save(createVoice());

        // Project 엔티티 생성 및 저장
        Project savedProject = projectRepository.save(createProject());

        // ttsSentence 엔티티 생성 및 저장
        TtsSentence ttsSentence = createTtsSentence(savedVoice, savedProject);
        TtsSentence savedTtsSentence = ttsSentenceRepository.save(ttsSentence);

        // TtsProgressStatus 리스트 생성 및 저장
        List<TtsProgressStatus> ttsProgressStatusList = new ArrayList<>();
        int cnt = 10;
        for (int i = 0; i < cnt; i++) {
            TtsProgressStatus newTtsProgStat = createTtsProgStatus(savedTtsSentence, i);
            ttsProgressStatusList.add(newTtsProgStat);
            assertNotNull(ttsProgStatRepository.save(newTtsProgStat));
        }

        // 조회한 결과가 저장한  TtsProgressStatus 리스트 내용과 같아야함
        List<TtsProgressStatus> foundTpsStatusList = ttsProgStatRepository.findAll();
        assertNotNull(foundTpsStatusList);
        assertTrue(foundTpsStatusList.size() == cnt);

        assertTrue(ttsProgressStatusList.containsAll(foundTpsStatusList));
    }

    // Voice 엔티티 생성 메서드
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

    // Project 엔티티 생성 메서드
    private Project createProject() {
        return Project.builder().build();
    }

    // TtsSentence 엔티티 생성 메서드
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

    // TtsProgressStatus 엔티티 생성 메서드
    private TtsProgressStatus createTtsProgStatus(TtsSentence ttsSentence, int n) {
        return TtsProgressStatus.builder()
                .progressStatus(TtsProgressStatusCode.values()[n % 4])
                .ttsSentence(ttsSentence)
                .build();
    }
}
