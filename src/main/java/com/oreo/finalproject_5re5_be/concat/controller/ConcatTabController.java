package com.oreo.finalproject_5re5_be.concat.controller;

import com.oreo.finalproject_5re5_be.concat.dto.request.ConcatCreateRequestDto;
import com.oreo.finalproject_5re5_be.concat.dto.request.ConcatUpdateRequestDto;
import com.oreo.finalproject_5re5_be.concat.dto.response.ConcatTabResponseDto;
import com.oreo.finalproject_5re5_be.concat.service.ConcatTabService;
import com.oreo.finalproject_5re5_be.global.dto.response.ResponseDto;
import com.oreo.finalproject_5re5_be.member.dto.CustomUserDetails;
import com.oreo.finalproject_5re5_be.project.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Concat", description = "Concat 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/concat/tab")
public class ConcatTabController {

    private final ConcatTabService concatTabService;
    private final ProjectService projectService;

    @Operation(summary = "프로젝트의 Concat 탭을 생성합니다.", description = "생성이 성공했다면 True를 반환합니다.")
    @PostMapping("create")
    public ResponseEntity<ResponseDto<Boolean>> create(
            @AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestParam Long projectSeq) {
        projectService.projectCheck(customUserDetails.getMember().getSeq(), projectSeq);
        // 사용자 예외처리
        return new ResponseDto<>(
                        HttpStatus.OK.value(),
                        concatTabService.createConcatTab(
                                new ConcatCreateRequestDto(projectSeq, customUserDetails.getMember().getSeq())))
                .toResponseEntity();
    }

    @Operation(summary = "탭 조회", description = "프로젝트의 Concat 탭 정보를 조회합니다. 탭에 저장된 행 정보는 반환되지 않습니다.")
    @GetMapping("read")
    public ResponseEntity<ResponseDto<ConcatTabResponseDto>> read(
            @RequestParam Long projectSeq, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        projectService.projectCheck(customUserDetails.getMember().getSeq(), projectSeq);
        // 사용자 예외 처리
        return new ResponseDto<>(
                        HttpStatus.OK.value(),
                        concatTabService.readConcatTab(projectSeq, customUserDetails.getMember().getSeq()))
                .toResponseEntity();
    }

    @Operation(summary = "탭 업데이트", description = "행 정보를 업데이트 합니다. 성공했다면 True를 반환합니다.")
    @PostMapping("update")
    public ResponseEntity<ResponseDto<Boolean>> update(
            @RequestBody ConcatUpdateRequestDto updateRequestDto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        projectService.projectCheck(
                customUserDetails.getMember().getSeq(), updateRequestDto.getTabId());
        return new ResponseDto<>(
                        HttpStatus.OK.value(),
                        concatTabService.updateConcatTab(
                                updateRequestDto, customUserDetails.getMember().getSeq()))
                .toResponseEntity();
    }

    @Operation(
            summary = "BGM 오디오 파일 업데이트",
            description = "BGM 오디오 파일을 추가하거나 제거합니다. BgmFile ID 목록을 전달합니다.")
    @PostMapping("update-bgm")
    public ResponseEntity<ResponseDto<Boolean>> updateBgm(
            @RequestParam Long tabSeq,
            @RequestBody(required = false)
                    @Schema(description = "BgmFile ID 목록", example = "[101, 102, 103]")
                    List<Long> bgmAudioFileSeqs,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        // 프로젝트 권한 확인
        projectService.projectCheck(customUserDetails.getMember().getSeq(), tabSeq);

        // BGM 업데이트 처리
        boolean result = concatTabService.updateBgmAudioFiles(tabSeq, bgmAudioFileSeqs);

        // 응답 반환
        return new ResponseDto<>(HttpStatus.OK.value(), result).toResponseEntity();
    }
}
