package com.oreo.finalproject_5re5_be.vc.controller;

import com.oreo.finalproject_5re5_be.global.component.AudioInfo;
import com.oreo.finalproject_5re5_be.global.component.S3Service;
import com.oreo.finalproject_5re5_be.global.dto.response.ResponseDto;
import com.oreo.finalproject_5re5_be.member.dto.CustomUserDetails;
import com.oreo.finalproject_5re5_be.project.service.ProjectService;
import com.oreo.finalproject_5re5_be.vc.dto.request.VcRowRequest;
import com.oreo.finalproject_5re5_be.vc.dto.request.VcTextRequest;
import com.oreo.finalproject_5re5_be.vc.service.VcApiService;
import com.oreo.finalproject_5re5_be.vc.service.VcHistoryService;
import com.oreo.finalproject_5re5_be.vc.service.VcService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "VC", description = "VC 관련 API")
@RestController
@Slf4j
@Validated
@CrossOrigin(origins = "*")
@RequestMapping("/api/vc")
public class VcController {
    private VcService vcService;
    private AudioInfo audioInfo;
    private S3Service s3Service;
    private VcApiService vcApiService;
    private VcHistoryService vcHistoryService;
    private ProjectService projectService;

    @Autowired
    public VcController(VcService vcService,
                        AudioInfo audioInfo,
                        S3Service s3Service,
                        VcApiService vcApiService,
                        VcHistoryService vcHistoryService,
                        ProjectService projectService) {
        this.vcService = vcService;
        this.audioInfo = audioInfo;
        this.s3Service = s3Service;
        this.vcApiService = vcApiService;
        this.vcHistoryService = vcHistoryService;
        this.projectService = projectService;
    }

