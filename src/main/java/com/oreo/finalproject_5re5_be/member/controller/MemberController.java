package com.oreo.finalproject_5re5_be.member.controller;

import com.oreo.finalproject_5re5_be.member.dto.request.MemberRegisterRequest;
import com.oreo.finalproject_5re5_be.member.dto.response.ErrorResponse;
import com.oreo.finalproject_5re5_be.member.dto.response.MemberReadResponse;
import com.oreo.finalproject_5re5_be.member.dto.response.MemberRegisterResponse;
import com.oreo.finalproject_5re5_be.member.exception.MemberDuplicatedEmailException;
import com.oreo.finalproject_5re5_be.member.exception.MemberDuplicatedIdException;
import com.oreo.finalproject_5re5_be.member.exception.MemberMandatoryTermNotAgreedException;
import com.oreo.finalproject_5re5_be.member.exception.MemberNotFoundException;
import com.oreo.finalproject_5re5_be.member.exception.MemberWrongCountTermCondition;
import com.oreo.finalproject_5re5_be.member.exception.RetryFailedException;
import com.oreo.finalproject_5re5_be.member.service.MemberServiceImpl;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
            RetryFailedException.class,
            MailSendException.class
    })
    public ResponseEntity<ErrorResponse> handleInternalServerException(RuntimeException e) {
        // 재시도 복구 실패시 에러 메시지 반환
        // - 500 Internal Server Error 와 에러 메시지 반환
        ErrorResponse errorResponse = ErrorResponse.of(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(errorResponse);
    }

    @ExceptionHandler({
            MemberNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFoundException(RuntimeException e) {
        // 비즈니스 예외 발생시 에러 메시지 반환
        // - 404 Not Found 와 에러 메시지 반환
        ErrorResponse errorResponse = ErrorResponse.of(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(errorResponse);
    }

    // 회원 파트에서 발생한 비즈니스 예외 처리 핸들러
    @ExceptionHandler({
            MemberDuplicatedEmailException.class,
            MemberDuplicatedIdException.class,
            MemberMandatoryTermNotAgreedException.class,
            MemberWrongCountTermCondition.class,
            UsernameNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleWrongRequestException(Exception e) {
        // 비즈니스 예외 발생시 에러 메시지 반환
        // - 400 Bad Request 와 에러 메시지 반환
        ErrorResponse errorResponse = ErrorResponse.of(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(errorResponse);
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

    // 이메일 인증 번호 발송 처리
    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestBody String email) {
        // 인증번호 생성 및 유저에게 이메일 발송
        String verificationCode = memberService.sendVerificationCode(email);
        // 인증번호 반환
        return ResponseEntity.ok()
                             .body(verificationCode);
    }

    // 회원 단순 조회 처리
    @GetMapping("/read/{memberId}")
    public ResponseEntity<MemberReadResponse> read(@PathVariable("memberId") String memberId) {
        // 회원 조회
        MemberReadResponse response = memberService.read(memberId);
        // 응답 반환
        return ResponseEntity.ok()
                             .body(response);
    }

    // 회원 상세 조회 처리
    // 회원 프로필 정보 조회 처리(회원 단순 조회, 회원 프로젝트 내역 조회)


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
