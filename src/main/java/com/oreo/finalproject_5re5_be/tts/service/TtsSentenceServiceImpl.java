package com.oreo.finalproject_5re5_be.tts.service;

import com.oreo.finalproject_5re5_be.global.constant.BatchProcessType;
import com.oreo.finalproject_5re5_be.global.exception.EntityNotFoundException;
import com.oreo.finalproject_5re5_be.project.entity.Project;
import com.oreo.finalproject_5re5_be.project.exception.ProjectNotFoundException;
import com.oreo.finalproject_5re5_be.project.repository.ProjectRepository;
import com.oreo.finalproject_5re5_be.project.service.ProjectService;
import com.oreo.finalproject_5re5_be.tts.dto.request.TtsAttributeInfo;
import com.oreo.finalproject_5re5_be.tts.dto.request.TtsSentenceBatchInfo;
import com.oreo.finalproject_5re5_be.tts.dto.request.TtsSentenceBatchRequest;
import com.oreo.finalproject_5re5_be.tts.dto.request.TtsSentenceRequest;
import com.oreo.finalproject_5re5_be.tts.dto.response.SentenceInfo;
import com.oreo.finalproject_5re5_be.tts.dto.response.TtsSentenceDto;
import com.oreo.finalproject_5re5_be.tts.dto.response.TtsSentenceListDto;
import com.oreo.finalproject_5re5_be.tts.entity.TtsProgressStatus;
import com.oreo.finalproject_5re5_be.tts.entity.TtsProgressStatusCode;
import com.oreo.finalproject_5re5_be.tts.entity.TtsSentence;
import com.oreo.finalproject_5re5_be.tts.entity.Voice;
import com.oreo.finalproject_5re5_be.tts.exception.InValidRequestException;
import com.oreo.finalproject_5re5_be.tts.exception.ProjectMismatchException;
import com.oreo.finalproject_5re5_be.tts.exception.TtsSentenceInValidInput;
import com.oreo.finalproject_5re5_be.tts.exception.TtsSentenceNotFound;
import com.oreo.finalproject_5re5_be.tts.exception.VoiceEntityNotFound;
import com.oreo.finalproject_5re5_be.tts.repository.TtsProgressStatusRepository;
import com.oreo.finalproject_5re5_be.tts.repository.TtsSentenceRepository;
import com.oreo.finalproject_5re5_be.tts.repository.VoiceRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Service
@Validated
public class TtsSentenceServiceImpl implements TtsSentenceService {

    private final TtsSentenceRepository ttsSentenceRepository;
    private final ProjectRepository projectRepository;
    private final VoiceRepository voiceRepository;
    private final TtsProgressStatusRepository ttsProgressStatusRepository;
    private final ProjectService projectService;

    public TtsSentenceServiceImpl(
            TtsSentenceRepository ttsSentenceRepository,
            ProjectRepository projectRepository,
            VoiceRepository voiceRepository,
            TtsProgressStatusRepository ttsProgressStatusRepository,
            ProjectService projectService) {
        this.ttsSentenceRepository = ttsSentenceRepository;
        this.projectRepository = projectRepository;
        this.voiceRepository = voiceRepository;
        this.ttsProgressStatusRepository = ttsProgressStatusRepository;
        this.projectService = projectService;
    }

