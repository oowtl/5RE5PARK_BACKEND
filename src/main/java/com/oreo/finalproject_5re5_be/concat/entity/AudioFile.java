package com.oreo.finalproject_5re5_be.concat.entity;

import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "audio_file")
public class AudioFile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "audio_file_seq_generator")
    @SequenceGenerator(
            name = "audio_file_seq_generator",
            sequenceName = "audio_file_seq", // 실제 시퀀스 이름
            allocationSize = 1 // ID를 하나씩 할당
            )
    @Column(name = "audio_file_seq")
    private Long audioFileSeq;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "concat_row_seq")
    private ConcatRow concatRow;

    private String audioUrl;
    private String extension;
    private Long fileSize;
    private Long fileLength;
    private String fileName;

    @CreatedDate
    @Column(updatable = false, name = "created_date")
    private LocalDateTime createdDate;
}
