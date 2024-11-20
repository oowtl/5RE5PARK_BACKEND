package com.oreo.finalproject_5re5_be.concat.entity;

import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "concat_result")
public class ConcatResult extends BaseEntity {

    @Id
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
    private Long fileLength;

    @Column(name = "file_name")
    private String fileName;

    @OneToMany(mappedBy = "concatResult", cascade = CascadeType.ALL)
    @Builder.Default
    private List<MaterialAudio> materialAudioList = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "audio_format_seq")
    private AudioFormat audioFormat;

}