    @Override
    @Transactional
    public TtsSentenceDto addSentence(
            @Valid @NotNull Long projectSeq, @Valid TtsSentenceRequest createRequest) {
        // 1. TtsSentenceRequest 유효성 검증 : Text (not blank) => @NotBlank

        // 2.1 projectSeq 유효성 검증 : not null => @NotNull
        // 2.2. projectSeq : 조회 가능한 projectSeq (존재 여부) 검증 및 할당
        Project project =
                projectRepository.findById(projectSeq).orElseThrow(ProjectNotFoundException::new);

        // 3.1 TtsSentenceRequest.voiceSeq 유효성 검증 : not null => @NotNull
        // 3.2 voiceSeq : 조회 가능한 voiceSeq (존재 여부) 검증 및 할당
        Voice voice =
                voiceRepository.findById(createRequest.getVoiceSeq()).orElseThrow(VoiceEntityNotFound::new);

        // 4. TtsSentenceRequest -> TtsSentence 변환
        TtsAttributeInfo attribute = createRequest.getAttribute();
        TtsSentence ttsSentence =
                TtsSentence.builder()
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
                        .build();

        // 5. TtsSentence 저장
        TtsSentence createdTtsSentence = ttsSentenceRepository.save(ttsSentence);
        log.info("[ttsSentenceEntity] save : {}", createdTtsSentence);

        // 6. TtsProgressStatus 저장
        TtsProgressStatus ttsProgressStatus =
                TtsProgressStatus.builder()
                        .ttsSentence(createdTtsSentence)
                        .progressStatus(TtsProgressStatusCode.CREATED)
                        .build();
        TtsProgressStatus createdTtsProgress = ttsProgressStatusRepository.save(ttsProgressStatus);
        log.info("[ttsProgressStatus] save : {}", createdTtsProgress);

        // 6. TtsSentenceResponse 변환
        return TtsSentenceDto.of(createdTtsSentence);
    }

    /**
     * @param projectSeq
     * @param tsSeq
     * @param updateRequest
     * @return
     * @apiNote TtsSentence 엔티티 수정
     */
    @Override
    public TtsSentenceDto updateSentence(
            @Valid @NotNull Long projectSeq,
            @Valid @NotNull Long tsSeq,
            @Valid TtsSentenceRequest updateRequest) {
        // 1. TtsSentenceRequest 유효성 검증
        if (updateRequest == null) {
            throw new InValidRequestException();
        }

        // 2. DB 유효성 검증
        // 2.1 projectSeq 조회 가능한 projectSeq (존재 여부) 검증 및 할당
        Project project =
                projectRepository.findById(projectSeq).orElseThrow(ProjectNotFoundException::new);

        // 2.2 voiceSeq 조회 가능한 voiceSeq (존재 여부) 검증 및 할당
        Voice voice =
                voiceRepository.findById(updateRequest.getVoiceSeq()).orElseThrow(VoiceEntityNotFound::new);

        // 3. TtsSentenceRequest -> TtsSentence 변환
        // 3.1 TtsSentence 엔티티 조회
        TtsSentence sentence =
                ttsSentenceRepository.findById(tsSeq).orElseThrow(TtsSentenceNotFound::new);

        // 3.2 TtsSentence 엔티티 수정
        TtsSentence updateSentence =
                sentence.toBuilder()
                        .text(updateRequest.getText())
                        .voice(voice)
                        .sortOrder(updateRequest.getOrder())
                        .volume(updateRequest.getAttribute().getVolume())
                        .speed(updateRequest.getAttribute().getSpeed())
                        .startPitch(updateRequest.getAttribute().getStPitch())
                        .emotion(updateRequest.getAttribute().getEmotion())
                        .emotionStrength(updateRequest.getAttribute().getEmotionStrength())
                        .sampleRate(updateRequest.getAttribute().getSampleRate())
                        .alpha(updateRequest.getAttribute().getAlpha())
                        .endPitch(updateRequest.getAttribute().getEndPitch())
                        .audioFormat(updateRequest.getAttribute().getAudioFormat())
                        .ttsAudiofile(null) // 3.3 TtsSentence 에 연관된 ttsAudioFile 연결 끊기
                        .build();

        // 4. TtsSentence 저장
        TtsSentence updatedSentence = ttsSentenceRepository.save(updateSentence);

        // 5. TtsSentenceResponse 변환
        return TtsSentenceDto.of(updatedSentence);
    }

