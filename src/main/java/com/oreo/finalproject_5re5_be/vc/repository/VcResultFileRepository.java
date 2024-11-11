package com.oreo.finalproject_5re5_be.vc.repository;

import com.oreo.finalproject_5re5_be.vc.entity.VcResultFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VcResultFileRepository extends JpaRepository<VcResultFile, Long> {
    VcResultFile findBySrcSeq(Long srcSeq);
}
