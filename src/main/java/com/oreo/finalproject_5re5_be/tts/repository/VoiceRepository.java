package com.oreo.finalproject_5re5_be.tts.repository;

import com.oreo.finalproject_5re5_be.tts.entity.Language;
import com.oreo.finalproject_5re5_be.tts.entity.Style;
import com.oreo.finalproject_5re5_be.tts.entity.Voice;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoiceRepository extends JpaRepository<Voice, Long> {
    List<Voice> findAllByName(String name);

    List<Voice> findAllByNameContaining(String name);

    List<Voice> findAllByLanguageAndStyleAndEnabled(Language language, Style style, char enabled);
}
