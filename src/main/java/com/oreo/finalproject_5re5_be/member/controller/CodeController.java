package com.oreo.finalproject_5re5_be.member.controller;

import com.oreo.finalproject_5re5_be.global.dto.response.ErrorResponseDto;
import com.oreo.finalproject_5re5_be.global.exception.ErrorCode;
import com.oreo.finalproject_5re5_be.member.dto.request.CodeRequest;
import com.oreo.finalproject_5re5_be.member.dto.request.CodeUpdateRequest;
import com.oreo.finalproject_5re5_be.member.dto.response.CodeResponse;
import com.oreo.finalproject_5re5_be.member.dto.response.CodeResponses;
import com.oreo.finalproject_5re5_be.member.exception.CodeDuplicatedException;
import com.oreo.finalproject_5re5_be.member.exception.CodeNotFoundException;
import com.oreo.finalproject_5re5_be.member.service.CodeServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "CODE", description = "CODE 관련 API")
@Validated
@RestController
@RequestMapping("/api/code")
public class CodeController {

    private final CodeServiceImpl codeService;


    public CodeController(CodeServiceImpl codeService) {
        this.codeService = codeService;
    }

    @ExceptionHandler({
            CodeNotFoundException.class
    })
    public ResponseEntity<ErrorResponseDto> handleNotFoundException(RuntimeException e) {
        log.error("[CodeNotFoundException]", e);
        ErrorResponseDto errorResponseDto = ErrorResponseDto.of(ErrorCode.INVALID_INPUT_VALUE.getStatus(),
                                                                e.getMessage());
        return ResponseEntity.status(ErrorCode.INVALID_INPUT_VALUE.getStatus())
                             .body(errorResponseDto);
    }

    @ExceptionHandler({
            CodeDuplicatedException.class
    })
    public ResponseEntity<ErrorResponseDto> handleDuplicatedException(RuntimeException e) {
        log.error("[CodeDuplicatedException]", e);
        ErrorResponseDto errorResponseDto = ErrorResponseDto.of(ErrorCode.ENTITY_NOT_FOUND.getStatus(),
                                                                e.getMessage());

        return ResponseEntity.status(ErrorCode.ENTITY_NOT_FOUND.getStatus()) // 상태코드가 중복해서 처리됨. 이 부분 이야기 해보기
                             .body(errorResponseDto);
    }

    // 코드 등록
    @PostMapping("/register")
    public ResponseEntity<CodeResponse> create(@RequestBody @Valid CodeRequest request) {
        // 서비스를 호출하여 코드를 등록한다
        CodeResponse response = codeService.create(request);
        // 등록된 코드를 반환한다
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(response);

    }

    // 모든 코드 조회
    @GetMapping("/all")
    public ResponseEntity<CodeResponses> readAll() {
        // 서비스를 호출하여 모든 코드를 조회한다
        CodeResponses responses = codeService.readAll();
        // 조회된 코드를 반환한다
        return ResponseEntity.status(HttpStatus.OK)
                             .body(responses);
    }

    // 시퀀스로 특정 코드 조회
    @GetMapping("/seq/{codeSeq}")
    public ResponseEntity<CodeResponse> readBySeq(@PathVariable Long codeSeq) {
        // 서비스를 호출하여 시퀀스로 특정 코드를 조회한다
        CodeResponse response = codeService.read(codeSeq);
        // 조회된 코드를 반환한다
        return ResponseEntity.status(HttpStatus.OK)
                             .body(response);
    }

    // 코드 번호로 특정 코드 조회
    @GetMapping("/{code}")
    public ResponseEntity<CodeResponse> readByCode(@PathVariable String code) {
        // 서비스를 호출하여 코드 번호로 특정 코드를 조회한다
        CodeResponse response = codeService.read(code);
        // 조회된 코드를 반환한다
        return ResponseEntity.status(HttpStatus.OK)
                             .body(response);
    }

    // 각 파트(cateNum)으로 사용 가능한 코드 조회
    @GetMapping("/{cateNum}/available")
    public ResponseEntity<CodeResponses> readAvailable(@PathVariable String cateNum) {
        // 서비스를 호출하여 각 파트별 사용 가능한 코드를 조회한다
        CodeResponses responses = codeService.readAvailableCodeByCateNum(cateNum);
        // 조회된 코드를 반환한다
        return ResponseEntity.status(HttpStatus.OK)
                             .body(responses);
    }

    // 각 파트(cateNum)으로 모든 코드 조회
    @GetMapping("/{cateNum}/all")
    public ResponseEntity<CodeResponses> readAll(@PathVariable String cateNum) {
        // 서비스를 호출하여 각 파트별 모든 코드를 조회한다
        CodeResponses responses = codeService.readAllByCateNum(cateNum);
        // 조회된 코드를 반환한다
        return ResponseEntity.status(HttpStatus.OK)
                             .body(responses);
    }

    // 코드 수정
    @PatchMapping("/{codeSeq}")
    public ResponseEntity<Void> update(@PathVariable Long codeSeq, @Valid @RequestBody CodeUpdateRequest request) {
        // 서비스를 호출하여 코드를 수정한다
        codeService.update(codeSeq, request);
        // 수정된 코드를 반환한다
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                             .build();
    }

    // 코드 삭제
    @DeleteMapping("/{codeSeq}")
    public ResponseEntity<Void> delete(@PathVariable Long codeSeq) {
        // 서비스를 호출하여 코드를 삭제한다
        codeService.delete(codeSeq);
        // 삭제된 코드를 반환한다
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                             .build();
    }
}
