package com.oreo.finalproject_5re5_be.tts.repository;

import com.oreo.finalproject_5re5_be.tts.entity.Voice;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Repository
public interface VoiceRepository extends JpaRepository<Voice, Long> {
    List<Voice> findAllByName(String name);
    List<Voice> findAllByNameContaining(String name);
    Optional<Voice> findByVoiceSeq(Long voiceSeq);
}
