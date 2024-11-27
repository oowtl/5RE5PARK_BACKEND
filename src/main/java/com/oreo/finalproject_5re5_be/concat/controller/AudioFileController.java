package com.oreo.finalproject_5re5_be.concat.controller;

import com.oreo.finalproject_5re5_be.concat.dto.request.AudioFileDto;
import com.oreo.finalproject_5re5_be.concat.dto.request.AudioFileRequestDto;
import com.oreo.finalproject_5re5_be.concat.dto.request.OriginAudioRequest;
import com.oreo.finalproject_5re5_be.concat.entity.AudioFile;
import com.oreo.finalproject_5re5_be.concat.service.AudioFileService;
import com.oreo.finalproject_5re5_be.global.dto.response.ResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.List;

@Tag(name = "Concat", description = "Concat 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/concat/audio")
public class AudioFileController {
    private final AudioFileService audioFileService;

    @PostMapping(value = "extension/check",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDto<List<AudioFileRequestDto>>> check(
            @RequestParam("audio") List<MultipartFile> audioFiles) throws IOException {
        List<AudioFileRequestDto> audioDto = convertToDto(audioFiles);
        List<AudioFileRequestDto> audioFileRequestDtos = audioFileService.checkExtension(audioDto);

        HttpStatus status = audioFileRequestDtos.isEmpty() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return buildResponse(status, audioFileRequestDtos);
    }

    @PostMapping(value = "save",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDto<List<OriginAudioRequest>>> save(
            @RequestParam("audio") List<MultipartFile> audioFiles) throws IOException, UnsupportedAudioFileException {
        List<AudioFileRequestDto> audioDto = convertToDto(audioFiles);
        List<OriginAudioRequest> originAudioRequests = audioFileService.saveAudioFile(audioDto);
        return buildResponse(HttpStatus.OK, originAudioRequests);
    }

    @PostMapping("read")
    public ResponseEntity<ResponseDto<List<AudioFileDto>>> read(@RequestParam List<Long> concatRowSeq) {
        concatRowSeq.sort(Long::compareTo);
        List<AudioFile> audioFile = audioFileService.findByConcatRowSeq(concatRowSeq);
        List<AudioFileDto> list = audioFile.stream().map(this::convertToDto).toList();
        return buildResponse(HttpStatus.OK, list);
    }

    private List<AudioFileRequestDto> convertToDto(List<MultipartFile> audioFiles) {
        return audioFiles.stream()
                .map(file -> new AudioFileRequestDto(file, file.getOriginalFilename()))
                .toList();
    }

    private <T> ResponseEntity<ResponseDto<T>> buildResponse(HttpStatus status, T data) {
        return new ResponseEntity<>(new ResponseDto<>(status.value(), data), status);
    }

    private AudioFileDto convertToDto(AudioFile audioFile) {
        return AudioFileDto.builder()
                .audioFileSeq(audioFile.getAudioFileSeq())
                .audioUrl(audioFile.getAudioUrl())
                .fileLength(audioFile.getFileLength())
                .fileName(audioFile.getFileName())
                .fileSize(audioFile.getFileSize())
                .createdDate(audioFile.getCreatedDate())
                .extension(audioFile.getExtension())
                .build();
    }
}
