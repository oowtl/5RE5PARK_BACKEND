package com.oreo.finalproject_5re5_be.concat.service.lambda;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oreo.finalproject_5re5_be.concat.dto.ConcatResultDto;
import com.oreo.finalproject_5re5_be.concat.dto.lambda.AudioFormatDto;
import com.oreo.finalproject_5re5_be.concat.dto.lambda.LambdaConcatRequest;
import com.oreo.finalproject_5re5_be.concat.dto.lambda.response.LambdaConcatResultDto;
import com.oreo.finalproject_5re5_be.concat.dto.request.ConcatRowRequestDto;
import com.oreo.finalproject_5re5_be.concat.dto.request.OriginAudioRequest;
import com.oreo.finalproject_5re5_be.concat.dto.response.ConcatTabResponseDto;
import com.oreo.finalproject_5re5_be.concat.entity.AudioFile;
import com.oreo.finalproject_5re5_be.concat.entity.ConcatResult;
import com.oreo.finalproject_5re5_be.concat.entity.ConcatTab;
import com.oreo.finalproject_5re5_be.concat.entity.MaterialAudio;
import com.oreo.finalproject_5re5_be.concat.service.ConcatResultService;
import com.oreo.finalproject_5re5_be.concat.service.MaterialAudioService;
import com.oreo.finalproject_5re5_be.global.component.audio.AudioFormats;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class LambdaConcatService {

    private final ConcatResultService concatResultService;
    private final MaterialAudioService materialAudioService;
    private final RestTemplate restTemplate;

    // S3 저장까지 완료된 상태
    // 해야하는거
    // 재료 오디오 DB 저장
    // 결과 오디오 DB 저장
    // 병합 결과 반환
    public List<ConcatResultDto> concatOnLambda(
            ConcatTabResponseDto concatTabResponseDto, ConcatRowRequestDto audioRequests) {
        LambdaConcatRequest lambdaConcatRequest =
                LambdaConcatRequest.builder()
                        .concatTabResponseDto(concatTabResponseDto)
                        .audioFormatDto(new AudioFormatDto(AudioFormats.STEREO_FORMAT_SR441_B16))
                        .audios(audioRequests.getConcatRowRequests())
                        .fileName(audioRequests.getFileName())
                        .build();

        List<LambdaConcatResultDto> list = lambdaConcatRequest(lambdaConcatRequest);

        List<ConcatResult> concatResults = getConcatResults(concatTabResponseDto, list);

        return getConcatResultDtos(concatResults);
    }

    private static List<ConcatResultDto> getConcatResultDtos(List<ConcatResult> concatResults) {
        return concatResults.stream()
                .map(
                        concatResult ->
                                ConcatResultDto.builder()
                                        .concatResultSequence(concatResult.getConcatResultSequence())
                                        .audioUrl(concatResult.getAudioUrl())
                                        .fileName(concatResult.getFileName())
                                        .fileLength(concatResult.getFileLength())
                                        .fileSize(concatResult.getFileSize())
                                        .extension(concatResult.getExtension())
                                        .seperated(concatResult.getSeperated())
                                        .processId(concatResult.getProcessId())
                                        .build())
                .toList();
    }

    private List<LambdaConcatResultDto> lambdaConcatRequest(LambdaConcatRequest lambdaConcatRequest) {
        Object response =
                restTemplate.postForObject(
                        "https://bewpsh81xa.execute-api.ap-northeast-2.amazonaws.com/default/Test", // Lambda
                        // URL
                        lambdaConcatRequest,
                        Object.class);

        if (response == null) {
            throw new IllegalArgumentException("오디오 병합 실패 : 반환값이 없습니다.");
        }

        // ObjectMapper를 사용해 JSON을 List<LambdaConcatResultDto>로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(response, new TypeReference<>() {});
    }

    private List<ConcatResult> getConcatResults(
            ConcatTabResponseDto concatTabResponseDto, List<LambdaConcatResultDto> list) {
        // 재료 오디오와 결과 저장
        /*
         * 결과가 여러개로 나눠져 올 가능성 있음
         * 결과 하나당 재료 오디오 여러개
         * List<MaterialAudio> 하나는 나눠져 온 결과 오디오 하나를 변환 한 값
         * */
        List<ConcatResult> concatResults = new ArrayList<>();
        List<List<MaterialAudio>> materials =
                list.stream()
                        .map(
                                result -> {
                                    ConcatResult concatResult = of(result, concatTabResponseDto);
                                    concatResults.add(concatResult);
                                    List<OriginAudioRequest> processed = result.getProcessed();
                                    return processed.stream()
                                            .map(
                                                    process ->
                                                            MaterialAudio.builder()
                                                                    .concatResult(concatResult)
                                                                    .audioFile(
                                                                            AudioFile.builder().audioFileSeq(process.getSeq()).build())
                                                                    .method("NORMAL")
                                                                    .build())
                                            .toList();
                                })
                        .toList();

        materials.forEach(materialAudioService::saveMaterialAudio);
        return concatResults;
    }

    public ConcatResult of(
            LambdaConcatResultDto concatResult, ConcatTabResponseDto concatTabResponseDto) {
        ConcatResult build =
                ConcatResult.builder()
                        .concatTab(ConcatTab.builder().projectId(concatTabResponseDto.getTabId()).build())
                        .fileName(concatResult.getInfo().getFileName())
                        .extension(concatResult.getInfo().getContentType())
                        .fileSize(concatResult.getInfo().getContentSize())
                        .fileLength(concatResult.getInfo().getContentLength())
                        .audioUrl(concatResult.getS3Url())
                        .processId(concatResult.getProcessId())
                        .seperated(concatResult.getI())
                        .build();
        return concatResultService.saveConcatResult(build);
    }
}
