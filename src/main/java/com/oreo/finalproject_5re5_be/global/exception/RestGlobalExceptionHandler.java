package com.oreo.finalproject_5re5_be.global.exception;

import com.oreo.finalproject_5re5_be.global.dto.response.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestGlobalExceptionHandler {
    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ResponseDto<String>> handleDataNotFoundException(DataNotFoundException ex) {
        return new ResponseDto<>(HttpStatus.NOT_FOUND.value(), ex.getMessage()).toResponseEntity();
    }
}