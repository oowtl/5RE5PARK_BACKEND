package com.oreo.finalproject_5re5_be.concat.controller;

import com.oreo.finalproject_5re5_be.concat.dto.ConcatResponseDto;
import com.oreo.finalproject_5re5_be.concat.dto.RowInfoDto;
import com.oreo.finalproject_5re5_be.concat.dto.request.ConcatResultRequest;
import com.oreo.finalproject_5re5_be.concat.dto.request.SelectedConcatRowRequest;
import com.oreo.finalproject_5re5_be.concat.dto.response.ConcatUrlResponse;
import com.oreo.finalproject_5re5_be.concat.service.AudioFileService;
import com.oreo.finalproject_5re5_be.concat.service.ConcatResultService;
import com.oreo.finalproject_5re5_be.concat.service.MaterialAudioService;
import com.oreo.finalproject_5re5_be.concat.service.bgm.BgmProcessor;
import com.oreo.finalproject_5re5_be.concat.service.concatenator.AudioProperties;
import com.oreo.finalproject_5re5_be.concat.service.concatenator.IntervalConcatenator;
import com.oreo.finalproject_5re5_be.concat.service.concatenator.StereoIntervalConcatenator;
import com.oreo.finalproject_5re5_be.global.component.ByteArrayMultipartFile;
import com.oreo.finalproject_5re5_be.global.component.S3Service;
import com.oreo.finalproject_5re5_be.global.component.audio.AudioFormats;
import com.oreo.finalproject_5re5_be.global.component.audio.AudioResample;
import com.oreo.finalproject_5re5_be.global.dto.response.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Tag(name = "Concat", description = "Concat 관련 API")
@RestController
@RequestMapping("/api/concat")
@RequiredArgsConstructor
public class ConcatWithBgmController {

    private final S3Service s3Service;
    private final MaterialAudioService materialAudioService;
    private final ConcatResultService concatResultService;
    private final AudioFileService audioFileService;
    private final AudioResample audioResample = new AudioResample(); // 리샘플링 유틸. Bean이 아니라 new로 생성
    private final AudioFormat defaultAudioFormat = AudioFormats.STEREO_FORMAT_SR441_B32; // 기본 포맷

