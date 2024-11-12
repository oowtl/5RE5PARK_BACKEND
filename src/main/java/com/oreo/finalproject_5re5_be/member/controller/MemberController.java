package com.oreo.finalproject_5re5_be.member.controller;

import com.oreo.finalproject_5re5_be.member.dto.request.MemberRegisterRequest;
import com.oreo.finalproject_5re5_be.member.dto.response.MemberRegisterResponse;
import com.oreo.finalproject_5re5_be.member.exception.MemberDuplicatedEmailException;
import com.oreo.finalproject_5re5_be.member.exception.MemberDuplicatedIdException;
import com.oreo.finalproject_5re5_be.member.exception.MemberMandatoryTermNotAgreedException;
import com.oreo.finalproject_5re5_be.member.exception.MemberWrongCountTermCondition;
import com.oreo.finalproject_5re5_be.member.exception.RetryFailedException;
import com.oreo.finalproject_5re5_be.member.service.MemberServiceImpl;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/member")
public class MemberController {

    private final MemberServiceImpl memberService;

    public MemberController(MemberServiceImpl memberService) {
        this.memberService = memberService;
    }

    // 재시도 복구 실패할 경우 예외 처리 핸들러
    @ExceptionHandler({
            RetryFailedException.class
    })
    public ResponseEntity<String> handleRetryFailedException(Exception e) {
        // 재시도 복구 실패시 에러 메시지 반환
        // - 500 Internal Server Error 와 에러 메시지 반환
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(e.getMessage());
    }

    // 회원 파트에서 발생한 비즈니스 예외 처리 핸들러
    @ExceptionHandler({
            MemberDuplicatedEmailException.class,
            MemberDuplicatedIdException.class,
            MemberMandatoryTermNotAgreedException.class,
            MemberWrongCountTermCondition.class,
            UsernameNotFoundException.class
    })
    public ResponseEntity<String> handleException(Exception e) {
        // 비즈니스 예외 발생시 에러 메시지 반환
        // - 400 Bad Request 와 에러 메시지 반환
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(e.getMessage());
    }

    // 회원가입 처리
    @PostMapping("/register")
    public ResponseEntity<MemberRegisterResponse> register(@Valid @RequestBody MemberRegisterRequest memberRegisterRequest, BindingResult result) {
        // 유효성 검증 실패시 에러 메시지 반환
        if (result.hasErrors()) {
            // 에러 메시지 생성
            String errorsMessage = createErrorMessage(result.getAllErrors());
            // 회원가입 실패 응답 생성
            MemberRegisterResponse response = MemberRegisterResponse.of(errorsMessage);
            // 응답 반환
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(response);
        }

        // 회원가입 처리
        memberService.create(memberRegisterRequest);
        // 회원가입 완료 응답 생성
        MemberRegisterResponse response = MemberRegisterResponse.of("회원가입이 완료되었습니다");
        // 응답 반환
        return ResponseEntity.ok()
                             .body(response);
    }

    @GetMapping("/verify/email")
    public ResponseEntity<String> verifyEmail(String email) {
        memberService.sendVerificationCode(email);
        return null;
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
