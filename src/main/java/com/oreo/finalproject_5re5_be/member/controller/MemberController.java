package com.oreo.finalproject_5re5_be.member.controller;

import com.oreo.finalproject_5re5_be.member.dto.request.MemberRegisterRequest;
import com.oreo.finalproject_5re5_be.member.exception.MemberDuplicatedEmailException;
import com.oreo.finalproject_5re5_be.member.exception.MemberDuplicatedIdException;
import com.oreo.finalproject_5re5_be.member.exception.MemberMandatoryTermNotAgreedException;
import com.oreo.finalproject_5re5_be.member.exception.MemberWrongCountTermCondition;
import com.oreo.finalproject_5re5_be.member.service.MemberServiceImpl;
import com.oreo.finalproject_5re5_be.member.validator.MemberValidator;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/member")
public class MemberController {

    private final MemberServiceImpl memberService;
    private final MemberValidator memberValidator;

    public MemberController(MemberServiceImpl memberService, MemberValidator memberValidator) {
        this.memberService = memberService;
        this.memberValidator = memberValidator;
    }

    @InitBinder("memberRegisterRequest")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(memberValidator);
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
    public ResponseEntity<String> register(@RequestBody @Valid MemberRegisterRequest request, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("데이터 입력 형식이 잘못되었습니다");
        }

        memberService.create(request);
        return ResponseEntity.ok().body("회원가입이 완료되었습니다");
    }

}
