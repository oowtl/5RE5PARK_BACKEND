package com.oreo.finalproject_5re5_be.tts.controller;

import com.oreo.finalproject_5re5_be.global.dto.response.ErrorResponseDto;
import com.oreo.finalproject_5re5_be.global.dto.response.ResponseDto;
import com.oreo.finalproject_5re5_be.global.exception.BusinessException;
import com.oreo.finalproject_5re5_be.tts.dto.request.TtsSentenceCreateRequest;
import com.oreo.finalproject_5re5_be.tts.dto.response.TtsSentenceDto;
import com.oreo.finalproject_5re5_be.global.exception.ErrorCode;
import com.oreo.finalproject_5re5_be.tts.service.TtsSentenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Tag(name = "TTS", description = "TTS 관련 API")
@Validated
@RestController
@RequestMapping("/api/project/{projectSeq}/tts")
public class TtsController {
    private final TtsSentenceService ttsSentenceService;

    public TtsController(TtsSentenceService ttsSentenceService) {
        this.ttsSentenceService = ttsSentenceService;
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDto> runtimeExceptionHandler(RuntimeException e) {
        log.error("[RuntimeException]", e);

        ErrorResponseDto errorResponseDto = ErrorResponseDto.of(ErrorCode.INTERNAL_SERVER_ERROR.getStatus(), ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
        return ResponseEntity
                .status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(errorResponseDto);
    }


    // BusinessException 처리
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponseDto> ttsExceptionHandler(BusinessException e) {
        log.error("[BusinessException]", e);

        ErrorResponseDto errorResponseDto = ErrorResponseDto.of(e.getErrorCode().getStatus(), e.getMessage());
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(errorResponseDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.error("[MethodArgumentNotValidException]", e);

        // bindingResult에 에러가 있는 경우
        if (e.getBindingResult().hasErrors()) {

            // fieldErrors 생성
            List<ErrorResponseDto.FieldErrorDetail> fieldErrors = e.getBindingResult().getFieldErrors().stream()
                    .map(error -> ErrorResponseDto.FieldErrorDetail.of(error.getField(), error.getDefaultMessage()))
                    .collect(Collectors.toList());

            // errorResponseDto 생성
            ErrorResponseDto errorResponseDto = ErrorResponseDto.of(ErrorCode.INVALID_INPUT_VALUE.getStatus(), ErrorCode.INVALID_INPUT_VALUE.getMessage(), fieldErrors);

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(errorResponseDto);
        }

        // bindingResult에 에러가 없는 경우
        ErrorResponseDto errorResponseDto = ErrorResponseDto.of(HttpStatus.BAD_REQUEST.value(), "Invalid Request");
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponseDto);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto> constraintViolationExceptionHandler(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();

        List<ErrorResponseDto.FieldErrorDetail> fieldErrors = constraintViolations.stream()
                .map(violation -> ErrorResponseDto.FieldErrorDetail.of(violation.getPropertyPath().toString(), violation.getMessage()))
                .collect(Collectors.toList());

        ErrorResponseDto errorResponseDto = ErrorResponseDto.of(ErrorCode.INVALID_INPUT_VALUE.getStatus(), ErrorCode.INVALID_INPUT_VALUE.getMessage(), fieldErrors);
        return ResponseEntity
                .status(ErrorCode.INVALID_INPUT_VALUE.getStatus())
                .body(errorResponseDto);
    }

    @Operation(summary = "TTS 문장 생성 요청")
    @PostMapping("/sentence")
    public ResponseEntity<ResponseDto<TtsSentenceDto>> registerSentence(
            @Parameter(description = "Project ID") @Min(value = 1L, message = "projectSeq is invalid") @PathVariable Long projectSeq,
            @Parameter(description = "tts 문장 생성 요청 body") @Valid @RequestBody TtsSentenceCreateRequest createRequest) {

        // 문장 생성
        TtsSentenceDto response = ttsSentenceService.addSentence(projectSeq, createRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto<>(HttpStatus.CREATED.value(), response));
    }
}
