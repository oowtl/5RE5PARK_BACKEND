package com.oreo.finalproject_5re5_be.tts.service;

import com.oreo.finalproject_5re5_be.tts.dto.response.StyleListDto;
import com.oreo.finalproject_5re5_be.tts.entity.Style;
import com.oreo.finalproject_5re5_be.tts.repository.StyleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StyleService {
    private final StyleRepository styleRepository;

    public StyleService(StyleRepository styleRepository) {
        this.styleRepository = styleRepository;
    }

    public StyleListDto getStyleList() {
        // 전체 스타일 목록 조회
        List<Style> styleList = styleRepository.findAll();

        // 스타일 목록 응답 형태로 변환하여 반환
        return StyleListDto.of(styleList);
    }
}
