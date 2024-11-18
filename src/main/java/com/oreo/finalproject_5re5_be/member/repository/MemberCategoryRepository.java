package com.oreo.finalproject_5re5_be.member.repository;

import com.oreo.finalproject_5re5_be.member.entity.MemberCategory;
import com.oreo.finalproject_5re5_be.member.entity.MemberChangeHistory;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberCategoryRepository extends JpaRepository<MemberCategory, Long> {
    public MemberCategory findByCateCode(Long cateCode);
    public MemberCategory findByName(String name);
}
