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
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
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

    @ExceptionHandler({
            RetryFailedException.class
    })
    public ResponseEntity<String> handleRetryFailedException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    @ExceptionHandler({
            MemberDuplicatedEmailException.class,
            MemberDuplicatedIdException.class,
            MemberMandatoryTermNotAgreedException.class,
            MemberWrongCountTermCondition.class
    })
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @PostMapping("/register")
    public ResponseEntity<MemberRegisterResponse> register(@Valid @RequestBody MemberRegisterRequest memberRegisterRequest, BindingResult result) {
        if (result.hasErrors()) {
            String errorsMessage = createErrorMessage(result.getAllErrors());
            MemberRegisterResponse response = MemberRegisterResponse.of(errorsMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(response);
        }

        memberService.RetryableCreateMember(memberRegisterRequest);
        MemberRegisterResponse response = MemberRegisterResponse.of("회원가입이 완료되었습니다");
        return ResponseEntity.ok()
                             .body(response);
    }

    private String createErrorMessage(List<ObjectError> errors) {
        StringBuilder sb = new StringBuilder();
        sb.append("데이터 입력 형식이 잘못되었습니다. 상세 내용은 다음과 같습니다.")
                .append("\n");

        for (ObjectError error : errors) {
            sb.append(error.getDefaultMessage())
                    .append("\n");
        }

        return sb.toString();
    }

}
