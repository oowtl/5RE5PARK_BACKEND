package com.oreo.finalproject_5re5_be.tts.service;

import com.oreo.finalproject_5re5_be.global.component.S3Service;
import com.oreo.finalproject_5re5_be.project.entity.Project;
import com.oreo.finalproject_5re5_be.tts.entity.*;
import com.oreo.finalproject_5re5_be.tts.exception.SaveTtsMakeResultException;
import com.oreo.finalproject_5re5_be.tts.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class SaveTtsMakeResultTest {

    @Autowired
    private SaveTtsMakeResultService saveTtsMakeResultService;

    @MockBean
    private TtsSentenceRepository ttsSentenceRepository;

    @MockBean
    private VoiceRepository voiceRepository;

    @MockBean
    private S3Service s3Service;

    @MockBean
    private TtsProgressStatusRepository ttsProgressStatusRepository;

    @MockBean
    private TtsAudioFileRepository ttsAudioFileRepository;


    // TTS 생성 테스트 - 결과 저장 재시도 테스트
    @Test
    public void retrySaveResultTest() throws IOException {
        // 1. given: Mock 데이터 생성
        Long langSeq = 100L;
        Long voiceSeq = 30L;
        Long tsSeq = 1L;
        Project project = createProject();
        Language language = createLanguage(langSeq);
        Voice voice = createVoice(voiceSeq, language);
        TtsSentence ttsSentence = createTtsSentence(tsSeq, voice, project);
        String ttsUrl = "//aws/tts/test-audio";
        MockMultipartFile mockFile = new MockMultipartFile(
                "test",
                "test.wav",
                "audio/wav",
                new FileInputStream(new File("test.wav"))
        );

        // 2. repository 동작 설정 - ttsSentence 저장할 때 QueryTimeException 발생시키기
        when(ttsAudioFileRepository.save(any(TtsAudioFile.class))).thenReturn(TtsAudioFile.builder().build());
        when(ttsSentenceRepository.save(any(TtsSentence.class))).thenThrow(new QueryTimeoutException(""));

        // 3. when: ttsMake 서비스 실행
        assertThatThrownBy(() -> saveTtsMakeResultService.saveTtsMakeResult(mockFile, ttsUrl, ttsSentence))
                .isInstanceOf(SaveTtsMakeResultException.class); // Retry 및 Recover 동작으로 인해 SaveTtsMakeResultException이 발생하는지 확인

        // 4. then: saveTtsMakeResult가 3번 호출되었는지 확인
        verify(ttsAudioFileRepository, times(3)).save(any(TtsAudioFile.class));

        // 5. s3 파일 삭제 로직이 호출 되었는지 확인
        verify(s3Service, times(1)).deleteFile(anyString(), anyString());
    }

    // Voice 엔티티 생성 메서드
    private Voice createVoice(Long seq, Language language) {
        return Voice.builder()
                .voiceSeq(seq)
                .name("nl-NL-Standard-D")
                .gender("female")
                .age(30)
                .description("Sample Description")
                .enabled('Y')
                .server(ServerCode.GOOGLE_CLOUD)
                .language(language)
                .build();
    }

    private Language createLanguage(Long seq) {
        return Language.builder()
                .langSeq(seq)
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
    private TtsSentence createTtsSentence(Long seq,Voice voice, Project project) {
        return TtsSentence.builder()
                .tsSeq(seq)
                .text("Sample TtsSentence")
                .sortOrder(1)
                .volume(10)
                .speed(1.0f)
                .endPitch(0.0f)
                .voice(voice)
                .project(project)
                .build();
    }

    // TtsProgressStatus 엔티티 생성 메서드
    private TtsProgressStatus createTtsProgressStatus(Long seq, TtsSentence ttsSentence, TtsProgressStatusCode progressStatusCode) {
        return TtsProgressStatus.builder()
                .tpsSeq(seq)
                .ttsSentence(ttsSentence)
                .progressStatus(progressStatusCode)
                .build();
    }
}
