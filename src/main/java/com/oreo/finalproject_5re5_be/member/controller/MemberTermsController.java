package com.oreo.finalproject_5re5_be.member.controller;

import com.oreo.finalproject_5re5_be.member.dto.request.MemberTermRequest;
import com.oreo.finalproject_5re5_be.member.dto.request.MemberTermUpdateRequest;
import com.oreo.finalproject_5re5_be.member.dto.response.ErrorResponse;
import com.oreo.finalproject_5re5_be.member.dto.response.MemberTermResponse;
import com.oreo.finalproject_5re5_be.member.dto.response.MemberTermResponses;
import com.oreo.finalproject_5re5_be.member.entity.MemberTerms;
import com.oreo.finalproject_5re5_be.member.exception.MemberTermInvalidException;
import com.oreo.finalproject_5re5_be.member.exception.MemberTermsConditionNotFoundException;
import com.oreo.finalproject_5re5_be.member.exception.MemberTermsNotFoundException;
import com.oreo.finalproject_5re5_be.member.service.MemberTermsServiceImpl;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/member-term")
public class MemberTermsController {

    private final MemberTermsServiceImpl memberTermsService;

    public MemberTermsController(MemberTermsServiceImpl memberTermsService) {
        this.memberTermsService = memberTermsService;
    }

    // 예외처리 핸들링
    @ExceptionHandler({
            MemberTermsNotFoundException.class,
            MemberTermsConditionNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFoundException(RuntimeException e) {
        ErrorResponse errorResponse = ErrorResponse.of(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(errorResponse);
    }

    @ExceptionHandler({
            MemberTermInvalidException.class,
            ConstraintViolationException.class
    })
    public ResponseEntity<ErrorResponse> handleInvalidException(RuntimeException e) {
        ErrorResponse errorResponse = ErrorResponse.of(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(errorResponse);
    }

    // 등록
    @PostMapping("/register")
    public ResponseEntity<MemberTermResponse> register(@Valid @RequestBody MemberTermRequest request, BindingResult result) {
        // 유효성 검증 실패시 에러 메시지 반환
        if (result.hasErrors()) {
            // 에러 메시지 생성
            String errorMessage = createErrorMessage(result.getAllErrors());
            // 유효성 에러 반환
            throw new MemberTermInvalidException(errorMessage);
        }

        // 회원 약관 등록 처리
        MemberTermResponse response = memberTermsService.create(request);

        // 등록된 회원 약관 반환
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(response);
    }

    // 조회
    @GetMapping("/all")
    public ResponseEntity<MemberTermResponses> readAll() {
        // 모든 회원 약관에 대해 조회
        MemberTermResponses memberTermResponses = memberTermsService.readAll();
        // 조회된 회원 약관을 반환
        return ResponseEntity.status(HttpStatus.OK)
                             .body(memberTermResponses);
    }

    @GetMapping("/available")
    public ResponseEntity<MemberTermResponses> readAvailable() {
        // 사용 가능한 약관에 대해 조회
        MemberTermResponses memberTermResponses = memberTermsService.readAvailable();

        // 조회된 약관 반환
        return ResponseEntity.status(HttpStatus.OK)
                .body(memberTermResponses);
    }

    @GetMapping("/not-available")
    public ResponseEntity<MemberTermResponses> readNotAvailable() {
        // 사용 불가능한 약관에 대해 조회
        MemberTermResponses memberTermResponses = memberTermsService.readNotAvailable();

        // 조회된 약관 반환
        return ResponseEntity.status(HttpStatus.OK)
                .body(memberTermResponses);
    }

    @GetMapping("/latest-available")
    public ResponseEntity<MemberTermResponse> readLatestAvailable() {
        // 가장 최근에 사용 가능한 약관 조회
        MemberTermResponse response = memberTermsService.readLatestAvailable();

        // 조회된 약관 반환
        return ResponseEntity.status(HttpStatus.OK)
                             .body(response);
    }

    // 수정
    @PatchMapping("/{termSeq}")
    public ResponseEntity<Void> update(@PathVariable("termSeq") Long seq, @Valid @RequestBody
            MemberTermUpdateRequest request, BindingResult result) {
        // 유효성 검증 실패시 에러 메시지 반환

        // 수정 처리
        memberTermsService.update(seq, request);

        // 수정 성공시 응답 반환
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                             .build();
    }

    // 삭제
    @DeleteMapping("/{termSeq}")
    public ResponseEntity<Void> remove(@PathVariable("termSeq") Long seq) {
        // 삭제 처리
        memberTermsService.remove(seq);
        // 삭제 성공시 응답 반환
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
