package com.oreo.finalproject_5re5_be.concat.controller;

import com.oreo.finalproject_5re5_be.concat.dto.ConcatRowListDto;
import com.oreo.finalproject_5re5_be.concat.dto.request.OriginAudioRequest;
import com.oreo.finalproject_5re5_be.concat.dto.response.ConcatResultResponse;
import com.oreo.finalproject_5re5_be.concat.entity.BgmFile;
import com.oreo.finalproject_5re5_be.concat.entity.ConcatRow;
import com.oreo.finalproject_5re5_be.concat.service.MaterialAudioService;
import com.oreo.finalproject_5re5_be.global.dto.response.ResponseDto;
import com.oreo.finalproject_5re5_be.member.dto.CustomUserDetails;
import com.oreo.finalproject_5re5_be.project.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Concat", description = "Concat 관련 API")
@RestController
@RequestMapping("/api/concat/audio/materials")
public class ConcatMaterialController {

    private final MaterialAudioService materialAudioService;
    private final ProjectService projectService;

    public ConcatMaterialController(
            MaterialAudioService materialAudioService, ProjectService projectService) {
        this.materialAudioService = materialAudioService;
        this.projectService = projectService;
    }

    @Operation(summary = "결과에 사용된 모든 오디오 파일과 BGM 파일 조회")
    @GetMapping("")
    public ResponseEntity<ResponseDto<ConcatResultResponse>> getAllMaterialsByResultSeq(
            @RequestParam("concatresultseq") Long concatResultSeq,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        projectService.projectCheck(userDetails.getMember().getSeq(), concatResultSeq);

        // ConcatResult에서 결과 파일 URL 조회
        String resultAudioUrl =
                materialAudioService.findResultAudioUrlByConcatResultSeq(concatResultSeq);

        // BGM 파일 조회
        BgmFile bgmFile = materialAudioService.findBgmFileByConcatResultSeq(concatResultSeq);
        OriginAudioRequest bgmFileResponse = null;
        if (bgmFile != null) {
            bgmFileResponse =
                    OriginAudioRequest.builder()
                            .seq(bgmFile.getBgmFileSeq())
                            .audioUrl(bgmFile.getAudioUrl())
                            .extension(bgmFile.getExtension())
                            .fileSize(bgmFile.getFileSize())
                            .fileLength(bgmFile.getFileLength())
                            .fileName(bgmFile.getFileName())
                            .build();
        }

        // 재료 오디오 파일 조회
        List<OriginAudioRequest> materialAudioFiles =
                materialAudioService.findMaterialAudioFilesByConcatResultSeq(concatResultSeq);

        // 응답 생성
        ConcatResultResponse response =
                ConcatResultResponse.builder()
                        .concatResultSeq(concatResultSeq)
                        .audioUrl(resultAudioUrl)
                        .bgmFile(bgmFileResponse)
                        .materialAudioFiles(materialAudioFiles)
                        .build();

        return new ResponseDto<>(HttpStatus.OK.value(), response).toResponseEntity();
    }

    // 재료 오디오 목록의 행 정보 불러오기
    @Operation(summary = "concat 결과에 연관된 행 정보 불러오기")
    @Parameter(name = "concatresultseq", description = "concat 결과 seq(식별번호)")
    @GetMapping("/rows")
    public ResponseEntity<ResponseDto<ConcatRowListDto>> getMaterialRowListByResultSeq(
            @NotNull @RequestParam("concatresultseq") Long concatResultSeq,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        projectService.projectCheck(userDetails.getMember().getSeq(), concatResultSeq);

        // resultSeq로 재료가 된 concatRowList 얻어오기
        List<ConcatRow> materialConcatRowList =
                materialAudioService.findConcatRowListByResultSeq(concatResultSeq);

        // 응답
        return new ResponseDto<>(HttpStatus.OK.value(), ConcatRowListDto.of(materialConcatRowList))
                .toResponseEntity();
        // 조회 결과 응답 DTO로 변환
    }
}
