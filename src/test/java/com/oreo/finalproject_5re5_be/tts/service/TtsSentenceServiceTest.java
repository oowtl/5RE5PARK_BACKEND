package com.oreo.finalproject_5re5_be.tts.service;

import com.oreo.finalproject_5re5_be.project.entity.Project;
import com.oreo.finalproject_5re5_be.project.repository.ProjectRepository;
import com.oreo.finalproject_5re5_be.tts.dto.request.TtsAttributeInfo;
import com.oreo.finalproject_5re5_be.tts.dto.request.TtsSentenceCreateRequest;
import com.oreo.finalproject_5re5_be.tts.dto.response.TtsSentenceDto;
import com.oreo.finalproject_5re5_be.tts.entity.*;
import com.oreo.finalproject_5re5_be.tts.repository.*;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class TtsSentenceServiceTest {

    @Autowired
    private TtsSentenceService ttsSentenceService;

    @MockBean
    private TtsSentenceRepository ttsSentenceRepository;

    @MockBean
    private ProjectRepository projectRepository;

    @MockBean
    private TtsAudioFileRepository ttsAudioFileRepository;

    @MockBean
    private VoiceRepository voiceRepository;

    @MockBean
    private StyleRepository styleRepository;

    @MockBean
    private TtsProgressStatusRepository ttsProgressStatusRepository;

    /*
    필수 정보 유효성 검증
    1. text : not null
    2. projectSeq : not null
    3. projectSeq : 조회 가능한 projectSeq (존재 여부)
    4. voiceSeq : not blank
    5. voiceSeq : 조회 가능한 voiceSeq (존재 여부)

    옵션 정보 유효성 검증
    1. styleSeq : 조회 가능한 스타일 id (존재 여부)
    2. attribute : 옵션 정보 유효성 검증

    addSentence 검증
    1. TtsSentence 추가 성공
     */

    // 1. 필수 정보 유효성 검증 - text: not null
    @Test
    @DisplayName("필수 정보 유효성 검증 - text: not blank")
    public void validateTextNotBlank() {
        // given
        Long projectSeq = 99999L;

        // 1. TtsAttributeInfo 생성하기
        TtsAttributeInfo ttsAttributeInfo = TtsAttributeInfo.of(100, 1.0f, 0, "normal", 0, 16000, 0, 0.0f, "wav");

        // 2. TtsSentenceCreateRequest 생성하기
        // text가 null 인 경우
        TtsSentenceCreateRequest nullTextCreateRequest = TtsSentenceCreateRequest.of(1L, 1L, null, 1, ttsAttributeInfo);
        // text가 empty 인 경우
        TtsSentenceCreateRequest emptyTextCreateRequest = TtsSentenceCreateRequest.of(1L, 1L, "", 1, ttsAttributeInfo);
        // text가 blank 인 경우
        TtsSentenceCreateRequest blankTextCreateRequest = TtsSentenceCreateRequest.of(1L, 1L, " ", 1, ttsAttributeInfo);

        // when, then
        // 3. ConstraintViolationException 발생
        assertThrows(ConstraintViolationException.class, () -> ttsSentenceService.addSentence(projectSeq, nullTextCreateRequest));
        assertThrows(ConstraintViolationException.class, () -> ttsSentenceService.addSentence(projectSeq, emptyTextCreateRequest));
        assertThrows(ConstraintViolationException.class, () -> ttsSentenceService.addSentence(projectSeq, blankTextCreateRequest));
    }

    // 2. 필수 정보 유효성 검증 - projectSeq: not null
    @Test
    @DisplayName("필수 정보 유효성 검증 - projectSeq: not null")
    public void validateProjectSeqNotNull() {
        // given
        Long projectSeq = null;

        // 1. TtsAttributeInfo 생성하기
        TtsAttributeInfo ttsAttributeInfo = TtsAttributeInfo.of(100, 1.0f, 0, "normal", 0, 16000, 0, 0.0f, "wav");

        // 2. TtsSentenceCreateRequest 생성하기
        TtsSentenceCreateRequest ttsSentenceCreateRequest = TtsSentenceCreateRequest.of(1L, 1L, "text", 1, ttsAttributeInfo);

        // when, then
        // 3. ConstraintViolationException 발생
        assertThrows(ConstraintViolationException.class, () -> ttsSentenceService.addSentence(projectSeq, ttsSentenceCreateRequest));
    }

    // 3. 필수 정보 유효성 검증 - projectSeq: 조회 가능한 projectSeq (존재 여부)
    @Test
    @DisplayName("필수 정보 유효성 검증 - projectSeq: 조회 가능한 projectSeq (존재 여부)")
    public void validateProjectSeqExist() {
        // given
        Long projectSeq = 99999L;

        // 1. TtsAttributeInfo 생성하기
        TtsAttributeInfo ttsAttributeInfo = TtsAttributeInfo.of(100, 1.0f, 0, "normal", 0, 16000, 0, 0.0f, "wav");

        // 2. TtsSentenceCreateRequest 생성하기
        TtsSentenceCreateRequest ttsSentenceCreateRequest = TtsSentenceCreateRequest.of(1L, 1L, "text", 1, ttsAttributeInfo);

        // 3. project repository findById 메소드가 null을 반환하도록 설정
        when(projectRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when, then
        // 3. IllegalArgumentException 발생
        assertThrows(IllegalArgumentException.class, () -> ttsSentenceService.addSentence(projectSeq, ttsSentenceCreateRequest));
    }

    // 4. 필수 정보 유효성 검증 - voiceSeq: not null
    @Test
    @DisplayName("필수 정보 유효성 검증 - voiceSeq: not blank")
    public void validateVoiceSeqNotNull() {
        // given
        Long projectSeq = 99999L;

        // 1. TtsAttributeInfo 생성하기
        TtsAttributeInfo ttsAttributeInfo = TtsAttributeInfo.of(100, 1.0f, 0, "normal", 0, 16000, 0, 0.0f, "wav");

        // 2. TtsSentenceCreateRequest 생성하기
        TtsSentenceCreateRequest nullVoiceSeqCreateRequest = TtsSentenceCreateRequest.of(1L, null, "text", 1, ttsAttributeInfo);

        // when, then
        // 3. ConstraintViolationException 발생
        assertThrows(ConstraintViolationException.class, () -> ttsSentenceService.addSentence(projectSeq, nullVoiceSeqCreateRequest));
    }

    // 5. 필수 정보 유효성 검증 - voiceSeq: 조회 가능한 voiceSeq (존재 여부)
    @Test
    @DisplayName("필수 정보 유효성 검증 - voiceSeq: 조회 가능한 voiceSeq (존재 여부)")
    public void validateVoiceSeqExist() {
        // given
        Long projectSeq = 99999L;
        Long testVoiceSeq = 99999L;

        // 1. TtsAttributeInfo 생성하기
        TtsAttributeInfo ttsAttributeInfo = TtsAttributeInfo.of(100, 1.0f, 0, "normal", 0, 16000, 0, 0.0f, "wav");

        // 2. TtsSentenceCreateRequest 생성하기
        TtsSentenceCreateRequest ttsSentenceCreateRequest = TtsSentenceCreateRequest.of(1L, testVoiceSeq, "text", 1, ttsAttributeInfo);

        // 3. Project 객체 생성하기 및 projectRepository findById 메소드가 객체를 반환
        Project project = Project.builder().proSeq(projectSeq).build();
        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(project));

        // 4. VoiceRepository findByVoiceSeq 메소드가 null을 반환하도록 설정
        when(voiceRepository.findByVoiceSeq(anyLong())).thenReturn(Optional.empty());

        // when, then
        // 3. IllegalArgumentException 발생
        assertThrows(IllegalArgumentException.class, () -> ttsSentenceService.addSentence(projectSeq, ttsSentenceCreateRequest));
    }

    // 6. 옵션 정보 유효성 검증 - styleSeq: 조회 가능한 스타일 id (존재 여부)
    @Test
    @DisplayName("옵션 정보 유효성 검증 - styleSeq: 조회 가능한 스타일 id (존재 여부)")
    public void validateStyleSeqExist() {
        // given
        Long projectSeq = 99999L;
        Long testVoiceSeq = 99999L;
        Long testStyleSeq = 99999L;

        // 1. TtsAttributeInfo 생성하기
        TtsAttributeInfo ttsAttributeInfo = TtsAttributeInfo.of(100, 1.0f, 0, "normal", 0, 16000, 0, 0.0f, "wav");

        // 2. TtsSentenceCreateRequest 생성하기
        TtsSentenceCreateRequest ttsSentenceCreateRequest = TtsSentenceCreateRequest.of(testStyleSeq, testVoiceSeq, "text", 1, ttsAttributeInfo);

        // 3. project, voice repository findById 메소드가 객체를 반환하도록 설정
        Project project = Project.builder().proSeq(projectSeq).build();
        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(project));

        // 4. voice repository findByVoiceSeq 메소드가 객체를 반환하도록 설정
        Voice voice = Voice.builder().voiceSeq(testVoiceSeq).build();
        when(voiceRepository.findByVoiceSeq(anyLong())).thenReturn(Optional.of(voice));

        // 3. style repository findById 메소드가 Optional.empty 을 반환하도록 설정
        when(styleRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when, then
        // 3. IllegalArgumentException 발생
        assertThrows(IllegalArgumentException.class, () -> ttsSentenceService.addSentence(projectSeq, ttsSentenceCreateRequest));
    }
    
    // 7. TtsAttributeInfo 유효성 검증 : volume 값이 0보다 작거나 100보다 클 때
    @Test
    @DisplayName("TtsAttributeInfo 유효성 검증 : volume 값이 0보다 작거나 100보다 클 때")
    public void validateVolumeConstraint() {
        // given
        Long projectSeq = 99999L;
        Long testVoiceSeq = 99999L;
        Long testStyleSeq = 99999L;

        Integer testMinusVolume = -100;
        Integer testPlusVolume = -100;

        // 1. TtsAttributeInfo 생성하기
        TtsAttributeInfo minusVolumeAttribute = TtsAttributeInfo.of(testMinusVolume, 1.0f, 0, "normal", 0, 16000, 0, 0.0f, "wav");
        TtsAttributeInfo plusVolumeAttribute = TtsAttributeInfo.of(testPlusVolume, 1.0f, 0, "normal", 0, 16000, 0, 0.0f, "wav");

        // 2. TtsSentenceCreateRequest 생성하기
        TtsSentenceCreateRequest minusVolumeRequest = TtsSentenceCreateRequest.of(testStyleSeq, testVoiceSeq, "test", 1, minusVolumeAttribute);
        TtsSentenceCreateRequest plusVolumeRequest = TtsSentenceCreateRequest.of(testStyleSeq, testVoiceSeq, "test", 1, plusVolumeAttribute);

        // 3. project, voice, style repository findById 메소드가 객체를 반환
        Project project = Project.builder().proSeq(projectSeq).build();
        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(project));

        Voice voice = Voice.builder().voiceSeq(testVoiceSeq).build();
        when(voiceRepository.findByVoiceSeq(anyLong())).thenReturn(Optional.of(voice));

        Style style = Style.builder().styleSeq(testStyleSeq).build();
        when(styleRepository.findById(anyLong())).thenReturn(Optional.of(style));

        // when, then
        // 4. ConstraintViolationException 발생
        assertThrows(ConstraintViolationException.class, () -> ttsSentenceService.addSentence(projectSeq, minusVolumeRequest));
        assertThrows(ConstraintViolationException.class, () -> ttsSentenceService.addSentence(projectSeq, plusVolumeRequest));
    }

    // 8. TtsAttributeInfo 유효성 검증 : stPitch 값이 -20보다 작거나 20보다 클 때
    @Test
    @DisplayName("TtsAttributeInfo 유효성 검증 : stPitch 값이 -20보다 작거나 20보다 클 때")
    public void validateStartPitchConstraint() {
        // given
        Long projectSeq = 99999L;
        Long testVoiceSeq = 99999L;
        Long testStyleSeq = 99999L;

        Integer testMinusStPitch = -100;
        Integer testPlusStPitch = 100;

        // 1. TtsAttributeInfo 생성하기
        TtsAttributeInfo minusStPitchAttribute = TtsAttributeInfo.of(100, 1.0f, testMinusStPitch, "normal", 0, 16000, 0, 0.0f, "wav");
        TtsAttributeInfo plusStPitchAttribute = TtsAttributeInfo.of(100, 1.0f, testPlusStPitch, "normal", 0, 16000, 0, 0.0f, "wav");

        // 2. TtsSentenceCreateRequest 생성하기
        TtsSentenceCreateRequest minusStPitchRequest = TtsSentenceCreateRequest.of(testStyleSeq, testVoiceSeq, "test", 1, minusStPitchAttribute);
        TtsSentenceCreateRequest plusStPitchRequest = TtsSentenceCreateRequest.of(testStyleSeq, testVoiceSeq, "test", 1, plusStPitchAttribute);

        // 3. project, voice, style repository findById 메소드가 객체를 반환
        Project project = Project.builder().proSeq(projectSeq).build();
        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(project));

        Voice voice = Voice.builder().voiceSeq(testVoiceSeq).build();
        when(voiceRepository.findByVoiceSeq(anyLong())).thenReturn(Optional.of(voice));

        Style style = Style.builder().styleSeq(testStyleSeq).build();
        when(styleRepository.findById(anyLong())).thenReturn(Optional.of(style));

        // when, then
        // 4. ConstraintViolationException 발생
        assertThrows(ConstraintViolationException.class, () -> ttsSentenceService.addSentence(projectSeq, minusStPitchRequest));
        assertThrows(ConstraintViolationException.class, () -> ttsSentenceService.addSentence(projectSeq, plusStPitchRequest));
    }

    // addSentence 검증
    // 1. TtsSentence 추가 성공
    @Test
    @DisplayName("TtsSentence 추가 성공")
    public void addSentenceSuccess() {
        // given
        Long projectSeq = 99999L;
        Long testVoiceSeq = 99999L;
        Long testStyleSeq = 99999L;

        // 1. TtsAttributeInfo 생성하기
        TtsAttributeInfo ttsAttributeInfo = TtsAttributeInfo.of(100, 1.0f, 0, "normal", 0, 16000, 0, 0.0f, "wav");

        // 2. TtsSentenceCreateRequest 생성하기
        TtsSentenceCreateRequest ttsSentenceCreateRequest = TtsSentenceCreateRequest.of(testStyleSeq, testVoiceSeq, "text", 1, ttsAttributeInfo);

        // 3. project repository findById 메소드가 객체를 반환
        Project project = Project.builder().proSeq(projectSeq).build();
        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(project));

        // 4. voice repository findByVoiceSeq 메소드가 객체를 반환
        Voice voice = Voice.builder().voiceSeq(testVoiceSeq).build();
        when(voiceRepository.findByVoiceSeq(anyLong())).thenReturn(Optional.of(voice));

        // 5. style repository findById 메소드가 객체를 반환
        Style style = Style.builder().styleSeq(testStyleSeq).build();
        when(styleRepository.findById(anyLong())).thenReturn(Optional.of(style));

        // 6. ttsSentenceRepository save 메소드가 객체를 반환
        TtsSentence ttsSentence = TtsSentence.builder()
                .text(ttsSentenceCreateRequest.getText())
                .sortOrder(ttsSentenceCreateRequest.getOrder())
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
                .style(style)
                .build();
        when(ttsSentenceRepository.save(ttsSentence)).thenReturn(ttsSentence);

        // 7. ttsProgressStatusRepository save 메소드가 객체를 반환
        TtsProgressStatus ttsProgressStatus = TtsProgressStatus.builder()
                .ttsSentence(ttsSentence)
                .progressStatus(TtsProgressStatusCode.CREATED)
                .build();
        when(ttsProgressStatusRepository.save(any(TtsProgressStatus.class))).thenReturn(ttsProgressStatus);

        // when
        // 8. ttsSentenceService addSentence 메소드 호출
        TtsSentenceDto ttsSentenceResponse = ttsSentenceService.addSentence(projectSeq, ttsSentenceCreateRequest);

        // then
        // 9. ttsSentenceResponse 가 null 이 아님
        assertNotNull(ttsSentenceResponse);
    }
}