package com.oreo.finalproject_5re5_be.vc.repository;

import com.oreo.finalproject_5re5_be.vc.entity.VcText;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VcTextRepository extends JpaRepository<VcText, Long> {
    VcText findFirstBySrcSeq_SrcSeqOrderBySrcSeqDesc(Long seq);
}
