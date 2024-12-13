package com.oreo.finalproject_5re5_be.concat.repository;

import com.oreo.finalproject_5re5_be.concat.entity.AudioFile;
import com.oreo.finalproject_5re5_be.concat.entity.ConcatResult;
import com.oreo.finalproject_5re5_be.concat.entity.ConcatRow;
import com.oreo.finalproject_5re5_be.concat.entity.MaterialAudio;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialAudioRepository extends JpaRepository<MaterialAudio, Long> {

    @Query(
            "SELECT ma.audioFile FROM material_audio ma WHERE ma.concatResult.concatResultSequence = :resultSeq")
    List<AudioFile> findAudioFilesByConcatResult(Long resultSeq);

    @Query(
            "SELECT ma.concatResult FROM material_audio ma WHERE ma.audioFile.audioFileSeq = :audioFileSeq")
    List<ConcatResult> findConcatResultsByAudioFileSeq(Long audioFileSeq);

    @Modifying(clearAutomatically = true) // 변경 후 자동으로 영속성 컨텍스트를 초기화
    @Query(
            "DELETE FROM material_audio ma WHERE ma.concatResult.concatResultSequence = :concatResultSeq")
    void deleteByConcatResultSeq(Long concatResultSeq);

    @Query(
            "SELECT ma FROM material_audio ma WHERE ma.concatResult.concatResultSequence = :concatResultSeq")
    List<MaterialAudio> findByConcatResultSeq(Long concatResultSeq);

    @Query(
            "SELECT ma.audioFile.concatRow FROM material_audio ma WHERE ma.concatResult.concatResultSequence = :concatResultSeq")
    List<ConcatRow> findConcatRowListByConcatResultSeq(Long concatResultSeq);
}
