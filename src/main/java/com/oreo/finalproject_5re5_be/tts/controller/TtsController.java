package com.oreo.finalproject_5re5_be.tts.controller;

import com.oreo.finalproject_5re5_be.global.dto.response.ErrorResponseDto;
import com.oreo.finalproject_5re5_be.global.dto.response.ResponseDto;
import com.oreo.finalproject_5re5_be.global.exception.BusinessException;
import com.oreo.finalproject_5re5_be.global.exception.ErrorCode;
import com.oreo.finalproject_5re5_be.project.service.ProjectService;
import com.oreo.finalproject_5re5_be.tts.dto.request.TtsSentenceBatchRequest;
import com.oreo.finalproject_5re5_be.tts.dto.request.TtsSentenceRequest;
import com.oreo.finalproject_5re5_be.tts.dto.response.TtsSentenceDto;
import com.oreo.finalproject_5re5_be.tts.dto.response.TtsSentenceListDto;
import com.oreo.finalproject_5re5_be.tts.service.TtsMakeService;
import com.oreo.finalproject_5re5_be.tts.service.TtsSentenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@Slf4j
@Tag(name = "TTS", description = "TTS 관련 API")
@Validated
@RestController
@RequestMapping("/api/project/{proSeq}/tts")
public class TtsController {

    private final TtsSentenceService ttsSentenceService;
    private final TtsMakeService ttsMakeService;
    private final ProjectService projectService;

