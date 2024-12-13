package com.oreo.finalproject_5re5_be.project.exception;

import com.oreo.finalproject_5re5_be.global.dto.response.ResponseDto;
import com.oreo.finalproject_5re5_be.global.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(value = "com.oreo.finalproject_5re5_be.project")
public class ProjectExceptionHandler {
    @ExceptionHandler(value = Exception.class) // 어떤 예외클래스를 처리할건지 지정
    public ResponseEntity<ResponseDto<String>> handle(Exception e, HttpServletRequest request) {
        log.error(
                "VC RestController1 내 handlerException 호출 , {} , {}",
                request.getRequestURI(),
                e.getMessage());
        return ResponseEntity.badRequest()
                .body(new ResponseDto<>(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }

    @ExceptionHandler(value = BusinessException.class) // 어떤 예외클래스를 처리할건지 지정
    public ResponseEntity<ResponseDto<String>> handle2(Exception e, HttpServletRequest request) {
        log.error(
                "VC RestController2 내 handlerException 호출 , {} , {}",
                request.getRequestURI(),
                e.getMessage());
        return ResponseEntity.badRequest()
                .body(new ResponseDto<>(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }
}
