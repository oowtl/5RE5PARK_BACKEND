package com.oreo.finalproject_5re5_be.tts.entity;

import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tts_sentence")
@Getter
@Setter
public class TtsSentence extends BaseEntity {
    @Id
    @Column(name = "ts_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tsSeq;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "sort_ord", nullable = false)
    private Integer sortOrder;

    @Column(name = "volume")
    private Integer volume;

    @Column(name = "speed")
    private Float speed;

    @Column(name = "st_pitch")
    private Integer startPitch;

    @Column(name = "emotion")
    private Integer emotion;

    @Column(name = "emt_strength")
    private Integer emotionStrength;

    @Column(name = "smpl_rate")
    private Integer sampleRate;

    @Column(name = "alpha")
    private Integer alpha;

    @Column(name = "end_pitch")
    private Float endPitch;

    @Column(name = "aud_fmt")
    private String audioFormat;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tts_aud_seq")
    private TtsAudiofile ttsAudiofile;

    @OneToOne(optional = false)
    @JoinColumn(name = "voice_seq")
    private Voice voice;
}
