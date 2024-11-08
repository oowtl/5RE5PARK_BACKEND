package com.oreo.finalproject_5re5_be.tts.entity;

import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "style")
@Getter
@Setter
public class Style extends BaseEntity {
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

    @Column(name = "dscp")
    private String description;

    @Column(name = "use_cnt")
    private Integer useCnt;

    @Column(name = "recm")
    private char isRecommend;
}
