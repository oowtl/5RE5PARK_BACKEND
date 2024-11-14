package com.oreo.finalproject_5re5_be.member.controller;

import com.oreo.finalproject_5re5_be.member.dto.request.MemberTermConditionRequest;
import com.oreo.finalproject_5re5_be.member.dto.response.ErrorResponse;
import com.oreo.finalproject_5re5_be.member.dto.response.MemberRegisterResponse;
import com.oreo.finalproject_5re5_be.member.dto.response.MemberTermConditionResponse;
import com.oreo.finalproject_5re5_be.member.exception.MemberInvalidTermConditionException;
import com.oreo.finalproject_5re5_be.member.service.MemberTermsConditionServiceImpl;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
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

    @PostMapping("/register")
    public ResponseEntity<MemberTermConditionResponse> register(@Valid @RequestBody MemberTermConditionRequest request, BindingResult result) {
        // 유효성 검증 실패시 에러 메시지 반환
        if (result.hasErrors()) {
            // 에러 메시지 생성
            String errorsMessage = createErrorMessage(result.getAllErrors());
            // 유효성 에러 반환
            throw new MemberInvalidTermConditionException(errorsMessage);
        }

        MemberTermConditionResponse response = memberTermConditionService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(response);
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
