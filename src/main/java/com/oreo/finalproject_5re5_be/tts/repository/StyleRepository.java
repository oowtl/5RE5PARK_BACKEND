package com.oreo.finalproject_5re5_be.tts.repository;

import com.oreo.finalproject_5re5_be.tts.entity.Style;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StyleRepository extends JpaRepository<Style, Long> {
    Optional<Style> findByName(String name);

    // 언어 식별 번호 기반으로 목소리가 있는 스타일을 반환
    @Query("SELECT DISTINCT v.style FROM Voice v WHERE v.language.langSeq = :langSeq")
    List<Style> findListBylangSeq(@Param("langSeq") Long langSeq);
}
