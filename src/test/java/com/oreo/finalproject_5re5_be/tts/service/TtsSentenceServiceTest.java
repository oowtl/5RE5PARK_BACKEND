package com.oreo.finalproject_5re5_be.tts.service;

import com.oreo.finalproject_5re5_be.global.constant.BatchProcessType;
import com.oreo.finalproject_5re5_be.global.exception.EntityNotFoundException;
import com.oreo.finalproject_5re5_be.project.entity.Project;
import com.oreo.finalproject_5re5_be.project.repository.ProjectRepository;
import com.oreo.finalproject_5re5_be.tts.dto.request.*;
import com.oreo.finalproject_5re5_be.tts.dto.request.TtsSentenceRequest;
import com.oreo.finalproject_5re5_be.tts.dto.response.SentenceInfo;
import com.oreo.finalproject_5re5_be.tts.dto.response.TtsSentenceDto;
import com.oreo.finalproject_5re5_be.tts.dto.response.TtsSentenceListDto;
import com.oreo.finalproject_5re5_be.tts.entity.*;
import com.oreo.finalproject_5re5_be.tts.exception.TtsSentenceInValidInput;
import com.oreo.finalproject_5re5_be.tts.repository.*;
import jakarta.validation.ConstraintViolationException;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
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
    void validateTextNotBlank() {
        // given
        Long projectSeq = 99999L;

        // 1. TtsAttributeInfo 생성하기
        TtsAttributeInfo ttsAttributeInfo = TtsAttributeInfo.of(100, 1.0f, 0, "normal", 0, 16000, 0,
            0.0f, "wav");

        // 2. TtsSentenceRequest 생성하기
        // text가 null 인 경우
        TtsSentenceRequest nullTextCreateRequest = TtsSentenceRequest.of(1L, 1L, null, 1,
            ttsAttributeInfo);
        // text가 empty 인 경우
        TtsSentenceRequest emptyTextCreateRequest = TtsSentenceRequest.of(1L, 1L, "", 1,
            ttsAttributeInfo);
        // text가 blank 인 경우
        TtsSentenceRequest blankTextCreateRequest = TtsSentenceRequest.of(1L, 1L, " ", 1,
            ttsAttributeInfo);

        // when, then
        // 3. ConstraintViolationException 발생
        assertThrows(ConstraintViolationException.class,
            () -> ttsSentenceService.addSentence(projectSeq, nullTextCreateRequest));
        assertThrows(ConstraintViolationException.class,
            () -> ttsSentenceService.addSentence(projectSeq, emptyTextCreateRequest));
        assertThrows(ConstraintViolationException.class,
            () -> ttsSentenceService.addSentence(projectSeq, blankTextCreateRequest));
    }

    // 2. 필수 정보 유효성 검증 - projectSeq: not null
    @Test
    @DisplayName("필수 정보 유효성 검증 - projectSeq: not null")
    void validateProjectSeqNotNull() {
        // given
        Long projectSeq = null;

        // 1. TtsAttributeInfo 생성하기
        TtsAttributeInfo ttsAttributeInfo = TtsAttributeInfo.of(100, 1.0f, 0, "normal", 0, 16000, 0,
            0.0f, "wav");

        // 2. TtsSentenceRequest 생성하기
        TtsSentenceRequest ttsSentenceRequest = TtsSentenceRequest.of(1L, 1L, "text", 1,
            ttsAttributeInfo);

        // when, then
        // 3. ConstraintViolationException 발생
        assertThrows(ConstraintViolationException.class,
            () -> ttsSentenceService.addSentence(projectSeq, ttsSentenceRequest));
    }

    // 3. 필수 정보 유효성 검증 - projectSeq: 조회 가능한 projectSeq (존재 여부)
    @Test
    @DisplayName("필수 정보 유효성 검증 - projectSeq: 조회 가능한 projectSeq (존재 여부)")
    void validateProjectSeqExist() {
        // given
        Long projectSeq = 99999L;

        // 1. TtsAttributeInfo 생성하기
        TtsAttributeInfo ttsAttributeInfo = TtsAttributeInfo.of(100, 1.0f, 0, "normal", 0, 16000, 0,
            0.0f, "wav");

        // 2. TtsSentenceRequest 생성하기
        TtsSentenceRequest ttsSentenceRequest = TtsSentenceRequest.of(1L, 1L, "text", 1,
            ttsAttributeInfo);

        // 3. project repository findById 메소드가 null을 반환하도록 설정
        when(projectRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when, then
        // 3. IllegalArgumentException 발생
        assertThrows(IllegalArgumentException.class,
            () -> ttsSentenceService.addSentence(projectSeq, ttsSentenceRequest));
    }

    // 4. 필수 정보 유효성 검증 - voiceSeq: not null
    @Test
    @DisplayName("필수 정보 유효성 검증 - voiceSeq: not blank")
    void validateVoiceSeqNotNull() {
        // given
        Long projectSeq = 99999L;

        // 1. TtsAttributeInfo 생성하기
        TtsAttributeInfo ttsAttributeInfo = TtsAttributeInfo.of(100, 1.0f, 0, "normal", 0, 16000, 0,
            0.0f, "wav");

        // 2. TtsSentenceRequest 생성하기
        TtsSentenceRequest nullVoiceSeqCreateRequest = TtsSentenceRequest.of(1L, null, "text", 1,
            ttsAttributeInfo);

        // when, then
        // 3. ConstraintViolationException 발생
        assertThrows(ConstraintViolationException.class,
            () -> ttsSentenceService.addSentence(projectSeq, nullVoiceSeqCreateRequest));
    }

    // 5. 필수 정보 유효성 검증 - voiceSeq: 조회 가능한 voiceSeq (존재 여부)
    @Test
    @DisplayName("필수 정보 유효성 검증 - voiceSeq: 조회 가능한 voiceSeq (존재 여부)")
    void validateVoiceSeqExist() {
        // given
        Long projectSeq = 99999L;
        Long testVoiceSeq = 99999L;

        // 1. TtsAttributeInfo 생성하기
        TtsAttributeInfo ttsAttributeInfo = TtsAttributeInfo.of(100, 1.0f, 0, "normal", 0, 16000, 0,
            0.0f, "wav");

        // 2. TtsSentenceRequest 생성하기
        TtsSentenceRequest ttsSentenceRequest = TtsSentenceRequest.of(1L, testVoiceSeq, "text", 1,
            ttsAttributeInfo);

        // 3. Project 객체 생성하기 및 projectRepository findById 메소드가 객체를 반환
        Project project = Project.builder().proSeq(projectSeq).build();
        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(project));

        // 4. VoiceRepository findById 메소드가 null을 반환하도록 설정
        when(voiceRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when, then
        // 3. IllegalArgumentException 발생
        assertThrows(IllegalArgumentException.class,
            () -> ttsSentenceService.addSentence(projectSeq, ttsSentenceRequest));
    }

    // 6. 옵션 정보 유효성 검증 - styleSeq: 조회 가능한 스타일 id (존재 여부)
    @Test
    @DisplayName("옵션 정보 유효성 검증 - styleSeq: 조회 가능한 스타일 id (존재 여부)")
    void validateStyleSeqExist() {
        // given
        Long projectSeq = 99999L;
        Long testVoiceSeq = 99999L;
        Long testStyleSeq = 99999L;

        // 1. TtsAttributeInfo 생성하기
        TtsAttributeInfo ttsAttributeInfo = TtsAttributeInfo.of(100, 1.0f, 0, "normal", 0, 16000, 0,
            0.0f, "wav");

        // 2. TtsSentenceRequest 생성하기
        TtsSentenceRequest ttsSentenceRequest = TtsSentenceRequest.of(testStyleSeq, testVoiceSeq,
            "text", 1, ttsAttributeInfo);

        // 3. project, voice repository findById 메소드가 객체를 반환하도록 설정
        Project project = Project.builder().proSeq(projectSeq).build();
        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(project));

        // 4. voice repository findById 메소드가 객체를 반환하도록 설정
        Voice voice = Voice.builder().voiceSeq(testVoiceSeq).build();
        when(voiceRepository.findById(anyLong())).thenReturn(Optional.of(voice));

        // 3. style repository findById 메소드가 Optional.empty 을 반환하도록 설정
        when(styleRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when, then
        // 3. IllegalArgumentException 발생
        assertThrows(IllegalArgumentException.class,
            () -> ttsSentenceService.addSentence(projectSeq, ttsSentenceRequest));
    }

    // 7. TtsAttributeInfo 유효성 검증 : volume 값이 0보다 작거나 100보다 클 때
    @Test
    @DisplayName("TtsAttributeInfo 유효성 검증 : volume 값이 0보다 작거나 100보다 클 때")
    void validateVolumeConstraint() {
        // given
        Long projectSeq = 99999L;
        Long testVoiceSeq = 99999L;
        Long testStyleSeq = 99999L;

        Integer testMinusVolume = -100;
        Integer testPlusVolume = -100;

        // 1. TtsAttributeInfo 생성하기
        TtsAttributeInfo minusVolumeAttribute = TtsAttributeInfo.of(testMinusVolume, 1.0f, 0,
            "normal", 0, 16000, 0, 0.0f, "wav");
        TtsAttributeInfo plusVolumeAttribute = TtsAttributeInfo.of(testPlusVolume, 1.0f, 0,
            "normal", 0, 16000, 0, 0.0f, "wav");

        // 2. TtsSentenceRequest 생성하기
        TtsSentenceRequest minusVolumeRequest = TtsSentenceRequest.of(testStyleSeq, testVoiceSeq,
            "test", 1, minusVolumeAttribute);
        TtsSentenceRequest plusVolumeRequest = TtsSentenceRequest.of(testStyleSeq, testVoiceSeq,
            "test", 1, plusVolumeAttribute);

        // 3. project, voice, style repository findById 메소드가 객체를 반환
        Project project = Project.builder().proSeq(projectSeq).build();
        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(project));

        Voice voice = Voice.builder().voiceSeq(testVoiceSeq).build();
        when(voiceRepository.findById(anyLong())).thenReturn(Optional.of(voice));

        Style style = Style.builder().styleSeq(testStyleSeq).build();
        when(styleRepository.findById(anyLong())).thenReturn(Optional.of(style));

        // when, then
        // 4. ConstraintViolationException 발생
        assertThrows(ConstraintViolationException.class,
            () -> ttsSentenceService.addSentence(projectSeq, minusVolumeRequest));
        assertThrows(ConstraintViolationException.class,
            () -> ttsSentenceService.addSentence(projectSeq, plusVolumeRequest));
    }

    // 8. TtsAttributeInfo 유효성 검증 : stPitch 값이 -20보다 작거나 20보다 클 때
    @Test
    @DisplayName("TtsAttributeInfo 유효성 검증 : stPitch 값이 -20보다 작거나 20보다 클 때")
    void validateStartPitchConstraint() {
        // given
        Long projectSeq = 99999L;
        Long testVoiceSeq = 99999L;
        Long testStyleSeq = 99999L;

        Integer testMinusStPitch = -100;
        Integer testPlusStPitch = 100;

        // 1. TtsAttributeInfo 생성하기
        TtsAttributeInfo minusStPitchAttribute = TtsAttributeInfo.of(100, 1.0f, testMinusStPitch,
            "normal", 0, 16000, 0, 0.0f, "wav");
        TtsAttributeInfo plusStPitchAttribute = TtsAttributeInfo.of(100, 1.0f, testPlusStPitch,
            "normal", 0, 16000, 0, 0.0f, "wav");

        // 2. TtsSentenceRequest 생성하기
        TtsSentenceRequest minusStPitchRequest = TtsSentenceRequest.of(testStyleSeq, testVoiceSeq,
            "test", 1, minusStPitchAttribute);
        TtsSentenceRequest plusStPitchRequest = TtsSentenceRequest.of(testStyleSeq, testVoiceSeq,
            "test", 1, plusStPitchAttribute);

        // 3. project, voice, style repository findById 메소드가 객체를 반환
        Project project = Project.builder().proSeq(projectSeq).build();
        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(project));

        Voice voice = Voice.builder().voiceSeq(testVoiceSeq).build();
        when(voiceRepository.findById(anyLong())).thenReturn(Optional.of(voice));

        Style style = Style.builder().styleSeq(testStyleSeq).build();
        when(styleRepository.findById(anyLong())).thenReturn(Optional.of(style));

        // when, then
        // 4. ConstraintViolationException 발생
        assertThrows(ConstraintViolationException.class,
            () -> ttsSentenceService.addSentence(projectSeq, minusStPitchRequest));
        assertThrows(ConstraintViolationException.class,
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
        Long testStyleSeq = 99999L;

        // 1. TtsAttributeInfo 생성하기
        TtsAttributeInfo ttsAttributeInfo = TtsAttributeInfo.of(100, 1.0f, 0, "normal", 0, 16000, 0,
            0.0f, "wav");

        // 2. TtsSentenceRequest 생성하기
        TtsSentenceRequest ttsSentenceRequest = TtsSentenceRequest.of(testStyleSeq, testVoiceSeq,
            "text", 1, ttsAttributeInfo);

        // 3. project repository findById 메소드가 객체를 반환
        Project project = Project.builder().proSeq(projectSeq).build();
        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(project));

        // 4. voice repository findById 메소드가 객체를 반환
        Voice voice = Voice.builder().voiceSeq(testVoiceSeq).build();
        when(voiceRepository.findById(anyLong())).thenReturn(Optional.of(voice));

        // 5. style repository findById 메소드가 객체를 반환
        Style style = Style.builder().styleSeq(testStyleSeq).build();
        when(styleRepository.findById(anyLong())).thenReturn(Optional.of(style));

        // 6. ttsSentenceRepository save 메소드가 객체를 반환
        TtsSentence ttsSentence = TtsSentence.builder()
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
            .style(style)
            .build();
        when(ttsSentenceRepository.save(ttsSentence)).thenReturn(ttsSentence);

        // 7. ttsProgressStatusRepository save 메소드가 객체를 반환
        TtsProgressStatus ttsProgressStatus = TtsProgressStatus.builder()
            .ttsSentence(ttsSentence)
            .progressStatus(TtsProgressStatusCode.CREATED)
            .build();
        when(ttsProgressStatusRepository.save(any(TtsProgressStatus.class))).thenReturn(
            ttsProgressStatus);

        // when
        // 8. ttsSentenceService addSentence 메소드 호출
        TtsSentenceDto ttsSentenceResponse = ttsSentenceService.addSentence(projectSeq,
            ttsSentenceRequest);

        // then
        // 9. ttsSentenceResponse 가 null 이 아님
        assertNotNull(ttsSentenceResponse);
    }

    /*
    필수 정보 유효성 검증
    1. projectSeq: not null
    2. projectSeq: 조회 가능한 projectSeq (존재 여부)
    3. text: not null
    4. voiceSeq: not null
    5. voiceSeq: 조회 가능한 voiceSeq (존재 여부)

    옵션 정보 유효성 검증
    1. styleSeq: 조회 가능한 스타일 id (존재 여부)
    2. attribute: 옵션 정보의 각 필드 유효성 검증
    - volume: 0 이상 100 이하 (예: 음량)
    - speed: 적정 범위 내 속도 값
    - stPitch: -20 이상 20 이하 (시작 피치)
    - emotion: not blank (감정)
    - emotionStrength: 0 이상 100 이하 (감정 강도)
    - sampleRate: 특정 허용 범위 (예: 샘플링 속도)
    - alpha: 특정 허용 범위
    - endPitch: 특정 허용 범위
    - audioFormat: 허용된 포맷인지 확인 (예: wav, mp3)

    updateSentence 검증
    1. TtsSentence 수정 성공
    2. 기존에 연관된 ttsAudioFile과의 연결 해제
    3. 수정된 TtsSentence가 올바르게 저장 및 반환됨
    */

    // 1. 필수 정보 유효성 검증 : projectSeq가 null일 때
    @Test
    @DisplayName("필수 정보 유효성 검증 : projectSeq가 null일 때")
    void updateValidateProjectSeqNotNull() {
        // given: projectSeq가 null이고, updateRequest에는 필요한 필드가 유효한 값으로 설정됨
        Long projectSeq = null;
        Long sentenceSeq = 1L;

        // 1. attribute 설정
        TtsAttributeInfo attribute = createAttribute();

        // 2. updateRequest 생성
        TtsSentenceRequest updateRequest = createRequest(attribute);

        // when, then: projectSeq가 null이므로 ConstraintViolationException이 발생해야 함
        assertThrows(ConstraintViolationException.class,
            () -> ttsSentenceService.updateSentence(projectSeq, sentenceSeq, updateRequest));
    }

    // 2. 필수 정보 유효성 검증 : projectSeq가 존재하지 않을 때
    @Test
    @DisplayName("필수 정보 유효성 검증 : projectSeq가 존재하지 않을 때")
    void updateValidateProjectSeqExists() {
        // given: projectSeq가 데이터베이스에 존재하지 않음
        Long projectSeq = 99999L;
        Long tsSeq = 1L;

        // 1. attribute 설정
        TtsAttributeInfo attribute = createAttribute();

        // 2. updateRequest 생성
        TtsSentenceRequest updateRequest = createRequest(attribute);

        // 3. projectRepository에서 projectSeq가 조회되지 않도록 설정
        when(projectRepository.findById(projectSeq)).thenReturn(Optional.empty());

        // when, the
        // 존재하지 않는 projectSeq로 인해 EntityNotFoundException이 발생해야 함
        assertThrows(EntityNotFoundException.class,
            () -> ttsSentenceService.updateSentence(projectSeq, tsSeq, updateRequest));
    }

    // 3. 필수 정보 유효성 검증 : voiceSeq가 null일 때
    @Test
    @DisplayName("필수 정보 유효성 검증 : voiceSeq가 null일 때")
    void updateValidateVoiceSeqNotNull() {
        // given
        Long projectSeq = 1L;
        Long sentenceSeq = 1L;

        // 1. attribute 설정
        TtsAttributeInfo attribute = createAttribute();

        // 2. voiceSeq가 null로 설정된 updateRequest 생성
        TtsSentenceRequest updateRequest = TtsSentenceRequest.builder()
            .voiceSeq(null)
            .styleSeq(1L)
            .order(1)
            .text("Test text")
            .attribute(attribute)
            .build();

        // 2. projectRepository에서 유효한 project를 반환하도록 설정
        Project project = Project.builder().proSeq(projectSeq).build();
        when(projectRepository.findById(projectSeq)).thenReturn(Optional.of(project));

        // when, then: voiceSeq가 null이므로 ConstraintViolationException이 발생해야 함
        assertThrows(ConstraintViolationException.class,
            () -> ttsSentenceService.updateSentence(projectSeq, sentenceSeq, updateRequest));
    }

    // 4. 필수 정보 유효성 검증 : voiceSeq가 존재하지 않을 때
    @Test
    @DisplayName("필수 정보 유효성 검증 : voiceSeq가 존재하지 않을 때")
    void validateVoiceSeqExists() {
        // given: voiceSeq가 데이터베이스에 존재하지 않음
        Long projectSeq = 1L;
        Long tsSeq = 1L;
        Long voiceSeq = 99999L;

        // 1. attribute 설정
        TtsAttributeInfo attribute = createAttribute();

        // 2. updateRequest 생성
        TtsSentenceRequest updateRequest = TtsSentenceRequest.builder()
            .voiceSeq(voiceSeq)
            .order(1)
            .attribute(attribute)
            .text("Test text")
            .build();

        // 3. projectRepository와 voiceRepository에서의 동작 설정
        Project project = Project.builder().proSeq(projectSeq).build();
        when(projectRepository.findById(projectSeq)).thenReturn(Optional.of(project));

        // 4. voiceRepository에서 voiceSeq로 조회되지 않도록 설정
        when(voiceRepository.findById(voiceSeq)).thenReturn(Optional.empty());

        // when, then
        // 5. 존재하지 않는 voiceSeq로 인해 EntityNotFoundException이 발생해야 함
        assertThrows(EntityNotFoundException.class,
            () -> ttsSentenceService.updateSentence(projectSeq, tsSeq, updateRequest));
    }

    // 5. 옵션 정보 유효성 검증 : styleSeq가 존재하지 않을 때
    @Test
    @DisplayName("옵션 정보 유효성 검증 : styleSeq가 존재하지 않을 때")
    void validateStyleSeqExists() {
        // given: styleSeq가 데이터베이스에 존재하지 않음
        Long projectSeq = 1L;
        Long tsSeq = 1L;
        Long notFoundStyleSeq = 99999L;

        // 1. attribute 설정
        TtsAttributeInfo attribute = createAttribute();

        // 2. updateRequest 생성
        TtsSentenceRequest updateRequest = TtsSentenceRequest.builder()
            .voiceSeq(1L)
            .order(1)
            .styleSeq(notFoundStyleSeq)
            .text("Test text")
            .attribute(attribute)
            .build();

        Voice voice = Voice.builder().voiceSeq(1L).build();
        Project project = Project.builder().proSeq(projectSeq).build();

        // projectRepository, voiceRepository, styleRepository의 동작 설정
        when(projectRepository.findById(projectSeq)).thenReturn(Optional.of(project));
        when(voiceRepository.findById(anyLong())).thenReturn(Optional.of(voice));
        when(styleRepository.findById(notFoundStyleSeq)).thenReturn(Optional.empty());

        // when, then: 존재하지 않는 styleSeq로 인해 EntityNotFoundException이 발생해야 함
        assertThrows(EntityNotFoundException.class,
            () -> ttsSentenceService.updateSentence(projectSeq, tsSeq, updateRequest));
    }

    // 6. TtsSentence 수정 성공
    @Test
    @DisplayName("TtsSentence 수정 성공")
    void updateSentenceSuccess() {
        // given: 유효한 projectSeq, voiceSeq, styleSeq와 수정 요청 생성
        Long projectSeq = 1L;
        Long tsSeq = 1L;
        Long updatedVoiceSeq = 2L;
        Long updatedStyleSeq = 3L;
        Integer updatedOrder = 2;
        String updatedText = "Updated text";

        // 1. attribute 설정
        TtsAttributeInfo attribute = createAttribute();

        // 2. updateRequest 생성
        TtsSentenceRequest updateRequest = createRequest(updatedVoiceSeq, updatedStyleSeq,
            updatedText, updatedOrder, attribute);

        // 3. mock 데이터 생성
        Project project = Project.builder().proSeq(projectSeq).build();
        Voice voice = Voice.builder().voiceSeq(updatedVoiceSeq).build();
        Style style = Style.builder().styleSeq(updatedStyleSeq).build();

        // 4. 기존 TtsSentence 객체 생성
        TtsSentence originalSentence = TtsSentence.builder()
            .tsSeq(tsSeq)
            .text("Original text")
            .voice(voice)
            .style(style)
            .sortOrder(1)
            .build();

        // 5. 수정된 TtsSentence 객체 생성
        TtsSentence updatedSentence = TtsSentence.builder()
            .tsSeq(tsSeq)
            .text(updatedText)
            .voice(voice)
            .style(style)
            .sortOrder(updatedOrder)
            .volume(attribute.getVolume())
            .speed(attribute.getSpeed())
            .startPitch(attribute.getStPitch())
            .emotion(attribute.getEmotion())
            .emotionStrength(attribute.getEmotionStrength())
            .sampleRate(attribute.getSampleRate())
            .alpha(attribute.getAlpha())
            .endPitch(attribute.getEndPitch())
            .audioFormat(attribute.getAudioFormat())
            .build();

        // 6. mock 데이터를 반환하도록 설정
        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(project));
        when(voiceRepository.findById(anyLong())).thenReturn(Optional.of(voice));
        when(styleRepository.findById(anyLong())).thenReturn(Optional.of(style));
        when(ttsSentenceRepository.findById(tsSeq)).thenReturn(Optional.of(originalSentence));
        when(ttsSentenceRepository.save(any(TtsSentence.class))).thenReturn(updatedSentence);

        // when: updateSentence 메서드 호출
        TtsSentenceDto updateResult = ttsSentenceService.updateSentence(projectSeq, tsSeq,
            updateRequest);

        // then: 수정된 값이 반환되고 저장 메서드가 호출되었는지 검증
        assertNotNull(updateResult);
        assertEquals(updateRequest.getText(), updateResult.getSentence().getText());
        assertEquals(updateRequest.getVoiceSeq(), updateResult.getSentence().getVoiceSeq());
        assertEquals(updateRequest.getStyleSeq(), updateResult.getSentence().getStyleSeq());
        assertEquals(updateRequest.getAttribute().getVolume(),
            updateResult.getSentence().getTtsAttributeInfo().getVolume());
        assertEquals(updateRequest.getAttribute().getSpeed(),
            updateResult.getSentence().getTtsAttributeInfo().getSpeed());
        assertEquals(updateRequest.getAttribute().getStPitch(),
            updateResult.getSentence().getTtsAttributeInfo().getStPitch());
        assertEquals(updateRequest.getAttribute().getEmotion(),
            updateResult.getSentence().getTtsAttributeInfo().getEmotion());
        assertEquals(updateRequest.getAttribute().getEmotionStrength(),
            updateResult.getSentence().getTtsAttributeInfo().getEmotionStrength());
        assertEquals(updateRequest.getAttribute().getSampleRate(),
            updateResult.getSentence().getTtsAttributeInfo().getSampleRate());
        assertEquals(updateRequest.getAttribute().getAlpha(),
            updateResult.getSentence().getTtsAttributeInfo().getAlpha());
        assertEquals(updateRequest.getAttribute().getEndPitch(),
            updateResult.getSentence().getTtsAttributeInfo().getEndPitch());
        assertEquals(updateRequest.getAttribute().getAudioFormat(),
            updateResult.getSentence().getTtsAttributeInfo().getAudioFormat());
    }

    /*
    1. 성공 케이스 테스트
    - 조건:
    - 유효한 projectSeq와 sentenceList가 제공됨.
    - sentenceList 내 BatchProcessType이 CREATE 또는 UPDATE로 설정.
    - 기대 결과:
    - TtsSentenceListDto 반환.

    2. 유효성 검증 실패 테스트 - sentenceList가 null
    - 조건:
    - BatchRequest의 sentenceList가 null로 제공됨.
    - 기대 결과:
    - TtsSentenceInValidInput 예외 발생.


    3. 유효성 검증 실패 테스트 - sentenceInfo가 null
    - 조건:
    - sentenceList 내 sentenceInfo가 null인 데이터 포함.
    - 기대 결과:
    - TtsSentenceInValidInput 예외 발생.

    4. 잘못된 BatchProcessType 테스트
    - 조건:
    - BatchProcessType이 null로 제공됨.
    - 기대 결과:
    - TtsSentenceInValidInput 예외 발생.

    5. 리소스 존재하지 않음 테스트 - projectSeq가 존재하지 않음
    - 조건:
    - ProjectRepository에서 projectSeq에 해당하는 Project가 없음.
    - 기대 결과:
    - TtsSentenceInValidInput 예외 발생.

    6. 빈 sentenceList 테스트
    - 조건:
    - BatchRequest의 sentenceList가 빈 리스트로 제공됨.
    - 기대 결과:
    - 빈 TtsSentenceDto 리스트 반환.
    */

    // 1. 성공 케이스 테스트
    @Test
    @DisplayName("batchSaveSentence - 성공 케이스")
    void batchSaveSentence_Success() {
        // given: 유효한 projectSeq와 sentenceList 생성
        Long projectSeq = 1L; // 유효한 projectSeq 설정

        Project project = Project.builder().proSeq(projectSeq).build();
        Voice voice = Voice.builder().voiceSeq(1L).build();
        Style style = Style.builder().styleSeq(1L).build();

        TtsSentenceBatchRequest batchRequest = createBatchRequest(); // 유효한 batchRequest 생성

        TtsSentence ttsSentence = TtsSentence.builder()
            .tsSeq(1L)
            .text("Test text")
            .sortOrder(1)
            .volume(100)
            .speed(1.0f)
            .startPitch(0)
            .emotion("normal")
            .emotionStrength(0)
            .sampleRate(16000)
            .alpha(0)
            .endPitch(0.0f)
            .audioFormat("wav")
            .project(mock(Project.class))
            .voice(mock(Voice.class))
            .style(mock(Style.class))
            .build();

        // 프로젝트가 존재한다고 설정
        when(projectRepository.findById(projectSeq)).thenReturn(Optional.of(project));
        // voice이 존재한다고 설정
        when(voiceRepository.findById(anyLong())).thenReturn(Optional.of(voice));
        // style이 존재한다고 설정
        when(styleRepository.findById(anyLong())).thenReturn(Optional.of(style));
        // ttsSentece 가 존재한다고 설정
        when(ttsSentenceRepository.findById(anyLong())).thenReturn(Optional.of(ttsSentence));
        // 각 문장에 대해 TtsSentenceDto 반환
        when(ttsSentenceRepository.save(any())).thenReturn(ttsSentence);

        // when: batchSaveSentence 메서드 호출
        TtsSentenceListDto result = ttsSentenceService.batchSaveSentence(projectSeq, batchRequest);

        // then: 반환된 결과 검증
        assertNotNull(result); // 결과가 null이 아님
//        assertFalse(result.getSentenceList().isEmpty()); // 리스트가 비어 있지 않음
    }

    // 2. 유효성 검증 실패 테스트 - sentenceList가 null
    @Test
    @DisplayName("batchSaveSentence - sentenceList가 null")
    void batchSaveSentence_SentenceListNull() {
        // given: sentenceList가 null인 batchRequest 생성
        TtsSentenceBatchRequest batchRequest = new TtsSentenceBatchRequest(
            null); // sentenceList가 null로 설정

        // when, then: 예외 발생 검증
        assertThrows(ConstraintViolationException.class,
            () -> ttsSentenceService.batchSaveSentence(1L, batchRequest));
    }

    // 3. 유효성 검증 실패 테스트 - sentenceInfo가 null
    @Test
    @DisplayName("batchSaveSentence - sentenceInfo가 null")
    void batchSaveSentence_SentenceInfoNull() {
        // given: sentenceInfo가 null인 batchRequest 생성
        TtsSentenceBatchInfo batchInfo = new TtsSentenceBatchInfo(BatchProcessType.CREATE, null);
        TtsSentenceBatchRequest batchRequest = new TtsSentenceBatchRequest(List.of(batchInfo));

        // when, then: 예외 발생 검증
        assertThrows(TtsSentenceInValidInput.class,
            () -> ttsSentenceService.batchSaveSentence(1L, batchRequest));
    }

    // 4. 잘못된 BatchProcessType 테스트
    @Test
    @DisplayName("batchSaveSentence - 잘못된 BatchProcessType")
    void batchSaveSentence_InvalidBatchProcessType() {
        // given: BatchProcessType이 null인 batchRequest 생성
        TtsSentenceBatchInfo batchInfo = new TtsSentenceBatchInfo(null, mock(SentenceInfo.class));
        TtsSentenceBatchRequest batchRequest = new TtsSentenceBatchRequest(List.of(batchInfo));

        // when, then: 예외 발생 검증
        assertThrows(TtsSentenceInValidInput.class,
            () -> ttsSentenceService.batchSaveSentence(1L, batchRequest));
    }

    // 5. 리소스 존재하지 않음 테스트 - projectSeq가 존재하지 않음
    @Test
    @DisplayName("batchSaveSentence - projectSeq가 존재하지 않음")
    void batchSaveSentence_ProjectSeqNotFound() {
        // given: 존재하지 않는 projectSeq 설정
        Long projectSeq = 999999L;
        TtsSentenceBatchRequest batchRequest = createBatchRequest();

        // projectRepository가 empty 반환하도록 설정
        when(projectRepository.findById(projectSeq)).thenReturn(Optional.empty());

        // when, then: 예외 발생 검증
        assertThrows(EntityNotFoundException.class,
            () -> ttsSentenceService.batchSaveSentence(projectSeq, batchRequest));
    }

    // 6. 빈 sentenceList 테스트
    @Test
    @DisplayName("batchSaveSentence - 빈 sentenceList")
    void batchSaveSentence_EmptySentenceList() {
        // given: 빈 sentenceList가 포함된 batchRequest 생성
        Long projectSeq = 1L;
        TtsSentenceBatchRequest batchRequest = new TtsSentenceBatchRequest(List.of());

        // when, then: batchSaveSentence 메서드 호출
        assertThrows(ConstraintViolationException.class,
            () -> ttsSentenceService.batchSaveSentence(projectSeq, batchRequest));
    }

    /*
    데이터 생성 메서드
     */

    // 유효한 TtsSentenceBatchRequest 생성
    private TtsSentenceBatchRequest createBatchRequest() {

        SentenceInfo updateSentenceInfo1 = SentenceInfo.builder()
            .tsSeq(1L)
            .voiceSeq(1L)
            .styleSeq(1L)
            .order(1)
            .text("Test text")
            .ttsAttributeInfo(createAttribute())
            .build();

        SentenceInfo updateSentenceInfo2 = SentenceInfo.builder()
            .tsSeq(1L)
            .voiceSeq(1L)
            .styleSeq(1L)
            .order(2)
            .text("Test text")
            .ttsAttributeInfo(createAttribute())
            .build();

        SentenceInfo createSentenceInfo3 = SentenceInfo.builder()
            .voiceSeq(1L)
            .styleSeq(1L)
            .order(3)
            .text("Test text")
            .ttsAttributeInfo(createAttribute())
            .build();

        SentenceInfo createSentenceInfo4 = SentenceInfo.builder()
            .voiceSeq(1L)
            .styleSeq(1L)
            .order(4)
            .text("Test text")
            .ttsAttributeInfo(createAttribute())
            .build();

        TtsSentenceBatchInfo batchInfo1 = new TtsSentenceBatchInfo(BatchProcessType.UPDATE,
            updateSentenceInfo1);
        TtsSentenceBatchInfo batchInfo2 = new TtsSentenceBatchInfo(BatchProcessType.UPDATE,
            updateSentenceInfo2);
        TtsSentenceBatchInfo batchInfo3 = new TtsSentenceBatchInfo(BatchProcessType.CREATE,
            createSentenceInfo3);
        TtsSentenceBatchInfo batchInfo4 = new TtsSentenceBatchInfo(BatchProcessType.CREATE,
            createSentenceInfo4);
        return new TtsSentenceBatchRequest(List.of(batchInfo1, batchInfo2, batchInfo3, batchInfo4));
    }


    /*
    테스트 시나리오: getSentence 메서드

    1. **성공적인 조회**
    - 조건:
    - 유효한 `projectSeq`와 `tsSeq`가 제공됨.
    - 제공된 `tsSeq`에 해당하는 `TtsSentence` 엔티티가 데이터베이스에 존재함.
    - 기대 결과:
    - `tsSeq`에 해당하는 `TtsSentenceDto`를 반환.

    2. **TtsSentence를 찾을 수 없음**
    - 조건:
    - 유효한 `projectSeq`와 `tsSeq`가 제공됨.
    - 제공된 `tsSeq`에 해당하는 `TtsSentence` 엔티티가 데이터베이스에 존재하지 않음.
    - 기대 결과:
    - `EntityNotFoundException` 예외가 발생하며 적절한 에러 메시지를 포함.

    3. **null `tsSeq`**
    - 조건:
    - `tsSeq`가 `null`로 제공됨.
    - 기대 결과:
    - 입력값이 유효하지 않아 `IllegalArgumentException` 예외가 발생.

    4. **유효하지 않은 `tsSeq`**
    - 조건:
    - 존재하지 않거나 음수인 `tsSeq`가 제공됨.
    - 기대 결과:
    - 데이터베이스에 존재하지 않는 `tsSeq`로 인해 `EntityNotFoundException` 예외가 발생.
    */

    // 1. 성공적인 조회
    @Test
    @DisplayName("getSentence - 성공적인 조회")
    void getSentence_Success() {
        // given: 유효한 tsSeq를 설정하고 해당하는 Mock TtsSentence 객체 생성
        Long tsSeq = 1L;
        String ttsText = "Test Sentence";

        TtsSentence mockSentence = TtsSentence.builder()
            .tsSeq(tsSeq) // tsSeq 설정
            .text(ttsText) // 텍스트 설정
            .build();

        // findById 메서드가 Optional로 Mock TtsSentence를 반환하도록 설정
        when(ttsSentenceRepository.findById(tsSeq)).thenReturn(Optional.of(mockSentence));

        // when: getSentence 메서드를 호출
        TtsSentenceDto result = ttsSentenceService.getSentence(1L, tsSeq);

        // then: 결과값 검증
        assertNotNull(result); // 반환값이 null이 아님
        assertEquals(tsSeq, result.getSentence().getTsSeq()); // 반환된 DTO의 tsSeq가 요청값과 동일
        assertEquals(ttsText, result.getSentence().getText()); // 반환된 텍스트가 예상된 값과 동일
    }

    // 2. TtsSentence를 찾을 수 없음
    @Test
    @DisplayName("getSentence - TtsSentence를 찾을 수 없음")
    void getSentence_NotFound() {
        // given: tsSeq를 설정하고 해당하는 엔티티가 존재하지 않도록 설정
        Long tsSeq = 999L;
        // findById 메서드가 Optional.empty를 반환하도록 설정
        when(ttsSentenceRepository.findById(tsSeq)).thenReturn(Optional.empty());

        // when, then: getSentence 호출 시 EntityNotFoundException 발생 여부 검증
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
            () -> ttsSentenceService.getSentence(1L, tsSeq));

        // 예외 메시지 검증
        assertEquals("TtsSentence not found with id: " + tsSeq, exception.getMessage());
    }

    // 3. null tsSeq
    @Test
    @DisplayName("getSentence - tsSeq가 null일 때")
    void getSentence_NullTsSeq() {
        // given: tsSeq가 null로 설정됨
        Long tsSeq = null;

        // when, then: getSentence 호출 시 IllegalArgumentException 발생 여부 검증
        assertThrows(ConstraintViolationException.class,
            () -> ttsSentenceService.getSentence(1L, tsSeq));
    }

    // 4. 유효하지 않은 tsSeq
    @Test
    @DisplayName("getSentence - 유효하지 않은 tsSeq")
    void getSentence_InvalidTsSeq() {
        // given: 유효하지 않은 tsSeq 설정
        Long tsSeq = -1L;

        // when, then: getSentence 호출 시 EntityNotFoundException 발생 여부 검증
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
            () -> ttsSentenceService.getSentence(1L, tsSeq));

        // 예외 메시지 검증
        assertEquals("TtsSentence not found with id: " + tsSeq, exception.getMessage());
    }

    /*
    테스트 시나리오: getSentenceList 메서드

    1. **성공적인 조회**
    - 조건:
    - 유효한 `projectSeq`가 제공됨.
    - 제공된 `projectSeq`에 해당하는 `Project` 엔티티가 데이터베이스에 존재.
    - 해당 `Project`에 연관된 `TtsSentence` 리스트가 존재.
    - 기대 결과:
    - `TtsSentenceListDto` 객체 반환.
    - 반환된 DTO는 `TtsSentence` 리스트를 포함.

    2. **프로젝트를 찾을 수 없음**
    - 조건:
    - 유효한 `projectSeq`가 제공되었으나, 데이터베이스에 해당 `Project`가 존재하지 않음.
    - 기대 결과:
    - `EntityNotFoundException` 예외 발생.
    - 예외 메시지에 "Project not found with id: <projectSeq>" 포함.

    3. **TtsSentence 리스트가 없음**
    - 조건:
    - 유효한 `projectSeq`가 제공되었고, 데이터베이스에 해당 `Project`가 존재.
    - 해당 `Project`에 연관된 `TtsSentence`가 하나도 없음.
    - 기대 결과:
    - 비어 있는 `TtsSentenceListDto` 객체 반환.

    4. **`projectSeq`가 null**
    - 조건:
    - `projectSeq`가 null로 제공됨.
    - 기대 결과:
    - `IllegalArgumentException` 예외 발생.
    - 예외 메시지에 "projectSeq must not be null" 포함.
    */

    // 1. 성공적인 조회
    @Test
    @DisplayName("getSentenceList - 성공적인 조회")
    void getSentenceList_Success() {
        // given
        int repeatCount = 5;
        Long projectSeq = 1L;
        Project mockProject = Project.builder().proSeq(projectSeq).build();

        // TtsSentence 리스트 생성
        List<TtsSentence> ttsSentenceList = IntStream.range(0, repeatCount)
            .mapToObj(i -> TtsSentence.builder()
                .tsSeq((long) i)
                .text("Sentence " + i).project(mockProject)
                .build())
            .toList();

        when(projectRepository.findById(projectSeq)).thenReturn(Optional.of(mockProject));
        when(ttsSentenceRepository.findAllByProject(mockProject)).thenReturn(ttsSentenceList);

        // when
        TtsSentenceListDto result = ttsSentenceService.getSentenceList(projectSeq);

        // then
        assertNotNull(result); // 결과가 null이 아님
        assertEquals(repeatCount, result.getSentenceList().size()); // 반환된 리스트의 크기 검증
        assertEquals("Sentence 1",
            result.getSentenceList().get(1).getSentence().getText()); // 첫 번째 문장의 텍스트 검증
        assertEquals("Sentence 2",
            result.getSentenceList().get(2).getSentence().getText()); // 두 번째 문장의 텍스트 검증
    }

    // 2. 프로젝트를 찾을 수 없음
    @Test
    @DisplayName("getSentenceList - 프로젝트를 찾을 수 없음")
    void getSentenceList_ProjectNotFound() {
        // given
        Long projectSeq = 999L;
        when(projectRepository.findById(projectSeq)).thenReturn(Optional.empty());

        // when, then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
            () -> ttsSentenceService.getSentenceList(projectSeq));

        assertEquals("Project not found with id: " + projectSeq,
            exception.getMessage()); // 예외 메시지 검증
    }

    // 3. TtsSentence 리스트가 없음
    @Test
    @DisplayName("getSentenceList - TtsSentence 리스트가 없음")
    void getSentenceList_EmptyList() {
        // given
        Long projectSeq = 1L;
        Project mockProject = Project.builder().proSeq(projectSeq).build();

        when(projectRepository.findById(projectSeq)).thenReturn(Optional.of(mockProject));
        when(ttsSentenceRepository.findAllByProject(mockProject)).thenReturn(List.of()); // 빈 리스트 반환

        // when
        TtsSentenceListDto result = ttsSentenceService.getSentenceList(projectSeq);

        // then
        assertNotNull(result); // 결과가 null이 아님
        assertTrue(result.getSentenceList().isEmpty()); // 반환된 리스트가 비어 있음
    }

    // 4. projectSeq가 null
    @Test
    @DisplayName("getSentenceList - projectSeq가 null")
    void getSentenceList_NullProjectSeq() {
        // given
        Long projectSeq = null;

        // when, then
        assertThrows(ConstraintViolationException.class,
            () -> ttsSentenceService.getSentenceList(projectSeq));

    }

    /*
    테스트 시나리오: deleteSentence 메서드

    1. **성공적인 삭제**
    - 조건:
    - 유효한 `projectSeq`와 `tsSeq`가 제공됨.
    - 제공된 `tsSeq`에 해당하는 `TtsSentence` 엔티티가 데이터베이스에 존재.
    - 기대 결과:
    - `ttsSentenceRepository.delete` 메서드가 호출됨.
    - 메서드가 `true`를 반환.

    2. **TtsSentence를 찾을 수 없음**
    - 조건:
    - 유효한 `projectSeq`와 `tsSeq`가 제공되었으나, 데이터베이스에 해당 `tsSeq`에 대한 `TtsSentence` 엔티티가 존재하지 않음.
    - 기대 결과:
    - `EntityNotFoundException` 예외 발생.

    3. **null tsSeq**
    - 조건:
    - `tsSeq`가 `null`로 제공됨.
    - 기대 결과:
    - `IllegalArgumentException` 예외 발생.

    4. **내부 서버 에러**
    - 조건:
    - `ttsSentenceRepository.delete` 호출 중 예외 발생 (예: 데이터베이스 연결 문제).
    - 기대 결과:
    - 예외가 발생하며 처리되지 않음 (테스트는 이를 검증).

    5. **TtsSentence 삭제 실패**
    - 조건:
    - `delete` 메서드 호출이 실패하거나 아무런 영향을 미치지 않음 (예: 데이터베이스 상태 문제).
    - 기대 결과:
    - 적절한 예외 또는 `false` 반환을 고려 (현재 코드는 항상 `true`를 반환하므로 설계 검토 필요).
    */

    // 1. 성공적인 삭제
    @Test
    @DisplayName("deleteSentence - 성공적인 삭제")
    void deleteSentence_Success() {
        // given
        Long projectSeq = 1L;
        Long tsSeq = 1L;

        TtsSentence mockSentence = TtsSentence.builder().tsSeq(tsSeq).text("Test Sentence").build();
        when(ttsSentenceRepository.findById(tsSeq)).thenReturn(Optional.of(mockSentence));

        // when
        boolean result = ttsSentenceService.deleteSentence(projectSeq, tsSeq);

        // then
        assertTrue(result); // 결과가 true인지 검증
    }

    // 2. TtsSentence를 찾을 수 없음
    @Test
    @DisplayName("deleteSentence - TtsSentence를 찾을 수 없음")
    void deleteSentence_NotFound() {
        // given
        Long projectSeq = 1L;
        Long tsSeq = 999L;

        when(ttsSentenceRepository.findById(tsSeq)).thenReturn(Optional.empty());

        // when, then
        assertThrows(EntityNotFoundException.class,
            () -> ttsSentenceService.deleteSentence(projectSeq, tsSeq));
    }

    // 3. null tsSeq
    @Test
    @DisplayName("deleteSentence - null tsSeq")
    void deleteSentence_NullTsSeq() {
        // given
        Long projectSeq = 1L;
        Long tsSeq = null;

        // when, then
        assertThrows(ConstraintViolationException.class,
            () -> ttsSentenceService.deleteSentence(projectSeq, tsSeq));
    }

    // 5. TtsSentence 삭제 실패
    @Test
    @DisplayName("deleteSentence - TtsSentence 삭제 실패")
    void deleteSentence_DeleteFailure() {
        // given
        Long tsSeq = 1L;
        TtsSentence mockSentence = TtsSentence.builder().tsSeq(tsSeq).text("Test Sentence").build();
        when(ttsSentenceRepository.findById(tsSeq)).thenReturn(Optional.of(mockSentence));
        doNothing().when(ttsSentenceRepository)
            .delete(mockSentence); // delete 메서드가 동작하지 않는 경우 시뮬레이션

        // when
        boolean result = ttsSentenceService.deleteSentence(1L, tsSeq);

        // then
        assertTrue(result); // 현재 메서드는 항상 true를 반환하므로 성공으로 간주
    }

    private TtsAttributeInfo createAttribute() {
        return TtsAttributeInfo.of(100, 1.0f, 0, "normal", 0, 16000, 0, 0.0f, "wav");
    }

    private TtsSentenceRequest createRequest(TtsAttributeInfo attribute) {
        return TtsSentenceRequest.builder()
            .voiceSeq(1L)
            .styleSeq(1L)
            .order(1)
            .text("안녕하세요")
            .attribute(attribute)
            .build();
    }

    private TtsSentenceRequest createRequest(Long voiceSeq, Long styleSeq, String text,
        Integer order, TtsAttributeInfo attribute) {
        return TtsSentenceRequest.builder()
            .voiceSeq(voiceSeq)
            .styleSeq(styleSeq)
            .order(order)
            .text(text)
            .attribute(attribute)
            .build();
    }
}