package com.oreo.finalproject_5re5_be.tts.entity;

import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import com.oreo.finalproject_5re5_be.project.entity.Project;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tts_process_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
@ToString
@EqualsAndHashCode(callSuper = false)
public class TtsProcessHistory extends BaseEntity {
    @Id
    @Column(name = "tph_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tphSeq;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "sort_ord")
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
    @JoinColumn(name = "tts_aud_seq", nullable = false)
    private TtsAudioFile ttsAudiofile;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "voice_seq", nullable = false)
    private Voice voice;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pro_seq", nullable = false)
    private Project project;
}
