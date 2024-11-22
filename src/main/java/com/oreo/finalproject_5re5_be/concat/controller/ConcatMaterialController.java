package com.oreo.finalproject_5re5_be.concat.controller;

import com.oreo.finalproject_5re5_be.concat.dto.ConcatRowListDto;
import com.oreo.finalproject_5re5_be.concat.entity.ConcatRow;
import com.oreo.finalproject_5re5_be.concat.service.MaterialAudioService;
import com.oreo.finalproject_5re5_be.global.dto.response.ResponseDto;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/concat/audio/materials")
public class ConcatMaterialController {

    private final MaterialAudioService materialAudioService;

    public ConcatMaterialController(MaterialAudioService materialAudioService) {
        this.materialAudioService = materialAudioService;
    }

    // 재료 오디오 목록의 행 정보 불러오기
    @GetMapping("/rows")
    ResponseEntity<ResponseDto<ConcatRowListDto>> getMaterialRowListByResultSeq(
            @NotNull @RequestParam("concatresultseq") Long concatResultSeq
    ) {
        // resultSeq로 재료가 된 concatRowList 얻어오기
        List<ConcatRow> materialConcatRowList =  materialAudioService.findConcatRowListByResultSeq(concatResultSeq);

        // 응답
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto<>(
                        HttpStatus.OK.value(),
                        ConcatRowListDto.of(materialConcatRowList) // 조회 결과 응답 DTO로 변환
                ));
    }
}
