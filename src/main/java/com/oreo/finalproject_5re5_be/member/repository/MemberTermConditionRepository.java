package com.oreo.finalproject_5re5_be.member.repository;

import com.oreo.finalproject_5re5_be.member.entity.MemberTermsCondition;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberTermConditionRepository extends JpaRepository<MemberTermsCondition, Long> {

    MemberTermsCondition findMemberTermsConditionByCondCode(String condCode);

    @Query( "SELECT mtc " +
            "FROM MemberTermsCondition mtc " +
            "WHERE mtc.chkUse = 'Y' " +
            "ORDER BY mtc.ord")
    List<MemberTermsCondition> findAvailableMemberTermsConditions();

    @Query( "SELECT mtc " +
            "FROM MemberTermsCondition mtc " +
            "WHERE mtc.chkUse = 'N' " +
            "ORDER BY mtc.ord")
    List<MemberTermsCondition> findNotAvailableMemberTermsConditions();
}
