package com.oreo.finalproject_5re5_be.member.controller;

import com.oreo.finalproject_5re5_be.member.dto.request.MemberTermRequest;
import com.oreo.finalproject_5re5_be.member.dto.request.MemberTermUpdateRequest;
import com.oreo.finalproject_5re5_be.member.entity.MemberTerms;
import com.oreo.finalproject_5re5_be.member.exception.MemberTermsConditionNotFoundException;
import com.oreo.finalproject_5re5_be.member.exception.MemberTermsNotFoundException;
import com.oreo.finalproject_5re5_be.member.service.MemberTermsServiceImpl;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 추후에 응답 데이터 Response 만들어서 반환하도록 리팩토링
 * - 기본처리 틀 잡아 놓기
 */

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
    public ResponseEntity<String> handleNotFoundException(RuntimeException e) {
        // 비즈니스 예외 발생시 에러 메시지 반환
        // - 404 Not Found 와 에러 메시지 반환
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(e.getMessage());
    }

    // 등록
    @PostMapping("/register")
    public ResponseEntity<MemberTerms> register(@Valid @RequestBody MemberTermRequest request, BindingResult result) {
        // 유효성 검증 실패시 에러 메시지 반환
        if (result.hasErrors()) {
            // 에러 메시지 생성
            String errorMessage = createErrorMessage(result.getAllErrors());
            // 응답 데이터 반환
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(null);
        }

        // 회원 약관 생성 서비스 호출
        MemberTerms savedMemberTerms = memberTermsService.create(request);
        // 응답 데이터 반환
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(savedMemberTerms);
    }

    // 조회
    @GetMapping("/all")
    public ResponseEntity<List<MemberTerms>> readAll() {
        // 회원 약관 전체 조회 서비스 호출
        List<MemberTerms> memberTermsList = memberTermsService.readAll();
        // 응답 데이터 반환
        return ResponseEntity.status(HttpStatus.OK)
                             .body(memberTermsList);
    }

    @GetMapping("/available")
    public ResponseEntity<List<MemberTerms>> readAvailable() {
        // 사용 가능한 회원 약관 조회 서비스 호출
        List<MemberTerms> memberTermsList = memberTermsService.readAvailable();
        // 응답 데이터 반환
        return ResponseEntity.status(HttpStatus.OK)
                             .body(memberTermsList);
    }

    @GetMapping("/not-available")
    public ResponseEntity<List<MemberTerms>> readNotAvailable() {
        // 사용 불가능한 회원 약관 조회 서비스 호출
        List<MemberTerms> memberTermsList = memberTermsService.readNotAvailable();
        // 응답 데이터 반환
        return ResponseEntity.status(HttpStatus.OK)
                             .body(memberTermsList);
    }

    @GetMapping("/latest-available")
    public ResponseEntity<MemberTerms> readLatestAvailable() {
        // 최신 사용 가능한 회원 약관 조회 서비스 호출
        MemberTerms memberTerms = memberTermsService.readLatestAvailable();
        // 응답 데이터 반환
        return ResponseEntity.status(HttpStatus.OK)
                             .body(memberTerms);
    }

    // 수정
    @PatchMapping("/{termSeq}")
    public ResponseEntity<Void> update(@PathVariable("termSeq") Long seq, @Valid @RequestBody
            MemberTermUpdateRequest request, BindingResult result) {
        // 유효성 검증 실패시 에러 메시지 반환
        if (result.hasErrors()) {
            // 에러 메시지 생성
            String errorMessage = createErrorMessage(result.getAllErrors());
            // 응답 데이터 반환
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }

        // 회원 약관 수정 서비스 호출
        memberTermsService.update(seq, request);

        // 응답 데이터 반환
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                             .build();
    }

    // 삭제
    @DeleteMapping("/{termSeq}")
    public ResponseEntity<Void> remove(@PathVariable("termSeq") Long seq) {
        // 회원 약관 삭제 서비스 호출
        memberTermsService.remove(seq);
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
