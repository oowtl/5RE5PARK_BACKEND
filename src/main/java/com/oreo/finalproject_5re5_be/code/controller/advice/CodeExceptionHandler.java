package com.oreo.finalproject_5re5_be.code.controller.advice;

import static com.oreo.finalproject_5re5_be.global.exception.ErrorCode.CODE_INVALID_INPUT_VALUE_ERROR;
import static com.oreo.finalproject_5re5_be.global.exception.ErrorCode.INTERNAL_SERVER_ERROR;

import com.oreo.finalproject_5re5_be.global.dto.response.ErrorResponseDto;
import com.oreo.finalproject_5re5_be.global.dto.response.ErrorResponseDto.FieldErrorDetail;
import com.oreo.finalproject_5re5_be.global.exception.BusinessException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "com.oreo.finalproject_5re5_be.member.controller")
public class CodeExceptionHandler {

    // 코드 파트에서 RuntimeException이 발생한 경우
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDto> handleRuntimeException(RuntimeException e) {
        // 로그로 어떤 예외 클래스가 발생했는지 표시
        log.error("CODE : [RuntimeException] ", e);

        // 응답 데이터 생성
        ErrorResponseDto response = ErrorResponseDto.of(INTERNAL_SERVER_ERROR.getStatus(),
                e.getMessage());

        // 응답 데이터 반환
        return ResponseEntity.status(response.getStatus())
                .body(response);

    }


    // 코드 파트에서 BusinessException이 발생한 경우
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponseDto> handleBusinessException(BusinessException e) {
        // 로그로 어떤 예외 클래스가 발생했는지 표시
        log.error("CODE : [BusinessException] ", e);

        // 응답 데이터 생성
        ErrorResponseDto response = ErrorResponseDto.of(e.getErrorCode().getStatus(),
                e.getMessage());

        // 응답 데이터 반환
        return ResponseEntity.status(response.getStatus())
                .body(response);
    }


    // 코드 파트에서 MethodArgumentNotValidException이 발생한 경우 - 데이터 유효성 검증 실패
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        // 로그로 어떤 예외 클래스가 발생했는지 표시
        log.error("CODE : [MethodArgumentNotValidException] ", e);

        // 응답 데이터 생성
        // BindingResult 조회
        BindingResult result = e.getBindingResult();

        // 필드 에러 리스트 형태로 변환
        List<FieldErrorDetail> fieldErrorList = result.getFieldErrors()
                .stream()
                .map(error -> FieldErrorDetail.of(error.getField(), error.getDefaultMessage()))
                .toList();

        // ErrorResponseDto 생성
        ErrorResponseDto response = ErrorResponseDto.of(CODE_INVALID_INPUT_VALUE_ERROR.getStatus(),
                                                        CODE_INVALID_INPUT_VALUE_ERROR.getMessage(),
                                                        fieldErrorList);

        // 응답 데이터 반환
        return ResponseEntity.status(response.getStatus())
                .body(response);
    }


    // 코드 파트에서 ConstraintViolationException 발생한 경우 - 데이터베이스 제약 조건 위반할 경우 발생(JPA/Hibernate)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleConstraintViolationException(ConstraintViolationException e) {
        // 로그로 어떤 예외 클래스가 발생했는지 표시
        log.error("CODE : [ConstraintViolationException] ", e);

        // 각 violation에 접근하여 에러 내용 상세화한 Set 생성
        Set<ConstraintViolation<?>> constraintViolationSet = e.getConstraintViolations();

        List<FieldErrorDetail> violationList = constraintViolationSet.stream()
                .map(violation -> FieldErrorDetail.of(violation.getPropertyPath().toString(), violation.getMessage()))
                .toList();

        // ErrorResponseDto 생성
        ErrorResponseDto response = ErrorResponseDto.of(CODE_INVALID_INPUT_VALUE_ERROR.getStatus(),
                                                        CODE_INVALID_INPUT_VALUE_ERROR.getMessage(),
                                                        violationList);


        // 응답 데이터 반환
        return ResponseEntity.status(CODE_INVALID_INPUT_VALUE_ERROR.getStatus())
                .body(response);
    }
}
