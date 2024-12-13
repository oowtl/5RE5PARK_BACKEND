package com.oreo.finalproject_5re5_be.tts.service;

import com.oreo.finalproject_5re5_be.global.component.AudioInfo;
import com.oreo.finalproject_5re5_be.global.component.S3Service;
import com.oreo.finalproject_5re5_be.global.dto.response.AudioFileInfo;
import com.oreo.finalproject_5re5_be.tts.dto.external.TtsMakeResponse;
import com.oreo.finalproject_5re5_be.tts.dto.response.TtsSentenceDto;
import com.oreo.finalproject_5re5_be.tts.entity.TtsAudioFile;
import com.oreo.finalproject_5re5_be.tts.entity.TtsProcessHistory;
import com.oreo.finalproject_5re5_be.tts.entity.TtsSentence;
import com.oreo.finalproject_5re5_be.tts.exception.SaveTtsMakeResultException;
import com.oreo.finalproject_5re5_be.tts.repository.TtsAudioFileRepository;
import com.oreo.finalproject_5re5_be.tts.repository.TtsProcessHistoryRepository;
import com.oreo.finalproject_5re5_be.tts.repository.TtsSentenceRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

@Service
@Validated
public class SaveTtsMakeResultService {
    private final TtsSentenceRepository ttsSentenceRepository;
    private final TtsAudioFileRepository ttsAudioFileRepository;
    private final TtsProcessHistoryRepository ttsProcessHistoryRepository;
    private final AudioInfo audioInfo;
    private final S3Service s3Service;

    public SaveTtsMakeResultService(
            TtsSentenceRepository ttsSentenceRepository,
            TtsAudioFileRepository ttsAudioFileRepository,
            TtsProcessHistoryRepository ttsProcessHistoryRepository,
            AudioInfo audioInfo,
            S3Service s3Service) {
        this.ttsSentenceRepository = ttsSentenceRepository;
        this.ttsAudioFileRepository = ttsAudioFileRepository;
        this.ttsProcessHistoryRepository = ttsProcessHistoryRepository;
        this.audioInfo = audioInfo;
        this.s3Service = s3Service;
    }

    // tts 생성 결과 저장 메서드 (일시적 문제가 발생할 경우 최대 3회 재시도)
    @Retryable(
            retryFor = {RuntimeException.class},
            maxAttempts = 3, // 최대 재시도 횟수
            backoff =
                    @Backoff( // 재시도 간격
                            delay = 1000,
                            maxDelay = 3000,
                            multiplier = 2.0 // 시도 후 지연 시간 두배
                            ))
    @Transactional(rollbackFor = RuntimeException.class)
    public TtsSentenceDto saveTtsMakeResult(
            @NotNull MultipartFile ttsFile,
            @NotNull String uploadedUrl,
            @NotNull TtsSentence ttsSentence) {
        // 1. 오디오 파일 메타데이터 DB 저장
        TtsAudioFile savedTtsAudioFile = saveTtsAudioFile(ttsFile, uploadedUrl);

        // 2. TTS 문장 정보 업데이트
        // TTS 행 엔티티의 ttsAudioFile 정보 수정 후 저장
        TtsSentence updatedSentence = ttsSentence.toBuilder().ttsAudiofile(savedTtsAudioFile).build();
        TtsSentence savedSentence = ttsSentenceRepository.save(updatedSentence);

        // 3. TTS 처리 내역 저장
        saveTtsProcessHistory(savedSentence, savedTtsAudioFile);

        // 4. 업데이트 된 문장 정보 반환
        return TtsSentenceDto.of(updatedSentence);
    }

    @Retryable(
            retryFor = {RuntimeException.class},
            maxAttempts = 3, // 최대 재시도 횟수
            backoff =
                    @Backoff( // 재시도 간격
                            delay = 1000,
                            maxDelay = 3000,
                            multiplier = 2.0 // 시도 후 지연 시간 두배
                            ))
    @Transactional(rollbackFor = RuntimeException.class)
    public TtsSentenceDto saveTtsMakeResult(
            @NotNull TtsMakeResponse ttsMakeResponse, @NotNull TtsSentence ttsSentence) {
        // 1. 오디오 파일 메타데이터 DB 저장
        TtsAudioFile savedTtsAudioFile = saveTtsAudioFile(ttsMakeResponse);

        // 2. TTS 문장 정보 업데이트
        // TTS 행 엔티티의 ttsAudioFile 정보 수정 후 저장
        TtsSentence updatedSentence = ttsSentence.toBuilder().ttsAudiofile(savedTtsAudioFile).build();
        TtsSentence savedSentence = ttsSentenceRepository.save(updatedSentence);

        // 3. TTS 처리 내역 저장
        saveTtsProcessHistory(savedSentence, savedTtsAudioFile);

        // 4. 업데이트 된 문장 정보 반환
        return TtsSentenceDto.of(updatedSentence);
    }

