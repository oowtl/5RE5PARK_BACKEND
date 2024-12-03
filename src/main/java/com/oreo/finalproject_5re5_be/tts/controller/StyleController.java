package com.oreo.finalproject_5re5_be.tts.controller;

import com.oreo.finalproject_5re5_be.global.dto.response.ResponseDto;
import com.oreo.finalproject_5re5_be.tts.dto.response.StyleListDto;
import com.oreo.finalproject_5re5_be.tts.service.StyleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "TTS-Voice", description = "Voice의 Style 관련 API")
@RestController
@RequestMapping("/api/style")
public class StyleController {

    private final StyleService styleService;

    public StyleController(StyleService styleService) {
        this.styleService = styleService;
    }

    @Operation(summary = "voice 스타일 전체 조회")
    @GetMapping("")
    public ResponseEntity<ResponseDto<StyleListDto>> getStyleList() {

        // 스타일 전체 조회 결과 가져오기
        StyleListDto styleListDto = styleService.getStyleList();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto<>(HttpStatus.OK.value(), styleListDto));
    }

    @GetMapping(params = "languagecode")
    public ResponseEntity<ResponseDto<StyleListDto>> getStyleListByLang(
            @RequestParam("languagecode") String langCode
    ) {
        // langCode로 목소리가 존재하는 스타일 조회 결과 가져오기
        StyleListDto styleListDto = styleService.getStyleListByLang(langCode);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto<>(HttpStatus.OK.value(), styleListDto));
    }

}
