package com.oreo.finalproject_5re5_be.concat.service;


import com.oreo.finalproject_5re5_be.concat.dto.request.OriginAudioRequest;
import com.oreo.finalproject_5re5_be.concat.dto.request.SelectedConcatRowRequest;
import com.oreo.finalproject_5re5_be.concat.dto.response.ConcatUrlResponse;
import com.oreo.finalproject_5re5_be.concat.entity.*;
import com.oreo.finalproject_5re5_be.concat.repository.AudioFileRepository;
import com.oreo.finalproject_5re5_be.concat.repository.BgmFileRepository;
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
    private final BgmFileRepository bgmFileRepository;

    // 결과에 사용된 BGM 파일 조회
    public BgmFile findBgmFileByConcatResultSeq(Long concatResultSeq) {
        List<BgmFile> bgmFiles = bgmFileRepository.findByConcatResultSeq(concatResultSeq);

        // 첫 번째 요소 반환 (없으면 null). 어차피 브금에 bgmFile 하나 들어감
        return bgmFiles.isEmpty() ? null : bgmFiles.get(0);
    }

    // 결과에 사용된 Material 파일 조회
    public List<OriginAudioRequest> findMaterialAudioFilesByConcatResultSeq(Long concatResultSeq) {
        return materialAudioRepository.findByConcatResultSeq(concatResultSeq).stream()
                .map(material -> OriginAudioRequest.builder()
                        .seq(material.getAudioFile().getAudioFileSeq())
                        .audioUrl(material.getAudioFile().getAudioUrl())
                        .extension(material.getAudioFile().getExtension())
                        .fileSize(material.getAudioFile().getFileSize())
                        .fileLength(material.getAudioFile().getFileLength())
                        .fileName(material.getAudioFile().getFileName())
                        .build())
                .toList();
    }

    //결과물seq로 결과물url조회
    public String findResultAudioUrlByConcatResultSeq(Long concatResultSeq) {
        ConcatResult concatResult = concatResultRepository.findById(concatResultSeq)
                .orElseThrow(() -> new IllegalArgumentException("ConcatResult not found with seq: " + concatResultSeq));
        return concatResult.getAudioUrl();
    }

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

    // concatResult의 seq로 매칭되는 audioFile seq들을 조회
    public List<Long> findAudioFileSeqsByConcatResultSeq(Long concatResultSeq) {
        return materialAudioRepository.findByConcatResultSeq(concatResultSeq)
                .stream()
                .map(materialAudio -> materialAudio.getAudioFile().getAudioFileSeq()) // AudioFile의 seq를 추출
                .collect(Collectors.toList());
    }

    // concatResult의 seq로 재료가 된 행 정보 리스트 조회
    public List<ConcatRow> findConcatRowListByResultSeq(Long concatResultSeq) {
        return materialAudioRepository.findConcatRowListByConcatResultSeq(concatResultSeq);
    }

    public boolean saveMaterialAudio(List<MaterialAudio> materialAudios) {
        try {
            materialAudioRepository.saveAll(materialAudios);
            return true;
        } catch (Exception e) {
            throw new IllegalArgumentException("재료 오디오 저장 실패", e);
        }
    }

    // 기존 기능 외 추가 메서드: SelectedConcatRowRequest 처리
    public void saveMaterialsForSelectedRows(SelectedConcatRowRequest selectedRows, ConcatUrlResponse concatResultResponse) {
        List<Long> usedAudioFileSeqs = selectedRows.getRows().stream()
                .map(SelectedConcatRowRequest.Row::getAudioUrl) // URL 추출
                .map(audioFileRepository::findByAudioUrl) // URL로 AudioFile 조회
                .map(audioFile -> audioFile.orElseThrow(() -> new IllegalArgumentException("AudioFile not found with URL: " + audioFile.get().getAudioUrl())))
                .map(AudioFile::getAudioFileSeq) // Seq 추출
                .toList();

        // 기존 saveMaterials 메서드를 호출하여 저장
        saveMaterials(concatResultResponse.getSeq(), usedAudioFileSeqs);
    }

}
