package com.oreo.finalproject_5re5_be.concat.controller;

import com.oreo.finalproject_5re5_be.concat.dto.request.AudioFileDto;
import com.oreo.finalproject_5re5_be.concat.dto.request.AudioFileRequestDto;
import com.oreo.finalproject_5re5_be.concat.dto.request.OriginAudioRequest;
import com.oreo.finalproject_5re5_be.concat.entity.AudioFile;
import com.oreo.finalproject_5re5_be.concat.service.AudioFileService;
import com.oreo.finalproject_5re5_be.global.dto.response.ResponseDto;
import com.oreo.finalproject_5re5_be.global.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.List;

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
        // MultipartFile -> AudioFileRequestDto 변환
        List<AudioFileRequestDto> audioDto = audioFiles.stream()
                .map(file -> AudioFileRequestDto.builder()
                        .audioFile(file)
                        .build())
                .toList();

        List<AudioFileRequestDto> audioFileRequestDtos = audioFileService.checkExtension(audioDto);
        if (audioFileRequestDtos.isEmpty()) {
            return new ResponseDto<>(HttpStatus.OK.value(), audioFileService.checkExtension(audioDto)).toResponseEntity();
        }
        return new ResponseEntity<>(
                new ResponseDto<>(HttpStatus.BAD_REQUEST.value(), audioFileRequestDtos), HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "save",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDto<List<OriginAudioRequest>>> save(
            @RequestParam("audio") List<MultipartFile> audioFiles) throws IOException, UnsupportedAudioFileException {
        // MultipartFile -> AudioFileRequestDto 변환
        List<AudioFileRequestDto> audioDto = audioFiles.stream()
                .map(file -> AudioFileRequestDto.builder()
                        .audioFile(file)
                        .build())
                .toList();

        List<OriginAudioRequest> originAudioRequests = audioFileService.saveAudioFile(audioDto);
        return new ResponseDto<>(HttpStatus.OK.value(), originAudioRequests).toResponseEntity();
    }

    // DataNotFoundException 처리
    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ResponseDto<String>> handleDataNotFoundException(DataNotFoundException ex) {
        String errorMessage = ex.getMessage();
        return new ResponseDto<>(HttpStatus.NOT_FOUND.value(), errorMessage).toResponseEntity();
    }

    @PostMapping("read")
    public ResponseEntity<ResponseDto<List<AudioFileDto>>> read(@RequestParam List<Long> concatRowSeq) {
        concatRowSeq.sort(Long::compareTo);

        List<AudioFile> audioFile = audioFileService.findByConcatRowSeq(concatRowSeq);
        List<AudioFileDto> list = audioFile.stream().map(af -> AudioFileDto.builder()
                .audioFileSeq(af.getAudioFileSeq())
                .audioUrl(af.getAudioUrl())
                .fileLength(af.getFileLength())
                .fileName(af.getFileName())
                .fileSize(af.getFileSize())
                .createdDate(af.getCreatedDate())
                .extension(af.getExtension()).build()).toList();
        return new ResponseDto<>(HttpStatus.OK.value(), list).toResponseEntity();

    }

    @PostMapping("execute")
    public void executeConcat() {

    }
}