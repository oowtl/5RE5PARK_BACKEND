package com.oreo.finalproject_5re5_be.concat.entity;

import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "audio_format")
public class AudioFormat extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "audio_format_seq")
    private Long audioFormatSeq;


    @OneToOne(mappedBy = "audioFormat")
    private AudioFile audioFile;

    @OneToOne(mappedBy = "audioFormat")
    private ConcatResult concatResult;


    private String encoding;
    private Integer sampleRate;
    private Short sampleSizeBit;
    private Short channel;
    private Integer frameSize;
    private Short frameRate;
    private Character isBigEndian;

}
