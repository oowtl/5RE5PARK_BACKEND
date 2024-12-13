package com.oreo.finalproject_5re5_be.tts.service;

import static org.junit.jupiter.api.Assertions.*;

import com.oreo.finalproject_5re5_be.global.exception.EntityNotFoundException;
import com.oreo.finalproject_5re5_be.project.entity.Project;
import com.oreo.finalproject_5re5_be.project.repository.ProjectRepository;
import com.oreo.finalproject_5re5_be.tts.entity.*;
import com.oreo.finalproject_5re5_be.tts.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class TtsMakeServiceTest {
    @Autowired TtsMakeService ttsMakeService;
    @Autowired private TtsSentenceRepository ttsSentenceRepository;
    @Autowired private VoiceRepository voiceRepository;
    @Autowired private ProjectRepository projectRepository;
    @Autowired private LanguageRepository languageRepository;
    @Autowired private TtsAudioFileRepository ttsAudioFileRepository;
    @Autowired private TtsProcessHistoryRepository ttsProcessHistoryRepository;
    @Autowired private TtsProgressStatusRepository ttsProgressStatusRepository;

    /*
     * [ tts 생성 서비스 테스트 ]
     * 1. 행의 정보로 TTS 생성
     * 2. 존재하지 않는 행의 정보로 TTS 생성
     * */

    // 테스트로 인한 TTS 생성 및 s3 업로드를 막기 위해 빌드 테스트에서 제외 합니다.
    //    @Test
    //    @DisplayName("tts 생성 테스트 - 행의 정보로 TTS 생성")
    //    public void ttsMakeServiceTest() throws Exception {
    //        // 1. given: Voice, Project, TtsSentence 정보 저장
    //        Language savedLanguage = languageRepository.save(createLanguage());
    //        assertNotNull(savedLanguage);
    //        Voice savedVoice = voiceRepository.save(createVoice(savedLanguage));
    //        assertNotNull(savedVoice);
    //        Project savedProject= projectRepository.save(createProject());
    //        assertNotNull(savedProject);
    //        TtsSentence savedTtsSentence = ttsSentenceRepository.save(createTtsSentence(savedVoice,
    // savedProject));
    //        assertNotNull(savedTtsSentence);
    //
    //        // 2. when: tts 생성 서비스 수행
    //        TtsSentenceDto ttsSentenceDto = ttsMakeService.makeTts(savedTtsSentence.getTsSeq());
    //        assertNotNull(ttsSentenceDto);
    //        assertNotNull(ttsSentenceDto.getSentence());
    //        SentenceInfo sentenceInfo = ttsSentenceDto.getSentence();
    //
    //        // 3. then: TTS 행의 오디오 파일 정보와 조회한 결과가 같은지 확인
    //        TtsAudioFile findTtsAudioFile =
    //
    // ttsAudioFileRepository.findById(sentenceInfo.getTtsAudioFileInfo().getTtsAudioSeq()).get();
    //        assertNotNull(findTtsAudioFile);
    //        assertEquals(sentenceInfo.getTtsAudioFileInfo().getAudioUrl(),
    // findTtsAudioFile.getAudioPath());
    //
    //        // 4. then: TTS 처리 내역 리스트 조회 결과 갯수가 1인지 확인
    //        List<TtsProcessHistory> list = ttsProcessHistoryRepository.findAll();
    //        assertTrue(list.size() == 1);
    //
    //        // 5. then: 해당 TTS 행의 처리 상태 조회 결과가 IN_PROGRESS, FINISH 상태를 가지고 있는지 확인
    //        List<TtsProgressStatus> statusList =
    // ttsProgressStatusRepository.findAllByTtsSentence(savedTtsSentence);
    //        assertTrue(statusList.stream()
    //                .anyMatch(status ->
    // TtsProgressStatusCode.IN_PROGRESS.equals(status.getProgressStatus()))
    //        );
    //        assertTrue(statusList.stream()
    //                .anyMatch(status ->
    // TtsProgressStatusCode.FINISHED.equals(status.getProgressStatus()))
    //        );
    //    }

    @Test
    @DisplayName("tts 생성 테스트 - 존재하지 않는 행의 정보로 TTS 생성")
    public void ttsMakeServiceTestFromNotExistTs() throws Exception {
        // 존재하지 않는 행 확인
        Long notExistedId = 2000L;
        assertFalse(ttsSentenceRepository.findById(notExistedId).isPresent());

        assertThrows(EntityNotFoundException.class, () -> ttsMakeService.makeTts(notExistedId));
    }

    // Voice 엔티티 생성 메서드
    private Voice createVoice(Language language) {
        return Voice.builder()
                .name("nl-NL-Standard-D")
                .gender("female")
                .age(30)
                .description("Sample Description")
                .enabled('Y')
                .server(ServerCode.GOOGLE_CLOUD)
                .language(language)
                .build();
    }

    private Language createLanguage() {
        return Language.builder()
                .langCode("nl-NL")
                .regionName("뉴질랜드")
                .regionCode("하하")
                .enabled('y')
                .langName("영어")
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
                .volume(10)
                .speed(1.0f)
                .endPitch(0.0f)
                .voice(voice)
                .project(project)
                .build();
    }
}
