package com.oreo.finalproject_5re5_be.vc.repository;

import com.oreo.finalproject_5re5_be.vc.entity.VcResultFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface VcResultFileRepository extends JpaRepository<VcResultFile, Long> {
    VcResultFile findFirstBySrcSeq_SrcSeqOrderBySrcSeqDesc(Long srcSeq);
}
