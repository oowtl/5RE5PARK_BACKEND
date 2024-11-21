package com.oreo.finalproject_5re5_be.concat.repository;

import com.oreo.finalproject_5re5_be.concat.entity.ConcatResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConcatResultRepository extends JpaRepository<ConcatResult, Long> {
    Optional<ConcatResult> findByConcatResultSequence(Long concatResultSeq);
}
