package com.oreo.finalproject_5re5_be.project.repository;

import com.oreo.finalproject_5re5_be.project.entity.Project;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("SELECT p FROM Project p JOIN p.member m WHERE m.seq = :memberSeq")
    List<Project> findByMemberSeq(@Param("memberSeq") Long memberSeq);
}
