package com.oreo.finalproject_5re5_be.tts.repository;

import com.oreo.finalproject_5re5_be.tts.entity.TtsAudioFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TtsAudioFileRepository extends JpaRepository<TtsAudioFile, Long> {
}
