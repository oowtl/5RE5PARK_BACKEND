package com.oreo.finalproject_5re5_be.concat.repository;

import com.oreo.finalproject_5re5_be.concat.entity.BgmFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BgmFileRepository extends JpaRepository<BgmFile, Long> {
    // concatTab seq에 들어가는 bgmFile 정보들 조회
    @Query("SELECT b FROM bgm_file b WHERE b.concatTab.projectId = :tabSeq")
    List<BgmFile> findByConcatTabSeq(Long tabSeq);

}