    /**
     * @apiNote TtsSentence 엔티티 Batch 저장
     */
    @Override
    public TtsSentenceListDto batchSaveSentence(
            @Valid @NotNull Long projectSeq, @Valid TtsSentenceBatchRequest batchRequest) {
        // 10개의 TTSSentence 가 있다고 가정했을 때 5개의 TTSSentence 를 수정하고 전체 정렬을 했을 때, 순서를 보장하기 위해서는?

        // 1. DELETE 를 먼저 삭제한다. (삭제할 것들은 삭제 후에 정렬)
        batchRequest.getSentenceList().stream()
                .filter(sentence -> sentence.getBatchProcessType() == BatchProcessType.DELETE)
                .forEach(sentence -> deleteSentence(projectSeq, sentence.getSentence().getTsSeq()));

        List<TtsSentenceBatchInfo> alivedList =
                batchRequest.getSentenceList().stream()
                        .filter(sentence -> sentence.getBatchProcessType() != BatchProcessType.DELETE)
                        .toList();

        // 살아남은 ttsSentenceList
        TtsSentenceBatchRequest alivedRequest =
                TtsSentenceBatchRequest.builder().sentenceList(alivedList).build();

        // 2. TtsSentenceBatchRequest.sentenceList -> TtsSentenceDto List 변환
        // 3. 정렬 및 정렬 순서 수정
        //        List<TtsSentenceBatchInfo> batchList = alivedRequest.sortSentenceList();

        // 4. 하나씩 처리
        for (TtsSentenceBatchInfo batchInfo : alivedRequest.getSentenceList()) {
            toSentenceDto(projectSeq, batchInfo);
        }

        return getSentenceList(projectSeq);
    }

    // batchInfo -> sentenceDto 변환
    private TtsSentenceDto toSentenceDto(Long projectSeq, TtsSentenceBatchInfo batchInfo) {
        SentenceInfo sentenceInfo = batchInfo.getSentence();

        // 1. TtsSentenceRequest 유효성 검증
        if (sentenceInfo == null) {
            throw new TtsSentenceInValidInput("SentenceInfo is null");
        }

        // 2. sentenceInfo -> TtsSentenceRequest 변환
        TtsSentenceRequest sentenceRequest =
                TtsSentenceRequest.builder()
                        .text(sentenceInfo.getText())
                        .voiceSeq(sentenceInfo.getVoiceSeq())
                        .order(sentenceInfo.getOrder())
                        .attribute(sentenceInfo.getTtsAttributeInfo())
                        .build();

        // 3. BatchProcessType 에 따른 분기처리
        // 3.1 CREATE : addSentence
        if (batchInfo.getBatchProcessType() == BatchProcessType.CREATE) {
            return addSentence(projectSeq, sentenceRequest);
        }

        // 3.2 UPDATE : updateSentence
        if (batchInfo.getBatchProcessType() == BatchProcessType.UPDATE) {
            return updateSentence(projectSeq, sentenceInfo.getTsSeq(), sentenceRequest);
        }

        // 3.3 DELETE : updateSentence
        if (batchInfo.getBatchProcessType() == BatchProcessType.DELETE) {
            boolean deleteResult = deleteSentence(projectSeq, sentenceInfo.getTsSeq());

            // deleteResult == false, 삭제 안됨
            if (deleteResult) {
                return null;
            }
        }

        // 4 해당하는 BatchProcessType 가 없으면 예외 발생
        throw new TtsSentenceInValidInput("BatchProcessType is invalid");
    }

    @Override
    public TtsSentenceDto getSentence(Long projectSeq, Long tsSeq) {

        // 1. TtsSentence 엔티티 조회
        TtsSentence ttsSentence =
                ttsSentenceRepository
                        .findById(tsSeq)
                        .orElseThrow(
                                () -> new EntityNotFoundException("TtsSentence not found with id: " + tsSeq));

        return TtsSentenceDto.of(ttsSentence);
    }

