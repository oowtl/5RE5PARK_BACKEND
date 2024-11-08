package com.oreo.finalproject_5re5_be.tts.entity;

import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name = "sample_audio")
@Getter
@Setter
public class SampleAudio extends BaseEntity {
    @Id
    @Column(name = "smpl_aud_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sampleAudioSeq;

    @Column(name = "aud_path", nullable = false)
    private String audioPath;

    @Column(name = "aud_name")
    private String audioName;

    @Column(name = "aud_ext")
    private String audioExtension;

    @Column(name = "aud_size")
    private String audioSize;

    @Column(name = "aud_time")
    private String audioTime;

    @Column(name = "script")
    private String script;

    @Column(name = "enabled", nullable = false)
    private char enabled;

    @CreatedDate
    @Column(name = "created_at")
    private char createdAt;
}
