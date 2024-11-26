package com.oreo.finalproject_5re5_be.concat.controller;


import com.oreo.finalproject_5re5_be.concat.dto.ConcatResultDto;
import com.oreo.finalproject_5re5_be.concat.dto.request.ConcatRowRequestDto;
import com.oreo.finalproject_5re5_be.concat.dto.response.ConcatTabResponseDto;
import com.oreo.finalproject_5re5_be.concat.service.ConcatResultService;
import com.oreo.finalproject_5re5_be.concat.service.ConcatService;
import com.oreo.finalproject_5re5_be.concat.service.ConcatTabService;
import com.oreo.finalproject_5re5_be.global.dto.response.ResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@Tag(name = "Concat", description = "Concat 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/concat")
public class ConcatController {
    private final ConcatService concatService;
    private final ConcatTabService concatTabService;
    private final ConcatResultService concatResultService;

    @PostMapping("")
    public ResponseEntity<ResponseDto<ConcatResultDto>> concat(@RequestBody ConcatRowRequestDto audioRequests, @RequestParam Long memberSeq) throws IOException {
        System.out.println("audioRequests.getFileName() = " + audioRequests.getFileName());
        ConcatTabResponseDto concatTabResponseDto
                = concatTabService.readConcatTab(audioRequests.getConcatTabId(), memberSeq);
        ConcatResultDto concat = concatService.concat(concatTabResponseDto, audioRequests);
        return new ResponseDto<>(HttpStatus.OK.value(), concat).toResponseEntity();
    }

    @GetMapping("read/result")
    public ResponseEntity<ResponseDto<List<ConcatResultDto>>> readConcatResult(@RequestParam Long projectSeq) {
        return new ResponseDto<>(HttpStatus.OK.value(), concatResultService.findByConcatTabSequence(projectSeq)).toResponseEntity();
    }

    // IllegalArgumentException 처리
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseDto<String>> handleDataNotFoundException(IllegalArgumentException ex) {
        String errorMessage = ex.getMessage();
        return new ResponseDto<>(HttpStatus.BAD_REQUEST.value(), errorMessage).toResponseEntity();
    }


    // NoSuchElementException 처리
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ResponseDto<String>> handleDataNotFoundException(NoSuchElementException ex) {
        String errorMessage = ex.getMessage();
        return new ResponseDto<>(HttpStatus.BAD_REQUEST.value(), errorMessage).toResponseEntity();
    }
}
