package com.oreo.finalproject_5re5_be.tts.controller;

import com.oreo.finalproject_5re5_be.global.dto.response.ResponseDto;
import com.oreo.finalproject_5re5_be.tts.dto.response.LanguageListDto;
import com.oreo.finalproject_5re5_be.tts.service.LanguageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "TTS-Voice", description = "Voice의 Language 관련 API")
@RestController
@RequestMapping("/api/language")
public class LanguageController {

    private final LanguageService languageService;

    public LanguageController(LanguageService languageService) {
        this.languageService = languageService;
    }

    @Operation(summary = "voice 언어 전체 조회")
    @GetMapping("")
    public ResponseEntity<ResponseDto<LanguageListDto>> getLanguageList() {

        // 언어 전체 조회 결과 가져오기
        LanguageListDto languageList = languageService.getLanguageList();

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto<>(HttpStatus.OK.value(), languageList));
    }
}
