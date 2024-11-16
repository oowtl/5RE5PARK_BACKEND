package com.oreo.finalproject_5re5_be.member.repository;

import com.oreo.finalproject_5re5_be.member.entity.Code;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CodeRepository extends JpaRepository<Code, Long> {

    // 코드 번호로 특정 코드를 조회합니다.
    public Code findCodeByCode(String code);

    // 각 파트별로 사용 가능한 코드를 조회합니다.
    @Query( "SELECT c " +
            "FROM Code c " +
            "WHERE c.cateNum = :cateNum " +
            "AND c.chkUse = 'Y' " +
            "ORDER BY c.ord")
    public List<Code> findAvailableCodesByCateNum(String cateNum);

    // 각 파트별로 모든 코드를 조회합니다.
    @Query( "SELECT c " +
            "FROM Code c " +
            "WHERE c.cateNum = :cateNum " +
            "ORDER BY c.ord")
    public List<Code> findCodesByCateNum(String cateNum);
}
