package com.oreo.finalproject_5re5_be.tts.entity;

import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tts_audio_file")
@Getter
@Setter
public class TtsAudiofile extends BaseEntity {
    @Id
    @Column(name = "tts_aud_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ttsAudioSeq;

    @Column(name = "aud_name")
    private String audioName;

    @Column(name = "aud_path")
    private String audioPath;

    @Column(name = "aud_ext")
    private String audioExtension;

    @Column(name = "aud_size")
    private String audioSize;

    @Column(name = "aud_time")
    private Integer audioTime;

    @Column(name = "down_cnt")
    private Integer downloadCount;

    @Column(name = "down_yn")
    private char downloadYn;

    @Column(name = "aud_play_yn")
    private char audioPlayYn;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
