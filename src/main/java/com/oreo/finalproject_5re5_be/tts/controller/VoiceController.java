package com.oreo.finalproject_5re5_be.tts.controller;

import com.oreo.finalproject_5re5_be.global.dto.response.ErrorResponseDto;
import com.oreo.finalproject_5re5_be.global.dto.response.ResponseDto;
import com.oreo.finalproject_5re5_be.global.exception.BusinessException;
import com.oreo.finalproject_5re5_be.global.exception.ErrorCode;
import com.oreo.finalproject_5re5_be.tts.dto.response.VoiceListDto;
import com.oreo.finalproject_5re5_be.tts.service.VoiceService;
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

@Tag(name = "TTS-Voice", description = "Voice 관련 API")
@Slf4j
@RestController
@RequestMapping("/api/voice")
@Validated
public class VoiceController {
    private final VoiceService voiceService;

    public VoiceController(VoiceService voiceService) {
        this.voiceService = voiceService;
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

    // 보이스 조건 조회 컨트롤러
    @Operation(
            summary = "언어코드, 스타일명으로 목소리 조회",
            description = "DB에 저장된 언어 코드, 스타일 명을 쿼리스트링으로 반드시 전달해야합니다.")
    @GetMapping("")
    public ResponseEntity<ResponseDto<VoiceListDto>> getVoiceList(
            @Parameter(description = "언어 코드(언어 식별 번호가 아닙니다)") @RequestParam("languagecode") @NotNull
                    String langCode,
            @Parameter(description = "스타일명(스타일 식별 번호가 아닙니다)") @RequestParam("stylename") @NotNull
                    String styleName) {
        // 보이스 조회 결과 가져오기
        VoiceListDto voiceListDto = voiceService.getVoiceList(langCode, styleName);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto<>(HttpStatus.OK.value(), voiceListDto));
    }
}
