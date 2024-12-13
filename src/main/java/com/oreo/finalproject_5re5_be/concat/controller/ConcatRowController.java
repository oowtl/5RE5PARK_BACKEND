package com.oreo.finalproject_5re5_be.concat.controller;

import com.oreo.finalproject_5re5_be.concat.dto.ConcatRowDto;
import com.oreo.finalproject_5re5_be.concat.dto.request.ConcatRowSaveRequestDto;
import com.oreo.finalproject_5re5_be.concat.service.ConcatRowService;
import com.oreo.finalproject_5re5_be.global.dto.response.ResponseDto;
import com.oreo.finalproject_5re5_be.member.dto.CustomUserDetails;
import com.oreo.finalproject_5re5_be.project.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Concat", description = "Concat 관련 API")
@RestController
@RequestMapping("/api/concat/row")
@RequiredArgsConstructor
public class ConcatRowController {
    private final ConcatRowService concatRowService;
    private final ProjectService projectService;

    // 오디오 파일 업로드 -> S3url리턴 -> 클라에서 url로 로우 생성 -> 저장 요청 -> url로 테이블 로우 생성
    @Operation(summary = "행 저장", description = "새로운 행을 저장합니다.")
    @PostMapping("save")
    public ResponseEntity<ResponseDto<Boolean>> save(
            @RequestBody ConcatRowSaveRequestDto concatRowSaveRequestDto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails)
            throws IOException {
        projectService.projectCheck(
                customUserDetails.getMember().getSeq(), concatRowSaveRequestDto.getConcatTabId());

        return new ResponseDto<>(
                        HttpStatus.OK.value(), concatRowService.saveConcatRows(concatRowSaveRequestDto))
                .toResponseEntity();
    }

    @Operation(summary = "행 비활성", description = "오디오 파일이 조회되지 않도록 비활성 처리 합니다.")
    @PostMapping("disable")
    public ResponseEntity<ResponseDto<Boolean>> disable(
            @RequestParam List<Long> rowSeq,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        projectService.projectCheck(
                customUserDetails.getMember().getSeq(),
                concatRowService.readConcatRow(rowSeq.get(0)).getConcatTab().getProjectId());

        return new ResponseDto<>(HttpStatus.OK.value(), concatRowService.disableConcatRows(rowSeq))
                .toResponseEntity();
    }

    @Operation(summary = "행 조회", description = "하나 행을 조회합니다. 이전에 비활성 된 행은 조회 할 수 없습니다.")
    @GetMapping("read")
    public ResponseEntity<ResponseDto<List<ConcatRowDto>>> readOne(
            @RequestParam Long concatRowSequence,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        projectService.projectCheck(
                customUserDetails.getMember().getSeq(),
                concatRowService.readConcatRow(concatRowSequence).getConcatTab().getProjectId());

        // 사용자 예외 처리
        return new ResponseDto<>(
                        HttpStatus.OK.value(), concatRowService.readConcatRows(concatRowSequence))
                .toResponseEntity();
    }

    @Operation(summary = "행 조회", description = "프로젝트의 활성 상태인 행을조회합니다. 이전에 비활성 된 행은 조회 할 수 없습니다.")
    @GetMapping("read/recent")
    public ResponseEntity<ResponseDto<List<ConcatRowDto>>> readRecent(
            @RequestParam Long projectSequence,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        projectService.projectCheck(customUserDetails.getMember().getSeq(), projectSequence);

        // 사용자 예외 처리
        return new ResponseDto<>(
                        HttpStatus.OK.value(), concatRowService.readRecentConcatRows(projectSequence))
                .toResponseEntity();
    }

    @Operation(summary = "행 업데이트", description = "행을 업데이트 합니다. 새로운 행이 생성 되므로 이전의 행과 다른 아이디를 반환 합니다.")
    @PostMapping("update")
    public ResponseEntity<ResponseDto<Boolean>> update(
            @RequestBody ConcatRowSaveRequestDto concatRowSaveRequestDto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        projectService.projectCheck(
                customUserDetails.getMember().getSeq(), concatRowSaveRequestDto.getConcatTabId());

        return new ResponseDto<>(
                        HttpStatus.OK.value(), concatRowService.updateConcatRows(concatRowSaveRequestDto))
                .toResponseEntity();
    }

    @Operation(summary = "행 텍스트 업로드", description = "행에 텍스트를 업로드 합니다. 이전과 같은 아이디를 반환합니다.")
    @PostMapping("upload/text")
    public ResponseEntity<ResponseDto<Boolean>> uploadText(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody ConcatRowSaveRequestDto concatRowSaveRequestDto) {

        // 서비스 호출
        boolean uploadText = concatRowService.uploadText(concatRowSaveRequestDto);

        // 결과 응답 생성
        if (uploadText) {
            return new ResponseDto<>(HttpStatus.OK.value(), uploadText).toResponseEntity();
        } else {
            return new ResponseDto<>(HttpStatus.BAD_REQUEST.value(), uploadText).toResponseEntity();
        }
    }
}
