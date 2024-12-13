package com.oreo.finalproject_5re5_be.tts.service;

import com.oreo.finalproject_5re5_be.global.exception.EntityNotFoundException;
import com.oreo.finalproject_5re5_be.tts.dto.response.VoiceListDto;
import com.oreo.finalproject_5re5_be.tts.entity.Language;
import com.oreo.finalproject_5re5_be.tts.entity.Style;
import com.oreo.finalproject_5re5_be.tts.entity.Voice;
import com.oreo.finalproject_5re5_be.tts.repository.LanguageRepository;
import com.oreo.finalproject_5re5_be.tts.repository.StyleRepository;
import com.oreo.finalproject_5re5_be.tts.repository.VoiceRepository;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class VoiceService {
    private final VoiceRepository voiceRepository;
    private final LanguageRepository languageRepository;
    private final StyleRepository styleRepository;

    public VoiceService(
            VoiceRepository voiceRepository,
            LanguageRepository languageRepository,
            StyleRepository styleRepository) {
        this.voiceRepository = voiceRepository;
        this.languageRepository = languageRepository;
        this.styleRepository = styleRepository;
    }

    public VoiceListDto getVoiceList(@NotNull String languageCode, @NotNull String styleName) {
        // 1. languageCode로 language 정보 조회
        Language findLanguage =
                languageRepository
                        .findByLangCode(languageCode)
                        .orElseThrow(
                                () ->
                                        new EntityNotFoundException("유효하지 않는 언어 코드 입니다. languageCode:" + languageCode));

        // 2. styleName으로 style 정보 조회
        Style findStyle =
                styleRepository
                        .findByName(styleName)
                        .orElseThrow(
                                () -> new EntityNotFoundException("유효하지 않는 스타일명 입니다. styleName:" + styleName));

        // 3. language, style 정보로 목소리 리스트 조회
        List<Voice> findVoiceList =
                voiceRepository.findAllByLanguageAndStyleAndEnabled(findLanguage, findStyle, 'y');

        // 4. 조회 결과 응답 형태로 변환 후 반환
        return VoiceListDto.of(findVoiceList);
    }
}
