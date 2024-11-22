package com.oreo.finalproject_5re5_be.vc.controller;

import com.oreo.finalproject_5re5_be.global.dto.response.AudioFileInfo;
import com.oreo.finalproject_5re5_be.global.component.AudioInfo;
import com.oreo.finalproject_5re5_be.global.dto.response.ResponseDto;
import com.oreo.finalproject_5re5_be.global.component.S3Service;
import com.oreo.finalproject_5re5_be.vc.dto.request.*;
import com.oreo.finalproject_5re5_be.vc.dto.response.*;
import com.oreo.finalproject_5re5_be.vc.entity.Vc;
import com.oreo.finalproject_5re5_be.vc.entity.VcSrcFile;
import com.oreo.finalproject_5re5_be.vc.repository.VcSrcFileRepository;
import com.oreo.finalproject_5re5_be.vc.service.VcApiService;
import com.oreo.finalproject_5re5_be.vc.service.VcService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@RestController
@Slf4j
@Validated
@CrossOrigin(origins = "*")
@RequestMapping("/api/vc")
public class VcController {
    private VcSrcFileRepository vcSrcFileRepository;
    private VcService vcService;
    private AudioInfo audioInfo;
    private S3Service s3Service;
    private VcApiService vcApiService;

    @Autowired
    public VcController(VcService vcService,
                        AudioInfo audioInfo,
                        S3Service s3Service,
                        VcApiService vcApiService,
                        VcSrcFileRepository vcSrcFileRepository) {
        this.vcService = vcService;
        this.audioInfo = audioInfo;
        this.s3Service = s3Service;
        this.vcApiService = vcApiService;
        this.vcSrcFileRepository = vcSrcFileRepository;
    }

