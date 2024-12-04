package com.oreo.finalproject_5re5_be.concat.controller;

import com.oreo.finalproject_5re5_be.concat.dto.ConcatResponseDto;
import com.oreo.finalproject_5re5_be.concat.dto.RowInfoDto;
import com.oreo.finalproject_5re5_be.concat.dto.request.SelectedConcatRowRequest;
import com.oreo.finalproject_5re5_be.concat.dto.response.ConcatUrlResponse;
import com.oreo.finalproject_5re5_be.concat.service.AudioFileService;
import com.oreo.finalproject_5re5_be.concat.service.AudioStreamService;
import com.oreo.finalproject_5re5_be.concat.service.ConcatResultService;
import com.oreo.finalproject_5re5_be.concat.service.MaterialAudioService;
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
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import java.io.ByteArrayOutputStream;
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
    private final AudioStreamService audioStreamService; // 추가된 서비스
    private final AudioResample audioResample = new AudioResample(); // 리샘플링 유틸. Bean이 아니라 new로 생성
    private final AudioFormat defaultAudioFormat = AudioFormats.STEREO_FORMAT_SR441_B32; // 기본 포맷
    private final ProjectService projectService;

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
            @Parameter(description = "결과물이 나온 concatTab", required = true) @RequestParam Long concatTabSeq,
            @Parameter(description = "user가 설정한 결과물 파일 이름", required = true) @RequestParam String concatResultFileName,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        projectService.projectCheck(customUserDetails.getMember().getSeq(), concatTabSeq);
        try {
            IntervalConcatenator intervalConcatenator = new StereoIntervalConcatenator(defaultAudioFormat);

            // 1. Row 오디오 파일 로드 및 무음 처리
            List<AudioProperties> audioProperties = audioStreamService.loadAudioFiles(selectedRows);

            // 2. 병합된 오디오 생성
            ByteArrayOutputStream concatenatedAudioBuffer = intervalConcatenator.intervalConcatenate(audioProperties, selectedRows.getInitialSilence());

            // 3. 병합된 오디오를 AudioInputStream으로 변환
            AudioInputStream concatenatedAudioStream = audioStreamService.createAudioInputStream(concatenatedAudioBuffer, defaultAudioFormat);

            // 4. BGM 스트림 로드 및 버퍼링
            AudioInputStream bufferedBgmStream = s3Service.loadAsBufferedStream(bgmFileUrl);

            // 5. BGM 길이 조정
            long targetFrames = audioStreamService.getValidFrameLength(concatenatedAudioStream);
            long bgmFrames = audioStreamService.getValidFrameLength(bufferedBgmStream);
            bufferedBgmStream = BgmProcessor.adjustBgmLength(bufferedBgmStream, targetFrames, bgmFrames);

            // 6. 믹싱
            AudioInputStream mixedAudioStream = BgmProcessor.mixAudio(concatenatedAudioStream, bufferedBgmStream);

            // 7. 결과파일 S3 업로드
            String audioUrl = s3Service.uploadAudioStream(mixedAudioStream, "concat/result", concatResultFileName);

            // 8. DB ConcatResult테이블에 결과 저장
            ConcatUrlResponse concatResultResponse = concatResultService.saveConcatResult(concatTabSeq, audioUrl, concatResultFileName, mixedAudioStream);

            // 9. Material 데이터 저장 (재료 파일, 결과파일 저장되어 있는 상태로 교차테이블에 데이터 저장)
            materialAudioService.saveMaterialsForSelectedRows(selectedRows, concatResultResponse);

            // 10. 성공 응답 생성
            return createSuccessResponse(audioUrl, selectedRows);
        } catch (Exception e) {
            // 11. 실패 응답 생성
            return createErrorResponse();
        }
    }

    private ResponseEntity<ResponseDto<ConcatResponseDto>> createSuccessResponse(String audioUrl, SelectedConcatRowRequest selectedRows) {
        List<RowInfoDto> rows = selectedRows.getRows().stream()
                .map(row -> new RowInfoDto(row.getAudioUrl(), row.getSilenceInterval()))
                .toList();
        ConcatResponseDto responseDto = ConcatResponseDto.builder()
                .audioUrl(audioUrl)
                .rows(rows)
                .build();
        return new ResponseDto<>(HttpStatus.OK.value(), responseDto).toResponseEntity();
    }

    private ResponseEntity<ResponseDto<ConcatResponseDto>> createErrorResponse() {
        return new ResponseDto<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ConcatResponseDto.builder().audioUrl(null).rows(new ArrayList<>()).build()
        ).toResponseEntity();
    }
}