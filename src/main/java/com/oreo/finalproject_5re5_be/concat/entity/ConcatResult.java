package com.oreo.finalproject_5re5_be.concat.entity;

import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "concat_result")
public class ConcatResult extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "concat_result")
    private Long concatResultSequence;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pro_seq")
    private ConcatTab concatTab;

    @ManyToOne(fetch = FetchType.LAZY)
    private ConcatOption option;

    @Column(name = "audio_url")
    private String audioUrl;

    @Column(name = "extension")
    private String extension;

    @Column(name = "file_length")
    private Float fileLength;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_size")
    private Long fileSize;

}