    @Override
    public TtsSentenceListDto getSentenceList(Long projectSeq) {

        // 1. Project 엔티티 조회
        Project project =
                projectRepository
                        .findById(projectSeq)
                        .orElseThrow(
                                () -> new EntityNotFoundException("Project not found with id: " + projectSeq));

        // 2. Project 에 연관된 TtsSentence 리스트 조회
        List<TtsSentence> ttsSentenceList =
                ttsSentenceRepository.findAllByProjectOrderBySortOrder(project);

        // 3. TtsSentenceDto 리스트 변환 및 반환
        return TtsSentenceListDto.of(ttsSentenceList);
    }

    @Override
    @Transactional
    public boolean deleteSentence(Long projectSeq, Long tsSeq) {
        // 1. TtsSentence 엔티티 조회
        TtsSentence ttsSentence =
                ttsSentenceRepository.findById(tsSeq).orElseThrow(EntityNotFoundException::new);

        // 1.1 ttsSentence 에 연관된 ttsProgressStatusList 조회
        List<TtsProgressStatus> ttsProgressStatusList =
                ttsProgressStatusRepository.findAllByTtsSentence(ttsSentence);

        // 2. 삭제
        // 2.1 TtsProgressStatus 삭제
        ttsProgressStatusList.forEach(ttsProgressStatusRepository::delete);

        // 2.2 TtsSentence 삭제
        ttsSentenceRepository.delete(ttsSentence);

        // 3. 결과 반환
        return true;
    }

    @Override
    public TtsSentenceDto patchSentenceOrder(Long projectSeq, Long tsSeq, Integer order) {
        // 1. TtsSentence 엔티티 조회
        TtsSentence ttsSentence =
                ttsSentenceRepository.findById(tsSeq).orElseThrow(EntityNotFoundException::new);

        // 2. TtsSentence 엔티티 수정
        TtsSentence updatedTtsSentence = ttsSentence.toBuilder().sortOrder(order).build();

        // 3. TtsSentence 저장
        TtsSentence savedTtsSentence = ttsSentenceRepository.save(updatedTtsSentence);

        return TtsSentenceDto.of(savedTtsSentence);
    }

    @Override
    public boolean checkSentenceWithMember(Long memberSeq, Long projectSeq, Long tsSeq) {
        // 1. tsSeq 로 TtsSentence 조회
        TtsSentence ttsSentence =
                ttsSentenceRepository.findById(tsSeq).orElseThrow(EntityNotFoundException::new);

        // 2. 조회한 TtsSentence 의 projectSeq 으로 멤버가 소유한 프로젝트인지 확인
        Project sentenceProject = ttsSentence.getProject();
        Long sentenceProjectProSeq = sentenceProject.getProSeq();

        // 3. 검증
        // 3.1 projectSeq 와 sentenceProjectProSeq 가 일치하지 않으면 예외 발생
        if (!sentenceProjectProSeq.equals(projectSeq)) {
            throw new ProjectMismatchException();
        }

        // 4. member 가 소유한 프로젝트인지 확인
        return projectService.projectCheck(memberSeq, projectSeq);
    }

    @Override
    public boolean checkSentenceWithMember(
            @Valid @NotNull Long memberSeq,
            @Valid @NotNull Long projectSeq,
            @Valid List<TtsSentenceBatchInfo> sentenceList) {

        // 순회하여 처리
        for (TtsSentenceBatchInfo batchInfo : sentenceList) {
            // 1. sentenceList 에서 create 제외
            if (batchInfo.getBatchProcessType() == BatchProcessType.CREATE) {
                continue;
            }

            // 1.1. checkSentenceWithMember 호출
            // checkSentenceWithMember 에서 예외 발생시킴
            boolean checkedSentenceWithMember =
                    checkSentenceWithMember(memberSeq, projectSeq, batchInfo.getSentence().getTsSeq());

            // 1.2. 멤버가 소유하지 않은 프로젝트의 ttsSentence 가 있으면 false 반환
            if (!checkedSentenceWithMember) {
                return false;
            }
        }

        // true 반환
        return true;
    }
}
