package com.oreo.finalproject_5re5_be.concat.service;

import com.oreo.finalproject_5re5_be.concat.entity.BgmFile;
import com.oreo.finalproject_5re5_be.concat.entity.ConcatResult;
import com.oreo.finalproject_5re5_be.concat.repository.BgmFileRepository;
import com.oreo.finalproject_5re5_be.concat.repository.ConcatResultRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class BgmFileService {

    private final BgmFileRepository bgmFileRepository;
    private final ConcatResultRepository concatResultRepository;

    public List<BgmFile> getBgmFilesByTabSeq(Long tabSeq) {
        return bgmFileRepository.findByConcatTabSeq(tabSeq);
    }

    public BgmFile getBgmFileByUrl(String bgmFileUrl) {
        return bgmFileRepository
                .findByAudioUrl(bgmFileUrl)
                .orElseThrow(
                        () -> new IllegalArgumentException("BGM File not found with URL: " + bgmFileUrl));
    }

    public BgmFile saveBgmFile(BgmFile bgmFile) {
        return bgmFileRepository.save(bgmFile);
    }

    public void updateBgmFileWithConcatResult(String bgmFileUrl, Long concatResultSeq) {
        // 중복 데이터가 있을 경우 처리
        List<BgmFile> bgmFiles = bgmFileRepository.findAllByAudioUrl(bgmFileUrl);
        if (bgmFiles.isEmpty()) {
            throw new IllegalArgumentException("BgmFile not found for URL: " + bgmFileUrl);
        }
        if (bgmFiles.size() > 1) {
            log.warn("Multiple BGM files found for URL: {}. Using the first result.", bgmFileUrl);
        }

        // 첫 번째 결과에 대해서만 처리
        BgmFile bgmFile = bgmFiles.get(0);
        ConcatResult concatResult =
                concatResultRepository
                        .findById(concatResultSeq)
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "ConcatResult not found for id: " + concatResultSeq));
        bgmFile.setConcatResult(concatResult);
        bgmFileRepository.save(bgmFile);

        log.info("[updateBgmFileWithConcatResult] Updated BgmFile with ConcatResult: {}", bgmFile);
    }
}
