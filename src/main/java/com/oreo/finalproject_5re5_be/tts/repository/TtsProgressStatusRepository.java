package com.oreo.finalproject_5re5_be.tts.repository;

import com.oreo.finalproject_5re5_be.tts.entity.TtsProgressStatus;
import com.oreo.finalproject_5re5_be.tts.entity.TtsSentence;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TtsProgressStatusRepository extends JpaRepository<TtsProgressStatus, Long> {
    List<TtsProgressStatus> findAllByTtsSentence(TtsSentence ttsSentence);
}
