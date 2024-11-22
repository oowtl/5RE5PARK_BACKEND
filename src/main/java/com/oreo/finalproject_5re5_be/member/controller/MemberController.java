package com.oreo.finalproject_5re5_be.member.controller;

import com.oreo.finalproject_5re5_be.member.dto.request.MemberRegisterRequest;
import com.oreo.finalproject_5re5_be.member.dto.request.MemberRemoveRequest;
import com.oreo.finalproject_5re5_be.member.dto.request.MemberUpdateRequest;
import com.oreo.finalproject_5re5_be.member.dto.response.MemberReadResponse;
import com.oreo.finalproject_5re5_be.member.dto.response.MemberRegisterResponse;
import com.oreo.finalproject_5re5_be.member.service.MemberServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "MEMBER", description = "MEMBER 관련 API")
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberServiceImpl memberService;

    @Operation(summary = "회원가입 처리")
    @PostMapping("/register")
    public ResponseEntity<MemberRegisterResponse> register(@Valid @RequestBody MemberRegisterRequest request) {
        // 회원가입 처리
        memberService.create(request);
        // 회원가입 완료 응답 생성
        MemberRegisterResponse response = MemberRegisterResponse.of("회원가입이 완료되었습니다");
        // 응답 반환
        return ResponseEntity.ok()
                             .body(response);
    }

    @Operation(summary = "이메일 인증 번호 발송 처리")
    @PostMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestBody String email) {
        // 인증번호 생성 및 유저에게 이메일 발송
        String verificationCode = memberService.sendVerificationCode(email);
        // 인증번호 반환
        return ResponseEntity.ok()
                             .body(verificationCode);
    }


    @Operation(summary = "회원 단순 조회 처리")
    @GetMapping("/read/{memberSeq}")
    public ResponseEntity<MemberReadResponse> read(@Parameter(description = "Member 시퀀스") @Min(value = 1L, message = "회원의 시퀀스가 잘못됐습니다. 자동증분으로 관리되기 때문에 1부터 시작해야합니다.") @PathVariable("memberSeq") Long memberSeq) {
        // 회원 조회
        MemberReadResponse response = memberService.read(memberSeq);
        // 응답 반환
        return ResponseEntity.ok()
                             .body(response);
    }

    @Operation(summary = "회원 수정 처리")
    @PatchMapping("/{memberSeq}")
    public ResponseEntity<Void> update(@Parameter(description = "Member 시퀀스") @Min(value = 1L, message = "회원의 시퀀스가 잘못됐습니다. 자동증분으로 관리되기 때문에 1부터 시작해야합니다.") @PathVariable("memberSeq") Long memberSeq, @Valid @RequestBody
            MemberUpdateRequest request) {
        // 수정 처리
        memberService.update(memberSeq, request);
        // 응답 반환
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                             .build();
    }

    @Operation(summary = "회원 삭제 처리")
    @DeleteMapping("/{memberSeq}")
    public ResponseEntity<Void> remove(@Parameter(description = "Member 시퀀스") @Min(value = 1L, message = "회원의 시퀀스가 잘못됐습니다. 자동증분으로 관리되기 때문에 1부터 시작해야합니다.") @PathVariable("memberSeq") Long memberSeq, @Valid @RequestBody MemberRemoveRequest request) {
        // 삭제 처리
        memberService.remove(memberSeq, request);
        // 응답 반환
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                             .build();
    }

}
