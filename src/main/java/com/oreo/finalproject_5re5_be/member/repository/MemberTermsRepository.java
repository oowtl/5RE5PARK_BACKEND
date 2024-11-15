package com.oreo.finalproject_5re5_be.member.repository;

import com.oreo.finalproject_5re5_be.member.entity.MemberTerms;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberTermsRepository extends JpaRepository<MemberTerms, Long> {

    // 사용 가능한 가장 최근 약관 조회
    @Query( "SELECT mt " +
            "FROM MemberTerms mt " +
            "WHERE mt.chkUse = 'Y' " +
            "AND mt.termRegDate = (SELECT MAX(mt.termRegDate) "
                                + "FROM MemberTerms mt "
                                + "WHERE mt.chkUse = 'Y')")
    MemberTerms findTopByChkUseOrderByTermRegDateDesc();


    // 사용 가능한 약관 모두 조회
    @Query( "SELECT mt " +
            "FROM MemberTerms mt " +
            "WHERE mt.chkUse = 'Y' " +
            "ORDER BY mt.termRegDate DESC")
    List<MemberTerms> findAvailableMemberTerms();

    // 사용 불가능한 약관 모두 조회
    @Query( "SELECT mt " +
            "FROM MemberTerms mt " +
            "WHERE mt.chkUse = 'N' " +
            "ORDER BY mt.termRegDate DESC")
    List<MemberTerms> findNotAvailableMemberTerms();

    // 약관 이름으로 조회
    MemberTerms findMemberTermsByName(String name);

    // 약관 시퀀스로 조회
    MemberTerms findMemberTermsByTermsSeq(Long termsSeq);

}
