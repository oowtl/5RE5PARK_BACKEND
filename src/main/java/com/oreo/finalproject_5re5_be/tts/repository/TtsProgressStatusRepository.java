package com.oreo.finalproject_5re5_be.tts.repository;

import com.oreo.finalproject_5re5_be.tts.entity.TtsProgressStatus;
import com.oreo.finalproject_5re5_be.tts.entity.TtsSentence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TtsProgressStatusRepository extends JpaRepository<TtsProgressStatus, Long> {
    List<TtsProgressStatus> findAllByTtsSentence(TtsSentence ttsSentence);
}
