package com.oreo.finalproject_5re5_be.concat.entity;

import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import com.oreo.finalproject_5re5_be.project.entity.Project;
import jakarta.persistence.*;
import java.util.List;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity(name = "concat_tab")
public class ConcatTab extends BaseEntity {
    @Id private Long projectId;

    @MapsId // Project 엔티티의 ID를 ConcatTab의 ID로 사용
    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "pro_seq")
    private Project project;

    @Column(name = "status")
    private Character status;

    @Column(name = "front_silence")
    private Float frontSilence;

    @ToString.Exclude
    // 하나의 ConcatTab에 여러 bgmFile들 매칭
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "concatTab", fetch = FetchType.LAZY)
    private List<BgmFile> bgmFiles;

    public void addBgmFile(BgmFile bgmFile) {
        bgmFiles.add(
                BgmFile.builder()
                        .concatTab(this)
                        .audioUrl(bgmFile.getAudioUrl())
                        .fileName(bgmFile.getFileName())
                        .fileLength(bgmFile.getFileLength())
                        .extension(bgmFile.getExtension())
                        .build());
    }

    public void addBgmFile(List<BgmFile> bgmFiles) {
        bgmFiles.forEach(this::addBgmFile);
    }
}
