package com.oreo.finalproject_5re5_be.code.controller;

import com.oreo.finalproject_5re5_be.code.dto.request.CodeRequest;
import com.oreo.finalproject_5re5_be.code.dto.request.CodeUpdateRequest;
import com.oreo.finalproject_5re5_be.code.dto.response.CodeResponse;
import com.oreo.finalproject_5re5_be.code.dto.response.CodeResponses;
import com.oreo.finalproject_5re5_be.code.service.CodeServiceImpl;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "CODE", description = "CODE 관련 API")
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/api/code")
public class CodeController {

    private final CodeServiceImpl codeService;

//    @Operation(summary = "코드 등록")
//    @PostMapping("/register")
    public ResponseEntity<CodeResponse> create(@RequestBody @Valid CodeRequest request) {
        // 서비스를 호출하여 코드를 등록한다
        CodeResponse response = codeService.create(request);
        // 등록된 코드를 반환한다
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(response);

    }

//    @Operation(summary = "등록된 모든 코드 조회")
//    @GetMapping("/all")
    public ResponseEntity<CodeResponses> readAll() {
        // 서비스를 호출하여 모든 코드를 조회한다
        CodeResponses responses = codeService.readAll();
        // 조회된 코드를 반환한다
        return ResponseEntity.status(HttpStatus.OK)
                             .body(responses);
    }

//    @Operation(summary = "시퀀스로 특정 코드 조회")
//    @GetMapping("/seq/{codeSeq}")
    public ResponseEntity<CodeResponse> readBySeq(@Parameter(description = "Code 시퀀스") @Min(value = 1L, message = "코드 시퀀스가 잘못됐습니다. 자동증분으로 관리되기 때문에 1부터 시작해야합니다.") @PathVariable("codeSeq") Long codeSeq) {
        // 서비스를 호출하여 시퀀스로 특정 코드를 조회한다
        CodeResponse response = codeService.read(codeSeq);
        // 조회된 코드를 반환한다
        return ResponseEntity.status(HttpStatus.OK)
                             .body(response);
    }

//    @Operation(summary = "코드 번호로 특정 코드 조회")
//    @GetMapping("/{code}")
    public ResponseEntity<CodeResponse> readByCode(@Parameter(description = "Code의 코드번호") @PathVariable("code") String code) {
        // 서비스를 호출하여 코드 번호로 특정 코드를 조회한다
        CodeResponse response = codeService.read(code);
        // 조회된 코드를 반환한다
        return ResponseEntity.status(HttpStatus.OK)
                             .body(response);
    }

//    @Operation(summary = "각 파트(cateNum)으로 사용 가능한 코드 조회")
//    @GetMapping("/{cateNum}/available")
    public ResponseEntity<CodeResponses> readAvailable(@Parameter(description = "Code의 파트별 번호") @PathVariable("cateNum") String cateNum) {
        // 서비스를 호출하여 각 파트별 사용 가능한 코드를 조회한다
        CodeResponses responses = codeService.readAvailableCodeByCateNum(cateNum);
        // 조회된 코드를 반환한다
        return ResponseEntity.status(HttpStatus.OK)
                             .body(responses);
    }

//    @Operation(summary = "각 파트(cateNum)으로 모든 코드 조회")
//    @GetMapping("/{cateNum}/all")
    public ResponseEntity<CodeResponses> readAll(@Parameter(description = "Code의 파트별 번호") @PathVariable("cateNum") String cateNum) {
        // 서비스를 호출하여 각 파트별 모든 코드를 조회한다
        CodeResponses responses = codeService.readAllByCateNum(cateNum);
        // 조회된 코드를 반환한다
        return ResponseEntity.status(HttpStatus.OK)
                             .body(responses);
    }

//    @Operation(summary = "특정 코드 수정 처리")
//    @PatchMapping("/{codeSeq}")
    public ResponseEntity<Void> update(@Parameter(description = "Code 시퀀스") @Min(value = 1L, message = "코드 시퀀스가 잘못됐습니다. 자동증분으로 관리되기 때문에 1부터 시작해야합니다.") @PathVariable("codeSeq") Long codeSeq, @Valid @RequestBody CodeUpdateRequest request) {
        // 서비스를 호출하여 코드를 수정한다
        codeService.update(codeSeq, request);
        // 수정된 코드를 반환한다
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                             .build();
    }

//    @Operation(summary = "특정 코드 삭제 처리")
//    @DeleteMapping("/{codeSeq}")
    public ResponseEntity<Void> delete(@Parameter(description = "Code 시퀀스") @Min(value = 1L, message = "코드 시퀀스가 잘못됐습니다. 자동증분으로 관리되기 때문에 1부터 시작해야합니다.") @PathVariable("codeSeq")  Long codeSeq) {
        // 서비스를 호출하여 코드를 삭제한다
        codeService.delete(codeSeq);
        // 삭제된 코드를 반환한다
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                             .build();
    }
}