    @Operation(
            summary = "SRC 저장",
            description = "프로젝트 seq와 파일을 받아 SRC 파일을 S3와 DB에 저장합니다."
    )
    @PostMapping(value = "/{proSeq}/src",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto<Map<String, List<Object>>>> srcSave(@Valid @Parameter(description = "프로젝트 ID")
                                                                        @PathVariable Long proSeq,
                                                                   @Valid @RequestParam List<MultipartFile> file) {
        List<AudioFileInfo> audioFileInfos = audioInfo.extractAudioFileInfo(file);//배열로 받은 파일 정보 추출
        List<String> upload = s3Service.upload(file, "vc/src");//파일 업로드
        //저장을 위한 파일 정보로 객체 생성
        List<VcSrcRequest> vcSrcRequests = vcService.vcSrcRequestBuilder(audioFileInfos, upload, proSeq);
        List<VcUrlResponse> vcUrlResponses = vcService.srcSave(vcSrcRequests, proSeq);//객체 저장
        return ResponseEntity.ok()
                .body(new ResponseDto<>(HttpStatus.OK.value(),mapCreate(vcUrlResponses,
                        "src 파일 저장 완료되었습니다.")));
    }



    @Operation(
            summary = "TRG 저장",
            description = "프로젝트 seq 와 파일을 받아 TRG 파일을 S3와 DB에 저장합니다."
    )
    @PostMapping(value = "/{proSeq}/trg",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDto<Map<String, List<Object>>>>trgSave(@Valid @Parameter(description = "프로젝트 seq")
                                                                             @PathVariable Long proSeq,
                                                                         @RequestParam MultipartFile file){
        //들어온 파일을 검사해서 확장자, 길이, 이름, 크기를 추출
        AudioFileInfo info = audioInfo.extractAudioFileInfo(file);
        //파일을 S3에 업로드
        String trgUrl = s3Service.upload(file, "vc/trg");
        //DB에 저장할 객체 생성
        VcAudioRequest trg = vcService.audioRequestBuilder(proSeq, info, trgUrl);
        VcUrlResponse vcUrlResponse = vcService.trgSave(trg);//저장
        return ResponseEntity.ok()
                .body(new ResponseDto<>(HttpStatus.OK.value(), mapCreate(vcUrlResponse,
                        "trg 파일 저장 완료되었습니다.")));
    }

    @Operation(
            summary = "Result 파일 저장(VC 생성)",
            description = "src seq 와 파일을 받아 Result 파일을 S3와 DB에 저장합니다."
    )
    @PostMapping(value = "/result",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDto<Map<String, Object>>> resultSave(
            @RequestParam("vcSrcUrlRequest") @Valid List<Long> srcSeq,
            @RequestParam("trgFile") MultipartFile trgFile) {
        Map<String, Object> map = new HashMap<>();//응답값 생성
//        String trgId = vcApiService.trgIdCreate(trgFile);//TRG ID 생성
//        log.info("[vccontroller] /result trgId: {}", trgId);
        List<MultipartFile> srcFile = new ArrayList<>();
        //vcSrcSeq로 url 가지고 오기
        List<VcSrcUrlRequest> vcSrcUrlRequest = vcService.vcSrcUrlRequests(srcSeq);
        log.info("[vccontroller] /result vcSrcUrlRequest: {}", vcSrcUrlRequest);
        try {

            for (int i = 0; i < vcSrcUrlRequest.size(); i++) {
                //다시 불러올때 URL이 이상하면 바로 오류 뜨는데 SRC 저장할때 확인해봐야할듯
                String url = "vc/src/"+vcSrcUrlRequest.get(i).getUrl().substring(
                        vcSrcUrlRequest.get(i).getUrl().lastIndexOf("/")+1);
                log.info("[vccontroller] /result url: {}", url);
                File file = s3Service.downloadFile(url);
                log.info("[vccontroller] /result file: {}", file);
                MultipartFile multipartFile = convertFileToMultipartFile(file);
                log.info("[vccontroller] /result multipartFile: {}", multipartFile);
                srcFile.add(multipartFile);
                log.info("[vccontroller] /result srcFile: {}", srcFile);
            }

                    
            log.info("[vccontroller] /result srcFile: {}", srcFile);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //결과 파일 생성(VC API)
//        List<MultipartFile> resultFile = vcApiService.resultFileCreate(srcFile, "Yr8eh9CaB3tXeZD1ogAu");//trgId
        List<MultipartFile> resultFile = new ArrayList<>();
        for (int i = 0; i < srcSeq.size(); i++) {
            resultFile.add(trgFile); //일단 그냥 SRC 파일로 출력 API 활용떄문에 꺼둠
        }

        log.info("[vccontroller] /result resultFile: {}", resultFile);
        //결과 s3 저장
        List<String> resultUrl = s3Service.upload(resultFile, "vc/result");
        log.info("[vccontroller] /result resultUrl: {}", resultUrl);
        //결과 파일 정보 추출
        List<AudioFileInfo> info = audioInfo.extractAudioFileInfo(resultFile);
        log.info("[vccontroller] /result info: {}", info);
        //결과 파일 정보들 가지고 객체 생성
        List<VcAudioRequest> requests = vcService.audioRequestBuilder(vcSrcUrlRequest, info, resultUrl);
        log.info("[vccontroller] /result requests: {}", requests);
        //객체 저장
        List<VcUrlResponse> vcUrlResponses = vcService.resultSave(requests);
        log.info("[vccontroller] /result vcUrlResponses: {}", vcUrlResponses);

        s3Service.deleteFolder(new File("file"));//SRC 파일 삭제(spring 서버에서 삭제)
        map.put("result", vcUrlResponses);
        map.put("message", "result 파일 저장이 완료되었습니다.");//완료 메시지
        //응답 생성
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
        //객체 생성
        List<VcTextRequest> vcTextRequests = vcService.vcTextResponses(srcSeq, text);
        log.info("[VcController] /src/text vcTextRequest {} : ", vcTextRequests);
        //저장
        List<VcTextResponse> vcTextResponses = vcService.textSave(vcTextRequests);
        log.info("[VcController] /src/text vcTextResponses {} : ", vcTextResponses);
        return ResponseEntity.ok()
                .body(new ResponseDto<>(HttpStatus.OK.value(),
                        mapCreate(vcTextResponses,
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
        //삭제 호출
        List<VcActivateResponse> vcActivateResponses = vcService.deleteSrcFile(srcSeq);
        return ResponseEntity.ok()
                .body(new ResponseDto<>(HttpStatus.OK.value(),
                                mapCreate(vcActivateResponses,
                                        "SRC 행 삭제 완료되었습니다.")));
    }

    @Operation(
            summary = "Text 수정",
            description = "text seq 로 text 내용을 변경합니다."
    )
    @PutMapping("/src/{textSeq}")
    public ResponseEntity<ResponseDto<Map<String, List<Object>>>> updateText(@Valid @PathVariable Long textSeq,
                                                          @Valid @RequestParam("text") String text){
        VcTextResponse vcTextResponse = vcService.updateText(textSeq, text);//textseq 로 text 값 변경
        return ResponseEntity.ok()
                .body(new ResponseDto<>(HttpStatus.OK.value(), mapCreate(vcTextResponse,
                        "Text 수정 완료되었습니다.")));
    }

    @PatchMapping("/vc/row")
    public ResponseEntity<ResponseDto<Map<String, List<Object>>>> updateRowOrder(@Valid @RequestBody List<VcRowRequest> row){
        List<VcRowResponse> vcRowResponses = vcService.updateRowOrder(row);
        return ResponseEntity.ok()
                .body(new ResponseDto<>(HttpStatus.OK.value(), mapCreate(vcRowResponses,
                        "행 수정 완료되었습니다.")));
    }


    //중복되는 map<String, List<Object>> url 리턴값 메서드로 변경
    private static Map<String, List<Object>> mapCreate(Object response, String message){
        Map<String, List<Object>> map = new HashMap<>();
        map.put("data", Collections.singletonList(response));//응답 값
        map.put("message", Collections.singletonList(message));//응답 메시지
        return map;
    }
    //File을  MultipartFile로 변경
    public MultipartFile convertFileToMultipartFile(File file) throws IOException {
        try (FileInputStream inputStream = new FileInputStream(file)) {
            return new MockMultipartFile(
                    "file",                          // 파라미터 이름
                    file.getName(),                  // 파일 이름
                    "application/octet-stream",      // MIME 타입
                    inputStream                      // 파일 내용
            );
        }
    }
}