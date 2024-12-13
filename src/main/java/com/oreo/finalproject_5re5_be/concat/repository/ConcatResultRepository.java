package com.oreo.finalproject_5re5_be.concat.repository;

import com.oreo.finalproject_5re5_be.concat.entity.ConcatResult;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ConcatResultRepository extends JpaRepository<ConcatResult, Long> {
    Optional<ConcatResult> findByConcatResultSequence(Long concatResultSeq);

    @Query(value = "SELECT * FROM concat_result WHERE pro_seq = :proSeq", nativeQuery = true)
    List<ConcatResult> findByConcatTabSequence(@Param("proSeq") Long proSeq);
}
