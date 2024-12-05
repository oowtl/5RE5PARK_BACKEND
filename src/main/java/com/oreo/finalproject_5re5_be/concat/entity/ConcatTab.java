package com.oreo.finalproject_5re5_be.concat.entity;

import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import com.oreo.finalproject_5re5_be.project.entity.Project;
import jakarta.persistence.*;
import lombok.*;

@ToString
@Getter
@Builder
@NoArgsConstructor
@Entity(name = "concat_tab")
public class ConcatTab extends BaseEntity {
    @Id
    private Long projectId;

    @MapsId // Project 엔티티의 ID를 ConcatTab의 ID로 사용
    @OneToOne
    @JoinColumn(name = "pro_seq")
    private Project project;

    @Column(name = "status")
    private Character status;

    @Column(name = "front_silence")
    private Float frontSilence;

    // Setter 메서드 추가
    //bgm으로 지정된 오디오파일과 일대일 매칭
    @Setter
    @OneToOne
    @JoinColumn(name = "bgm_audio_file_id", nullable = true)
    private AudioFile bgmAudioFile;

    public ConcatTab(Long projectId, Project project, Character status, Float frontSilence, AudioFile bgmAudioFile) {
        this.projectId = projectId;
        this.project = project;
        this.status = status;
        this.frontSilence = frontSilence;
        this.bgmAudioFile = bgmAudioFile;
    }

}


