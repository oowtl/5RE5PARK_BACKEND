package com.oreo.finalproject_5re5_be.member.repository;

import com.oreo.finalproject_5re5_be.member.entity.MemberState;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberStateRepository extends JpaRepository<MemberState, Long> {

    public List<MemberState> findAllByMemberSeq(Long seq);

    @Query(
            value =
                    " SELECT ms "
                            + " FROM MemberState ms "
                            + " WHERE ms.member.seq = :seq "
                            + " AND ms.code.code = :state ")
    public List<MemberState> findByMemberSeq(Long seq, String state);

    @Query(
            "SELECT ms "
                    + "FROM MemberState ms "
                    + "WHERE ms.member.seq = :memberSeq "
                    + "AND ms.stateSeq = (SELECT MAX(subMS.stateSeq) "
                    + "                   FROM MemberState subMS "
                    + "                   WHERE subMS.member.seq = :memberSeq)")
    public MemberState findLatestHistoryByMemberSeq(Long memberSeq);
}
