package com.oreo.finalproject_5re5_be.tts.service;

import com.oreo.finalproject_5re5_be.global.exception.EntityNotFoundException;
import com.oreo.finalproject_5re5_be.tts.dto.response.StyleListDto;
import com.oreo.finalproject_5re5_be.tts.entity.Language;
import com.oreo.finalproject_5re5_be.tts.entity.Style;
import com.oreo.finalproject_5re5_be.tts.repository.LanguageRepository;
import com.oreo.finalproject_5re5_be.tts.repository.StyleRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class StyleService {
    private final StyleRepository styleRepository;
    private final LanguageRepository languageRepository;

    public StyleService(StyleRepository styleRepository, LanguageRepository languageRepository) {
        this.styleRepository = styleRepository;
        this.languageRepository = languageRepository;
    }

    public StyleListDto getStyleList() {
        // 전체 스타일 목록 조회
        List<Style> styleList = styleRepository.findAll();

        // 스타일 목록 응답 형태로 변환하여 반환
        return StyleListDto.of(styleList);
    }

    public StyleListDto getStyleListByLang(String langCode) {
        // langCode로 유효한 Language 조회
        Language findLanguage =
                languageRepository
                        .findByLangCode(langCode)
                        .orElseThrow(
                                () ->
                                        new EntityNotFoundException(
                                                "해당 언어 코드로 언어 정보를 조회할 수 없습니다. lanuageCode: " + langCode));

        // langSeq로 voice가 있는 style 조회
        List<Style> styleList = styleRepository.findListBylangSeq(findLanguage.getLangSeq());

        // 응답 형태로 변환하여 반환
        return StyleListDto.of(styleList);
    }
}
