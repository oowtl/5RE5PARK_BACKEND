package com.oreo.finalproject_5re5_be.concat.controller;

import com.oreo.finalproject_5re5_be.concat.dto.ConcatRowListDto;
import com.oreo.finalproject_5re5_be.concat.dto.response.ConcatUrlResponse;
import com.oreo.finalproject_5re5_be.concat.entity.ConcatRow;
import com.oreo.finalproject_5re5_be.concat.service.MaterialAudioService;
import com.oreo.finalproject_5re5_be.global.dto.response.ResponseDto;
import com.oreo.finalproject_5re5_be.member.dto.CustomUserDetails;
import com.oreo.finalproject_5re5_be.project.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Concat", description = "Concat 관련 API")
@RestController
@RequestMapping("/api/concat/audio/materials")
public class ConcatMaterialController {

    private final MaterialAudioService materialAudioService;
    private final ProjectService projectService;

    public ConcatMaterialController(MaterialAudioService materialAudioService, ProjectService projectService) {
        this.materialAudioService = materialAudioService;
        this.projectService = projectService;
    }

    @Operation(
            summary = "결과에 사용된 오디오 불러오기",
            description = "결과에 사용된 오디오 URL을 반환 합니다."
    )
    @GetMapping("")
    public ResponseDto<List<ConcatUrlResponse>> getMaterials(@RequestParam("concatresultseq") Long concatResultSeq,
                                                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        projectService.projectCheck(userDetails.getMember().getSeq(), concatResultSeq);

        List<ConcatUrlResponse> audioFilesByConcatResultSeq
                = materialAudioService.findAudioFilesByConcatResultSeq(concatResultSeq);
        return new ResponseDto<>(HttpStatus.OK.value(), audioFilesByConcatResultSeq);
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
        List<ConcatRow> materialConcatRowList = materialAudioService.findConcatRowListByResultSeq(concatResultSeq);

        // 응답
        return new ResponseDto<>(HttpStatus.OK.value(), ConcatRowListDto.of(materialConcatRowList)).toResponseEntity();
        // 조회 결과 응답 DTO로 변환
    }

}