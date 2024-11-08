package com.oreo.finalproject_5re5_be.tts.entity;

import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "language")
@Getter
@Setter
public class Language extends BaseEntity {
    @Id
    @Column(name = "lang_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long langSeq;

    @Column(name = "lang_code", nullable = false)
    private String langCode;

    @Column(name = "lang_name", nullable = false)
    private String langName;

    @Column(name = "regn_code", nullable = false)
    private String regionCode;

    @Column(name = "regn_name", nullable = false)
    private String regionName;

    @Column(name = "enabled", nullable = false)
    private char enabled;
}
