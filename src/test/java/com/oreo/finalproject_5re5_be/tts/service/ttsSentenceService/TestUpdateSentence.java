package com.oreo.finalproject_5re5_be.tts.service.ttsSentenceService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.oreo.finalproject_5re5_be.global.exception.EntityNotFoundException;
import com.oreo.finalproject_5re5_be.project.entity.Project;
import com.oreo.finalproject_5re5_be.project.repository.ProjectRepository;
import com.oreo.finalproject_5re5_be.tts.dto.request.TtsAttributeInfo;
import com.oreo.finalproject_5re5_be.tts.dto.request.TtsSentenceRequest;
import com.oreo.finalproject_5re5_be.tts.dto.response.TtsSentenceDto;
import com.oreo.finalproject_5re5_be.tts.entity.TtsSentence;
import com.oreo.finalproject_5re5_be.tts.entity.Voice;
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
class TestUpdateSentence {

    @Autowired private TtsSentenceService ttsSentenceService;

    @MockBean private TtsSentenceRepository ttsSentenceRepository;

    @MockBean private ProjectRepository projectRepository;

    @MockBean private VoiceRepository voiceRepository;

    /*
    필수 정보 유효성 검증
    1. projectSeq: not null
    2. projectSeq: 조회 가능한 projectSeq (존재 여부)
    3. text: not null
    4. voiceSeq: not null
    5. voiceSeq: 조회 가능한 voiceSeq (존재 여부)

    옵션 정보 유효성 검증
    1. attribute: 옵션 정보의 각 필드 유효성 검증
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
        assertThrows(
                ConstraintViolationException.class,
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
        assertThrows(
                EntityNotFoundException.class,
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
        TtsSentenceRequest updateRequest =
                TtsSentenceRequest.builder()
                        .voiceSeq(null)
                        .order(1)
                        .text("Test text")
                        .attribute(attribute)
                        .build();

        // 2. projectRepository에서 유효한 project를 반환하도록 설정
        Project project = Project.builder().proSeq(projectSeq).build();
        when(projectRepository.findById(projectSeq)).thenReturn(Optional.of(project));

        // when, then: voiceSeq가 null이므로 ConstraintViolationException이 발생해야 함
        assertThrows(
                ConstraintViolationException.class,
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
        TtsSentenceRequest updateRequest =
                TtsSentenceRequest.builder()
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
        assertThrows(
                EntityNotFoundException.class,
                () -> ttsSentenceService.updateSentence(projectSeq, tsSeq, updateRequest));
    }

    // 6. TtsSentence 수정 성공
    @Test
    @DisplayName("TtsSentence 수정 성공")
    void updateSentenceSuccess() {
        // given: 유효한 projectSeq, voiceSeq와 수정 요청 생성
        Long projectSeq = 1L;
        Long tsSeq = 1L;
        Long updatedVoiceSeq = 2L;
        Integer updatedOrder = 2;
        String updatedText = "Updated text";

        // 1. attribute 설정
        TtsAttributeInfo attribute = createAttribute();

        // 2. updateRequest 생성
        TtsSentenceRequest updateRequest =
                createRequest(updatedVoiceSeq, updatedText, updatedOrder, attribute);

        // 3. mock 데이터 생성
        Project project = Project.builder().proSeq(projectSeq).build();
        Voice voice = Voice.builder().voiceSeq(updatedVoiceSeq).build();

        // 4. 기존 TtsSentence 객체 생성
        TtsSentence originalSentence =
                TtsSentence.builder().tsSeq(tsSeq).text("Original text").voice(voice).sortOrder(1).build();

        // 5. 수정된 TtsSentence 객체 생성
        TtsSentence updatedSentence =
                TtsSentence.builder()
                        .tsSeq(tsSeq)
                        .text(updatedText)
                        .voice(voice)
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
        when(ttsSentenceRepository.findById(tsSeq)).thenReturn(Optional.of(originalSentence));
        when(ttsSentenceRepository.save(any(TtsSentence.class))).thenReturn(updatedSentence);

        // when: updateSentence 메서드 호출
        TtsSentenceDto updateResult =
                ttsSentenceService.updateSentence(projectSeq, tsSeq, updateRequest);

        // then: 수정된 값이 반환되고 저장 메서드가 호출되었는지 검증
        assertNotNull(updateResult);
        assertEquals(updateRequest.getText(), updateResult.getSentence().getText());
        assertEquals(updateRequest.getVoiceSeq(), updateResult.getSentence().getVoiceSeq());
        assertEquals(
                updateRequest.getAttribute().getVolume(),
                updateResult.getSentence().getTtsAttributeInfo().getVolume());
        assertEquals(
                updateRequest.getAttribute().getSpeed(),
                updateResult.getSentence().getTtsAttributeInfo().getSpeed());
        assertEquals(
                updateRequest.getAttribute().getStPitch(),
                updateResult.getSentence().getTtsAttributeInfo().getStPitch());
        assertEquals(
                updateRequest.getAttribute().getEmotion(),
                updateResult.getSentence().getTtsAttributeInfo().getEmotion());
        assertEquals(
                updateRequest.getAttribute().getEmotionStrength(),
                updateResult.getSentence().getTtsAttributeInfo().getEmotionStrength());
        assertEquals(
                updateRequest.getAttribute().getSampleRate(),
                updateResult.getSentence().getTtsAttributeInfo().getSampleRate());
        assertEquals(
                updateRequest.getAttribute().getAlpha(),
                updateResult.getSentence().getTtsAttributeInfo().getAlpha());
        assertEquals(
                updateRequest.getAttribute().getEndPitch(),
                updateResult.getSentence().getTtsAttributeInfo().getEndPitch());
        assertEquals(
                updateRequest.getAttribute().getAudioFormat(),
                updateResult.getSentence().getTtsAttributeInfo().getAudioFormat());
    }

    private TtsAttributeInfo createAttribute() {
        return TtsAttributeInfo.of(10, 1.0f, 0, "normal", 0, 16000, 0, 0.0f, "wav");
    }

    private TtsSentenceRequest createRequest(TtsAttributeInfo attribute) {
        return TtsSentenceRequest.builder()
                .voiceSeq(1L)
                .order(1)
                .text("안녕하세요")
                .attribute(attribute)
                .build();
    }

    private TtsSentenceRequest createRequest(
            Long voiceSeq, String text, Integer order, TtsAttributeInfo attribute) {
        return TtsSentenceRequest.builder()
                .voiceSeq(voiceSeq)
                .order(order)
                .text(text)
                .attribute(attribute)
                .build();
    }
}
