package com.oreo.finalproject_5re5_be.concat.repository;

import com.oreo.finalproject_5re5_be.concat.entity.AudioFile;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AudioFileRepository extends JpaRepository<AudioFile, Long> {

    //    Optional<AudioFile> findByAudioUrl(String audioUrl);

    Optional<AudioFile> findByFileName(String filename);

    @Query("SELECT af FROM audio_file af WHERE af.concatRow.concatRowSeq = :concatRowSeq")
    Optional<AudioFile> findByRowSeq(Long concatRowSeq);

    @Query("SELECT af FROM audio_file af WHERE FUNCTION('DATE', af.createdDate) = :date")
    List<AudioFile> findByCreatedDateOnly(LocalDate date);

    // 페이징 처리해서 데이터를 한번에 모두 가져오지 않고 필요한 만큼만 나눠서 처리
    Page<AudioFile> findByExtension(String extension, Pageable pageable);

    @Query(
            value = "SELECT * FROM audio_file" + " WHERE concat_row_seq IN (:concatRowSeq)",
            nativeQuery = true)
    List<AudioFile> findAllByConcatRowSeq(@Param("concatRowSeq") List<Long> concatRowSeq);

    void deleteById(Long seq);

    @Query(
            "SELECT af.concatRow.concatRowSeq FROM audio_file af WHERE af.audioFileSeq IN :audioFileSeqs")
    List<Long> findConcatRowSeqsByAudioFileSeqs(List<Long> audioFileSeqs);

    @Query(
            value = "SELECT * FROM audio_file" + " WHERE concat_row_seq IN (:concatRowSeq)",
            nativeQuery = true)
    List<AudioFile> findAllByConcatRowSeqs(@Param("concatRowSeq") List<Long> concatRowSeqs);

    @Query(
            "SELECT af FROM audio_file af "
                    + "JOIN af.concatRow cr "
                    + "JOIN cr.concatTab p "
                    + "WHERE p.project.member.seq = :member_seq")
    List<AudioFile> findAudioFileByMember(@Param("member_seq") Long memberSeq, Pageable pageable);

    @Query(
            "SELECT COUNT(af) FROM audio_file af "
                    + "JOIN af.concatRow cr "
                    + "JOIN cr.concatTab p "
                    + "WHERE p.project.member.seq = :memberSeq")
    long countByMemberSeq(@Param("memberSeq") Long memberSeq);

    @Query("SELECT a FROM audio_file a WHERE a.audioFileSeq = :audioFileSeq")
    AudioFile findAudioFileById(Long audioFileSeq);

    @Query(
            "SELECT af FROM audio_file af"
                    + " JOIN af.concatRow cr"
                    + " JOIN cr.concatTab ct"
                    + " WHERE ct.projectId = :proSeq"
                    + " AND cr.status = 'Y'")
    List<AudioFile> findAudioFileByProjectSeq(@Param("proSeq") Long projectSeq);

    @Query(
            value = "SELECT * FROM audio_file WHERE audio_file_seq = :audioFileSeq ",
            nativeQuery = true)
    Optional<AudioFile> findByAudioFileSeq(Long audioFileSeq);
}
