package com.oreo.finalproject_5re5_be.concat.repository;

import com.oreo.finalproject_5re5_be.concat.entity.ConcatRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConcatRowRepository extends JpaRepository<ConcatRow, Long> {
    List<ConcatRow> findByStatusAndConcatTab_Project_ProSeq(Character status, Long projectSeq);
    @Modifying
    @Query(value = "UPDATE concat_row " +
            "SET status = :status " +
            "WHERE concat_row_seq IN :concatRowSeq",
            nativeQuery = true)
    int updateStatusByConcatRowSeq(@Param("concatRowSeq") List<Long> concatRowSeq,
                                   @Param("status") Character status);

    List<ConcatRow> findByConcatRowSeq(long concatRowSequence);


    @Query(value = "SELECT concat_row_seq " +
            " FROM concat_row" +
            " WHERE pro_seq=:proSeq",
            nativeQuery = true)
    List<Long> findConcatRowIdByProjectSeq(@Param("proSeq") Long projectSeq);


}
