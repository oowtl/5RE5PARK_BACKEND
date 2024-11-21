package com.oreo.finalproject_5re5_be.tts.service;

import com.oreo.finalproject_5re5_be.global.component.AudioInfo;
import com.oreo.finalproject_5re5_be.global.component.S3Service;
import com.oreo.finalproject_5re5_be.global.dto.response.AudioFileInfo;
import com.oreo.finalproject_5re5_be.global.exception.EntityNotFoundException;
import com.oreo.finalproject_5re5_be.tts.client.AudioConfigGenerator;
import com.oreo.finalproject_5re5_be.tts.client.GoogleTTSService;
import com.oreo.finalproject_5re5_be.tts.client.SynthesisInputGenerator;
import com.oreo.finalproject_5re5_be.tts.client.VoiceParamsGenerator;
import com.oreo.finalproject_5re5_be.tts.dto.response.TtsSentenceDto;
import com.oreo.finalproject_5re5_be.tts.entity.TtsAudioFile;
import com.oreo.finalproject_5re5_be.tts.entity.TtsProcessHistory;
import com.oreo.finalproject_5re5_be.tts.entity.TtsSentence;
import com.oreo.finalproject_5re5_be.tts.entity.Voice;
import com.oreo.finalproject_5re5_be.tts.repository.TtsAudioFileRepository;
import com.oreo.finalproject_5re5_be.tts.repository.TtsProcessHistoryRepository;
import com.oreo.finalproject_5re5_be.tts.repository.TtsSentenceRepository;
import com.oreo.finalproject_5re5_be.tts.repository.VoiceRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class TtsMakeService {

    private TtsAudioFileRepository ttsAudioFileRepository;
    private TtsProcessHistoryRepository ttsProcessHistoryRepository;
    private TtsSentenceRepository ttsSentenceRepository;
    private GoogleTTSService googleTTSService;
    private S3Service s3Service;
    private AudioInfo audioInfo;
    private VoiceRepository voiceRepository;

    public TtsMakeService(
            TtsAudioFileRepository ttsAudioFileRepository,
            TtsProcessHistoryRepository ttsProcessHistoryRepository,
            TtsSentenceRepository ttsSentenceRepository,
            GoogleTTSService googleTTSService,
            S3Service s3Service,
            AudioInfo audioInfo,
            VoiceRepository voiceRepository
    ) {
        this.ttsAudioFileRepository = ttsAudioFileRepository;
        this.ttsProcessHistoryRepository = ttsProcessHistoryRepository;
        this.ttsSentenceRepository = ttsSentenceRepository;
        this.googleTTSService = googleTTSService;
        this.s3Service = s3Service;
        this.audioInfo = audioInfo;
        this.voiceRepository = voiceRepository;
    }

    // TTS 생성 서비스
    @Transactional
    public TtsSentenceDto makeTts(@NotNull Long sentenceSeq) {
        // 0. sentenceSeq 로 행 정보 조회
        TtsSentence ttsSentence = ttsSentenceRepository.findById(sentenceSeq)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 TTS 행입니다. id:"+sentenceSeq));

        // 1. TTS 생성
        MultipartFile ttsFile = makeTtsAudioFile(ttsSentence);

        // 2. TTS 결과 파일 AWS S3에 업로드
        String uploadedUrl = s3Service.upload(ttsFile, "tts");

        // 3. 오디오 파일 메타데이터 DB 저장
        TtsAudioFile savedTtsAudioFile = saveTtsAudioFile(ttsFile, uploadedUrl);

        // 4. TTS 문장 정보 업데이트
        TtsSentence updatedSentence = updateTtsAudioFileOfSentence(ttsSentence, savedTtsAudioFile);

        // 5. TTS 처리 내역 저장
        saveTtsProcessHistory(ttsSentence, savedTtsAudioFile);

        return TtsSentenceDto.of(updatedSentence);
    }

    // TTS 생성
    private MultipartFile makeTtsAudioFile(@NotNull TtsSentence ttsSentence) {
        // 행 정보로부터 Voice 정보 얻기
        Voice voice = voiceRepository.findById(ttsSentence.getVoice().getVoiceSeq())
                .orElseThrow(() -> new EntityNotFoundException("voice 정보를 찾을 수 없습니다."));

        // 행 정보로부터 TTS 파일명 생성
        String ttsFileName = makeFilename(ttsSentence);

        // 행 정보와 voice 정보를 가지고 TTS 오디오 파일 생성
        MultipartFile ttsFile = googleTTSService.makeToMultipartFile(
                SynthesisInputGenerator.generate(ttsSentence.getText()), // text 입력 정보 세팅
                VoiceParamsGenerator.generate(                           // 보이스 입력 정보 세팅
                        voice.getLanguage().getLangCode(),
                        voice.getName(),
                        voice.getGender()),
                AudioConfigGenerator.generate(                          // 오디오 옵션 정보 세팅
                        ttsSentence.getSpeed(),
                        ttsSentence.getEndPitch(),
                        ttsSentence.getVolume())
                , ttsFileName                                           // 파일명 세팅
        );
        return ttsFile;
    }

    /// TTS 오디오 파일 메타데이터 저장
    private TtsAudioFile saveTtsAudioFile(@NotNull MultipartFile ttsFile, @NotNull String url) {

        if(ttsFile == null) {
            throw new IllegalArgumentException("TTS 파일이 없습니다.");
        }

        // TTS 오디오 파일로부터 메타 정보 추출
        AudioFileInfo audioFileInfo = audioInfo.extractAudioFileInfo(ttsFile);


        // 저장할 TTS 오디오 파일 엔티티 생성
        TtsAudioFile ttsAudioFile = TtsAudioFile.builder()
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

    // TTS 행 정보 중 오디오 파일 정보 업데이트
    private TtsSentence updateTtsAudioFileOfSentence(@NotNull TtsSentence ttsSentence, @NotNull TtsAudioFile ttsAudioFile) {

        // TTS 행 엔티티의 ttsAudioFile 정보 수정
        TtsSentence updatedSentence =
                ttsSentence.toBuilder()
                        .ttsAudiofile(ttsAudioFile)
                        .build();
        // 수정된 엔티티 저장
        return ttsSentenceRepository.save(updatedSentence);
    }

    // TTS 처리 내역 엔티티 저장
    private TtsProcessHistory saveTtsProcessHistory(TtsSentence sentence, TtsAudioFile audioFile) {

        // TTS 행 엔티티와 TtsAudioFile 엔티티 정보로 TTS 처리 내역 엔티티 생성
        TtsProcessHistory processHistory = TtsProcessHistory.builder()
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


    // TTS 파일 이름 생성 메서드
    private static String makeFilename(TtsSentence ttsSentence) {
        return "project-" + ttsSentence.getProject().getProSeq() + "-tts-" + ttsSentence.getTsSeq();
    }

}
