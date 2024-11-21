package com.oreo.finalproject_5re5_be.code.repository;

import com.oreo.finalproject_5re5_be.code.entity.Code;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CodeRepository extends JpaRepository<Code, Long> {

    // 코드 번호로 특정 코드를 조회.
    Code findCodeByCode(String code);

    // 코드 시퀀스로 특정 코드를 조회.
    Code findCodeByCodeSeq(Long codeSeq);

    // 각 파트별로 사용 가능한 코드를 조회.
    @Query( "SELECT c " +
            "FROM Code c " +
            "WHERE c.cateNum = :cateNum " +
            "AND c.chkUse = 'Y' " +
            "ORDER BY c.ord")
    List<Code> findAvailableCodesByCateNum(String cateNum);

    // 각 파트별로 모든 코드를 조회.
    @Query( "SELECT c " +
            "FROM Code c " +
            "WHERE c.cateNum = :cateNum " +
            "ORDER BY c.ord")
    List<Code> findCodesByCateNum(String cateNum);

    // 코드 번호로 코드가 존재하는지 확인.
    boolean existsByCode(String code);

    //
    boolean existsByCodeSeq(Long codeSeq);
}
