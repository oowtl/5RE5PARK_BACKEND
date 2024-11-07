package com.oreo.finalproject_5re5_be.tts.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "style")
@Getter
@Setter
public class Style {
    @Id
    @Column(name = "style_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long styleSeq;

    @Column(name = "name")
    private String name;

    @Column(name = "mood")
    private String mood;

    @Column(name = "contents")
    private String contents;

    @Column(name = "desc")
    private String description;

    @Column(name = "use_cnt")
    private Integer useCnt;

    @Column(name = "recm")
    private char isRecommend;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "voice_seq")
//    private Voice voice;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "smpl_aud_seq")
//    private SampleAudio sampleAudio;
}
