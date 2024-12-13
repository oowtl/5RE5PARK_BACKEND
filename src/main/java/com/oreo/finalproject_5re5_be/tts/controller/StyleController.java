package com.oreo.finalproject_5re5_be.tts.controller;

import com.oreo.finalproject_5re5_be.global.dto.response.ErrorResponseDto;
import com.oreo.finalproject_5re5_be.global.dto.response.ResponseDto;
import com.oreo.finalproject_5re5_be.global.exception.BusinessException;
import com.oreo.finalproject_5re5_be.global.exception.ErrorCode;
import com.oreo.finalproject_5re5_be.tts.dto.response.StyleListDto;
import com.oreo.finalproject_5re5_be.tts.service.StyleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "TTS-Voice", description = "Voice의 Style 관련 API")
@RestController
@RequestMapping("/api/style")
@Validated
public class StyleController {

    private final StyleService styleService;

    public StyleController(StyleService styleService) {
        this.styleService = styleService;
    }

    // 예외 핸들링
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDto> runtimeExceptionHandler(RuntimeException e) {
        log.error("[RuntimeException]", e);

        ErrorResponseDto errorResponseDto =
                ErrorResponseDto.of(
                        ErrorCode.INTERNAL_SERVER_ERROR.getStatus(),
                        ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
        return ResponseEntity.status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(errorResponseDto);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponseDto> ttsExceptionHandler(BusinessException e) {
        log.error("[BusinessException]", e);

        ErrorResponseDto errorResponseDto =
                ErrorResponseDto.of(e.getErrorCode().getStatus(), e.getMessage());
        return ResponseEntity.status(e.getErrorCode().getStatus()).body(errorResponseDto);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto> constraintViolationExceptionHandler(
            ConstraintViolationException e) {
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();

        List<ErrorResponseDto.FieldErrorDetail> fieldErrors =
                constraintViolations.stream()
                        .map(
                                violation ->
                                        ErrorResponseDto.FieldErrorDetail.of(
                                                violation.getPropertyPath().toString(), violation.getMessage()))
                        .collect(Collectors.toList());

        ErrorResponseDto errorResponseDto =
                ErrorResponseDto.of(
                        ErrorCode.INVALID_INPUT_VALUE.getStatus(),
                        ErrorCode.INVALID_INPUT_VALUE.getMessage(),
                        fieldErrors);
        return ResponseEntity.status(ErrorCode.INVALID_INPUT_VALUE.getStatus()).body(errorResponseDto);
    }

    @Operation(summary = "voice 스타일 전체 조회")
    @GetMapping("")
    public ResponseEntity<ResponseDto<StyleListDto>> getStyleList() {

        // 스타일 전체 조회 결과 가져오기
        StyleListDto styleListDto = styleService.getStyleList();

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto<>(HttpStatus.OK.value(), styleListDto));
    }

    @Operation(summary = "언어 코드로 voice가 있는 style만 조회")
    @Parameter(name = "languagecode", description = "언어 코드명을 작성해주세요")
    @GetMapping("/search")
    public ResponseEntity<ResponseDto<StyleListDto>> getStyleListByLang(
            @RequestParam("languagecode") @NotNull String langCode) {
        // langCode로 목소리가 존재하는 스타일 조회 결과 가져오기
        StyleListDto styleListDto = styleService.getStyleListByLang(langCode);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto<>(HttpStatus.OK.value(), styleListDto));
    }
}
