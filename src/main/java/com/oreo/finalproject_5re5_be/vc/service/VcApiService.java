package com.oreo.finalproject_5re5_be.vc.service;

import org.springframework.web.multipart.MultipartFile;

public interface VcApiService {
    /**
     * 1. TRG ID 값 생성 및 추출
     * 2. File + TRG ID = Result File 추출
     */
    String trgIdCreate(MultipartFile file);
    MultipartFile resultFileCreate(MultipartFile file, String trgId);
}
