package com.oreo.finalproject_5re5_be.concat.service;

import com.oreo.finalproject_5re5_be.concat.dto.request.ConcatRowSaveRequestDto;
import com.oreo.finalproject_5re5_be.concat.dto.request.ConcatUpdateRequestDto;
import com.oreo.finalproject_5re5_be.concat.dto.request.TabRowUpdateRequestDto;
import com.oreo.finalproject_5re5_be.concat.entity.BgmFile;
import com.oreo.finalproject_5re5_be.concat.repository.BgmFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcatRowTabService {

    private final ConcatTabService concatTabService;
    private final ConcatRowService concatRowService;
    private final BgmFileService bgmFileService;
    private final BgmFileRepository bgmFileRepository;

    @Transactional
    public boolean saveTabAndRows(TabRowUpdateRequestDto dto, Long memberSeq) {
        // 1. ConcatTab 업데이트
        ConcatUpdateRequestDto concatTabs = dto.getConcatTab();
        boolean updateConcatTab = concatTabService.updateConcatTab(concatTabs, memberSeq);

        // 2. ConcatRow 업데이트
        ConcatRowSaveRequestDto concatRows = dto.getConcatRows();
        boolean updateConcatRows = concatRowService.updateConcatRows(concatRows);

        // 3. BgmFile 저장
        List<BgmFile> bgmFiles = bgmFileService.getBgmFilesByTabSeq(concatTabs.getTabId());
        bgmFileRepository.saveAll(bgmFiles);

        return updateConcatTab && updateConcatRows;
    }
}
