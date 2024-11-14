package com.oreo.finalproject_5re5_be.vc.controller;

import com.oreo.finalproject_5re5_be.global.dto.response.AudioFileInfo;
import com.oreo.finalproject_5re5_be.global.component.AudioInfo;
import com.oreo.finalproject_5re5_be.global.dto.response.ResponseDto;
import com.oreo.finalproject_5re5_be.global.component.S3Service;
import com.oreo.finalproject_5re5_be.vc.dto.request.VcAudioRequest;
import com.oreo.finalproject_5re5_be.vc.dto.request.VcSrcRequest;
import com.oreo.finalproject_5re5_be.vc.dto.request.VcTextRequest;
import com.oreo.finalproject_5re5_be.vc.dto.response.VcResponse;
import com.oreo.finalproject_5re5_be.vc.service.VcService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@Validated
@CrossOrigin(origins = "*")
@RequestMapping("/api/vc")
public class VcController {
    private VcService vcService;
    private AudioInfo audioInfo;
    private S3Service s3Service;

    @Autowired
    public VcController(VcService vcService, AudioInfo audioInfo, S3Service s3Service) {
        this.vcService = vcService;
        this.audioInfo = audioInfo;
        this.s3Service = s3Service;
    }

    @Operation(
            summary = "SRC 저장",
            description = "프로젝트 seq와 파일을 받아 SRC 파일을 S3와 DB에 저장합니다."
    )
    @PostMapping("/project/{proSeq}/src")
    public ResponseDto<Map<String, Object>> srcSave(@Valid @Parameter(description = "프로젝트 ID") @PathVariable Long proSeq,
                                       @Valid @RequestParam MultipartFile file) {
        //들어온 파일을 검사해서 확장자, 길이, 이름, 크기를 추출
        AudioFileInfo info = audioInfo.extractAudioFileInfo(file);
        //파일을 S3에 업로드
        String fileUrl = s3Service.upload(file, "vc/src");
        //DB에 저장할 객체 생성
        VcSrcRequest request = VcSrcRequest.builder()
                .seq(proSeq)
                .rowOrder(1)
                .name(info.getName())
                .fileUrl(fileUrl)
                .length(info.getLength())
                .size(info.getSize())
                .extension(info.getExtension())
                .build();
        vcService.srcSave(request);//저장
        //응답에 대한 값 입력
        Map<String, Object> map = new HashMap<>();
        map.put("url",fileUrl);
        map.put("message", "src 파일 저장 완료되었습니다.");
        return new ResponseDto<>(HttpStatus.OK.value(),map);
    }

    @Operation(
            summary = "TRG 저장",
            description = "프로젝트 seq 와 파일을 받아 TRG 파일을 S3와 DB에 저장합니다."
    )
    @PostMapping("/project/{proSeq}/trg")
    public ResponseDto<String> trgSave(@Valid @Parameter(description = "프로젝트 seq") @PathVariable Long proSeq,
                                          @Valid @RequestParam MultipartFile file){
        //들어온 파일을 검사해서 확장자, 길이, 이름, 크기를 추출
        AudioFileInfo info = audioInfo.extractAudioFileInfo(file);
        //파일을 S3에 업로드
        String trgUrl = s3Service.upload(file, "vc/trg");
        //DB에 저장할 객체 생성
        VcAudioRequest trg = VcAudioRequest.builder()
                .seq1(proSeq)
                .name(info.getName())
                .fileUrl(trgUrl)
                .length(info.getLength())
                .size(info.getSize())
                .extension(info.getExtension())
                .build();
        vcService.trgSave(trg);//저장
        return new ResponseDto<>(HttpStatus.OK.value(), "TRG 파일 저장이 완료되었습니다.");
    }

