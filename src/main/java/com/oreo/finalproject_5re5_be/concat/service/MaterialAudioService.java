package com.oreo.finalproject_5re5_be.concat.service;


import com.oreo.finalproject_5re5_be.concat.dto.response.ConcatUrlResponse;
import com.oreo.finalproject_5re5_be.concat.entity.AudioFile;
import com.oreo.finalproject_5re5_be.concat.entity.ConcatResult;
import com.oreo.finalproject_5re5_be.concat.entity.MaterialAudio;
import com.oreo.finalproject_5re5_be.concat.repository.AudioFileRepository;
import com.oreo.finalproject_5re5_be.concat.repository.ConcatResultRepository;
import com.oreo.finalproject_5re5_be.concat.repository.MaterialAudioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MaterialAudioService {

    private final MaterialAudioRepository materialAudioRepository;
    private final ConcatResultRepository concatResultRepository;
    private final AudioFileRepository audioFileRepository;

    // 1개의 concatResult와 그에 매칭되는 1개의 AudioFile을 저장 (1개)
    public MaterialAudio saveMaterial(Long concatResultSeq, Long audioFileSeq) {
        // ConcatResult 조회
        ConcatResult concatResult = concatResultRepository.findById(concatResultSeq)
                .orElseThrow(() -> new IllegalArgumentException("ConcatResult not found with id: " + concatResultSeq));

        // AudioFile 조회
        AudioFile audioFile = audioFileRepository.findById(audioFileSeq)
                .orElseThrow(() -> new IllegalArgumentException("AudioFile not found with id: " + audioFileSeq));

        // MaterialAudio 생성
        MaterialAudio materialAudio = MaterialAudio.builder()
                .concatResult(concatResult)
                .audioFile(audioFile)
                .build();

        //객체를 저장
        return materialAudioRepository.save(materialAudio);

    }


    // 1개의 concatResult와 그에 매칭되는 여러개의 AudioFile을 저장 (N개)
    public List<MaterialAudio> saveMaterials(Long concatResultSeq, List<Long> audioFileSeqs) {
        // ConcatResult 조회
        ConcatResult concatResult = concatResultRepository.findById(concatResultSeq)
                .orElseThrow(() -> new IllegalArgumentException("ConcatResult not found with id: " + concatResultSeq));

        // AudioFileSeq를 하나씩 처리하여 MaterialAudio 생성 및 저장
        return audioFileSeqs.stream()
                .map(audioFileSeq -> {
                    // AudioFile 조회
                    AudioFile audioFile = audioFileRepository.findById(audioFileSeq)
                            .orElseThrow(() -> new IllegalArgumentException("AudioFile not found with id: " + audioFileSeq));

                    // MaterialAudio 생성
                    MaterialAudio materialAudio = MaterialAudio.builder()
                            .concatResult(concatResult)
                            .audioFile(audioFile)
                            .build();

                    // 저장
                    return materialAudioRepository.save(materialAudio);
                })
                .collect(Collectors.toList());
    }


    // concatResultSeq와 매칭되는 audioFile List 조회
    public List<ConcatUrlResponse> findAudioFilesByConcatResultSeq(Long concatResultSeq) {
        //MaterialAudio에서 AudioFile 리스트 조회
        List<AudioFile> audioFiles = materialAudioRepository.findAudioFilesByConcatResult(concatResultSeq);

        //AudioFile -> ConcatUrlResponse 변환
        return audioFiles.stream()
                .map(audioFile -> ConcatUrlResponse.builder()
                        .seq(audioFile.getAudioFileSeq())
                        .url(audioFile.getAudioUrl())
                        .build())
                .collect(Collectors.toList());

    }

    // AudioFile과 매칭되는 concatResult List 조회
    public List<ConcatResult> findConcatResultsByAudioFileSeq(Long audioFileSeq) {
        return materialAudioRepository.findConcatResultsByAudioFileSeq(audioFileSeq);
    }

    // 특정 concatResult에 매칭되는 모든 MaterialAudio 삭제
    public void deleteMaterialsByConcatResultSeq(Long concatResultSeq) {
        materialAudioRepository.deleteByConcatResultSeq(concatResultSeq);
    }


}