    @Operation(
            summary = "SRC 저장",
            description = "프로젝트 seq와 파일을 받아 SRC 파일을 S3와 DB에 저장합니다."
    )
    @PostMapping(value = "/{proSeq}/src",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDto<Map<String, List<Object>>>> srcSave(
            @Valid @Parameter(description = "프로젝트 ID")
            @PathVariable Long proSeq,
            @Valid @RequestParam List<MultipartFile> file,
            HttpSession session) {
        //회원의 정보인지 확인
        projectService.projectCheck((Long) session.getAttribute("memberSeq"), proSeq);
        try{
            //저장을 위한 파일 정보로 객체 생성
            return ResponseEntity.ok()
                    .body(new ResponseDto<>(HttpStatus.OK.value(),
                            mapCreate(vcService.srcSave(vcService.vcSrcRequestBuilder(
                                            audioInfo.extractAudioFileInfo(file),
                                            s3Service.upload(file, "vc/src"),
                                            proSeq), proSeq),
                            "src 파일 저장 완료되었습니다.")));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseDto<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            mapCreate(Collections.emptyList(), "파일 저장 중 오류가 발생했습니다.")));
        }
    }



    @Operation(
            summary = "TRG 저장",
            description = "프로젝트 seq 와 파일을 받아 TRG 파일을 S3와 DB에 저장합니다."
    )
    @PostMapping(value = "/{proSeq}/trg",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDto<Map<String, List<Object>>>>trgSave(
            @Valid @Parameter(description = "프로젝트 seq")
            @PathVariable Long proSeq,
            @RequestParam MultipartFile file,
            HttpSession session){
        //회원의 정보인지 확인
        projectService.projectCheck((Long) session.getAttribute("memberSeq"), proSeq);
        try{
            //들어온 파일을 검사해서 확장자, 길이, 이름, 크기를 추출 + 파일을 S3에 업로드
            //DB에 저장할 객체 생성 + 저장
            return ResponseEntity.ok()
                    .body(new ResponseDto<>(HttpStatus.OK.value(),
                            mapCreate(
                            vcService.trgSave(
                                    vcService.audioRequestBuilder(
                                            proSeq,
                                            audioInfo.extractAudioFileInfo(file),
                                            s3Service.upload(file, "vc/trg"))),
                            "trg 파일 저장 완료되었습니다.")));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    mapCreate(Collections.emptyList(), "파일 저장 중 오류가 발생했습니다.")));
        }

    }

    @Operation(
            summary = "Result 파일 저장(VC 생성)",
            description = "src seq 와 파일을 받아 Result 파일을 S3와 DB에 저장합니다."
    )
    @PostMapping(value = "/result")
    public ResponseEntity<ResponseDto<Map<String, List<Object>>>> resultSave(
            @RequestParam("srcSeq") @Valid List<Long> srcSeq,
            @RequestParam("trgSeq") @Valid Long trgSeq,
            HttpSession session) throws IOException {
        //회원의 정보인지 확인
        vcService.srcCheck((Long) session.getAttribute("memberSeq"), srcSeq);
        //결과 파일 생성(VC API)
        List<MultipartFile> resultFile = vcApiService.resultFileCreate(
                vcService.getSrcFile(srcSeq),//srcFile
                vcApiService.trgIdCreate(vcService.getTrgFile(trgSeq)));//trgId
//        List<MultipartFile> resultFile = new ArrayList<>();
//        MultipartFile file = AudioFileTypeConverter.convertFileToMultipartFile(new File("ttsoutput.mp3"));
//        resultFile.add(file);//API가 사용되지 않게 test로 반환
        log.info("[VcController] resultSave  resultFile: {} ", resultFile);

        //SRC 파일 삭제(spring 서버에서 삭제)
        s3Service.deleteFolder(new File("file"));
        //응답 생성
        return ResponseEntity.ok()
                .body(new ResponseDto<>(HttpStatus.OK.value(),
                        mapCreate(vcService.resultSave(
                                        vcService.audioRequestBuilder(vcService.vcSrcUrlRequests(srcSeq),
                                                audioInfo.extractAudioFileInfo(resultFile),
                                                s3Service.upload(resultFile, "vc/result"))),
                                "result 파일 저장이 완료되었습니다.")));
    }

    @Operation(
            summary = "Text 저장",
            description = "src Seq 와 Text 를 받아 DB에 저장 합니다."
    )
    @PostMapping("/src/text")
    public ResponseEntity<ResponseDto<Map<String, List<Object>>>> textSave(
            @Valid @Parameter(description = "Src Seq")
            @RequestBody List<VcTextRequest> vcText,
            HttpSession session){
        //회원의 정보인지 확인
        for (VcTextRequest vc : vcText) {
            vcService.srcCheck((Long) session.getAttribute("memberSeq"), vc.getSeq());
        }

        try{
            //객체 생성 + 저장
            return ResponseEntity.ok()
                    .body(new ResponseDto<>(HttpStatus.OK.value(),
                            mapCreate(vcService.textSave(vcService.vcTextResponses(vcText)),
                                    "text 저장 완료되었습니다.")));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            mapCreate(Collections.emptyList(), "text 저장 중 오류가 발생했습니다.")));
        }
    }

    @Operation(
            summary = "src url 호출",
            description = "src Seq 로 파일 url을 가지고 옵니다."
    )
    @GetMapping("/src/url/{srcSeq}")
    public ResponseEntity<ResponseDto<Map<String, Object>>> srcURL(
            @Valid @PathVariable Long srcSeq,
            HttpSession session){
        //회원의 정보인지 확인
        vcService.srcCheck((Long) session.getAttribute("memberSeq"), srcSeq);
        try{
            //SRCFile URL 호출
            return ResponseEntity.ok()
                    .body(new ResponseDto<>(HttpStatus.OK.value(),
                            createOneResultMap("result", vcService.getSrcUrl(srcSeq))));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            createOneResultMap(Collections.emptyMap().toString(), "SRC URL 호출중 오류가 발생했습니다.")));
        }
    }

    @Operation(
            summary = "Result url 호출",
            description = "Result Seq 로 파일 url을 가지고 옵니다."
    )
    @GetMapping("/result/url/{resSeq}")
    public ResponseEntity<ResponseDto<Map<String, Object>>> resultURL(
            @Valid @PathVariable Long resSeq,
            HttpSession session){
        //회원의 정보인지 확인
        vcService.resCheck((Long) session.getAttribute("memberSeq"), resSeq);
        //Result Seq 로 URL 정보 추출
        try{
            return ResponseEntity.ok()
                    .body(new ResponseDto<>(HttpStatus.OK.value(), createOneResultMap("result",
                            vcService.getResultUrl(resSeq))));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            createOneResultMap(Collections.emptyMap().toString(),
                                    "Result URL 호출중 오류가 발생했습니다.")));
        }
    }

    @Operation(
            summary = "프로젝트의 VC 전체 행 조회",
            description = "Result Seq 로 VC 전체 행을 가지고 옵니다."
    )
    @GetMapping("/{proSeq}")
    public ResponseEntity<ResponseDto<Map<String, Object>>> vc(
            @Valid @PathVariable Long proSeq,
            HttpSession session){
        //회원의 정보인지 확인
        projectService.projectCheck((Long) session.getAttribute("memberSeq"), proSeq);
        //Project 의 src, result, text 정보 추출
        try{
            return ResponseEntity.ok()
                    .body(new ResponseDto<>(HttpStatus.OK.value(), createOneResultMap("row",
                            vcService.getVcResponse(proSeq))));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            createOneResultMap(Collections.emptyMap().toString(),
                                    proSeq+"- 프로젝트 VC 행 호출중 오류가 발생했습니다.")));
        }
    }

    @Operation(
            summary = "SRC 행 삭제",
            description = "SRC 행을 비활성화 상태로 변경합니다. active = 'N' "
    )
    @DeleteMapping("/src")
    public ResponseEntity<ResponseDto<Map<String, List<Object>>>> deleteSrc(
            @Valid @RequestBody List<Long> srcSeq,
            HttpSession session){
        //회원의 정보인지 확인
        vcService.srcCheck((Long) session.getAttribute("memberSeq"), srcSeq);
        //삭제 호출
        try{
            return ResponseEntity.ok()
                    .body(new ResponseDto<>(HttpStatus.OK.value(),
                            mapCreate(vcService.deleteSrcFile(srcSeq),
                                    "SRC 행 삭제 완료되었습니다.")));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            mapCreate(Collections.emptyList(), "SRC 행 삭제 중 오류가 발생했습니다.")));
        }

    }

    @Operation(
            summary = "Text 수정",
            description = "text seq 로 text 내용을 변경합니다."
    )
    @PutMapping("/src/{textSeq}")
    public ResponseEntity<ResponseDto<Map<String, List<Object>>>> updateText(
            @Valid @PathVariable Long textSeq,
            @Valid @RequestParam("text") String text,
            HttpSession session){
        //회원의 정보인지 확인
        vcService.textCheck((Long) session.getAttribute("memberSeq"), textSeq);
        //textseq 로 text 값 변경
        try{
            return ResponseEntity.ok()
                    .body(new ResponseDto<>(HttpStatus.OK.value(), mapCreate(vcService.updateText(textSeq, text),
                            "Text 수정 완료되었습니다.")));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            mapCreate(Collections.emptyList(), "Text 수정 중 오류가 발생했습니다.")));
        }
    }
    @Operation(
            summary = "행 수정",
            description = "Seq와 행순서를 가지고 변경합니다."
    )
    @PatchMapping("/row")
    public ResponseEntity<ResponseDto<Map<String, List<Object>>>> updateRowOrder(
            @Valid @RequestBody List<VcRowRequest> rows,
            HttpSession session){
        //회원의 정보인지 확인
        for (VcRowRequest row : rows) {
            vcService.srcCheck((Long) session.getAttribute("memberSeq"), row.getSeq());
        }
        //VC 행 순서 수정
        try{
            return ResponseEntity.ok()
                    .body(new ResponseDto<>(HttpStatus.OK.value(), mapCreate(vcService.updateRowOrder(rows),
                            "행 수정 완료되었습니다.")));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            mapCreate(Collections.emptyList(), "행 수정 중 오류가 발생했습니다.")));
        }
    }


    //중복되는 map<String, List<Object>> url 리턴값 메서드로 변경
    private static Map<String, List<Object>> mapCreate(Object response, String message){
        Map<String, List<Object>> map = new HashMap<>();
        map.put("data", Collections.singletonList(response));//응답 값
        map.put("message", Collections.singletonList(message));//응답 메시지
        return map;
    }
    //중복되는 단일 url map
    private static Map<String, Object> createOneResultMap(String key, Object value) {
        return Collections.singletonMap(key, value);
    }
}