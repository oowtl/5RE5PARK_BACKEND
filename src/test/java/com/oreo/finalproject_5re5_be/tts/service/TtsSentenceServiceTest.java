package com.oreo.finalproject_5re5_be.tts.service;

import com.oreo.finalproject_5re5_be.global.exception.EntityNotFoundException;
import com.oreo.finalproject_5re5_be.project.entity.Project;
import com.oreo.finalproject_5re5_be.project.repository.ProjectRepository;
import com.oreo.finalproject_5re5_be.tts.dto.request.TtsAttributeInfo;
import com.oreo.finalproject_5re5_be.tts.dto.request.TtsSentenceCreateRequest;
import com.oreo.finalproject_5re5_be.tts.dto.request.TtsSentenceUpdateRequest;
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
    public void updateValidateProjectSeqNotNull() {
        // given: projectSeq가 null이고, updateRequest에는 필요한 필드가 유효한 값으로 설정됨
        Long projectSeq = null;

        // 1. attribute 설정
        TtsAttributeInfo attribute = createAttribute();

        // 2. updateRequest 생성
        TtsSentenceUpdateRequest updateRequest = createUpdateRequest(attribute);

        // when, then: projectSeq가 null이므로 ConstraintViolationException이 발생해야 함
        assertThrows(ConstraintViolationException.class, () -> ttsSentenceService.updateSentence(projectSeq, updateRequest));
    }

    // 2. 필수 정보 유효성 검증 : projectSeq가 존재하지 않을 때
    @Test
    @DisplayName("필수 정보 유효성 검증 : projectSeq가 존재하지 않을 때")
    public void updateValidateProjectSeqExists() {
        // given: projectSeq가 데이터베이스에 존재하지 않음
        Long projectSeq = 99999L;

        // 1. attribute 설정
        TtsAttributeInfo attribute = createAttribute();

        // 2. updateRequest 생성
        TtsSentenceUpdateRequest updateRequest = createUpdateRequest(attribute);

        // 3. projectRepository에서 projectSeq가 조회되지 않도록 설정
        when(projectRepository.findById(projectSeq)).thenReturn(Optional.empty());

        // when, the
        // 존재하지 않는 projectSeq로 인해 EntityNotFoundException이 발생해야 함
        assertThrows(EntityNotFoundException.class, () -> ttsSentenceService.updateSentence(projectSeq, updateRequest));
    }

    // 3. 필수 정보 유효성 검증 : voiceSeq가 null일 때
    @Test
    @DisplayName("필수 정보 유효성 검증 : voiceSeq가 null일 때")
    public void updateValidateVoiceSeqNotNull() {
        // given
        Long projectSeq = 1L;

        // 1. attribute 설정
        TtsAttributeInfo attribute = createAttribute();

        // 2. voiceSeq가 null로 설정된 updateRequest 생성
        TtsSentenceUpdateRequest updateRequest = TtsSentenceUpdateRequest.builder()
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
        assertThrows(ConstraintViolationException.class, () -> ttsSentenceService.updateSentence(projectSeq, updateRequest));
    }

    // 4. 필수 정보 유효성 검증 : voiceSeq가 존재하지 않을 때
    @Test
    @DisplayName("필수 정보 유효성 검증 : voiceSeq가 존재하지 않을 때")
    public void validateVoiceSeqExists() {
        // given: voiceSeq가 데이터베이스에 존재하지 않음
        Long projectSeq = 1L;
        Long voiceSeq = 99999L;

        // 1. attribute 설정
        TtsAttributeInfo attribute = createAttribute();

        // 2. updateRequest 생성
        TtsSentenceUpdateRequest updateRequest = TtsSentenceUpdateRequest.builder()
                .voiceSeq(voiceSeq)
                .tsSeq(1L)
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
        assertThrows(EntityNotFoundException.class, () -> ttsSentenceService.updateSentence(projectSeq, updateRequest));
    }

    // 5. 옵션 정보 유효성 검증 : styleSeq가 존재하지 않을 때
    @Test
    @DisplayName("옵션 정보 유효성 검증 : styleSeq가 존재하지 않을 때")
    public void validateStyleSeqExists() {
        // given: styleSeq가 데이터베이스에 존재하지 않음
        Long projectSeq = 1L;
        Long notFoundStyleSeq = 99999L;

        // 1. attribute 설정
        TtsAttributeInfo attribute = createAttribute();

        // 2. updateRequest 생성
        TtsSentenceUpdateRequest updateRequest = TtsSentenceUpdateRequest.builder()
                .voiceSeq(1L)
                .tsSeq(1L)
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
        assertThrows(EntityNotFoundException.class, () -> ttsSentenceService.updateSentence(projectSeq, updateRequest));
    }

    // 6. TtsSentence 수정 성공
    @Test
    @DisplayName("TtsSentence 수정 성공")
    public void updateSentenceSuccess() {
        // given: 유효한 projectSeq, voiceSeq, styleSeq와 수정 요청 생성
        Long projectSeq = 1L;
        Long sentenceSeq = 1L;
        Long updatedVoiceSeq = 2L;
        Long updatedStyleSeq = 3L;
        Integer updatedOrder = 2;
        String updatedText = "Updated text";

        // 1. attribute 설정
        TtsAttributeInfo attribute = createAttribute();

        // 2. updateRequest 생성
        TtsSentenceUpdateRequest updateRequest = createUpdateRequest(sentenceSeq, updatedVoiceSeq, updatedStyleSeq, updatedText, updatedOrder, attribute);

        // 3. mock 데이터 생성
        Project project = Project.builder().proSeq(projectSeq).build();
        Voice voice = Voice.builder().voiceSeq(updatedVoiceSeq).build();
        Style style = Style.builder().styleSeq(updatedStyleSeq).build();

        // 4. 기존 TtsSentence 객체 생성
        TtsSentence originalSentence = TtsSentence.builder()
                .tsSeq(sentenceSeq)
                .text("Original text")
                .voice(voice)
                .style(style)
                .sortOrder(1)
                .build();

        // 5. 수정된 TtsSentence 객체 생성
        TtsSentence updatedSentence = TtsSentence.builder()
                .tsSeq(sentenceSeq)
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
        when(ttsSentenceRepository.findById(sentenceSeq)).thenReturn(Optional.of(originalSentence));
        when(ttsSentenceRepository.save(any(TtsSentence.class))).thenReturn(updatedSentence);

        // when: updateSentence 메서드 호출
        TtsSentenceDto updateResult = ttsSentenceService.updateSentence(projectSeq, updateRequest);

        // then: 수정된 값이 반환되고 저장 메서드가 호출되었는지 검증
        assertNotNull(updateResult);
        assertEquals(updateRequest.getText(), updateResult.getSentence().getText());
        assertEquals(updateRequest.getVoiceSeq(), updateResult.getSentence().getVoiceSeq());
        assertEquals(updateRequest.getStyleSeq(), updateResult.getSentence().getStyleSeq());
        assertEquals(updateRequest.getAttribute().getVolume(), updateResult.getSentence().getTtsAttributeInfo().getVolume());
        assertEquals(updateRequest.getAttribute().getSpeed(), updateResult.getSentence().getTtsAttributeInfo().getSpeed());
        assertEquals(updateRequest.getAttribute().getStPitch(), updateResult.getSentence().getTtsAttributeInfo().getStPitch());
        assertEquals(updateRequest.getAttribute().getEmotion(), updateResult.getSentence().getTtsAttributeInfo().getEmotion());
        assertEquals(updateRequest.getAttribute().getEmotionStrength(), updateResult.getSentence().getTtsAttributeInfo().getEmotionStrength());
        assertEquals(updateRequest.getAttribute().getSampleRate(), updateResult.getSentence().getTtsAttributeInfo().getSampleRate());
        assertEquals(updateRequest.getAttribute().getAlpha(), updateResult.getSentence().getTtsAttributeInfo().getAlpha());
        assertEquals(updateRequest.getAttribute().getEndPitch(), updateResult.getSentence().getTtsAttributeInfo().getEndPitch());
        assertEquals(updateRequest.getAttribute().getAudioFormat(), updateResult.getSentence().getTtsAttributeInfo().getAudioFormat());
    }

    private TtsAttributeInfo createAttribute() {
        return TtsAttributeInfo.of(100, 1.0f, 0, "normal", 0, 16000, 0, 0.0f, "wav");
    }

    private TtsSentenceUpdateRequest createUpdateRequest(TtsAttributeInfo attribute) {
        return TtsSentenceUpdateRequest.builder()
                .tsSeq(1L)
                .voiceSeq(1L)
                .styleSeq(1L)
                .order(1)
                .text("안녕하세요")
                .attribute(attribute)
                .build();
    }

    private TtsSentenceUpdateRequest createUpdateRequest(Long tsSeq, Long voiceSeq, Long styleSeq, String updatedText, Integer order, TtsAttributeInfo attribute) {
        return TtsSentenceUpdateRequest.builder()
                .tsSeq(tsSeq)
                .voiceSeq(voiceSeq)
                .styleSeq(styleSeq)
                .order(order)
                .text(updatedText)
                .attribute(attribute)
                .build();
    }

}