package com.oreo.finalproject_5re5_be.member.repository;

import com.oreo.finalproject_5re5_be.member.entity.MemberChangeHistory;
import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberChangeHistoryRepository extends JpaRepository<MemberChangeHistory, Long> {

    public MemberChangeHistory findByChngHistSeq(Long chngHistSeq);


    @Query( "SELECT m FROM MemberChangeHistory m " +
            "WHERE m.chngFieldCode.code = :code " +
            "AND m.member.seq = :memberSeq " +
            "AND m.chngHistSeq = (SELECT MAX(subM.chngHistSeq) " +
            "                     FROM MemberChangeHistory subM " +
            "                     WHERE subM.chngFieldCode.code = :code " +
            "                     AND subM.member.seq = :memberSeq)")
    public Optional<MemberChangeHistory> findLatestHistoryByIdAndCode( @Param("memberSeq") Long memberSeq, @Param("code") String memberFiledCode);
    @Query( "DELETE FROM MemberChangeHistory mch " +
            "WHERE mch.member.seq = :memberSeq")
    List<MemberChangeHistory> findMemberChangeHistoriesByMemberSeq(Long memberSeq);
}
