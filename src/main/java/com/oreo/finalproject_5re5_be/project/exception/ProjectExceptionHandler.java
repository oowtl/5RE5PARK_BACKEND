package com.oreo.finalproject_5re5_be.project.exception;

import com.oreo.finalproject_5re5_be.global.dto.response.ErrorResponseDto;
import com.oreo.finalproject_5re5_be.global.exception.BusinessException;
import com.oreo.finalproject_5re5_be.global.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "com.oreo.finalproject_5re5_be.project.controller")
public class ProjectExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDto> runtimeExceptionHandler(RuntimeException e) {
        log.error("[ProjectExceptionHandler] RuntimeException : ", e);

        ErrorResponseDto errorResponseDto = ErrorResponseDto.of(
                ErrorCode.INTERNAL_SERVER_ERROR.getStatus(),
                ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
        return ResponseEntity
                .status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(errorResponseDto);
    }
    // BusinessException 처리
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponseDto> businessExceptionHandler(BusinessException e) {
        log.error("[ProjectExceptionHandler] BusinessException : ", e);

        ErrorResponseDto errorResponseDto = ErrorResponseDto.of(e.getErrorCode().getStatus(),
                e.getMessage());
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(errorResponseDto);
    }
}
