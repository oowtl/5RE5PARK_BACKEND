package com.oreo.finalproject_5re5_be.tts.repository;

import com.oreo.finalproject_5re5_be.project.entity.Project;
import com.oreo.finalproject_5re5_be.tts.entity.TtsSentence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TtsSentenceRepository extends JpaRepository<TtsSentence, Long> {
    List<TtsSentence> findAllByProject(Project project);
}
