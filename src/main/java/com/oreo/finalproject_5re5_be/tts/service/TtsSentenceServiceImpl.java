package com.oreo.finalproject_5re5_be.tts.service;

import com.oreo.finalproject_5re5_be.project.entity.Project;
import com.oreo.finalproject_5re5_be.project.repository.ProjectRepository;
import com.oreo.finalproject_5re5_be.tts.dto.request.TtsAttributeInfo;
import com.oreo.finalproject_5re5_be.tts.dto.request.TtsSentenceCreateRequest;
import com.oreo.finalproject_5re5_be.tts.dto.response.TtsSentenceDto;
import com.oreo.finalproject_5re5_be.tts.entity.*;
import com.oreo.finalproject_5re5_be.tts.repository.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;


@Slf4j
@Service
@Validated
public class TtsSentenceServiceImpl implements TtsSentenceService {

    private final TtsSentenceRepository ttsSentenceRepository;
    private final ProjectRepository projectRepository;
    private final TtsAudioFileRepository ttsAudioFileRepository;
    private final VoiceRepository voiceRepository;
    private final StyleRepository styleRepository;
    private final TtsProgressStatusRepository ttsProgressStatusRepository;

    public TtsSentenceServiceImpl(TtsSentenceRepository ttsSentenceRepository, ProjectRepository projectRepository, TtsAudioFileRepository ttsAudioFileRepository, VoiceRepository voiceRepository, StyleRepository styleRepository, TtsProgressStatusRepository ttsProgressStatusRepository) {
        this.ttsSentenceRepository = ttsSentenceRepository;
        this.projectRepository = projectRepository;
        this.ttsAudioFileRepository = ttsAudioFileRepository;
        this.voiceRepository = voiceRepository;
        this.styleRepository = styleRepository;
        this.ttsProgressStatusRepository = ttsProgressStatusRepository;
    }

    @Override
    public TtsSentenceDto addSentence(@Valid @NotNull Long projectSeq, @Valid TtsSentenceCreateRequest createRequest) {
        // 1. TtsSentenceCreateRequest 유효성 검증 : Text (not blank) => @NotBlank

        // 2.1 projectSeq 유효성 검증 : not null => @NotNull
        // 2.2. projectSeq : 조회 가능한 projectSeq (존재 여부) 검증 및 할당
        Project project = projectRepository.findById(projectSeq)
                .orElseThrow(() -> new IllegalArgumentException("projectSeq is invalid"));

        // 3.1 TtsSentenceCreateRequest.voiceSeq 유효성 검증 : not null => @NotNull
        // 3.2 voiceSeq : 조회 가능한 voiceSeq (존재 여부) 검증 및 할당
        Voice voice = voiceRepository.findByVoiceSeq(createRequest.getVoiceSeq())
                .orElseThrow(() -> new IllegalArgumentException("voiceSeq is invalid"));

        // 3. TtsSentenceCreateRequest 유효성 검증 : StyleSeq
        // 3.1. styleSeq : not null 일 때 styleSeq 유효성 검증 및 할당
        Style style = null;
        if (createRequest.getStyleSeq() != null) {
            style = styleRepository.findById(createRequest.getStyleSeq())
                    .orElseThrow(() -> new IllegalArgumentException("styleSeq is invalid"));
        }

        // 4. TtsSentenceCreateRequest -> TtsSentence 변환
        TtsAttributeInfo attribute = createRequest.getAttribute();
        TtsSentence ttsSentence = TtsSentence.builder()
                .text(createRequest.getText())
                .sortOrder(createRequest.getOrder())
                .volume(attribute.getVolume())
                .speed(attribute.getSpeed())
                .startPitch(attribute.getStPitch())
                .emotion(attribute.getEmotion())
                .emotionStrength(attribute.getEmotionStrength())
                .sampleRate(attribute.getSampleRate())
                .alpha(attribute.getAlpha())
                .endPitch(attribute.getEndPitch())
                .audioFormat(attribute.getAudioFormat())
                .project(project)
                .voice(voice)
                .style(style)
                .build();

        // 5. TtsSentence 저장
        TtsSentence createdTtsSentence = ttsSentenceRepository.save(ttsSentence);
        log.info("[ttsSentenceEntity] save : {}", createdTtsSentence);

        // 6. TtsProgressStatus 저장
        TtsProgressStatus ttsProgressStatus = TtsProgressStatus.builder()
                .ttsSentence(createdTtsSentence)
                .progressStatus(TtsProgressStatusCode.CREATED)
                .build();
        TtsProgressStatus createdTtsProgress = ttsProgressStatusRepository.save(ttsProgressStatus);
        log.info("[ttsProgressStatus] save : {}", createdTtsProgress);

        // 6. TtsSentenceResponse 변환
        return TtsSentenceDto.of(createdTtsSentence);
    }
}
