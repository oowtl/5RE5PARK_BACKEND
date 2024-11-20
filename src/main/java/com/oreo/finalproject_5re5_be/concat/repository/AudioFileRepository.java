package com.oreo.finalproject_5re5_be.concat.repository;

import com.oreo.finalproject_5re5_be.concat.entity.AudioFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AudioFileRepository extends JpaRepository<AudioFile, Long> {

    Optional<AudioFile> findByAudioUrl(String audioUrl);

    Optional<AudioFile> findByFileName(String filename);

    @Query("SELECT af FROM audio_file af WHERE af.concatRow.concatRowSeq = :concatRowSeq")
    Optional<AudioFile> findByRowSeq(Long concatRowSeq);

    @Query("SELECT af FROM audio_file af WHERE FUNCTION('DATE', af.createdDate) = :date")
    List<AudioFile> findByCreatedDateOnly(LocalDate date);

    //페이징 처리해서 데이터를 한번에 모두 가져오지 않고 필요한 만큼만 나눠서 처리
    Page<AudioFile> findByExtension(String extension, Pageable pageable);


    void deleteById(Long seq);

}
