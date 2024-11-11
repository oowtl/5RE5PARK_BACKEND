package com.oreo.finalproject_5re5_be.member.repository;

import com.oreo.finalproject_5re5_be.member.entity.MemberState;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberStateRepository extends JpaRepository<MemberState, Long> {

    public List<MemberState> findByMemberSeq(Long seq);
}
