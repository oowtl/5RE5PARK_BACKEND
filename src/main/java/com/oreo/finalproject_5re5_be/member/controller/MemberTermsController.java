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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Tag(name = "MEMBER_TERM", description = "MEMBER_TERM 관련 API")
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/api/member-term")
public class MemberTermsController {

    private final MemberTermsServiceImpl memberTermsService;

    @Operation(summary = "회원 약관 등록 처리")
    @PostMapping("/register")
    public ResponseEntity<MemberTermResponse> register(@Valid @RequestBody MemberTermRequest request) {
        // 회원 약관 등록 처리
        MemberTermResponse response = memberTermsService.create(request);
        // 등록된 회원 약관 반환
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(response);
    }

    @Operation(summary = "등록된 모든 회원 약관 조회")
    @GetMapping("/all")
    public ResponseEntity<MemberTermResponses> readAll() {
        // 모든 회원 약관에 대해 조회
        MemberTermResponses memberTermResponses = memberTermsService.readAll();
        // 조회된 회원 약관을 반환
        return ResponseEntity.status(HttpStatus.OK)
                             .body(memberTermResponses);
    }

    @Operation(summary = "등록된 모든 회원 약관 중 사용 가능한 모든 약관 조회")
    @GetMapping("/available")
    public ResponseEntity<MemberTermResponses> readAvailable() {
        // 사용 가능한 약관에 대해 조회
        MemberTermResponses memberTermResponses = memberTermsService.readAvailable();
        // 조회된 약관 반환
        return ResponseEntity.status(HttpStatus.OK)
                .body(memberTermResponses);
    }

    @Operation(summary = "등록된 모든 회원 약관 중 사용 불가능한 모든 약관 조회")
    @GetMapping("/not-available")
    public ResponseEntity<MemberTermResponses> readNotAvailable() {
        // 사용 불가능한 약관에 대해 조회
        MemberTermResponses memberTermResponses = memberTermsService.readNotAvailable();
        // 조회된 약관 반환
        return ResponseEntity.status(HttpStatus.OK)
                .body(memberTermResponses);
    }

    @Operation(summary = "등록된 모든 회원 약관 중 사용 불가능한 모든 약관 조회")
    @GetMapping("/latest-available")
    public ResponseEntity<MemberTermResponse> readLatestAvailable() {
        // 가장 최근에 사용 가능한 약관 조회
        MemberTermResponse response = memberTermsService.readLatestAvailable();
        // 조회된 약관 반환
        return ResponseEntity.status(HttpStatus.OK)
                             .body(response);
    }

    @Operation(summary = "등록된 회원 약관 수정 처리")
    @PatchMapping("/{termSeq}")
    public ResponseEntity<Void> update(@Parameter(description = "Member Term 시퀀스") @Min(value = 1L, message = "회원 약관의 시퀀스가 잘못됐습니다. 자동증분으로 관리되기 때문에 1부터 시작해야합니다.") @PathVariable("termSeq") Long termSeq, @Valid @RequestBody MemberTermUpdateRequest request) {
        // 수정 처리
        memberTermsService.update(termSeq, request);
        // 수정 성공시 응답 반환
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                             .build();
    }

    @Operation(summary = "등록된 회원 약관 삭제 처리")
    @DeleteMapping("/{termSeq}")
    public ResponseEntity<Void> remove(@Parameter(description = "Member Term 시퀀스") @Min(value = 1L, message = "회원 약관의 시퀀스가 잘못됐습니다. 자동증분으로 관리되기 때문에 1부터 시작해야합니다.") @PathVariable("termSeq") Long seq) {
        // 삭제 처리
        memberTermsService.remove(seq);
        // 삭제 성공시 응답 반환
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                             .build();
    }


}
