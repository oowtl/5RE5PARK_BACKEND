package com.oreo.finalproject_5re5_be.concat.controller;

import com.oreo.finalproject_5re5_be.concat.dto.request.ConcatResultRequest;
import com.oreo.finalproject_5re5_be.concat.dto.response.ConcatUrlResponse;
import com.oreo.finalproject_5re5_be.concat.service.ConcatResultService;
import com.oreo.finalproject_5re5_be.global.component.S3Service;
import com.oreo.finalproject_5re5_be.global.dto.response.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

@Tag(name = "Concat", description = "Concat 관련 API")
@RestController("api/concat/audio/result")
public class ConcatResultController {

    private final ConcatResultService concatResultService;
    private final S3Service s3Service;

    public ConcatResultController(ConcatResultService concatResultService,
                                  S3Service s3Service) {
        this.concatResultService = concatResultService;
        this.s3Service = s3Service;
    }

    @Operation(summary = "Concat 결과 저장",
            description = "결과 파일을 S3와 DB에 저장합니다.")
    @PostMapping(value = "save",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDto<ConcatUrlResponse>> save(
            @RequestParam("resultFile") MultipartFile resultFile,
            @RequestBody ConcatResultRequest request) throws IOException, UnsupportedAudioFileException {

        try {
            // S3에 파일 저장
            String s3Url = s3Service.upload(resultFile, "concat/result");

            // 저장된 URL을 요청 객체에 추가
            request.setResultUrl(s3Url);

            // DB 저장
            ConcatUrlResponse response = concatResultService.saveConcatResult(request);

            return new ResponseDto<>(HttpStatus.OK.value(), response).toResponseEntity();

        } catch (Exception e) { // 실패 시 처리
            // 실패 응답으로 빈 ConcatUrlResponse 반환
            return new ResponseDto<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    ConcatUrlResponse.builder()
                            .seq(null) // 실패 시 seq를 null로
                            .url("Error occurred while processing the concat result.") // 실패 메시지를 URL 필드에 추가
                            .build()
            ).toResponseEntity();
        }
    }
}
