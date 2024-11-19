package com.oreo.finalproject_5re5_be.concat.service;

import com.oreo.finalproject_5re5_be.concat.dto.ConcatRowDto;
import com.oreo.finalproject_5re5_be.concat.dto.request.ConcatRowSaveRequestDto;
import com.oreo.finalproject_5re5_be.concat.entity.ConcatRow;
import com.oreo.finalproject_5re5_be.concat.entity.ConcatTab;
import com.oreo.finalproject_5re5_be.concat.repository.ConcatRowRepository;
import com.oreo.finalproject_5re5_be.concat.repository.ConcatTabRepository;
import com.oreo.finalproject_5re5_be.concat.service.helper.ConcatRowHelper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor

public class ConcatRowService {
    private ConcatRowRepository concatRowRepository;
    private ConcatTabRepository concatTabRepository;
    private final ConcatRowHelper concatRowHelper;

    //현재 Hibernate에서 데이터 무결성 확인을 위해 자동으로 project, concat_tab, concat_option을 확인 하는 쿼리를 생성함
    //
    @Transactional
    public boolean saveConcatRows(ConcatRowSaveRequestDto requestDto) {
        Optional<ConcatTab> concatTabOpt = concatTabRepository.findById(requestDto.getConcatTabId());

        if (concatTabOpt.isPresent()) {
            ConcatTab concatTab = concatTabOpt.get();
            List<ConcatRow> concatRowStream = requestDto.getConcatRowRequests()
                    .stream().map(crr -> ConcatRow.builder()
                            .concatTab(concatTab)
                            .rowText(crr.getRowText())
                            .selected(crr.getSelected())
                            .status(crr.getStatus())
                            .silence(crr.getSilence())
                            .rowIndex(crr.getRowIndex())
                            .build()).toList();
            concatRowHelper.batchInsert(concatRowStream);
            return true;
        }
        return false;
    }

    public List<ConcatRowDto> readRecentConcatRows(long projectSequence) {
        List<ConcatRow> concatRows = concatRowRepository
                .findByStatusAndConcatTab_Project_ProSeq('Y', projectSequence);
        if (concatRows.isEmpty()) {
            return new ArrayList<>();
        }
        return concatRows.stream().map(cr -> ConcatRowDto.builder()
                .concatRowSequence(cr.getConcatRowSeq())
                .rowText(cr.getRowText())
                .rowIndex(cr.getRowIndex())
                .selected(cr.getSelected())
                .silence(cr.getSilence())
                .build()
        ).toList();
    }

    public List<ConcatRowDto> readConcatRows(long concatRowSequence) {
        List<ConcatRow> concatRows = concatRowRepository
                .findByConcatRowSeq(concatRowSequence);
        if (concatRows.isEmpty()) {
            return new ArrayList<>();
        }
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
    public boolean disableConcatRows(ConcatRowSaveRequestDto requestDto) {
        Optional<ConcatTab> concatTabOpt = concatTabRepository.findById(requestDto.getConcatTabId());

        if (concatTabOpt.isPresent()) {

            List<Long> concatRowSeq = requestDto.getConcatRowRequests()
                    .stream().map(ConcatRowDto::getConcatRowSequence).toList();
            concatRowRepository.updateStatusByConcatRowSeq(concatRowSeq, 'N');//행 비활성 처리
            return true;
        }
        return false;
    }


    @Transactional(rollbackOn = Exception.class)//모든 예외에 대해 롤백 수행
    public boolean updateConcatRows(ConcatRowSaveRequestDto requestDto) {
        Optional<ConcatTab> concatTabOpt = concatTabRepository.findById(requestDto.getConcatTabId());

        if (concatTabOpt.isPresent()) {
            ConcatTab concatTab = concatTabOpt.get();
            return disableConcatRowsForUpdate(requestDto.getConcatRowRequests()) &&
                    createConcatRowsForUpdate(requestDto.getConcatRowRequests(), concatTab);
        }
        return false;
    }

    private boolean createConcatRowsForUpdate(List<ConcatRowDto> requestDto, ConcatTab concatTab) {
        List<ConcatRow> concatRowStream = requestDto
                .stream().filter(cr -> cr.getStatus() != 'N')
                .map(crr -> ConcatRow.builder()
                        .concatTab(concatTab)
                        .rowText(crr.getRowText())
                        .selected(crr.getSelected())
                        .status(crr.getStatus())
                        .silence(crr.getSilence())
                        .rowIndex(crr.getRowIndex())
                        .build()).toList();
        concatRowHelper.batchInsert(concatRowStream);
        return true;
    }

    private boolean disableConcatRowsForUpdate(List<ConcatRowDto> concatRows) {

        List<Long> concatRowSeq = concatRows
                .stream().map(ConcatRowDto::getConcatRowSequence).toList();
        //행 비활성 처리
        return concatRowRepository.updateStatusByConcatRowSeq(concatRowSeq, 'N') != 0;
    }
}