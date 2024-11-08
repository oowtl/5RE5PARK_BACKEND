package com.oreo.finalproject_5re5_be.tts.entity;

import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "voice")
@Getter
@Setter
public class Voice extends BaseEntity {
    @Id
    @Column(name = "voice_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long voiceSeq;

    @Column(name = "name")
    private String name;

    @Column(name = "gender")
    private String gender;

    @Column(name = "age")
    private Integer age;

    @Column(name = "dscp")
    private String description;

    @Column(name = "enabled")
    private char enabled;

    @Column(name = "server", nullable = false)
    private String server;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lang_seq")
    private Language language;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "style_seq")
    private Style style;
}
