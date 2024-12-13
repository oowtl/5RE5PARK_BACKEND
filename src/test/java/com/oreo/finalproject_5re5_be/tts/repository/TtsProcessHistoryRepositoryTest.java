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
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class TtsProcessHistoryRepositoryTest {
    @Autowired ProjectRepository projectRepository;

    @Autowired VoiceRepository voiceRepository;

    @Autowired TtsAudioFileRepository ttsAudioFileRepository;

    @Autowired TtsProcessHistoryRepository ttsProcessHistoryRepository;

    /*
     *  [ TtsProcessHistory 생성 테스트 ]
     *  1. 전체 필드 값 입력 후 추가
     *  2. 일부 필드 값 입력 후 추가(필수 입력 포함)
     *  3. 일부 필드 값 입력 후 추가(필수 입력 미포함)
     *  4. 필드 값 입력 없이 추가
     *
     *  [ TtsProcessHistory 조회 테스트 ]
     *  1. 한 개의 데이터 조회
     *  2. 여러 개의 데이터 조회
     *
     *  [ TtsProcessHistory 삭제 테스트 ]
     *  1. 한 개 데이터 삭제
     *  2. null 값으로 삭제
     *
     * */

    /* 생성 테스트 */
    @Test
    @DisplayName("TtsProcessHistory 생성 테스트 - 전체 필드 값 입력 후 추가")
    public void saveTtsProcessHistory() {

        // voice 엔티티 생성 및 저장
        Voice savedVoice = voiceRepository.save(createVoice());
        assertNotNull(savedVoice);

        // project 엔티티 생성 및 저장
        Project savedProject = projectRepository.save(createProject());
        assertNotNull(savedVoice);

        // TtsAudioFile 엔티티 생성
        TtsAudioFile savedTtsAudiofile = ttsAudioFileRepository.save(createTtsAudioFile());

        // 전체 필드 값 입력한 ttsProcessHistory 엔티티 생성
        TtsProcessHistory ttsProcessHistory =
                TtsProcessHistory.builder()
                        .text("test-text")
                        .alpha(10)
                        .speed(20.0F)
                        .emotion(2)
                        .audioFormat(".wav")
                        .endPitch(4.0F)
                        .startPitch(3)
                        .emotionStrength(2)
                        .volume(10)
                        .sortOrder(1)
                        .sampleRate(44000)
                        .voice(savedVoice)
                        .project(savedProject)
                        .ttsAudiofile(savedTtsAudiofile)
                        .build();

        // ttsProcessHistory 엔티티 저장
        TtsProcessHistory savedTtsProcessHistory = ttsProcessHistoryRepository.save(ttsProcessHistory);
        Long savedSeq = savedTtsProcessHistory.getTphSeq();
        assertNotNull(savedSeq);

        // 저장한 PK로 조회한 데이터와 저장한 데이터가 일치해야함
        Optional<TtsProcessHistory> selectedTtsProcessHistory =
                ttsProcessHistoryRepository.findById(savedSeq);
        assertNotNull(selectedTtsProcessHistory);

        TtsProcessHistory getTtsProcessHistory = selectedTtsProcessHistory.get();
        assertEquals(getTtsProcessHistory, ttsProcessHistory);
    }

    @Test
    @DisplayName("TtsProcessHistory 생성 테스트 - 일부 필드 값 입력 후 추가(필수 입력 포함)")
    public void saveTtsProcessHistoryWithSomeField() {

        // voice 엔티티 생성 및 저장
        Voice savedVoice = voiceRepository.save(createVoice());
        assertNotNull(savedVoice);

        // project 엔티티 생성 및 저장
        Project savedProject = projectRepository.save(createProject());
        assertNotNull(savedVoice);

        // TtsAudioFile 엔티티 생성
        TtsAudioFile savedTtsAudiofile = ttsAudioFileRepository.save(createTtsAudioFile());
        assertNotNull(savedTtsAudiofile);

        // 일부 필드 값 입력한 ttsProcessHistory 엔티티 생성
        TtsProcessHistory ttsProcessHistory =
                TtsProcessHistory.builder()
                        .text("test-text")
                        .voice(savedVoice)
                        .project(savedProject)
                        .ttsAudiofile(savedTtsAudiofile)
                        .alpha(10)
                        .speed(20.0F)
                        .emotion(2)
                        .audioFormat(".wav")
                        .build();

        // ttsProcessHistory 엔티티 저장
        TtsProcessHistory savedTtsProcessHistory = ttsProcessHistoryRepository.save(ttsProcessHistory);
        Long savedSeq = savedTtsProcessHistory.getTphSeq();
        assertNotNull(savedSeq);

        // 저장한 PK로 조회한 데이터와 저장한 데이터가 일치해야함
        Optional<TtsProcessHistory> selectedTtsProcessHistory =
                ttsProcessHistoryRepository.findById(savedSeq);
        assertNotNull(selectedTtsProcessHistory);

        TtsProcessHistory getTtsProcessHistory = selectedTtsProcessHistory.get();
        assertEquals(getTtsProcessHistory, ttsProcessHistory);
    }

    @Test
    @DisplayName("TtsProcessHistory 생성 테스트 - 일부 필드 값 입력 후 추가(필수 입력 미포함)")
    public void saveTtsProcessHistoryWithoutRequired() {

        // 필수 입력이 미포함된 ttsProcessHistory 엔티티 생성
        TtsProcessHistory ttsProcessHistory =
                TtsProcessHistory.builder()
                        .text("test-text")
                        .speed(20.0F)
                        .emotion(2)
                        .audioFormat(".wav")
                        .build();

        // 필수 입력이 미표함된 엔티티 저장 시 예외 발생
        assertThrows(
                DataIntegrityViolationException.class,
                () -> ttsProcessHistoryRepository.save(ttsProcessHistory));
    }

    @Test
    @DisplayName("TtsProcessHistory 생성 테스트 - 필드 값 입력 없이 추가")
    public void saveEmptyTtsProcessHistory() {

        // 필드 값 없는 ttsProcessHistory 엔티티 생성
        TtsProcessHistory ttsProcessHistory = TtsProcessHistory.builder().build();

        // 필드 값 없는 엔티티 저장 시 예외 발생
        assertThrows(
                DataIntegrityViolationException.class,
                () -> ttsProcessHistoryRepository.save(ttsProcessHistory));
    }

    /* 조회 테스트 */
    @Test
    @DisplayName("TtsProcessHistory 조회 테스트 - 한 개의 데이터 조회")
    public void selectTtsProcessHistory() {
        // voice 엔티티 생성 및 저장
        Voice savedVoice = voiceRepository.save(createVoice());
        assertNotNull(savedVoice);

        // project 엔티티 생성 및 저장
        Project savedProject = projectRepository.save(createProject());
        assertNotNull(savedVoice);

        // TtsAudioFile 엔티티 생성
        TtsAudioFile savedTtsAudiofile = ttsAudioFileRepository.save(createTtsAudioFile());
        assertNotNull(savedTtsAudiofile);

        // TtsProcessHistory 엔티티 생성
        TtsProcessHistory ttsProcessHistory =
                createTtsProcessHistory(savedVoice, savedProject, savedTtsAudiofile, 0);

        // ttsProcessHistory 엔티티 저장
        TtsProcessHistory savedTtsProcessHistory = ttsProcessHistoryRepository.save(ttsProcessHistory);
        Long savedSeq = savedTtsProcessHistory.getTphSeq();
        assertNotNull(savedSeq);

        // 저장된 엔티티가 1개여야함
        assertEquals(ttsProcessHistoryRepository.findAll().size(), 1);

        // 저장한 PK로 조회한 데이터와 저장한 데이터가 일치해야함
        Optional<TtsProcessHistory> selectedTtsProcessHistory =
                ttsProcessHistoryRepository.findById(savedSeq);
        assertNotNull(selectedTtsProcessHistory);

        TtsProcessHistory getTtsProcessHistory = selectedTtsProcessHistory.get();
        assertEquals(getTtsProcessHistory, ttsProcessHistory);
    }

    @Test
    @DisplayName("TtsProcessHistory 조회 테스트 - 여러 개의 데이터 조회 조회")
    public void selectTtsProcessHistoryList() {
        // voice 엔티티 생성 및 저장
        Voice savedVoice = voiceRepository.save(createVoice());
        assertNotNull(savedVoice);

        // project 엔티티 생성 및 저장
        Project savedProject = projectRepository.save(createProject());
        assertNotNull(savedVoice);

        // TtsProcessHistory 엔티티 10개 생성 및 저장
        List<TtsProcessHistory> ttsProcessHistoryList = new ArrayList<>();
        int cnt = 10;
        for (int i = 0; i < cnt; i++) {
            // TtsAudioFile 엔티티 생성(1:1 관계)
            TtsAudioFile savedTtsAudiofile = ttsAudioFileRepository.save(createTtsAudioFile(i));
            assertNotNull(savedTtsAudiofile);

            // 생성
            TtsProcessHistory ttsProcessHistory =
                    createTtsProcessHistory(savedVoice, savedProject, savedTtsAudiofile, i);
            ttsProcessHistoryList.add(ttsProcessHistory);

            // 저장
            assertNotNull(ttsProcessHistoryRepository.save(ttsProcessHistory));
        }

        // DB에 저장된 TtsProcessHistory 리스트 조회
        List<TtsProcessHistory> findTtsProcessHistoryList = ttsProcessHistoryRepository.findAll();

        // 조회한 리스트가 저장한 엔티티 10개 리스트와 일치해야함
        assertTrue(findTtsProcessHistoryList.containsAll(ttsProcessHistoryList));
    }

    /* 삭제 테스트 */
    @Test
    @DisplayName(" TtsProcessHistory 삭제 테스트 - 한 개 데이터 삭제")
    public void deleteTtsProcessHistory() {
        // voice 엔티티 생성 및 저장
        Voice savedVoice = voiceRepository.save(createVoice());
        assertNotNull(savedVoice);

        // project 엔티티 생성 및 저장
        Project savedProject = projectRepository.save(createProject());
        assertNotNull(savedVoice);

        // TtsAudioFile 엔티티 생성 및 저장
        TtsAudioFile savedTtsAudiofile = ttsAudioFileRepository.save(createTtsAudioFile());
        assertNotNull(savedTtsAudiofile);

        // TtsProcessHistory 엔티티 생성
        TtsProcessHistory ttsProcessHistory =
                createTtsProcessHistory(savedVoice, savedProject, savedTtsAudiofile, 0);

        // ttsProcessHistory 엔티티 저장
        TtsProcessHistory savedTtsProcessHistory = ttsProcessHistoryRepository.save(ttsProcessHistory);
        Long savedSeq = savedTtsProcessHistory.getTphSeq();
        assertNotNull(savedSeq);
        // 전체 조회 결과가 1이어야함
        assertEquals(ttsProcessHistoryRepository.findAll().size(), 1);

        // 저장된 PK로 해당 데이터 삭제
        ttsProcessHistoryRepository.deleteById(savedSeq);
        // 전체 조회 결과가 0이어야함
        assertEquals(ttsProcessHistoryRepository.findAll().size(), 0);
    }

    @Test
    @DisplayName(" TtsProcessHistory 삭제 테스트 - null 값으로 삭제")
    public void deleteNotExistTtsProcessHistory() {
        // null 값으로 데이터 삭제 시도 시 예외 발생
        assertThrows(
                InvalidDataAccessApiUsageException.class,
                () -> ttsProcessHistoryRepository.deleteById(null));
    }

    /* 헬퍼 메서드 */
    // Voice 엔티티 생성 메서드
    private Voice createVoice() {
        return Voice.builder()
                .name("Sample Voice")
                .gender("Male")
                .age(30)
                .description("Sample Description")
                .enabled('Y')
                .server(ServerCode.NAVER_CLOVA)
                .build();
    }

    // Project 엔티티 생성 메서드
    private Project createProject() {
        return Project.builder().build();
    }

    // TtsAudioFile 엔티티 생성 메서드
    private TtsAudioFile createTtsAudioFile() {
        return createTtsAudioFile(0);
    }

    private TtsAudioFile createTtsAudioFile(int n) {
        return TtsAudioFile.builder().audioName("audio" + n).audioPath("/tts/").build();
    }

    // TtsProcessHistory 엔티티 생성 메서드
    private TtsProcessHistory createTtsProcessHistory(
            Voice voice, Project project, TtsAudioFile ttsAudioFile, int n) {
        return TtsProcessHistory.builder()
                .text("test-text" + n)
                .voice(voice)
                .project(project)
                .ttsAudiofile(ttsAudioFile)
                .build();
    }
}
