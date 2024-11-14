package com.oreo.finalproject_5re5_be.tts.service;

import com.oreo.finalproject_5re5_be.audio.AudioDurationUtil;
import com.oreo.finalproject_5re5_be.audio.dto.response.AudioFileInfo;
import com.oreo.finalproject_5re5_be.audio.service.AudioInfo;
import com.oreo.finalproject_5re5_be.s3.S3Service;
import com.oreo.finalproject_5re5_be.tts.client.AudioConfigGenerator;
import com.oreo.finalproject_5re5_be.tts.client.GoogleTTSService;
import com.oreo.finalproject_5re5_be.tts.client.SynthesisInputGenerator;
import com.oreo.finalproject_5re5_be.tts.client.VoiceParamsGenerator;
import com.oreo.finalproject_5re5_be.tts.entity.TtsAudioFile;
import com.oreo.finalproject_5re5_be.tts.entity.TtsProcessHistory;
import com.oreo.finalproject_5re5_be.tts.entity.TtsSentence;
import com.oreo.finalproject_5re5_be.tts.entity.Voice;
import com.oreo.finalproject_5re5_be.tts.repository.TtsAudioFileRepository;
import com.oreo.finalproject_5re5_be.tts.repository.TtsProcessHistoryRepository;
import com.oreo.finalproject_5re5_be.tts.repository.TtsProgressStatusRepository;
import com.oreo.finalproject_5re5_be.tts.repository.TtsSentenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class TtsMakeService {
    @Autowired
    private TtsAudioFileRepository ttsAudioFileRepository;
    @Autowired
    private TtsProcessHistoryRepository ttsProcessHistoryRepository;
    @Autowired
    private TtsProgressStatusRepository ttsProgressStatusRepository;
    @Autowired
    private TtsSentenceRepository ttsSentenceRepository;
    @Autowired
    private GoogleTTSService googleTTSService;
    @Autowired
    private S3Service s3Service;
    @Autowired
    private AudioInfo audioInfo;

    // TTS 생성 서비스
    @Transactional
    public TtsSentence makeTts(Long sentenceSeq) throws Exception {
        TtsSentence ttsSentence = ttsSentenceRepository.findById(sentenceSeq)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 TTS 행입니다."));

        // 1. TTS 생성
        byte[] ttsResult = generateTtsAudio(ttsSentence);

        // 1-2. TTS 생성 결과(Byte[])를 MultipartFile 객체로 변환
        String ttsFileName = makeFilename(ttsSentence);
        MultipartFile ttsFile = convertToMultipartFile(ttsResult, ttsFileName + ".wav", "audio/wav");

        // 2. TTS 결과 파일 AWS S3에 업로드
        String uploadedUrl = s3Service.upload(ttsFile, "tts");

        // 3. 오디오 파일 메타데이터 DB 저장
        TtsAudioFile ttsAudioFileInfo = createTtsAudioFileInfo(uploadedUrl, ttsFile);
        TtsAudioFile savedAudioFile = ttsAudioFileRepository.save(ttsAudioFileInfo);

        // 4. TTS 문장 정보 업데이트
        TtsSentence updatedSentence = ttsSentence.toBuilder().ttsAudiofile(savedAudioFile).build();
        ttsSentenceRepository.save(updatedSentence);

        // 5. TTS 처리 내역 저장
        TtsProcessHistory processHistory = createProcessHistory(updatedSentence, savedAudioFile);
        ttsProcessHistoryRepository.save(processHistory);

        return updatedSentence;
    }

    // TTS 생성 메서드
    public byte[] generateTtsAudio(TtsSentence ttsSentence) throws Exception {
        Voice voice = ttsSentence.getVoice();
        return googleTTSService.make(
                SynthesisInputGenerator.generate(ttsSentence.getText()),
                VoiceParamsGenerator.generate(
                        voice.getLanguage().getLangCode(),
                        voice.getName(),
                        voice.getGender()),
                AudioConfigGenerator.generate(
                        ttsSentence.getSpeed(),
                        ttsSentence.getEndPitch(),
                        ttsSentence.getVolume())
        );
    }


    // TTS 오디오 파일 엔티티 생성 메서드
    public TtsAudioFile createTtsAudioFileInfo(String uploadedUrl, MultipartFile ttsFile) {
        AudioFileInfo audioFileInfo = audioInfo.extractAudioFileInfo(ttsFile);
        return TtsAudioFile.builder()
                .audioName(audioFileInfo.getName())
                .audioExtension(audioFileInfo.getExtension())
                .audioPath(uploadedUrl)
                .audioTime(AudioDurationUtil.getAudioDurationInMilliseconds(ttsFile))
                .audioSize(audioFileInfo.getSize())
                .audioPlayYn('y')
                .downloadYn('y')
                .downloadCount(0)
                .build();
    }

    // TTS 처리 내역 엔티티 생성 메서드
    public TtsProcessHistory createProcessHistory(TtsSentence sentence, TtsAudioFile audioFile) {
        return TtsProcessHistory.builder()
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
    }


    // TTS 파일 이름 생성 메서드
    private static String makeFilename(TtsSentence ttsSentence) {
        return "project-" + ttsSentence.getProject().getProSeq() + "-tts-" + ttsSentence.getTsSeq();
    }

    // byte[] -> MultipartFile 변환 메서드
    private MultipartFile convertToMultipartFile(byte[] bytes, String fileName, String contentType) {
        return new ByteArrayMultipartFile(
                bytes,
                fileName,
                contentType

        );
    }
}
