package com.oreo.finalproject_5re5_be.global.exception;

import com.oreo.finalproject_5re5_be.project.exception.InvalidProjectNameException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidProjectNameException.class)
    public ResponseEntity<String> handleInvalidProjectNameException(InvalidProjectNameException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
