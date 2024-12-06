package com.oreo.finalproject_5re5_be.concat.controller;

import com.oreo.finalproject_5re5_be.concat.dto.RowAudioFileDto;
import com.oreo.finalproject_5re5_be.concat.dto.request.*;
import com.oreo.finalproject_5re5_be.concat.dto.response.ConcatTabResponseDto;
import com.oreo.finalproject_5re5_be.concat.dto.response.TabRowResponseDto;
import com.oreo.finalproject_5re5_be.concat.service.AudioFileService;
import com.oreo.finalproject_5re5_be.concat.service.ConcatRowService;
import com.oreo.finalproject_5re5_be.concat.service.ConcatTabService;
import com.oreo.finalproject_5re5_be.global.dto.response.ResponseDto;
import com.oreo.finalproject_5re5_be.member.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Concat", description = "Concat 관련 API")

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v2/concat")
public class ConcatRowTabController {
    private final ConcatRowService concatRowService;
    private final ConcatTabService concatTabService;
    private final AudioFileService audioFileService;

    //탭 로우 조회
    //탭 로우 저장

    @Operation(
            summary = "ConcatRow, ConcatTab을 저장합니다.",
            description = ""
    )
    @PostMapping("save")
    public ResponseEntity<ResponseDto<Boolean>> saveRowAndTab(
            @RequestBody TabRowUpdateRequestDto dto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        ConcatRowSaveRequestDto concatRows = dto.getConcatRows();
        ConcatUpdateRequestDto concatTabs = dto.getConcatTab();
        boolean updateConcatTab = concatTabService.updateConcatTab(concatTabs, customUserDetails.getMember().getSeq());
        boolean updateConcatRows = concatRowService.updateConcatRows(concatRows);
        return new ResponseDto<>(HttpStatus.OK.value(), updateConcatTab && updateConcatRows).toResponseEntity();

    }

//    @Operation(
//            summary = "ConcatRow, ConcatTab을 저장합니다.",
//            description = ""
//    )
//    @PostMapping("save")
//    public ResponseEntity<ResponseDto<Boolean>> saveRowAndTab(
//            @RequestBody TabRowUpdateRequestDto dto,
//            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
//        ConcatUpdateRequestDto concatUpdateRequestDto = dto.getConcatTab();
//        Long memberSeq = customUserDetails.getMember().getSeq();
//        List<RowAudioFileDto> rowAudioFiles = dto.getAudioFiles();
//        boolean updateTab = false;
//        boolean updateRow = false;
//        if (concatUpdateRequestDto != null) {
//            updateTab = concatTabService.updateConcatTab(concatUpdateRequestDto,
//                    memberSeq);
//            updateRow = concatRowService.updateConcatRows(rowAudioFiles, concatUpdateRequestDto.getTabId());
//        }
//
//        return new ResponseDto<>(HttpStatus.OK.value(), updateTab && updateRow).toResponseEntity();
//    }
//

    @Operation(
            summary = "ConcatRow, ConcatTab을 조회합니다.",
            description = ""
    )
    @GetMapping("read")
    public ResponseEntity<ResponseDto<TabRowResponseDto>> readRowAndTab(
            @RequestParam Long projectSeq,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        ConcatTabResponseDto concatTabResponseDto
                = concatTabService.readConcatTab(projectSeq, customUserDetails.getMember().getSeq());

        List<RowAudioFileDto> audioFiles = audioFileService.getAudioFilesByProjectAndStatusTrue(projectSeq);


        List<ConcatRowRequest> concatRowRequests = audioFiles.stream().map(x -> ConcatRowRequest.builder()
                .originAudioRequest(OriginAudioRequest.builder()
                        .seq(x.getAudioFileSeq())
                        .audioUrl(x.getAudioUrl())
                        .fileName(x.getFileName())
                        .fileSize(x.getFileSize())
                        .fileLength(x.getFileLength())
                        .extension(x.getExtension()).build()
                )
                .rowText(x.getConcatRow().getRowText())
                .rowIndex(x.getConcatRow().getRowIndex())
                .rowSilence(x.getConcatRow().getSilence())
                .status(x.getConcatRow().getStatus())
                .seq(x.getConcatRow().getConcatRowSequence())
                .build()).toList();


        return new ResponseDto<>(HttpStatus.OK.value(), new TabRowResponseDto(concatTabResponseDto,
                new ConcatRowSaveRequestDto(concatTabResponseDto.getTabId(), concatRowRequests)))
                .toResponseEntity();
    }
}


