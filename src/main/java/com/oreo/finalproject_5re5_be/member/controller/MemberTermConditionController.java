package com.oreo.finalproject_5re5_be.member.controller;

import com.oreo.finalproject_5re5_be.member.dto.request.MemberTermConditionRequest;
import com.oreo.finalproject_5re5_be.member.dto.request.MemberTermConditionUpdateRequest;
import com.oreo.finalproject_5re5_be.member.dto.response.ErrorResponse;
import com.oreo.finalproject_5re5_be.member.dto.response.MemberTermConditionResponse;
import com.oreo.finalproject_5re5_be.member.dto.response.MemberTermConditionResponses;
import com.oreo.finalproject_5re5_be.member.exception.MemberInvalidTermConditionException;
import com.oreo.finalproject_5re5_be.member.exception.MemberTermsConditionNotFoundException;
import com.oreo.finalproject_5re5_be.member.service.MemberTermsConditionServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
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
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "MEMBER_TERM_CONDITION", description = "MEMBER_TERM_CONDITION 관련 API")
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/api/member-term-condition")
public class MemberTermConditionController {

    private final MemberTermsConditionServiceImpl memberTermConditionService;


//    @Operation(summary = "회원 약관 항목 등록 처리")
//    @PostMapping("/register")
    public ResponseEntity<MemberTermConditionResponse> register(@Valid @RequestBody MemberTermConditionRequest request) {
        // 단건 등록 처리
        MemberTermConditionResponse response = memberTermConditionService.create(request);
        // 응답 데이터 반환
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(response);
    }

//    @Operation(summary = "회원 약관 항목 여러개 등록 처리")
//    @PostMapping("/register-all")
    public ResponseEntity<MemberTermConditionResponses> register(@Valid @RequestBody List<MemberTermConditionRequest> requests) {
        // 여러건 등록 처리
        MemberTermConditionResponses response = memberTermConditionService.create(requests);
        // 응답 데이터 반환
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(response);
    }

//    @Operation(summary = "특정 회원 약관 항목 조회")
//    @GetMapping("/{condCode}")
    public ResponseEntity<MemberTermConditionResponse> read(@PathVariable("condCode") String condCode) {
        // 단건 조회 처리
        MemberTermConditionResponse response = memberTermConditionService.read(condCode);
        // 응답 데이터 반환
        return ResponseEntity.status(HttpStatus.OK)
                             .body(response);
    }

//    @Operation(summary = "모든 회원 약관 항목 조회")
//    @GetMapping("/all")
    public ResponseEntity<MemberTermConditionResponses> readAll() {
        // 모든 조회 처리
        MemberTermConditionResponses response = memberTermConditionService.readAll();
        // 응답 데이터 반환
        return ResponseEntity.status(HttpStatus.OK)
                             .body(response);
    }

//    @Operation(summary = "사용 가능한 약관 항목 조회")
//    @GetMapping("/available")
    public ResponseEntity<MemberTermConditionResponses> readAvailable() {
        // 사용 가능한 모든 조회 처리
        MemberTermConditionResponses response = memberTermConditionService.readAvailable();
        // 응답 데이터 반환
        return ResponseEntity.status(HttpStatus.OK)
                             .body(response);
    }

//    @Operation(summary = "사용 불가능한 약관 항목 조회")
//    @GetMapping("/not-available")
    public ResponseEntity<MemberTermConditionResponses> readNotAvailable() {
        // 사용 불가능한 모든 조회 처리
        MemberTermConditionResponses response = memberTermConditionService.readNotAvailable();
        // 응답 데이터 반환
        return ResponseEntity.status(HttpStatus.OK)
                             .body(response);
    }

//    @Operation(summary = "특정 약관 항목 수정")
//    @PatchMapping("/{condCode}")
    public ResponseEntity<Void> update(@PathVariable("condCode") String condCode, @RequestBody @Valid MemberTermConditionUpdateRequest request) {
        // 수정 처리
        memberTermConditionService.update(condCode, request);

        // 응답 데이터 반환
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                             .build();
    }

//    @Operation(summary = "특정 약관 항목 삭제")
//    @DeleteMapping("/{condCode}")
    public ResponseEntity<Void> remove(@PathVariable("condCode") String condCode) {
        // 삭제 처리
        memberTermConditionService.remove(condCode);

        // 응답 데이터 반환
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                             .build();
    }

}
