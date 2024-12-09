package com.oreo.finalproject_5re5_be.tts.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oreo.finalproject_5re5_be.global.component.S3Service;
//import com.oreo.finalproject_5re5_be.global.component.SqsService;
import com.oreo.finalproject_5re5_be.global.constant.MessageType;
import com.oreo.finalproject_5re5_be.global.exception.EntityNotFoundException;
import com.oreo.finalproject_5re5_be.tts.client.AudioConfigGenerator;
import com.oreo.finalproject_5re5_be.tts.client.GoogleTTSService;
import com.oreo.finalproject_5re5_be.tts.client.SynthesisInputGenerator;
import com.oreo.finalproject_5re5_be.tts.client.VoiceParamsGenerator;
import com.oreo.finalproject_5re5_be.tts.dto.external.TtsMakeRequest;
import com.oreo.finalproject_5re5_be.tts.dto.external.TtsMakeResponse;
import com.oreo.finalproject_5re5_be.tts.dto.response.TtsSentenceDto;
import com.oreo.finalproject_5re5_be.tts.entity.TtsProgressStatus;
import com.oreo.finalproject_5re5_be.tts.entity.TtsProgressStatusCode;
import com.oreo.finalproject_5re5_be.tts.entity.TtsSentence;
import com.oreo.finalproject_5re5_be.tts.entity.Voice;
import com.oreo.finalproject_5re5_be.tts.exception.TtsMakeException;
import com.oreo.finalproject_5re5_be.tts.repository.TtsProgressStatusRepository;
import com.oreo.finalproject_5re5_be.tts.repository.TtsSentenceRepository;
import com.oreo.finalproject_5re5_be.tts.repository.VoiceRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
//import software.amazon.awssdk.services.sqs.model.Message;

import java.util.concurrent.TimeoutException;

@Service
@Validated
public class TtsMakeService {

    private final TtsProgressStatusRepository ttsProgressStatusRepository;
    private final TtsSentenceRepository ttsSentenceRepository;
    private final VoiceRepository voiceRepository;
    private final GoogleTTSService googleTTSService;
    private final S3Service s3Service;
    private final SaveTtsMakeResultService saveTtsMakeResultService;
//    private final SqsService sqsService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public TtsMakeService(
            TtsSentenceRepository ttsSentenceRepository,
            GoogleTTSService googleTTSService,
            S3Service s3Service,
            SaveTtsMakeResultService saveTtsMakeResultService,
            VoiceRepository voiceRepository,
            TtsProgressStatusRepository ttsProgressStatusRepository
//            SqsService sqsService
    ) {
        this.ttsSentenceRepository = ttsSentenceRepository;
        this.googleTTSService = googleTTSService;
        this.s3Service = s3Service;
        this.saveTtsMakeResultService = saveTtsMakeResultService;
        this.voiceRepository = voiceRepository;
        this.ttsProgressStatusRepository = ttsProgressStatusRepository;
//        this.sqsService = sqsService;
    }

    // TTS 생성 서비스
    public TtsSentenceDto makeTts(@NotNull Long sentenceSeq) {
        // 0. sentenceSeq 로 행 정보 조회
        TtsSentence ttsSentence = ttsSentenceRepository.findById(sentenceSeq)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 TTS 행입니다. id:"+sentenceSeq));

        // TTS 문장 '진행중' 상태 저장
        saveTtsProgressStatus(ttsSentence, TtsProgressStatusCode.IN_PROGRESS);

        try {
            // 1. TTS 생성
            MultipartFile ttsFile = makeTtsAudioFile(ttsSentence);

            // 2. TTS 결과 파일 AWS S3에 업로드
            String uploadedUrl = s3Service.upload(ttsFile, "tts");

            // 3. TTS 결과 정보 저장
            TtsSentenceDto saveResult =  saveTtsMakeResultService.saveTtsMakeResult(ttsFile, uploadedUrl, ttsSentence);

            // 4. TTS 문장 '성공' 상태 저장
            saveTtsProgressStatus(ttsSentence, TtsProgressStatusCode.FINISHED);

            return saveResult;
        } catch (RuntimeException e) {
            saveTtsProgressStatus(ttsSentence, TtsProgressStatusCode.FAILED);
            throw e;
        }
    }

//    public TtsSentenceDto makeTtsMulti(@NotNull Long sentenceSeq) {
//
//        // 0. sentenceSeq 로 행 정보 조회
//        TtsSentence ttsSentence = ttsSentenceRepository.findById(sentenceSeq)
//                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 TTS 행입니다. id:" + sentenceSeq));
//        try {
//            // 1. TTS 문장 '진행중' 상태 저장
//            saveTtsProgressStatus(ttsSentence, TtsProgressStatusCode.IN_PROGRESS);
//
//            // 2. TTS 생성 및 s3 업로드 요청
//            Message message = sqsService.sendMessage(
//                    TtsMakeRequest.of(ttsSentence, makeFilename(ttsSentence)),
//                    MessageType.TTS_MAKE
//            );
//            TtsMakeResponse ttsMakeResponse = objectMapper.readValue(message.body(), TtsMakeResponse.class);
//
//            // 3. TTS 결과 저장
//            TtsSentenceDto ttsSentenceDto = saveTtsMakeResultService.saveTtsMakeResult(ttsMakeResponse, ttsSentence);
//
//            // 4. TTS 문장 '완료' 상태 저장
//            saveTtsProgressStatus(ttsSentence, TtsProgressStatusCode.FINISHED);
//
//            return ttsSentenceDto;
//
//        } catch (RuntimeException e) {
//            // 예외 발생 시 TTS 문장 '실패' 상태 저장
//            saveTtsProgressStatus(ttsSentence, TtsProgressStatusCode.FAILED);
//            throw new TtsMakeException("TTS 생성 중 예외 발생");
//        } catch (JsonProcessingException e) {
//            saveTtsProgressStatus(ttsSentence, TtsProgressStatusCode.FAILED);
//            throw new TtsMakeException("tts 생성 응답 메세지 body 값을 TtsMakeResponse 객체로 변환 중 에러 발생");
//        } catch (TimeoutException e) {
//            saveTtsProgressStatus(ttsSentence, TtsProgressStatusCode.FAILED);
//            throw new TtsMakeException("tts make sqs request timeout...");
//        }
//    }

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


    // TTS 파일 이름 생성 메서드
    private String makeFilename(TtsSentence ttsSentence) {
        return "project-" + ttsSentence.getProject().getProSeq() + "-tts-" + ttsSentence.getTsSeq();
    }

    // TTS 문장 상태 저장 메서드
    private TtsProgressStatus saveTtsProgressStatus(TtsSentence ttsSentence, TtsProgressStatusCode statusCode) {
        TtsProgressStatus ttsProgressStatus = TtsProgressStatus.builder()
                .ttsSentence(ttsSentence)
                .progressStatus(statusCode)
                .build();
        return ttsProgressStatusRepository.save(ttsProgressStatus);
    }
}