    public TtsController(TtsSentenceService ttsSentenceService, TtsMakeService ttsMakeService,
        ProjectService projectService) {
        this.ttsSentenceService = ttsSentenceService;
        this.ttsMakeService = ttsMakeService;
        this.projectService = projectService;
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDto> runtimeExceptionHandler(RuntimeException e) {
        log.error("[RuntimeException]", e);

        ErrorResponseDto errorResponseDto = ErrorResponseDto.of(
            ErrorCode.INTERNAL_SERVER_ERROR.getStatus(),
            ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
        return ResponseEntity
            .status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
            .body(errorResponseDto);
    }


    // BusinessException 처리
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponseDto> ttsExceptionHandler(BusinessException e) {
        log.error("[BusinessException]", e);

        ErrorResponseDto errorResponseDto = ErrorResponseDto.of(e.getErrorCode().getStatus(),
            e.getMessage());
        return ResponseEntity
            .status(e.getErrorCode().getStatus())
            .body(errorResponseDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> methodArgumentNotValidExceptionHandler(
        MethodArgumentNotValidException e) {
        log.error("[MethodArgumentNotValidException]", e);

        // bindingResult에 에러가 있는 경우
        if (e.getBindingResult().hasErrors()) {

            // fieldErrors 생성
            List<ErrorResponseDto.FieldErrorDetail> fieldErrors = e.getBindingResult()
                .getFieldErrors().stream()
                .map(error -> ErrorResponseDto.FieldErrorDetail.of(error.getField(),
                    error.getDefaultMessage()))
                .toList();

            // errorResponseDto 생성
            ErrorResponseDto errorResponseDto = ErrorResponseDto.of(
                ErrorCode.INVALID_INPUT_VALUE.getStatus(),
                ErrorCode.INVALID_INPUT_VALUE.getMessage(), fieldErrors);

            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponseDto);
        }

        // bindingResult에 에러가 없는 경우
        ErrorResponseDto errorResponseDto = ErrorResponseDto.of(HttpStatus.BAD_REQUEST.value(),
            "Invalid Request");
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(errorResponseDto);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto> constraintViolationExceptionHandler(
        ConstraintViolationException e) {
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();

        List<ErrorResponseDto.FieldErrorDetail> fieldErrors = constraintViolations.stream()
            .map(violation -> ErrorResponseDto.FieldErrorDetail.of(
                violation.getPropertyPath().toString(), violation.getMessage()))
            .toList();

        ErrorResponseDto errorResponseDto = ErrorResponseDto.of(
            ErrorCode.INVALID_INPUT_VALUE.getStatus(), ErrorCode.INVALID_INPUT_VALUE.getMessage(),
            fieldErrors);
        return ResponseEntity
            .status(ErrorCode.INVALID_INPUT_VALUE.getStatus())
            .body(errorResponseDto);
    }

    @Operation(summary = "TTS 문장 생성 요청")
    @PostMapping("/sentence")
    public ResponseEntity<ResponseDto<TtsSentenceDto>> registerSentence(
        @Parameter(description = "Project ID") @Min(value = 1L, message = "projectSeq is invalid") @PathVariable Long proSeq,
        @Parameter(description = "tts 문장 생성 요청 body") @Valid @RequestBody TtsSentenceRequest createRequest,
        @SessionAttribute(value = "memberSeq") Long memberSeq) {
        //회원의 정보인지 확인
        projectService.projectCheck(memberSeq, proSeq);

        // 문장 생성
        TtsSentenceDto response = ttsSentenceService.addSentence(proSeq, createRequest);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(new ResponseDto<>(HttpStatus.CREATED.value(), response));
    }

    @Operation(summary = "TTS 문장 수정 요청")
    @PutMapping("/sentence/{tsSeq}")
    public ResponseEntity<ResponseDto<TtsSentenceDto>> updateSentence(
        @Parameter(description = "Project ID") @Min(value = 1L, message = "projectSeq is invalid") @PathVariable Long proSeq,
        @Parameter(description = "TTS 문장 ID") @Min(value = 1L, message = "tsSeq is invalid") @PathVariable Long tsSeq,
        @Parameter(description = "tts 문장 수정 요청 body") @Valid @RequestBody TtsSentenceRequest updateRequest) {

        // 문장 수정
        TtsSentenceDto response = ttsSentenceService.updateSentence(proSeq, tsSeq,
            updateRequest);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new ResponseDto<>(HttpStatus.OK.value(), response));
    }

    @Operation(summary = "TTS 현재 상태 저장 (저장 및 수정)", description = "TTS 문장 저장 시 정렬을 합니다. 순서 정보가 null 인 경우는 전부 뒤로 보낼 것이며, null 인 객체간의 순서는 보장되지 않습니다.")
    @PostMapping("/batch")
    public ResponseEntity<ResponseDto<TtsSentenceListDto>> batchSave(
        @Parameter(description = "Project ID") @Min(value = 1L, message = "projectSeq is invalid") @PathVariable Long proSeq,
        @Parameter(description = "tts 문장 생성 요청 body") @Valid @RequestBody TtsSentenceBatchRequest batchRequest) {

        // 문장 생성 및 수정
        TtsSentenceListDto response = ttsSentenceService.batchSaveSentence(proSeq,
            batchRequest);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new ResponseDto<>(HttpStatus.OK.value(), response));
    }

    @Operation(summary = "TTS 문장 조회 요청")
    @GetMapping("/sentence/{tsSeq}")
    public ResponseEntity<ResponseDto<TtsSentenceDto>> getSentence(
        @Parameter(description = "Project ID") @Min(value = 1L, message = "projectSeq is invalid") @PathVariable Long proSeq,
        @Parameter(description = "TTS 문장 ID") @Min(value = 1L, message = "tsSeq is invalid") @PathVariable Long tsSeq) {

        // 문장 조회
        TtsSentenceDto response = ttsSentenceService.getSentence(proSeq, tsSeq);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new ResponseDto<>(HttpStatus.OK.value(), response));
    }

    @Operation(summary = "프로젝트 TTS 조회")
    @GetMapping("")
    public ResponseEntity<ResponseDto<TtsSentenceListDto>> getSentenceList(
        @Parameter(description = "Project ID") @Min(value = 1L, message = "projectSeq is invalid") @PathVariable Long proSeq) {

        // 프로젝트 TTS 조회
        TtsSentenceListDto response = ttsSentenceService.getSentenceList(proSeq);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new ResponseDto<>(HttpStatus.OK.value(), response));
    }

    @Operation(summary = "TTS 생성 요청", description = "TTS 문장을 저장한 후 수행해주세요!")
    @GetMapping("/sentence/{tsSeq}/maketts")
    public ResponseEntity<ResponseDto<TtsSentenceDto>> makeTts(
        @Parameter(description = "TTS Sentence ID (문장 식별 번호)") @Min(value = 1L) @PathVariable Long tsSeq) {

        // tts 생성
        TtsSentenceDto ttsSentenceDto = ttsMakeService.makeTts(tsSeq);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                new ResponseDto<>(
                    HttpStatus.CREATED.value(),
                    ttsSentenceDto
                )
            );
    }

    @Operation(summary = "TTS 문장 삭제 요청")
    @DeleteMapping("/sentence/{tsSeq}")
    public ResponseEntity<ResponseDto<String>> deleteSentence(
        @Parameter(description = "Project ID") @Min(value = 1L, message = "projectSeq is invalid") @PathVariable Long proSeq,
        @Parameter(description = "TTS 문장 ID") @Min(value = 1L, message = "tsSeq is invalid") @PathVariable Long tsSeq) {

        // 문장 삭제
        boolean response = ttsSentenceService.deleteSentence(proSeq, tsSeq);

        // 응답 메시지
        String message = response ? "삭제 성공" : "삭제 실패";

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new ResponseDto<>(HttpStatus.OK.value(), message));
    }
}
