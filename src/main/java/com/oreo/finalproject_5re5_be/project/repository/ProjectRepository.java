package com.oreo.finalproject_5re5_be.project.repository;

import com.oreo.finalproject_5re5_be.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
}
