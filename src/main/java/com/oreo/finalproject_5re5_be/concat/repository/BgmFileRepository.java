package com.oreo.finalproject_5re5_be.concat.repository;

import com.oreo.finalproject_5re5_be.concat.entity.BgmFile;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BgmFileRepository extends JpaRepository<BgmFile, Long> {
    // concatTab seq에 들어가는 bgmFile 정보들 조회
    @Query("SELECT b FROM bgm_file b WHERE b.concatTab.projectId = :tabSeq")
    List<BgmFile> findByConcatTabSeq(Long tabSeq);

    // 특정 ConcatResult에 연결된 모든 BgmFile 조회
    @Query("SELECT b FROM bgm_file b WHERE b.concatResult.concatResultSequence = :resultSeq")
    List<BgmFile> findByConcatResultSeq(@Param("resultSeq") Long concatResultSeq);

    Optional<BgmFile> findByAudioUrl(String audioUrl);

    // 특정 audioUrl로 모든 BgmFile 조회
    List<BgmFile> findAllByAudioUrl(String audioUrl);
}
