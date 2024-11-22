package com.oreo.finalproject_5re5_be.vc.repository;

import com.oreo.finalproject_5re5_be.vc.entity.VcSrcFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VcSrcFileRepository extends JpaRepository<VcSrcFile, Long> {
    List<VcSrcFile> findByVcProjectSeq(Long proSeq);
    Integer countByVc_ProjectSeq(Long projectSeq);
    boolean existsById(Long id);
}