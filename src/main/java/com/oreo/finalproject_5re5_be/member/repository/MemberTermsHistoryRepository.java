package com.oreo.finalproject_5re5_be.member.repository;

import com.oreo.finalproject_5re5_be.member.entity.MemberTermsHistory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberTermsHistoryRepository extends JpaRepository<MemberTermsHistory, Long> {
    public List<MemberTermsHistory> findByMemberSeq(Long seq);
}
