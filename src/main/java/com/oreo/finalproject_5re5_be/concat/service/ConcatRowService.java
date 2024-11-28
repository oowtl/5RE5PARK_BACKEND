package com.oreo.finalproject_5re5_be.concat.service;

import com.oreo.finalproject_5re5_be.concat.dto.ConcatRowDto;
import com.oreo.finalproject_5re5_be.concat.dto.request.ConcatRowRequest;
import com.oreo.finalproject_5re5_be.concat.dto.request.ConcatRowRequestDto;
import com.oreo.finalproject_5re5_be.concat.dto.request.ConcatRowSaveRequestDto;
import com.oreo.finalproject_5re5_be.concat.dto.request.OriginAudioRequest;
import com.oreo.finalproject_5re5_be.concat.entity.AudioFile;
import com.oreo.finalproject_5re5_be.concat.entity.ConcatRow;
import com.oreo.finalproject_5re5_be.concat.entity.ConcatTab;
import com.oreo.finalproject_5re5_be.concat.repository.ConcatRowRepository;
import com.oreo.finalproject_5re5_be.concat.repository.ConcatTabRepository;
import com.oreo.finalproject_5re5_be.concat.service.helper.ConcatRowHelper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class ConcatRowService {
    private ConcatRowRepository concatRowRepository;
    private ConcatTabRepository concatTabRepository;
    private final ConcatRowHelper concatRowHelper;
    private final AudioFileService audioFileService;

    //현재 Hibernate에서 데이터 무결성 확인을 위해 자동으로 project, concat_tab, concat_option을 확인 하는 쿼리를 생성함
    public List<ConcatRowDto> readRecentConcatRows(long projectSequence) {
        List<ConcatRow> concatRows = concatRowRepository
                .findByStatusAndConcatTab_Project_ProSeq('Y', projectSequence);
        if (concatRows.isEmpty()) {
            return new ArrayList<>();
        }
        return concatRows.stream().map(cr -> ConcatRowDto.builder()
                .concatRowSequence(cr.getConcatRowSeq())
                .projectSequence(projectSequence)
                .rowText(cr.getRowText())
                .rowIndex(cr.getRowIndex())
                .selected(cr.getSelected())
                .status(cr.getStatus())
                .silence(cr.getSilence())
                .build()
        ).toList();
    }

    public List<ConcatRowDto> readConcatRows(long concatRowSequence) {
        List<ConcatRow> concatRows = concatRowRepository
                .findByConcatRowSeq(concatRowSequence);
        return concatRows.stream().map(cr -> ConcatRowDto.builder()
                .concatRowSequence(cr.getConcatRowSeq())
                .rowText(cr.getRowText())
                .rowIndex(cr.getRowIndex())
                .selected(cr.getSelected())
                .silence(cr.getSilence())
                .build()
        ).toList();
    }

    @Transactional
    public boolean disableConcatRows(List<Long> rowSeq) {

        concatRowRepository.updateStatusByConcatRowSeq(rowSeq, 'N');//행 비활성 처리
        return true;
    }


    @Transactional(rollbackOn = Exception.class)//모든 예외에 대해 롤백 수행
    public boolean updateConcatRows(ConcatRowSaveRequestDto requestDto) {
        Optional<ConcatTab> concatTabOpt = concatTabRepository.findById(requestDto.getConcatTabId());

        if (concatTabOpt.isPresent()) {
            return disableConcatRowsForUpdate(requestDto.getConcatRowRequests()) &&
                    saveConcatRows(requestDto);
        }
        return false;
    }

    private boolean disableConcatRowsForUpdate(List<ConcatRowRequest> concatRows) {

        List<Long> concatRowSeq = concatRows
                .stream().map(ConcatRowRequest::getSeq).toList();
        //행 비활성 처리
        return concatRowRepository.updateStatusByConcatRowSeq(concatRowSeq, 'N') != 0;
    }

    public ConcatRow readConcatRow(Long concatRowSeq) {
        return concatRowRepository.findById(concatRowSeq).orElse(null);
    }


    @Transactional
    public boolean saveConcatRows(ConcatRowSaveRequestDto requestDto) {
        // 1. ConcatTab 확인 및 조회
        ConcatTab concatTab = concatTabRepository.findById(requestDto.getConcatTabId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid ConcatTab ID: " + requestDto.getConcatTabId()));

        List<AudioFile> audioFiles = new ArrayList<>();
        // 2. ConcatRowRequest 처리
        List<ConcatRow> concatRows = getConcatRows(requestDto.getConcatRowRequests(), concatTab, audioFiles);

        // 3. ConcatRow, 저장
        concatRowHelper.batchInsert(concatRows);
        audioFileService.saveAudioFiles(audioFiles);
        return true;
    }

    @Transactional
    public boolean saveConcatRows(ConcatRowRequestDto requestDto) {
        // 1. ConcatTab 확인 및 조회
        ConcatTab concatTab = concatTabRepository.findById(requestDto.getConcatTabId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid ConcatTab ID: " + requestDto.getConcatTabId()));

        List<AudioFile> audioFiles = new ArrayList<>();
        // 2. ConcatRowRequest 처리
        List<ConcatRow> concatRows = getConcatRows(requestDto.getConcatRowRequests(), concatTab, audioFiles);

        // 3. ConcatRow, 저장
        concatRowHelper.batchInsert(concatRows);
        audioFileService.saveAudioFiles(audioFiles);
        return true;
    }

    private List<ConcatRow> getConcatRows(List<ConcatRowRequest> requestDto, ConcatTab concatTab, List<AudioFile> audioFiles) {
        return requestDto.stream()
                .filter(rowRequest -> rowRequest.getStatus() != 'N').map(rowRequest -> {

                    // 2.2 ConcatRow 생성
                    ConcatRow concatRow = ConcatRow.builder()
                            .concatTab(concatTab)
                            .rowText(rowRequest.getRowText())
                            .selected(rowRequest.getSelected())
                            .silence(rowRequest.getRowSilence())
                            .rowIndex(rowRequest.getRowIndex())
                            .status('Y') // 기본 상태 설정
                            .build();

                    // 2.3 AudioFile 저장 및 연관 설정
                    AudioFile originAudio = mapToAudioFile(rowRequest.getOriginAudioRequest(), concatRow);
                    audioFiles.add(originAudio);

                    return concatRow;
                }).toList();
    }

    private AudioFile mapToAudioFile(OriginAudioRequest originAudioRequest, ConcatRow concatRow) {
        // AudioFile 매핑
        return AudioFile.builder()
                .concatRow(concatRow)
                .audioUrl(originAudioRequest.getAudioUrl())
                .extension(originAudioRequest.getExtension())
                .fileSize(originAudioRequest.getFileSize())
                .fileLength(originAudioRequest.getFileLength())
                .fileName(originAudioRequest.getFileName())
                .build();
    }

    public boolean uploadText(List<ConcatRowRequest> concatRowSaveRequestDto) {
        boolean check = concatRowSaveRequestDto.stream()
                .anyMatch(rowRequest -> rowRequest.getStatus() == 'N' || rowRequest.getSeq() == null);
        if (check) {
            return false;
        }

        List<ConcatRow> list = concatRowSaveRequestDto.stream().map(cr -> ConcatRow
                .builder()
                .concatRowSeq(cr.getSeq())
                .rowText(cr.getRowText()).build()).toList();
        concatRowHelper.batchInsert(list);
        return true;
    }
}