    // tts 생성 결과 저장 실패할 경우 처리
    @Recover
    public TtsSentenceDto recoverSaveTtsMakeResult(
            RuntimeException e, MultipartFile ttsFile, String uploadUrl, TtsSentence ttsSentence) {
        // 1. 업로드 된 s3 파일 삭제
        if (!uploadUrl.isEmpty() && !uploadUrl.isBlank()) {
            s3Service.deleteFile("tts", uploadUrl.split("/")[3]);
        }

        // 2. 예외 발생 시키기
        throw new SaveTtsMakeResultException("TTS 생성 결과 저장 실패, 다시 시도 해 주세요.");
    }

    /// TTS 오디오 파일 메타데이터 저장
    private TtsAudioFile saveTtsAudioFile(@NotNull MultipartFile ttsFile, @NotNull String url) {

        if (ttsFile == null) {
            throw new IllegalArgumentException("TTS 파일이 없습니다.");
        }

        // TTS 오디오 파일로부터 메타 정보 추출
        AudioFileInfo audioFileInfo = audioInfo.extractAudioFileInfo(ttsFile);

        // 저장할 TTS 오디오 파일 엔티티 생성
        TtsAudioFile ttsAudioFile =
                TtsAudioFile.builder()
                        .audioName(audioFileInfo.getName())
                        .audioExtension(audioFileInfo.getExtension())
                        .audioPath(url)
                        .audioTime(audioFileInfo.getLength())
                        .audioSize(audioFileInfo.getSize())
                        .audioPlayYn('y')
                        .downloadYn('y')
                        .downloadCount(0)
                        .build();

        // TTS 오디오 파일 엔티티 저장
        return ttsAudioFileRepository.save(ttsAudioFile);
    }

    private TtsAudioFile saveTtsAudioFile(TtsMakeResponse ttsMakeResponse) {
        // 저장할 TTS 오디오 파일 엔티티 생성
        TtsAudioFile ttsAudioFile =
                TtsAudioFile.builder()
                        .audioName(ttsMakeResponse.getFileName())
                        .audioExtension(ttsMakeResponse.getFileExtension())
                        .audioPath(ttsMakeResponse.getUrl())
                        .audioTime(ttsMakeResponse.getFileLength())
                        .audioSize(ttsMakeResponse.getFileSize())
                        .audioPlayYn('y')
                        .downloadYn('y')
                        .downloadCount(0)
                        .build();

        // TTS 오디오 파일 엔티티 저장
        return ttsAudioFileRepository.save(ttsAudioFile);
    }

    // TTS 처리 내역 엔티티 저장
    private TtsProcessHistory saveTtsProcessHistory(TtsSentence sentence, TtsAudioFile audioFile) {

        // TTS 행 엔티티와 TtsAudioFile 엔티티 정보로 TTS 처리 내역 엔티티 생성
        TtsProcessHistory processHistory =
                TtsProcessHistory.builder()
                        .text(sentence.getText())
                        .voice(sentence.getVoice())
                        .volume(sentence.getVolume())
                        .speed(sentence.getSpeed())
                        .sampleRate(sentence.getSampleRate())
                        .startPitch(sentence.getStartPitch())
                        .alpha(sentence.getAlpha())
                        .endPitch(sentence.getEndPitch())
                        .emotionStrength(sentence.getEmotionStrength())
                        .audioFormat(sentence.getAudioFormat())
                        .ttsAudiofile(audioFile)
                        .project(sentence.getProject())
                        .build();

        // TTS 처리 내역 엔티티 저장
        return ttsProcessHistoryRepository.save(processHistory);
    }
}
