package com.oreo.finalproject_5re5_be.concat.controller;

import com.oreo.finalproject_5re5_be.concat.dto.ConcatRowDto;
import com.oreo.finalproject_5re5_be.concat.dto.request.ConcatRowSaveRequestDto;
import com.oreo.finalproject_5re5_be.concat.service.ConcatRowService;
import com.oreo.finalproject_5re5_be.global.dto.response.ResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Tag(name = "Concat", description = "Concat 관련 API")
@RestController
@RequestMapping("/api/concat/row")
@RequiredArgsConstructor
public class ConcatRowController {
    private final ConcatRowService concatRowService;

    //오디오 파일 업로드 -> S3url리턴 -> 클라에서 url로 로우 생성 -> 저장 요청 -> url로 테이블 로우 생성
    @PostMapping("save")
    public ResponseEntity<ResponseDto<Boolean>> save(
            @RequestBody ConcatRowSaveRequestDto concatRowSaveRequestDto) throws IOException {
        return new ResponseDto<>(HttpStatus.OK.value(), concatRowService.saveConcatRows(concatRowSaveRequestDto))
                .toResponseEntity();
    }

    @PostMapping("disable")
    public ResponseEntity<ResponseDto<Boolean>> disable(@RequestParam List<Long> rowSeq) {
        return new ResponseDto<>(HttpStatus.OK.value(), concatRowService.disableConcatRows(rowSeq))
                .toResponseEntity();
    }

    @GetMapping("read")
    public ResponseEntity<ResponseDto<List<ConcatRowDto>>> readOne(
            @RequestParam Long concatRowSequence
            , @RequestParam String memberSequence) {
        //사용자 예외 처리
        return new ResponseDto<>(HttpStatus.OK.value(), concatRowService.readConcatRows(concatRowSequence))
                .toResponseEntity();
    }

    @GetMapping("read/recent")
    public ResponseEntity<ResponseDto<List<ConcatRowDto>>> readRecent(
            @RequestParam Long projectSequence
            , @RequestParam String memberSequence) {
        //사용자 예외 처리
        return new ResponseDto<>(HttpStatus.OK.value(), concatRowService.readRecentConcatRows(projectSequence))
                .toResponseEntity();
    }

    @PostMapping("update")
    public ResponseEntity<ResponseDto<Boolean>> update(@RequestBody ConcatRowSaveRequestDto concatRowSaveRequestDto) {
        return new ResponseDto<>(HttpStatus.OK.value(), concatRowService.updateConcatRows(concatRowSaveRequestDto))
                .toResponseEntity();
    }
}
