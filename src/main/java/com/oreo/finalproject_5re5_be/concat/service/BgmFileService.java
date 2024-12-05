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
}