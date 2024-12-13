package com.oreo.finalproject_5re5_be.tts.service.ttsSentenceService;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.oreo.finalproject_5re5_be.project.entity.Project;
import com.oreo.finalproject_5re5_be.project.exception.ProjectNotFoundException;
import com.oreo.finalproject_5re5_be.project.repository.ProjectRepository;
import com.oreo.finalproject_5re5_be.tts.dto.request.TtsAttributeInfo;
import com.oreo.finalproject_5re5_be.tts.dto.request.TtsSentenceRequest;
import com.oreo.finalproject_5re5_be.tts.dto.response.TtsSentenceDto;
import com.oreo.finalproject_5re5_be.tts.entity.TtsProgressStatus;
import com.oreo.finalproject_5re5_be.tts.entity.TtsProgressStatusCode;
import com.oreo.finalproject_5re5_be.tts.entity.TtsSentence;
import com.oreo.finalproject_5re5_be.tts.entity.Voice;
import com.oreo.finalproject_5re5_be.tts.exception.VoiceEntityNotFound;
import com.oreo.finalproject_5re5_be.tts.repository.TtsProgressStatusRepository;
import com.oreo.finalproject_5re5_be.tts.repository.TtsSentenceRepository;
import com.oreo.finalproject_5re5_be.tts.repository.VoiceRepository;
import com.oreo.finalproject_5re5_be.tts.service.TtsSentenceService;
import jakarta.validation.ConstraintViolationException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class TestAddSentence {

    @Autowired private TtsSentenceService ttsSentenceService;

    @MockBean private TtsSentenceRepository ttsSentenceRepository;

    @MockBean private ProjectRepository projectRepository;

    @MockBean private VoiceRepository voiceRepository;

    @MockBean private TtsProgressStatusRepository ttsProgressStatusRepository;

    /*
    필수 정보 유효성 검증
    1. text : not null
    2. projectSeq : not null
    3. projectSeq : 조회 가능한 projectSeq (존재 여부)
    4. voiceSeq : not blank
    5. voiceSeq : 조회 가능한 voiceSeq (존재 여부)

    옵션 정보 유효성 검증
    1. attribute : 옵션 정보 유효성 검증

    addSentence 검증
    1. TtsSentence 추가 성공
     */

    // 1. 필수 정보 유효성 검증 - text: not null
    @Test
    @DisplayName("필수 정보 유효성 검증 - text: not blank")
    void validateTextNotBlank() {
        // given
        Long projectSeq = 99999L;

        // 1. TtsAttributeInfo 생성하기
        TtsAttributeInfo ttsAttributeInfo =
                TtsAttributeInfo.of(10, 1.0f, 0, "normal", 0, 16000, 0, 0.0f, "wav");

        // 2. TtsSentenceRequest 생성하기
        // text 가 null 인 경우
        TtsSentenceRequest nullTextCreateRequest = TtsSentenceRequest.of(1L, null, 1, ttsAttributeInfo);
        // text 가 empty 인 경우
        TtsSentenceRequest emptyTextCreateRequest = TtsSentenceRequest.of(1L, "", 1, ttsAttributeInfo);
        // text 가 blank 인 경우
        TtsSentenceRequest blankTextCreateRequest = TtsSentenceRequest.of(1L, " ", 1, ttsAttributeInfo);

        // when, then
        // 3. ConstraintViolationException 발생
        assertThrows(
                ConstraintViolationException.class,
                () -> ttsSentenceService.addSentence(projectSeq, nullTextCreateRequest));
        assertThrows(
                ConstraintViolationException.class,
                () -> ttsSentenceService.addSentence(projectSeq, emptyTextCreateRequest));
        assertThrows(
                ConstraintViolationException.class,
                () -> ttsSentenceService.addSentence(projectSeq, blankTextCreateRequest));
    }

    // 2. 필수 정보 유효성 검증 - projectSeq: not null
    @Test
    @DisplayName("필수 정보 유효성 검증 - projectSeq: not null")
    void validateProjectSeqNotNull() {
        // given

        // 1. TtsAttributeInfo 생성하기
        TtsAttributeInfo ttsAttributeInfo =
                TtsAttributeInfo.of(10, 1.0f, 0, "normal", 0, 16000, 0, 0.0f, "wav");

        // 2. TtsSentenceRequest 생성하기
        TtsSentenceRequest ttsSentenceRequest = TtsSentenceRequest.of(1L, "text", 1, ttsAttributeInfo);

        // when, then
        // 3. ConstraintViolationException 발생
        assertThrows(
                ConstraintViolationException.class,
                () -> ttsSentenceService.addSentence(null, ttsSentenceRequest));
    }

    // 3. 필수 정보 유효성 검증 - projectSeq: 조회 가능한 projectSeq (존재 여부)
    @Test
    @DisplayName("필수 정보 유효성 검증 - projectSeq: 조회 가능한 projectSeq (존재 여부)")
    void validateProjectSeqExist() {
        // given
        Long projectSeq = 99999L;

        // 1. TtsAttributeInfo 생성하기
        TtsAttributeInfo ttsAttributeInfo =
                TtsAttributeInfo.of(10, 1.0f, 0, "normal", 0, 16000, 0, 0.0f, "wav");

        // 2. TtsSentenceRequest 생성하기
        TtsSentenceRequest ttsSentenceRequest = TtsSentenceRequest.of(1L, "text", 1, ttsAttributeInfo);

        // 3. project repository findById 메소드가 null 을 반환하도록 설정
        when(projectRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when, then
        // 3. IllegalArgumentException 발생
        assertThrows(
                ProjectNotFoundException.class,
                () -> ttsSentenceService.addSentence(projectSeq, ttsSentenceRequest));
    }

    // 4. 필수 정보 유효성 검증 - voiceSeq: not null
    @Test
    @DisplayName("필수 정보 유효성 검증 - voiceSeq: not blank")
    void validateVoiceSeqNotNull() {
        // given
        Long projectSeq = 99999L;

        // 1. TtsAttributeInfo 생성하기
        TtsAttributeInfo ttsAttributeInfo =
                TtsAttributeInfo.of(10, 1.0f, 0, "normal", 0, 16000, 0, 0.0f, "wav");

        // 2. TtsSentenceRequest 생성하기
        TtsSentenceRequest nullVoiceSeqCreateRequest =
                TtsSentenceRequest.of(1L, "text", 1, ttsAttributeInfo);

        // when, then
        // 3. ConstraintViolationException 발생
        assertThrows(
                ProjectNotFoundException.class,
                () -> ttsSentenceService.addSentence(projectSeq, nullVoiceSeqCreateRequest));
    }

    // 5. 필수 정보 유효성 검증 - voiceSeq: 조회 가능한 voiceSeq (존재 여부)
    @Test
    @DisplayName("필수 정보 유효성 검증 - voiceSeq: 조회 가능한 voiceSeq (존재 여부)")
    void validateVoiceSeqExist() {
        // given
        Long projectSeq = 99999L;

        // 1. TtsAttributeInfo 생성하기
        TtsAttributeInfo ttsAttributeInfo =
                TtsAttributeInfo.of(10, 1.0f, 0, "normal", 0, 16000, 0, 0.0f, "wav");

        // 2. TtsSentenceRequest 생성하기
        TtsSentenceRequest ttsSentenceRequest = TtsSentenceRequest.of(1L, "text", 1, ttsAttributeInfo);

        // 3. Project 객체 생성하기 및 projectRepository findById 메소드가 객체를 반환
        Project project = Project.builder().proSeq(projectSeq).build();
        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(project));

        // 4. VoiceRepository findById 메소드가 null 을 반환하도록 설정
        when(voiceRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when, then
        // 3. IllegalArgumentException 발생
        assertThrows(
                VoiceEntityNotFound.class,
                () -> ttsSentenceService.addSentence(projectSeq, ttsSentenceRequest));
    }

    // 7. TtsAttributeInfo 유효성 검증 : volume 값이 0보다 작거나 100보다 클 때
    @Test
    @DisplayName("TtsAttributeInfo 유효성 검증 : volume 값이 0보다 작거나 100보다 클 때")
    void validateVolumeConstraint() {
        // given
        Long projectSeq = 99999L;
        Long testVoiceSeq = 99999L;

        Integer testMinusVolume = -100;
        Integer testPlusVolume = 100;

        // 1. TtsAttributeInfo 생성하기
        TtsAttributeInfo minusVolumeAttribute =
                TtsAttributeInfo.of(testMinusVolume, 1.0f, 0, "normal", 0, 16000, 0, 0.0f, "wav");
        TtsAttributeInfo plusVolumeAttribute =
                TtsAttributeInfo.of(testPlusVolume, 1.0f, 0, "normal", 0, 16000, 0, 0.0f, "wav");

        // 2. TtsSentenceRequest 생성하기
        TtsSentenceRequest minusVolumeRequest =
                TtsSentenceRequest.of(testVoiceSeq, "test", 1, minusVolumeAttribute);
        TtsSentenceRequest plusVolumeRequest =
                TtsSentenceRequest.of(testVoiceSeq, "test", 1, plusVolumeAttribute);

        // 3. project, voice repository findById 메소드가 객체를 반환
        Project project = Project.builder().proSeq(projectSeq).build();
        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(project));

        Voice voice = Voice.builder().voiceSeq(testVoiceSeq).build();
        when(voiceRepository.findById(anyLong())).thenReturn(Optional.of(voice));

        // when, then
        // 4. ConstraintViolationException 발생
        assertThrows(
                ConstraintViolationException.class,
                () -> ttsSentenceService.addSentence(projectSeq, minusVolumeRequest));
        assertThrows(
                ConstraintViolationException.class,
                () -> ttsSentenceService.addSentence(projectSeq, plusVolumeRequest));
    }

    // 8. TtsAttributeInfo 유효성 검증 : stPitch 값이 -20보다 작거나 20보다 클 때
    @Test
    @DisplayName("TtsAttributeInfo 유효성 검증 : stPitch 값이 -20보다 작거나 20보다 클 때")
    void validateStartPitchConstraint() {
        // given
        Long projectSeq = 99999L;
        Long testVoiceSeq = 99999L;
        Integer testMinusStPitch = -100;
        Integer testPlusStPitch = 100;

        // 1. TtsAttributeInfo 생성하기
        TtsAttributeInfo minusStPitchAttribute =
                TtsAttributeInfo.of(10, 1.0f, testMinusStPitch, "normal", 0, 16000, 0, 0.0f, "wav");
        TtsAttributeInfo plusStPitchAttribute =
                TtsAttributeInfo.of(10, 1.0f, testPlusStPitch, "normal", 0, 16000, 0, 0.0f, "wav");

        // 2. TtsSentenceRequest 생성하기
        TtsSentenceRequest minusStPitchRequest =
                TtsSentenceRequest.of(testVoiceSeq, "test", 1, minusStPitchAttribute);
        TtsSentenceRequest plusStPitchRequest =
                TtsSentenceRequest.of(testVoiceSeq, "test", 1, plusStPitchAttribute);

        // 3. project, voice
        // repository findById 메소드가 객체를 반환
        Project project = Project.builder().proSeq(projectSeq).build();
        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(project));

        Voice voice = Voice.builder().voiceSeq(testVoiceSeq).build();
        when(voiceRepository.findById(anyLong())).thenReturn(Optional.of(voice));

        // when, then
        // 4. ConstraintViolationException 발생
        assertThrows(
                ConstraintViolationException.class,
                () -> ttsSentenceService.addSentence(projectSeq, minusStPitchRequest));
        assertThrows(
                ConstraintViolationException.class,
                () -> ttsSentenceService.addSentence(projectSeq, plusStPitchRequest));
    }

    // addSentence 검증
    // 1. TtsSentence 추가 성공
    @Test
    @DisplayName("TtsSentence 추가 성공")
    void addSentenceSuccess() {
        // given
        Long projectSeq = 99999L;
        Long testVoiceSeq = 99999L;

        // 1. TtsAttributeInfo 생성하기
        TtsAttributeInfo ttsAttributeInfo =
                TtsAttributeInfo.of(10, 1.0f, 0, "normal", 0, 16000, 0, 0.0f, "wav");

        // 2. TtsSentenceRequest 생성하기
        TtsSentenceRequest ttsSentenceRequest =
                TtsSentenceRequest.of(testVoiceSeq, "text", 1, ttsAttributeInfo);

        // 3. project repository findById 메소드가 객체를 반환
        Project project = Project.builder().proSeq(projectSeq).build();
        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(project));

        // 4. voice repository findById 메소드가 객체를 반환
        Voice voice = Voice.builder().voiceSeq(testVoiceSeq).build();
        when(voiceRepository.findById(anyLong())).thenReturn(Optional.of(voice));

        // 6. ttsSentenceRepository save 메소드가 객체를 반환
        TtsSentence ttsSentence =
                TtsSentence.builder()
                        .text(ttsSentenceRequest.getText())
                        .sortOrder(ttsSentenceRequest.getOrder())
                        .volume(ttsAttributeInfo.getVolume())
                        .speed(ttsAttributeInfo.getSpeed())
                        .startPitch(ttsAttributeInfo.getStPitch())
                        .emotion(ttsAttributeInfo.getEmotion())
                        .emotionStrength(ttsAttributeInfo.getEmotionStrength())
                        .sampleRate(ttsAttributeInfo.getSampleRate())
                        .alpha(ttsAttributeInfo.getAlpha())
                        .endPitch(ttsAttributeInfo.getEndPitch())
                        .audioFormat(ttsAttributeInfo.getAudioFormat())
                        .project(project)
                        .voice(voice)
                        .build();
        when(ttsSentenceRepository.save(ttsSentence)).thenReturn(ttsSentence);

        // 7. ttsProgressStatusRepository save 메소드가 객체를 반환
        TtsProgressStatus ttsProgressStatus =
                TtsProgressStatus.builder()
                        .ttsSentence(ttsSentence)
                        .progressStatus(TtsProgressStatusCode.CREATED)
                        .build();
        when(ttsProgressStatusRepository.save(any(TtsProgressStatus.class)))
                .thenReturn(ttsProgressStatus);

        // when
        // 8. ttsSentenceService addSentence 메소드 호출
        TtsSentenceDto ttsSentenceResponse =
                ttsSentenceService.addSentence(projectSeq, ttsSentenceRequest);

        // then
        // 9. ttsSentenceResponse 가 null 이 아님
        assertNotNull(ttsSentenceResponse);
    }
}
