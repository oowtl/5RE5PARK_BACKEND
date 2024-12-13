package com.oreo.finalproject_5re5_be.concat.service;

import com.oreo.finalproject_5re5_be.concat.dto.ConcatResultDto;
import com.oreo.finalproject_5re5_be.concat.dto.request.ConcatResultRequest;
import com.oreo.finalproject_5re5_be.concat.dto.response.ConcatResultDetailsResponse;
import com.oreo.finalproject_5re5_be.concat.dto.response.ConcatUrlResponse;
import com.oreo.finalproject_5re5_be.concat.entity.ConcatResult;
import com.oreo.finalproject_5re5_be.concat.entity.ConcatTab;
import com.oreo.finalproject_5re5_be.concat.repository.ConcatResultRepository;
import com.oreo.finalproject_5re5_be.concat.repository.ConcatTabRepository;
import java.util.List;
import javax.sound.sampled.AudioInputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ConcatResultService {

    private final ConcatResultRepository concatResultRepository;
    private final ConcatTabRepository concatTabRepository;

    // ConcatResult 정보를 받아서 저장 (1개)
    public ConcatUrlResponse saveConcatResult(ConcatResultRequest request) {
        // 1. ConcatTab 조회
        ConcatTab concatTab =
                concatTabRepository
                        .findById(request.getConcatTabSeq())
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "Invalid ConcatTab ID: " + request.getConcatTabSeq()));

        // 조회한 값과 입력한 값 저장을 하기 위한 ConcatResult 객체 생성
        ConcatResult concatResult =
                ConcatResult.builder()
                        .concatTab(concatTab) // concatTab 객체 필요
                        .audioUrl(request.getResultUrl())
                        .extension(request.getResultExtension())
                        .fileLength(request.getResultFileLength())
                        .fileName(request.getResultFileName())
                        .build();

        ConcatResult result = concatResultRepository.save(concatResult);

        return ConcatUrlResponse.builder() // response 객체 생성
                .seq(result.getConcatResultSequence()) // 저장된 객체의 seq
                .url(result.getAudioUrl()) // 저장된 객체의 url
                .build();
    }

    // AudioInputStream에서 ConcatResult 저장
    public ConcatUrlResponse saveConcatResult(
            Long concatTabSeq, String audioUrl, String fileName, AudioInputStream mixedAudioStream) {
        // 1. AudioInputStream을 사용하여 길이 계산
        long fileLength =
                (long) (mixedAudioStream.getFrameLength() / mixedAudioStream.getFormat().getFrameRate());

        // 2. ConcatResultRequest 생성
        ConcatResultRequest request =
                ConcatResultRequest.builder()
                        .concatTabSeq(concatTabSeq)
                        .ResultUrl(audioUrl)
                        .ResultFileName(fileName + ".wav")
                        .ResultExtension("wav")
                        .ResultFileLength((float) fileLength)
                        .build();

        // 3. 기존 메서드를 호출하여 저장
        return saveConcatResult(request);
    }

    public ConcatResult saveConcatResult(ConcatResult request) {
        return concatResultRepository.save(request);
    }

    // ConcatResult Seq를 받아서 ConcatResult의 세부 정보 조회 (1개)
    public ConcatResultDetailsResponse getConcatResultDetails(Long concatResultSeq) {
        // ConcatResult 조회
        ConcatResult concatResult =
                concatResultRepository
                        .findByConcatResultSequence(concatResultSeq)
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "ConcatResult not found with seq: " + concatResultSeq));

        // Response에 정보 담기
        return ConcatResultDetailsResponse.builder()
                .concatTabSeq(concatResult.getConcatTab().getProjectId())
                .audioUrl(concatResult.getAudioUrl())
                .extension(concatResult.getExtension())
                .fileLength(concatResult.getFileLength())
                .fileName(concatResult.getFileName())
                .build();
    }

    // ConcatResult Seq를 받아서 해당 ConcatResult를 삭제 (1개)
    public void deleteConcatResultBySeq(Long concatResultSeq) {
        // ConcatResult 존재 여부 확인
        ConcatResult concatResult =
                concatResultRepository
                        .findById(concatResultSeq)
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "ConcatResult not found with seq: " + concatResultSeq));

        // 삭제
        concatResultRepository.delete(concatResult);
    }

    public List<ConcatResultDto> findByConcatTabSequence(Long projectSeq) {
        List<ConcatResult> byProjectSeq = concatResultRepository.findByConcatTabSequence(projectSeq);
        return byProjectSeq.stream()
                .map(
                        cr ->
                                ConcatResultDto.builder()
                                        .concatResultSequence(cr.getConcatResultSequence())
                                        .fileName(cr.getFileName())
                                        .extension(cr.getExtension())
                                        .fileSize(cr.getFileSize())
                                        .audioUrl(cr.getAudioUrl())
                                        .fileLength(cr.getFileLength())
                                        .seperated(cr.getSeperated())
                                        .processId(cr.getProcessId())
                                        .build())
                .toList();
    }
}
