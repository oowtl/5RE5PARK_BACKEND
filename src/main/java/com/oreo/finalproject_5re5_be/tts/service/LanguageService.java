package com.oreo.finalproject_5re5_be.tts.service;

import com.oreo.finalproject_5re5_be.tts.dto.response.LanguageListDto;
import com.oreo.finalproject_5re5_be.tts.entity.Language;
import com.oreo.finalproject_5re5_be.tts.repository.LanguageRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class LanguageService {
    private final LanguageRepository languageRepository;

    public LanguageService(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    public LanguageListDto getLanguageList() {
        // 전체 언어 목록 조회
        List<Language> languageList = languageRepository.findAll();

        // 언어 목록 응답 형태로 변환하여 반환
        return LanguageListDto.of(languageList);
    }
}
