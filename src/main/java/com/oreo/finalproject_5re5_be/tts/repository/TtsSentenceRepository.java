package com.oreo.finalproject_5re5_be.tts.repository;

import com.oreo.finalproject_5re5_be.project.entity.Project;
import com.oreo.finalproject_5re5_be.tts.entity.TtsSentence;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TtsSentenceRepository extends JpaRepository<TtsSentence, Long> {
    List<TtsSentence> findAllByProjectOrderBySortOrder(Project project);

    Boolean existsByProject_ProSeq(Long proSeq);
}
