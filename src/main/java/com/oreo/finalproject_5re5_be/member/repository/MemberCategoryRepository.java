package com.oreo.finalproject_5re5_be.member.repository;

import com.oreo.finalproject_5re5_be.member.entity.MemberCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberCategoryRepository extends JpaRepository<MemberCategory, Long> {
    public MemberCategory findByCateCode(Long cateCode);

    public MemberCategory findByName(String name);
}
