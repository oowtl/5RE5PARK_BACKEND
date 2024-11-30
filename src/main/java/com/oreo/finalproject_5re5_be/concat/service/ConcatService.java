package com.oreo.finalproject_5re5_be.concat.service;

import com.oreo.finalproject_5re5_be.concat.dto.ConcatResultDto;
import com.oreo.finalproject_5re5_be.concat.dto.request.ConcatRowRequest;
import com.oreo.finalproject_5re5_be.concat.dto.request.ConcatRowRequestDto;
import com.oreo.finalproject_5re5_be.concat.dto.response.ConcatTabResponseDto;
import com.oreo.finalproject_5re5_be.concat.entity.AudioFile;
import com.oreo.finalproject_5re5_be.concat.entity.ConcatResult;
import com.oreo.finalproject_5re5_be.concat.entity.ConcatTab;
import com.oreo.finalproject_5re5_be.concat.entity.MaterialAudio;
import com.oreo.finalproject_5re5_be.concat.service.concatenator.AudioProperties;
import com.oreo.finalproject_5re5_be.concat.service.concatenator.IntervalConcatenator;
import com.oreo.finalproject_5re5_be.concat.service.concatenator.StereoIntervalConcatenator;
import com.oreo.finalproject_5re5_be.global.component.S3Service;
import com.oreo.finalproject_5re5_be.global.component.audio.AudioExtensionConverter;
import com.oreo.finalproject_5re5_be.global.component.audio.AudioFormats;
import com.oreo.finalproject_5re5_be.global.component.audio.AudioResample;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.sampled.AudioInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ConcatService {
    private final IntervalConcatenator concatenator
            = new StereoIntervalConcatenator(AudioFormats.STEREO_FORMAT_SR441_B16);
    private final AudioResample audioResample = new AudioResample(AudioFormats.STEREO_FORMAT_SR441_B16);
    private final S3Service s3Service;
    private final MaterialAudioService materialAudioService;
    private final ConcatResultService concatResultService;

    public ConcatResultDto concat(ConcatTabResponseDto concatTabResponseDto, ConcatRowRequestDto concatRowRequests)
            throws IOException {

        Result concatResult = getResult(concatTabResponseDto, concatRowRequests);
        if (concatResult == null) {
            throw new IllegalArgumentException("허용되지 않은 접근입니다.");
        }

        //재료 오디오 저장
        materialAudioService.saveMaterialAudio(prepareMaterialAudio(concatResult.audios(), concatResult.result()));
        return ConcatResultDto.builder()
                .concatResultSequence(concatResult.result().getConcatResultSequence())
                .extension(concatResult.result().getExtension())
                .fileName(concatResult.result().getFileName())
                .audioUrl(concatResult.result().getAudioUrl())
                .fileSize(concatResult.result().getFileSize())
                .fileLength(concatResult.result().getFileLength())
                .build();

    }

    //책임 : s3 저장, 결과 저장
    private Result getResult(ConcatTabResponseDto concatTabResponseDto, ConcatRowRequestDto concatRowRequests)
            throws IOException {
        List<ConcatRowRequest> audios = concatRowRequests.getConcatRowRequests();
        if (checkNull(audios)) {
            return null;
        }

        ConcatResult result = getConcatResult(concatTabResponseDto, concatRowRequests, audios);
        return new Result(audios, result);
    }

    private ConcatResult getConcatResult(ConcatTabResponseDto concatTabResponseDto, ConcatRowRequestDto concatRowRequests, List<ConcatRowRequest> audios) throws IOException {
        AudioInputStream concat = resampleAudio(concatTabResponseDto, audios);

        byte[] audioData = AudioExtensionConverter.mp3ToWav(concat); // AudioInputStream을 byte[]로 변환

        MultipartFile multipartFile
                = new AudioMultipartFile(audioData, concatRowRequests.getFileName(), "audio/wav");

        String uploadUrl = getUploadtoS3(multipartFile);
        log.info("[Uploaded file URL : {}] ", uploadUrl);

        //결과 저장
        ConcatResult result = ConcatResult.builder().concatTab(ConcatTab.builder()
                        .projectId(concatTabResponseDto.getTabId()).build())
                .option(null)
                .audioUrl(uploadUrl)
                .extension("WAV")
                .fileSize((long) audioData.length)
                .fileLength(concat.getFrameLength() / concat.getFormat().getFrameRate())
                .fileName(concatRowRequests.getFileName())
                .build();
        return concatResultService.saveConcatResult(result);
    }

    private boolean checkNull(List<ConcatRowRequest> audios) {
        for (ConcatRowRequest concatRowRequest : audios) {
            if (concatRowRequest.getSeq() == 0) {
                return true;
            }
        }
        return false;
    }

    private String getUploadtoS3(MultipartFile multipartFile) throws IOException {
        return s3Service.upload(
                multipartFile.getInputStream(),
                "concat/result",
                multipartFile.getOriginalFilename(),
                multipartFile.getSize(),
                multipartFile.getContentType()
        );
    }

    private AudioInputStream resampleAudio(ConcatTabResponseDto concatTabResponseDto, List<ConcatRowRequest> audios) throws IOException {
        List<AudioProperties> audioProperties = audios.stream().map(cr -> new AudioProperties(
                resample(S3Service.load(cr.getOriginAudioRequest().getAudioUrl()))
                , cr.getRowSilence()
        )).toList();//오디오 받아오기

        ByteArrayOutputStream concatResult = concatenator
                .intervalConcatenate(audioProperties, concatTabResponseDto.getFrontSilence());//결과 생성

        //리샘플
        return audioResample.resample(concatResult);
    }

    public List<MaterialAudio> prepareMaterialAudio(List<ConcatRowRequest> audios, ConcatResult concatResult) {

        return audios.stream().map(aud -> MaterialAudio.builder()
                .concatResult(concatResult)
                .method("Normal")
                .audioFile(AudioFile.builder().audioFileSeq(aud.getSeq()).build())
                .build()).toList();

    }

    public AudioInputStream resample(AudioInputStream audioInputStream) {
        return audioResample.resample(audioInputStream);
    }

    private record Result(List<ConcatRowRequest> audios, ConcatResult result) {
    }

}



















