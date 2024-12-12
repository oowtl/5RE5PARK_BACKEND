package com.oreo.finalproject_5re5_be.concat.service;

import com.oreo.finalproject_5re5_be.concat.entity.BgmFile;
import com.oreo.finalproject_5re5_be.concat.repository.BgmFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BgmFileService {

    private final BgmFileRepository bgmFileRepository;


    public List<BgmFile> getBgmFilesByTabSeq(Long tabSeq) {
        return bgmFileRepository.findByConcatTabSeq(tabSeq);
    }

    public BgmFile getBgmFileByUrl(String bgmFileUrl) {
        return bgmFileRepository.findByAudioUrl(bgmFileUrl)
                .orElseThrow(() -> new IllegalArgumentException("BGM File not found with URL: " + bgmFileUrl));
    }

    public BgmFile saveBgmFile(BgmFile bgmFile) {
        return bgmFileRepository.save(bgmFile);
    }


}