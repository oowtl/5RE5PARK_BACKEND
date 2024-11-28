package com.oreo.finalproject_5re5_be.concat.controller;

import com.oreo.finalproject_5re5_be.concat.dto.request.ConcatCreateRequestDto;
import com.oreo.finalproject_5re5_be.concat.dto.request.ConcatUpdateRequestDto;
import com.oreo.finalproject_5re5_be.concat.dto.response.ConcatTabResponseDto;
import com.oreo.finalproject_5re5_be.concat.service.ConcatTabService;
import com.oreo.finalproject_5re5_be.global.dto.response.ResponseDto;
import com.oreo.finalproject_5re5_be.project.service.ProjectService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Concat", description = "Concat 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/concat/tab")
public class ConcatTabController {

    private final ConcatTabService concatTabService;
    private final ProjectService projectService;

    @PostMapping("create")
    public ResponseEntity<ResponseDto<Boolean>> create(@RequestBody ConcatCreateRequestDto createRequestDto) {
        projectService.projectCheck(createRequestDto.getMemberSequence(), createRequestDto.getProjectSequence());
        //사용자 예외처리
        return new ResponseDto<>(HttpStatus.OK.value(), concatTabService.createConcatTab(createRequestDto))
                .toResponseEntity();
    }

    @GetMapping("read")
    public ResponseEntity<ResponseDto<ConcatTabResponseDto>> read(
            @RequestParam Long projectSeq,
            @RequestParam Long memberSeq) {
        projectService.projectCheck(memberSeq, projectSeq);
        //사용자 예외 처리
        return new ResponseDto<>(HttpStatus.OK.value(), concatTabService.readConcatTab(projectSeq, memberSeq))
                .toResponseEntity();
    }

    @PostMapping("update")
    public ResponseEntity<ResponseDto<Boolean>> update(@RequestBody ConcatUpdateRequestDto updateRequestDto) {
        projectService.projectCheck(updateRequestDto.getMemberSeq(), updateRequestDto.getTabId());
        return new ResponseDto<>(HttpStatus.OK.value(), concatTabService.updateConcatTab(updateRequestDto))
                .toResponseEntity();
    }
}
