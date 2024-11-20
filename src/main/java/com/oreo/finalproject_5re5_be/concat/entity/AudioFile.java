package com.oreo.finalproject_5re5_be.concat.entity;


import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "audio_file")
public class AudioFile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "audio_file_seq")
    private Long audioFileSeq;

    @OneToOne()
    @JoinColumn(name = "concat_row_seq")
    private ConcatRow concatRow;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "audio_format_seq")
    private AudioFormat audioFormat;


    private String audioUrl;
    private String extension;
    private Long fileSize;
    private Long fileLength;
    private String fileName;
    private LocalDateTime createdDate;


}

