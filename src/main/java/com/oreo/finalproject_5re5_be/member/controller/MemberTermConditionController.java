package com.oreo.finalproject_5re5_be.member.controller;

import com.oreo.finalproject_5re5_be.member.dto.request.MemberTermConditionRequest;
import com.oreo.finalproject_5re5_be.member.dto.request.MemberTermConditionUpdateRequest;
import com.oreo.finalproject_5re5_be.member.dto.response.ErrorResponse;
import com.oreo.finalproject_5re5_be.member.dto.response.MemberRegisterResponse;
import com.oreo.finalproject_5re5_be.member.dto.response.MemberTermConditionResponse;
import com.oreo.finalproject_5re5_be.member.dto.response.MemberTermConditionsResponse;
import com.oreo.finalproject_5re5_be.member.exception.MemberInvalidTermConditionException;
import com.oreo.finalproject_5re5_be.member.exception.MemberTermsConditionNotFoundException;
import com.oreo.finalproject_5re5_be.member.service.MemberTermsConditionServiceImpl;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/member-term-condition")
public class MemberTermConditionController {

    private final MemberTermsConditionServiceImpl memberTermConditionService;

    public MemberTermConditionController(MemberTermsConditionServiceImpl memberTermConditionService) {
        this.memberTermConditionService = memberTermConditionService;
    }

    @ExceptionHandler(
            MemberInvalidTermConditionException.class
    )
    public ResponseEntity<ErrorResponse> handleInvalidTermConditionException(RuntimeException e) {
        // 회원 약관 항목 등록 실패시 에러 메시지 반환
        // - 400 Bad Request 와 에러 메시지 반환
        ErrorResponse errorResponse = ErrorResponse.of(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(errorResponse);
    }

    @ExceptionHandler(
            MemberTermsConditionNotFoundException.class
    )
    public ResponseEntity<ErrorResponse> handleNotFoundException(RuntimeException e) {
        // 비즈니스 예외 발생시 에러 메시지 반환
        // - 404 Not Found 와 에러 메시지 반환
        ErrorResponse errorResponse = ErrorResponse.of(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }

    // 회원 약관 항목 등록 처리
    @PostMapping("/register")
    public ResponseEntity<MemberTermConditionResponse> register(@Valid @RequestBody MemberTermConditionRequest request, BindingResult result) {
        // 유효성 검증 실패시 에러 메시지 반환
        if (result.hasErrors()) {
            // 에러 메시지 생성
            String errorsMessage = createErrorMessage(result.getAllErrors());
            // 유효성 에러 반환
            throw new MemberInvalidTermConditionException(errorsMessage);
        }

        // 단건 등록 처리
        MemberTermConditionResponse response = memberTermConditionService.create(request);
        // 응답 데이터 반환
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(response);
    }

    // 회원 약관 항목 여러개 등록 처리
    @PostMapping("/register-all")
    public ResponseEntity<MemberTermConditionsResponse> register(@Valid @RequestBody List<MemberTermConditionRequest> requests, BindingResult result) {
        // 유효성 검증 실패시 에러 메시지 반환
        if (result.hasErrors()) {
            // 에러 메시지 생성
            String errorsMessage = createErrorMessage(result.getAllErrors());
            // 유효성 에러 반환
            throw new MemberInvalidTermConditionException(errorsMessage);
        }

        // 여러건 등록 처리
        MemberTermConditionsResponse response = memberTermConditionService.create(requests);
        // 응답 데이터 반환
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(response);
    }

    // 특정 회원 약관 항목 조회
    @GetMapping("/{condCode}")
    public ResponseEntity<MemberTermConditionResponse> read(@PathVariable("condCode") String condCode) {
        // 단건 조회 처리
        MemberTermConditionResponse response = memberTermConditionService.read(condCode);
        // 응답 데이터 반환
        return ResponseEntity.status(HttpStatus.OK)
                             .body(response);
    }

    // 모든 회원 약관 항목 조회
    @GetMapping("/all")
    public ResponseEntity<MemberTermConditionsResponse> readAll() {
        // 모든 조회 처리
        MemberTermConditionsResponse response = memberTermConditionService.readAll();
        // 응답 데이터 반환
        return ResponseEntity.status(HttpStatus.OK)
                             .body(response);
    }

    // 사용 가능한 약관 항목 조회
    @GetMapping("/available")
    public ResponseEntity<MemberTermConditionsResponse> readAvailable() {
        // 사용 가능한 모든 조회 처리
        MemberTermConditionsResponse response = memberTermConditionService.readAvailable();
        // 응답 데이터 반환
        return ResponseEntity.status(HttpStatus.OK)
                             .body(response);
    }

    // 사용 불가능한 약관 항목 조회
    @GetMapping("/not-available")
    public ResponseEntity<MemberTermConditionsResponse> readNotAvailable() {
        // 사용 불가능한 모든 조회 처리
        MemberTermConditionsResponse response = memberTermConditionService.readNotAvailable();
        // 응답 데이터 반환
        return ResponseEntity.status(HttpStatus.OK)
                             .body(response);
    }

    // 특정 약관 항목 수정
    @PatchMapping("/{condCode}")
    public ResponseEntity<Void> update(@PathVariable("condCode") String condCode, @RequestBody @Valid MemberTermConditionUpdateRequest request, BindingResult result) {
        // 유효성 검증 실패시 에러 메시지 반환
        if (result.hasErrors()) {
            // 에러 메시지 생성
            String errorsMessage = createErrorMessage(result.getAllErrors());
            // 유효성 에러 반환
            throw new MemberInvalidTermConditionException(errorsMessage);
        }

        // 수정 처리
        memberTermConditionService.update(condCode, request);

        // 응답 데이터 반환
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                             .build();
    }

    // 특정 약관 항목 삭제
    @DeleteMapping("/{condCode}")
    public ResponseEntity<Void> remove(@PathVariable("condCode") String condCode) {
        // 삭제 처리
        memberTermConditionService.remove(condCode);

        // 응답 데이터 반환
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                             .build();
    }


    // 유효성 검증 실패시 에러 메시지 생성
    private String createErrorMessage(List<ObjectError> errors) {
        // 문자열 연산 성능을 고려한 StringBuilder 사용
        StringBuilder sb = new StringBuilder();
        // 에러 메시지 생성
        sb.append("데이터 입력 형식이 잘못되었습니다. 상세 내용은 다음과 같습니다.")
                .append("\n");

        // 각 에러 객체를 조회하여 에러 메시지를 생성
        for (ObjectError error : errors) {
            sb.append(error.getDefaultMessage())
                    .append("\n");
        }

        // 문자열로 반환
        return sb.toString();
    }

}
