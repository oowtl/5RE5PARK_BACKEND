package com.oreo.finalproject_5re5_be.concat.entity;

import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "concat_result")
public class ConcatResult extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "concat_result")
    private Long concatResultSequence;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pro_seq")
    private ConcatTab concatTab;

    @ToString.Exclude
    @OneToMany(mappedBy = "concatResult", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BgmFile> bgmFiles = new ArrayList<>();

    @Column(name = "audio_url")
    private String audioUrl;

    @Column(name = "extension")
    private String extension;

    @Column(name = "file_length")
    private Float fileLength;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "process_id")
    private String processId;

    @Column private Integer seperated;

    public void addBgmFile(BgmFile bgmFile) {
        this.bgmFiles.add(
                BgmFile.builder()
                        .concatResult(this)
                        .audioUrl(bgmFile.getAudioUrl())
                        .fileName(bgmFile.getFileName())
                        .fileLength(bgmFile.getFileLength())
                        .extension(bgmFile.getExtension())
                        .build());
    }

    public void addBgmFiles(List<BgmFile> bgmFiles) {
        bgmFiles.forEach(this::addBgmFile);
    }
}
