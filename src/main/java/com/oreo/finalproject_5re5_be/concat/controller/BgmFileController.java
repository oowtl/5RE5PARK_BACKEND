package com.oreo.finalproject_5re5_be.concat.controller;

import com.oreo.finalproject_5re5_be.concat.entity.BgmFile;
import com.oreo.finalproject_5re5_be.concat.service.BgmFileService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bgms")
public class BgmFileController {
    private final BgmFileService bgmFileService;

    @GetMapping("/concat-tab/{tabSeq}")
    public ResponseEntity<List<BgmFile>> getBgmFiles(@PathVariable Long tabSeq) {
        List<BgmFile> bgmFiles = bgmFileService.getBgmFilesByTabSeq(tabSeq);
        return ResponseEntity.ok(bgmFiles);
    }
}
