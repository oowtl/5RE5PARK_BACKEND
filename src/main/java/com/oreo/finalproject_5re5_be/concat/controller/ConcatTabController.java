package com.oreo.finalproject_5re5_be.concat.controller;

import com.oreo.finalproject_5re5_be.concat.dto.request.ConcatCreateRequestDto;
import com.oreo.finalproject_5re5_be.concat.dto.request.ConcatUpdateRequestDto;
import com.oreo.finalproject_5re5_be.concat.dto.response.ConcatTabResponseDto;
import com.oreo.finalproject_5re5_be.concat.service.ConcatTabService;
import com.oreo.finalproject_5re5_be.global.dto.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/concat/tab")
public class ConcatTabController {

    private final ConcatTabService concatTabService;

    @PostMapping("create")
    public ResponseEntity<ResponseDto<Boolean>> create(@RequestBody ConcatCreateRequestDto createRequestDto) {
        //사용자 예외처리
        return new ResponseDto<>(HttpStatus.OK.value(), concatTabService.createConcatTab(createRequestDto))
                .toResponseEntity();
    }

    @GetMapping("read")
    public ResponseEntity<ResponseDto<ConcatTabResponseDto>> read (
            @RequestParam Long projectSeq,
            @RequestParam Long memberSeq) {
        //사용자 예외 처리
        return new ResponseDto<>(HttpStatus.OK.value(), concatTabService.readConcatTab(projectSeq, memberSeq))
                .toResponseEntity();
    }

    @PostMapping("update")
    public ResponseEntity<ResponseDto<Boolean>> update(@RequestBody ConcatUpdateRequestDto updateRequestDto) {
        return new ResponseDto<>(HttpStatus.OK.value(), concatTabService.updateConcatTab(updateRequestDto))
                .toResponseEntity();
    }
}
