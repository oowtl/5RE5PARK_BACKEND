package com.oreo.finalproject_5re5_be.concat.service;

import com.oreo.finalproject_5re5_be.concat.dto.request.ConcatRowSaveRequestDto;
import com.oreo.finalproject_5re5_be.concat.dto.request.ConcatUpdateRequestDto;
import com.oreo.finalproject_5re5_be.concat.dto.request.TabRowUpdateRequestDto;
import com.oreo.finalproject_5re5_be.concat.entity.BgmFile;
import com.oreo.finalproject_5re5_be.concat.entity.ConcatTab;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConcatRowTabService {

    private final ConcatTabService concatTabService;
    private final ConcatRowService concatRowService;

    @Transactional // 트랜잭션이 완료되기 전까지 엔티티 변경내용 적용 안됨
    public boolean saveTabAndRows(TabRowUpdateRequestDto dto, Long memberSeq) {
        // 1. ConcatTab 업데이트
        ConcatUpdateRequestDto concatTabs = dto.getConcatTab();
        // 2. ConcatRow 업데이트 (말만 업데이트고 사실 생성임)
        ConcatRowSaveRequestDto concatRows = dto.getConcatRows();

        // 3. BgmFile 저장
        ConcatTab concatTab = concatTabService.getConcatTabBySeq(concatTabs.getTabId()); // ConcatTab 조회

        List<BgmFile> bgmFiles =
                concatTabs.getBgmFileList().stream()
                        .map(
                                bgmRequest ->
                                        BgmFile.builder()
                                                .concatTab(concatTab)
                                                .audioUrl(bgmRequest.getAudioUrl())
                                                .fileName(bgmRequest.getFileName())
                                                .fileSize(bgmRequest.getFileSize())
                                                .fileLength(bgmRequest.getFileLength())
                                                .extension(bgmRequest.getExtension())
                                                .build())
                        .toList();
        concatTab.addBgmFile(bgmFiles);

        return concatTabService.updateConcatTab(concatTabs, memberSeq)
                && concatRowService.updateConcatRows(concatRows);
    }
}
