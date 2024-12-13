package com.oreo.finalproject_5re5_be.concat.controller;

import com.oreo.finalproject_5re5_be.concat.dto.request.AudioFileDto;
import com.oreo.finalproject_5re5_be.concat.dto.request.AudioFileRequestDto;
import com.oreo.finalproject_5re5_be.concat.dto.request.OriginAudioRequest;
import com.oreo.finalproject_5re5_be.concat.service.AudioFileService;
import com.oreo.finalproject_5re5_be.concat.service.ConcatRowService;
import com.oreo.finalproject_5re5_be.concat.service.MaterialAudioService;
import com.oreo.finalproject_5re5_be.global.dto.response.ResponseDto;
import com.oreo.finalproject_5re5_be.member.dto.CustomUserDetails;
import com.oreo.finalproject_5re5_be.project.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.List;
import javax.sound.sampled.UnsupportedAudioFileException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Concat", description = "Concat 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/concat/audio")
public class AudioFileController {
    private final AudioFileService audioFileService;
    private final ProjectService projectService;
    private final ConcatRowService concatRowService;
    private final MaterialAudioService materialAudioService;

    @Operation(summary = "업로드 할 오디오 형식 검사", description = "업로드 할 수 없는 오디오 목록을 반환합니다.")
    @PostMapping(
            value = "extension/check",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDto<List<AudioFileRequestDto>>> check(
            @RequestParam("audio") List<MultipartFile> audioFiles) throws IOException {
        List<AudioFileRequestDto> audioDto = convertToDto(audioFiles);
        List<AudioFileRequestDto> audioFileRequestDtos = audioFileService.checkExtension(audioDto);

        HttpStatus status = audioFileRequestDtos.isEmpty() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return buildResponse(status, audioFileRequestDtos);
    }

    @Operation(summary = "오디오 업로드", description = "업로드한 오디오의 정보를 반환합니다.")
    @PostMapping(
            value = "save",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDto<List<OriginAudioRequest>>> save(
            @RequestParam("audio") List<MultipartFile> audioFiles)
            throws IOException, UnsupportedAudioFileException {
        List<AudioFileRequestDto> audioDto = convertToDto(audioFiles);
        List<OriginAudioRequest> originAudioRequests = audioFileService.saveAudioFile(audioDto);
        return buildResponse(HttpStatus.OK, originAudioRequests);
    }

    @Operation(summary = "행의 아이디에 저장된 오디오 불러오기", description = "업로드 된 오디오의 정보를 반환합니다.")
    @PostMapping("read")
    public ResponseEntity<ResponseDto<List<AudioFileDto>>> read(
            @RequestParam List<Long> concatRowSeq,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        concatRowSeq.forEach(
                seq -> {
                    Long projectId = concatRowService.readConcatRow(seq).getConcatTab().getProjectId();
                    projectService.projectCheck(userDetails.getMember().getSeq(), projectId);
                });

        concatRowSeq.sort(Long::compareTo);
        List<AudioFileDto> audioFileList = audioFileService.findByConcatRowSeq(concatRowSeq);
        return buildResponse(HttpStatus.OK, audioFileList);
    }

    @Operation(summary = "사용자가 업로드 한 모든 오디오 파일을 조회합니다.", description = "업로드 된 오디오의 정보를 반환합니다.")
    @GetMapping("read/my/audio")
    public ResponseEntity<ResponseDto<List<AudioFileDto>>> readMyAudio(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String sort) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getPrincipal().toString();
        System.out.println("user = " + user);

        Sort.Direction direction = Sort.Direction.fromString(sort); // "ASC" 또는 "DESC"
        Pageable pageable =
                PageRequest.of(
                        page, size, Sort.by(direction, "createdDate")); // "created_date"가 아니라 매핑된 엔티티 필드 이름 사용
        return buildResponse(
                HttpStatus.OK,
                audioFileService.getMemberAudioFile(userDetails.getMember().getSeq(), pageable));
    }

    @Operation(
            summary = "전달받은 페이지 사이즈로 계산한 페이지 배열을 반환 합니다.",
            description = "숫자 배열을 반환합니다. 숫자는 0부터 시작합니다.")
    @GetMapping("read/my/audio/pages")
    public ResponseEntity<ResponseDto<List<Integer>>> readMyAudioPages(
            @AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam int size) {
        return buildResponse(
                HttpStatus.OK, audioFileService.getAudioFilePages(userDetails.getMember().getSeq(), size));
    }

    private List<AudioFileRequestDto> convertToDto(List<MultipartFile> audioFiles) {
        return audioFiles.stream()
                .map(file -> new AudioFileRequestDto(file, file.getOriginalFilename()))
                .toList();
    }

    private <T> ResponseEntity<ResponseDto<T>> buildResponse(HttpStatus status, T data) {
        return new ResponseEntity<>(new ResponseDto<>(status.value(), data), status);
    }
}
