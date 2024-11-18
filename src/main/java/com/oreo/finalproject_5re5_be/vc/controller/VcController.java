package com.oreo.finalproject_5re5_be.vc.controller;

import com.oreo.finalproject_5re5_be.global.dto.response.AudioFileInfo;
import com.oreo.finalproject_5re5_be.global.component.AudioInfo;
import com.oreo.finalproject_5re5_be.global.dto.response.ResponseDto;
import com.oreo.finalproject_5re5_be.global.component.S3Service;
import com.oreo.finalproject_5re5_be.vc.dto.request.VcAudioRequest;
import com.oreo.finalproject_5re5_be.vc.dto.request.VcSrcRequest;
import com.oreo.finalproject_5re5_be.vc.dto.request.VcTextRequest;
import com.oreo.finalproject_5re5_be.vc.dto.response.VcActivateResponse;
import com.oreo.finalproject_5re5_be.vc.dto.response.VcResponse;
import com.oreo.finalproject_5re5_be.vc.dto.response.VcTextResponse;
import com.oreo.finalproject_5re5_be.vc.dto.response.VcUrlResponse;
import com.oreo.finalproject_5re5_be.vc.service.VcService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

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
    @PostMapping("/{proSeq}/src")
    public ResponseEntity<ResponseDto<Map<String, List<Object>>>> srcSave(@Valid @Parameter(description = "프로젝트 ID")
                                                                        @PathVariable Long proSeq,
                                                                   @Valid @RequestParam List<MultipartFile> file) {
        List<VcUrlResponse> responses = new ArrayList<>();
        for (int i = 0; i < file.size(); i++) {
            //들어온 파일을 검사해서 확장자, 길이, 이름, 크기를 추출
            AudioFileInfo info = audioInfo.extractAudioFileInfo(file.get(i));
            //파일을 S3에 업로드
            String fileUrl = s3Service.upload(file.get(i), "vc/src");
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
            VcUrlResponse response = vcService.srcSave(request);//저장
            responses.add(response);//배열로 저장
        }
        return ResponseEntity.ok()
                .body(new ResponseDto<>(HttpStatus.OK.value(),mapCreate(responses,
                        "src 파일 저장 완료되었습니다.")));
    }



    @Operation(
            summary = "TRG 저장",
            description = "프로젝트 seq 와 파일을 받아 TRG 파일을 S3와 DB에 저장합니다."
    )
    @PostMapping("/{proSeq}/trg")
    public ResponseEntity<ResponseDto<String>>trgSave(@Valid @Parameter(description = "프로젝트 seq") @PathVariable Long proSeq,
                                          @Valid @RequestParam MultipartFile file){
        //들어온 파일을 검사해서 확장자, 길이, 이름, 크기를 추출
        AudioFileInfo info = audioInfo.extractAudioFileInfo(file);
        //파일을 S3에 업로드
        String trgUrl = s3Service.upload(file, "vc/trg");
        //DB에 저장할 객체 생성
        VcAudioRequest trg = VcAudioRequest.builder()
                .seq(proSeq)
                .name(info.getName())
                .fileUrl(trgUrl)
                .length(info.getLength())
                .size(info.getSize())
                .extension(info.getExtension())
                .build();
        vcService.trgSave(trg);//저장
        return ResponseEntity.ok()
                .body(new ResponseDto<>(HttpStatus.OK.value(), "TRG 파일 저장이 완료되었습니다."));
    }

    @Operation(
            summary = "Result 파일 저장(VC 생성)",
            description = "src seq 와 파일을 받아 Result 파일을 S3와 DB에 저장합니다."
    )
    @PostMapping("/{srcSeq}/result")
    public ResponseEntity<ResponseDto<Map<String,Object>>> resultSave(
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
                    .seq(srcSeq)
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
        return ResponseEntity.ok()
                .body(new ResponseDto<>(HttpStatus.OK.value(), map));
    }

    @Operation(
            summary = "Text 저장",
            description = "src Seq 와 Text 를 받아 DB에 저장 합니다."
    )
    @PostMapping("/src/text")
    public ResponseEntity<ResponseDto<Map<String, List<Object>>>> textSave(@Valid @Parameter(description = "Src Seq")
                                                                               @RequestParam  List<Long> srcSeq,
                                           @Valid @RequestBody List<String> text){
        List<VcTextResponse> responses = new ArrayList<>();
        for (int i = 0; i < srcSeq.size(); i++) {
            VcTextRequest textRequest = VcTextRequest.builder()//Text 객체 생성
                    .seq(srcSeq.get(i))
                    .text(text.get(i))
                    .build();
            VcTextResponse vcTextResponse = vcService.textSave(textRequest);//저장
            responses.add(vcTextResponse);//응답 값 저장
        }
        return ResponseEntity.ok()
                .body(new ResponseDto<>(HttpStatus.OK.value(),
                        mapCreate(responses,
                        "text 저장 완료되었습니다.")));
    }

    @Operation(
            summary = "src url 호출",
            description = "src Seq 로 파일 url을 가지고 옵니다."
    )
    @GetMapping("/src/url/{srcSeq}")
    public ResponseEntity<ResponseDto<Map<String, Object>>> srcURL(@Valid @PathVariable Long srcSeq){
        //SrcSeq로 URL 정보 추출
        VcUrlResponse srcFile = vcService.getSrcFile(srcSeq);
        Map<String, Object> map = new HashMap<>();//응답값
        map.put("result", srcFile);
        return ResponseEntity.ok()
                .body(new ResponseDto<>(HttpStatus.OK.value(), map));
    }

    @Operation(
            summary = "Result url 호출",
            description = "Result Seq 로 파일 url을 가지고 옵니다."
    )
    @GetMapping("/result/url/{resSeq}")
    public ResponseEntity<ResponseDto<Map<String, Object>>> resultURL(@Valid @PathVariable Long resSeq){
        //Result Seq 로 URL 정보 추출
        VcUrlResponse resultFile = vcService.getResultFile(resSeq);
        Map<String, Object> map = new HashMap<>();//응답갑
        map.put("result", resultFile);
        return ResponseEntity.ok()
                .body(new ResponseDto<>(HttpStatus.OK.value(), map));
    }

    @Operation(
            summary = "프로젝트의 VC 전체 행 조회",
            description = "Result Seq 로 VC 전체 행을 가지고 옵니다."
    )
    @GetMapping("/{proSeq}")
    public ResponseEntity<ResponseDto<Map<String, Object>>> vc(@Valid @PathVariable Long proSeq){
        //Project 의 src, result, text 정보 추출
        List<VcResponse> response = vcService.getVcResponse(proSeq);
        Map<String, Object> map = new HashMap<>();//응답값
        map.put("row", response);
        return ResponseEntity.ok()
                .body(new ResponseDto<>(HttpStatus.OK.value(), map));
    }

    @Operation(
            summary = "SRC 행 삭제",
            description = "SRC 행을 비활성화 상태로 변경합니다. active = 'N' "
    )
    @DeleteMapping("/src")
    public ResponseEntity<ResponseDto<Map<String, List<Object>>>> deleteSrc(@Valid @RequestBody List<Long> srcSeq){
        List<VcActivateResponse> responses = new ArrayList<>();
        for (int i = 0; i < srcSeq.size(); i++) {
            //srcSeq 로 그 값의 active 값 변경
            VcActivateResponse vcActivateResponse =
                    vcService.deleteSrcFile(srcSeq.get(i));
            responses.add(vcActivateResponse);
        }
        return ResponseEntity.ok()
                .body(new ResponseDto<>(HttpStatus.OK.value(),
                                mapCreate(responses,
                                        "SRC 행 삭제 완료되었습니다.")));
    }

    @Operation(
            summary = "Text 수정",
            description = "text seq 로 text 내용을 변경합니다."
    )
    @PutMapping("/src/{textSeq}")
    public ResponseEntity<ResponseDto<String>> updateText(@Valid @PathVariable Long textSeq,
                                                          @Valid @RequestParam("text") String text){
        vcService.updateText(textSeq, text); //textseq 로 text 값 변경
        return ResponseEntity.ok()
                .body(new ResponseDto<>(HttpStatus.OK.value(), "Text 수정이 완료되었습니다."));
    }

    //중복되는 map<String, List<Object>> url 리턴값 메서드로 변경
    private static Map<String, List<Object>> mapCreate(Object response, String message){
        Map<String, List<Object>> map = new HashMap<>();
        map.put("data", Collections.singletonList(response));//응답 값
        map.put("message", Collections.singletonList(message));//응답 메시지
        return map;
    }
}