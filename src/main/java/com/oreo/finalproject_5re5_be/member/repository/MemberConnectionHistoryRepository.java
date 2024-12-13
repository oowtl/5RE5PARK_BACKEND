package com.oreo.finalproject_5re5_be.member.repository;

import com.oreo.finalproject_5re5_be.member.entity.MemberConnectionHistory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberConnectionHistoryRepository
        extends JpaRepository<MemberConnectionHistory, Long> {

    @Query("DELETE FROM MemberConnectionHistory mch " + "WHERE mch.member.seq = :memberSeq")
    List<MemberConnectionHistory> findMemberConnectionHistoriesByMemberSeq(Long memberSeq);
}
