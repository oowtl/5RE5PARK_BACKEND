package com.oreo.finalproject_5re5_be.vc.repository;

import com.oreo.finalproject_5re5_be.vc.entity.VcSrcFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VcSrcFileRepository extends JpaRepository<VcSrcFile, Long> {
    @Query("select v from VcSrcFile v where v.vc.proSeq = :projectId")
    List<VcSrcFile> findByProjectId(@Param("projectId")Long projectId);
}