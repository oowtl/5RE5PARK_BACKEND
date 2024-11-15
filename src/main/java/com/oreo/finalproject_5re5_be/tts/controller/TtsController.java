package com.oreo.finalproject_5re5_be.tts.controller;

import com.oreo.finalproject_5re5_be.global.dto.response.ResponseDto;
import com.oreo.finalproject_5re5_be.tts.dto.request.TtsSentenceCreateRequest;
import com.oreo.finalproject_5re5_be.tts.dto.response.TtsSentenceDto;
import com.oreo.finalproject_5re5_be.tts.exception.ErrorCode;
import com.oreo.finalproject_5re5_be.tts.exception.TtsErrorResponse;
import com.oreo.finalproject_5re5_be.tts.exception.TtsException;
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
    public ResponseEntity<TtsErrorResponse> runtimeExceptionHandler(RuntimeException e) {
        log.error("[RuntimeException]", e);

        TtsErrorResponse errorResponse = TtsErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR.getStatus(), ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
        return ResponseEntity
                .status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(errorResponse);
    }


    // TtsException 처리
    @ExceptionHandler(TtsException.class)
    public ResponseEntity<TtsErrorResponse> ttsExceptionHandler(TtsException e) {
        log.error("[TtsException]", e);

        TtsErrorResponse errorResponse = TtsErrorResponse.of(e.getErrorCode().getStatus(), e.getMessage());
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<TtsErrorResponse> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.error("[MethodArgumentNotValidException]", e);

        // bindingResult에 에러가 있는 경우
        if (e.getBindingResult().hasErrors()) {

            // fieldErrors 생성
            List<TtsErrorResponse.FieldErrorDetail> fieldErrors = e.getBindingResult().getFieldErrors().stream()
                    .map(error -> TtsErrorResponse.FieldErrorDetail.of(error.getField(), error.getDefaultMessage()))
                    .collect(Collectors.toList());

            // errorResponse 생성
            TtsErrorResponse errorResponse = TtsErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE.getStatus(), ErrorCode.INVALID_INPUT_VALUE.getMessage(), fieldErrors);

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(errorResponse);
        }

        // bindingResult에 에러가 없는 경우
        TtsErrorResponse errorResponse = TtsErrorResponse.of(HttpStatus.BAD_REQUEST.value(), "Invalid Request");
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<TtsErrorResponse> constraintViolationExceptionHandler(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();

        List<TtsErrorResponse.FieldErrorDetail> fieldErrors = constraintViolations.stream()
                .map(violation -> TtsErrorResponse.FieldErrorDetail.of(violation.getPropertyPath().toString(), violation.getMessage()))
                .collect(Collectors.toList());

        TtsErrorResponse errorResponse = TtsErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE.getStatus(), ErrorCode.INVALID_INPUT_VALUE.getMessage(), fieldErrors);
        return ResponseEntity
                .status(ErrorCode.INVALID_INPUT_VALUE.getStatus())
                .body(errorResponse);
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
