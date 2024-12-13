package com.oreo.finalproject_5re5_be.concat.controller;

import com.oreo.finalproject_5re5_be.concat.dto.ConcatResponseDto;
import com.oreo.finalproject_5re5_be.concat.dto.request.*;
import com.oreo.finalproject_5re5_be.concat.dto.response.ConcatUrlResponse;
import com.oreo.finalproject_5re5_be.concat.entity.AudioFile;
import com.oreo.finalproject_5re5_be.concat.service.*;
import com.oreo.finalproject_5re5_be.concat.service.bgm.BgmProcessor;
import com.oreo.finalproject_5re5_be.concat.service.concatenator.AudioProperties;
import com.oreo.finalproject_5re5_be.concat.service.concatenator.IntervalConcatenator;
import com.oreo.finalproject_5re5_be.concat.service.concatenator.StereoIntervalConcatenator;
import com.oreo.finalproject_5re5_be.global.component.S3Service;
import com.oreo.finalproject_5re5_be.global.component.audio.AudioFormats;
import com.oreo.finalproject_5re5_be.global.component.audio.AudioResample;
import com.oreo.finalproject_5re5_be.global.dto.response.ResponseDto;
import com.oreo.finalproject_5re5_be.member.dto.CustomUserDetails;
import com.oreo.finalproject_5re5_be.project.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Concat", description = "Concat 관련 API")
@RestController
@Log4j2
@RequestMapping("/api/concat")
@RequiredArgsConstructor
public class ConcatWithBgmController {

    private final S3Service s3Service;
    private final MaterialAudioService materialAudioService;
    private final ConcatResultService concatResultService;
    private final AudioFileService audioFileService;
    private final AudioStreamService audioStreamService; // 추가된 서비스
    private final AudioResample audioResample = new AudioResample(); // 리샘플링 유틸. Bean이 아니라 new로 생성
    private final AudioFormat defaultAudioFormat = AudioFormats.STEREO_FORMAT_SR441_B16; // 기본 포맷
    private final ProjectService projectService;
    private final BgmFileService bgmFileService;

    @Operation(
            summary = "Row 오디오와 BGM 파일 병합",
            description = "선택된 Row 오디오 파일과 BGM 파일을 병합하여 S3에 업로드합니다.",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "성공적으로 병합된 오디오 URL을 반환합니다.",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = ConcatResponseDto.class))),
                @ApiResponse(
                        responseCode = "500",
                        description = "병합 작업 중 오류 발생",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = ResponseDto.class)))
            })
    @PostMapping("/execute-with-bgm")
    public ResponseEntity<ResponseDto<ConcatResponseDto>> executeConcatWithBgm(
            @Parameter(description = "저장할 결과파일 이름", required = true) @RequestParam
                    String concatResultFileName,
            @RequestBody TabRowUpdateRequestDto requestDto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        try {

            // Extracting data from the new DTO
            ConcatUpdateRequestDto concatTab = requestDto.getConcatTab();
            ConcatRowSaveRequestDto concatRows = requestDto.getConcatRows();

            Long concatTabSeq = concatTab.getTabId();
            float frontSilence = concatTab.getFrontSilence();
            String bgmFileUrl = concatTab.getBgmFileList().get(0).getAudioUrl();

            log.info("concatTab:{}", concatTabSeq);

            projectService.projectCheck(
                    customUserDetails.getMember().getSeq(), requestDto.getConcatTab().getTabId());

            IntervalConcatenator intervalConcatenator =
                    new StereoIntervalConcatenator(defaultAudioFormat);

            List<AudioProperties> audioProperties = audioStreamService.loadAudioFiles(concatRows);

            // 2. 병합된 오디오 생성
            ByteArrayOutputStream concatenatedAudioBuffer =
                    intervalConcatenator.intervalConcatenate(audioProperties, frontSilence);

            AudioInputStream concatenatedAudioStream =
                    audioStreamService.createAudioInputStream(concatenatedAudioBuffer, defaultAudioFormat);

            // BGM 작업 1: BGM 스트림 로드 및 버퍼링
            AudioInputStream bufferedBgmStream = audioStreamService.loadAsBufferedStream(bgmFileUrl);

            // 3. BGM 길이 조정
            long targetFrames = audioStreamService.getValidFrameLength(concatenatedAudioStream);
            long bgmFrames = audioStreamService.getValidFrameLength(bufferedBgmStream);

            // 로그 추가: 프레임 길이 확인

            bufferedBgmStream = BgmProcessor.adjustBgmLength(bufferedBgmStream, targetFrames, bgmFrames);

            // 4. 믹싱
            AudioInputStream mixedAudioStream =
                    BgmProcessor.mixAudio(concatenatedAudioStream, bufferedBgmStream);

            // 결과파일 S3 업로드
            String resultAudioUrl =
                    s3Service.uploadAudioStream(mixedAudioStream, "concat/result", concatResultFileName);

            // DB 저장1. ConcatResult DB
            ConcatUrlResponse concatResultResponse =
                    concatResultService.saveConcatResult(
                            concatTabSeq, resultAudioUrl, concatResultFileName, mixedAudioStream);

            OriginAudioRequest bgmRequest = concatTab.getBgmFileList().get(0);

            // BGM 데이터와 ConcatResult 매칭 업데이트
            try {
                bgmFileService.updateBgmFileWithConcatResult(bgmFileUrl, concatResultResponse.getSeq());
            } catch (IllegalArgumentException e) {
                log.warn("[BGM] Failed to update BgmFile: {}", e.getMessage());
            }

            // DB 저장2. Material 데이터 저장 (재료 파일, 결과파일 저장되어 있는 상태로 교차테이블에 데이터 저장)
            materialAudioService.saveMaterialsForConcatRows(concatRows, concatResultResponse);

            // 응답에 들어갈 concatRowFiles 생성
            List<OriginAudioRequest> concatRowFiles =
                    concatRows.getConcatRowRequests().stream()
                            .map(
                                    row -> {
                                        AudioFile audioFile =
                                                audioFileService.getAudioFileByUrl(row.getOriginAudioRequest().getSeq());
                                        return audioFile;
                                    })
                            .map(this::convertToOriginAudioRequest)
                            .peek(
                                    originAudioRequest ->
                                            log.info("Converted to OriginAudioRequest: {}", originAudioRequest))
                            .toList();

            // 응답 생성
            ConcatResponseDto responseDto =
                    ConcatResponseDto.builder()
                            .audioUrl(resultAudioUrl)
                            .bgmFile(bgmRequest)
                            .concatRowFiles(concatRowFiles)
                            .build();

            return new ResponseDto<>(HttpStatus.OK.value(), responseDto).toResponseEntity();
        } catch (Exception e) {
            e.printStackTrace();
            return createErrorResponse();
        }
    }

    private OriginAudioRequest convertToOriginAudioRequest(AudioFile audioFile) {
        return OriginAudioRequest.builder()
                .seq(audioFile.getAudioFileSeq())
                .audioUrl(audioFile.getAudioUrl())
                .extension(audioFile.getExtension())
                .fileSize(audioFile.getFileSize())
                .fileLength(audioFile.getFileLength())
                .fileName(audioFile.getFileName())
                .build();
    }

    private ResponseEntity<ResponseDto<ConcatResponseDto>> createErrorResponse() {
        return new ResponseDto<>(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        ConcatResponseDto.builder()
                                .audioUrl(null) // 결과 파일 URL 없음
                                .bgmFile(null) // BGM 파일 정보 없음
                                .concatRowFiles(new ArrayList<>()) // ConcatRow 파일 정보 없음
                                .build())
                .toResponseEntity();
    }
}