    @Operation(
            summary = "Result 파일 저장(VC 생성)",
            description = "src seq 와 파일을 받아 Result 파일을 S3와 DB에 저장합니다."
    )
    @PostMapping("/src/{srcSeq}/result")
    public ResponseDto<Map<String,Object>> resultSave(
            @Valid @Parameter(description = "Src Seq") @PathVariable Long srcSeq,
                                             @RequestParam("src url")String url,
                                             @Valid @RequestParam("trg file") MultipartFile trgFile) {
        String key = url.substring(url.lastIndexOf("/") + 1); //키값만 잘라서 사용하게 변경해야함
        MultipartFile srcFile;
        MultipartFile resultFile;
        try {
            srcFile = (MultipartFile) s3Service.downloadFile(key);//url에서 파일을 다운로드
            //srcFile +  trgFile = result file VC API 사용에정

            //Result파일 설정 API에서 나올 값
            resultFile = null;
            //S3에 결과 파일 저장
            String resultUrl = s3Service.upload(resultFile, "vc/result");
            //결과 파일 데이터 추출
            AudioFileInfo info = audioInfo.extractAudioFileInfo(resultFile);
            //VcAudio 객체 생성
            VcAudioRequest result = VcAudioRequest.builder()
                    .seq1(srcSeq)
                    .name(info.getName())
                    .fileUrl(resultUrl)
                    .length(info.getLength())
                    .size(info.getSize())
                    .extension(info.getExtension())
                    .build();
            vcService.resultSave(result);//저장
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //응답 생성
        Map<String, Object> map = new HashMap<>();
        map.put("converted_vocie", resultFile);
        map.put("message", "result 파일 저장이 완료되었습니다.");
        return new ResponseDto<>(HttpStatus.OK.value(), map);
    }

    @Operation(
            summary = "Text 저장",
            description = "src Seq 와 Text 를 받아 DB에 저장 합니다."
    )
    @PostMapping("/src/{srcSeq}/text")
    public ResponseDto<String> textSave(@Valid @Parameter(description = "Src Seq") @PathVariable Long srcSeq,
                                           @Valid @RequestBody String text){
        //Text 객체 생성
        VcTextRequest textRequest = VcTextRequest.builder()
                .seq(srcSeq)
                .text(text)
                .build();
        vcService.textSave(textRequest);//저장
        return new ResponseDto<>(HttpStatus.OK.value(), "Text 파일 저장이 완료되었습니다.");
    }

    @Operation(
            summary = "src url 호출",
            description = "src Seq 로 파일 url을 가지고 옵니다."
    )
    @GetMapping("/src/url/{srcSeq}")
    public ResponseDto<Map<String, Object>> srcURL(@Valid @PathVariable Long srcSeq){
        //SrcSeq로 URL 정보 추출
        String sourceVoice = vcService.getSrcFile(srcSeq);
        Map<String, Object> map = new HashMap<>();//응답값
        map.put("result", sourceVoice);
        return new ResponseDto<>(HttpStatus.OK.value(), map);
    }

    @Operation(
            summary = "Result url 호출",
            description = "Result Seq 로 파일 url을 가지고 옵니다."
    )
    @GetMapping("/result/url/{resSeq}")
    public ResponseDto<Map<String, Object>> resultURL(@Valid @PathVariable Long resSeq){
        //Result Seq 로 URL 정보 추출
        String result = vcService.getResultFile(resSeq);
        Map<String, Object> map = new HashMap<>();//응답갑
        map.put("result", result);
        return new ResponseDto<>(HttpStatus.OK.value(), map);
    }

    @Operation(
            summary = "프로젝트의 VC 전체 행 조회",
            description = "Result Seq 로 VC 전체 행을 가지고 옵니다."
    )
    @GetMapping("/project/{proSeq}/vc")
    public ResponseDto<Map<String, Object>> vc(@Valid @PathVariable Long proSeq){
        //Project 의 src, result, text 정보 추출
        List<VcResponse> response = vcService.getVcResponse(proSeq);
        Map<String, Object> map = new HashMap<>();//응답값
        map.put("row", response);
        return new ResponseDto<>(HttpStatus.OK.value(), map);
    }

    @Operation(
            summary = "SRC 행 삭제",
            description = "SRC 행을 비활성화 상태로 변경합니다. active = 'N' "
    )
    @DeleteMapping("/src/{srcSeq}")
    public ResponseDto<String> deleteSrc(@Valid @PathVariable Long srcSeq){
        vcService.deleteSrcFile(srcSeq);//srcSeq 로 그 값의 active 값 변경
        return new ResponseDto<>(HttpStatus.OK.value(), "SRC 행 삭제 완료되었습니다");
    }

    @Operation(
            summary = "Text 수정",
            description = "text seq 로 text 내용을 변경합니다."
    )
    @PutMapping("/src/{textSeq}")
    public ResponseDto<String> updateText(@Valid @PathVariable Long textSeq, @Valid @RequestParam("text") String text){
        vcService.updateText(textSeq, text); //textseq 로 text 값 변경
        return new ResponseDto<>(HttpStatus.OK.value(), "Text 수정이 완료되었습니다.");
    }
}