    @Operation(
            summary = "Row 오디오와 BGM 파일 병합",
            description = "선택된 Row 오디오 파일과 BGM 파일을 병합하여 S3에 업로드합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "성공적으로 병합된 오디오 URL을 반환합니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ConcatResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "병합 작업 중 오류 발생",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseDto.class)
                            )
                    )
            }
    )
    @PostMapping("/execute-with-bgm")
    public ResponseEntity<ResponseDto<ConcatResponseDto>> executeConcatWithBgm(
            @RequestBody SelectedConcatRowRequest selectedRows,
            @Parameter(description = "S3에 저장된 BGM 파일의 URL", required = true) @RequestParam String bgmFileUrl,
            @RequestParam Long concatTabSeq) {
        try {
            // IntervalConcatenator 생성
            IntervalConcatenator intervalConcatenator = new StereoIntervalConcatenator(defaultAudioFormat);

            // 1. Row 오디오 파일 로드 및 병합
            List<AudioProperties> audioProperties = loadAudioFiles(selectedRows);
            ByteArrayOutputStream concatenatedAudio = intervalConcatenator.intervalConcatenate(audioProperties, selectedRows.getInitialSilence());

            // 2. BGM 파일 로드 및 처리
            AudioInputStream bgmStream = S3Service.load(bgmFileUrl);

            // 3. 병합된 Row 오디오와 BGM 믹싱
            AudioInputStream concatenatedAudioStream = new AudioInputStream(
                    new ByteArrayInputStream(concatenatedAudio.toByteArray()),
                    defaultAudioFormat,
                    concatenatedAudio.size() / defaultAudioFormat.getFrameSize()
            );

            // BGM 길이를 Row 오디오와 맞춤 (확장 또는 자르기)
            long targetFrames = concatenatedAudioStream.getFrameLength();

            // getFrameLength 값이 0 또는 음수인 경우 직접 계산
            if (targetFrames <= 0) {
                targetFrames = BgmProcessor.calculateTargetFrames(concatenatedAudioStream);
            }

            long bgmFrames = bgmStream.getFrameLength();
            if (bgmFrames <= 0) {
                bgmFrames = BgmProcessor.calculateTargetFrames(bgmStream);
            }

            // BGM 길이를 Row 오디오와 맞춤
            if (bgmFrames > targetFrames) {
                bgmStream = BgmProcessor.trimBgm(bgmStream, targetFrames);
            } else {
                bgmStream = BgmProcessor.extendBgm(bgmStream, targetFrames);
            }

            // 두 오디오 스트림을 믹싱
            AudioInputStream mixedAudioStream = BgmProcessor.mixAudio(concatenatedAudioStream, bgmStream);

            // 4. S3에 업로드
            String audioUrl = uploadToS3(mixedAudioStream);

            // 5. ConcatResult 테이블 DB에 저장 결과물 저장
            ConcatResultRequest concatResultRequest = ConcatResultRequest.builder()
                    .concatTabSeq(concatTabSeq)
                    .optionSeq(null)
                    .ResultUrl(audioUrl)
                    .ResultFileName("mixed_with_bgm.wav")
                    .ResultExtension("wav")
                    .ResultFileLength(mixedAudioStream.getFrameLength() / mixedAudioStream.getFormat().getFrameRate())
                    .build();
            ConcatUrlResponse concatResultResponse = concatResultService.saveConcatResult(concatResultRequest);


            // Material 테이블에 재료-결과물 관계 저장
            List<Long> usedAudioFileSeqs = audioFileService.getAudioFileSeqsByUrls(
                    selectedRows.getRows().stream()
                            .map(SelectedConcatRowRequest.Row::getAudioUrl)
                            .toList()
            );
            materialAudioService.saveMaterials(concatResultResponse.getSeq(), usedAudioFileSeqs);

            // Response 데이터 생성
            List<RowInfoDto> rows = selectedRows.getRows().stream()
                    .map(row -> new RowInfoDto(row.getAudioUrl(), row.getSilenceInterval()))
                    .toList();

            ConcatResponseDto responseDto = ConcatResponseDto.builder()
                    .audioUrl(audioUrl)
                    .rows(rows)
                    .build();

            return new ResponseDto<>(HttpStatus.OK.value(), responseDto).toResponseEntity();
        } catch (Exception e) {
            // 에러 응답 생성
            return new ResponseDto<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    ConcatResponseDto.builder().audioUrl(null).rows(new ArrayList<>()).build()
            ).toResponseEntity();
        }
    }

    private List<AudioProperties> loadAudioFiles(SelectedConcatRowRequest selectedRows) {
        List<AudioProperties> audioPropertiesList = new ArrayList<>();
        for (SelectedConcatRowRequest.Row row : selectedRows.getRows()) {
            // S3에서 오디오 파일 다운로드
            // 오디오 포맷 확인 및 변환
            AudioInputStream audioStream = S3Service.load(row.getAudioUrl());
            audioStream = audioResample.formatting(audioStream);

            // AudioProperties 생성
            audioPropertiesList.add(new AudioProperties(audioStream, row.getSilenceInterval()));
        }
        return audioPropertiesList;
    }

    private String uploadToS3(AudioInputStream audioStream) throws IOException {
        // AudioInputStream을 ByteArrayOutputStream으로 변환
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, outputStream);

        // ByteArrayOutputStream을 MultipartFile로 변환
        ByteArrayMultipartFile multipartFile = new ByteArrayMultipartFile(
                outputStream.toByteArray(),
                "mixed_with_bgm.wav",
                "audio/wav"
        );

        // S3에 업로드
        return s3Service.upload(multipartFile, "concat/result");
    }


}