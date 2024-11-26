package com.oreo.finalproject_5re5_be.vc.entity;

import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import com.oreo.finalproject_5re5_be.project.entity.Project;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vc_trgfile")
@Getter
@ToString
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
public class VcTrgFile extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trg_seq")
    private Long trgSeq;
    @Column(nullable = false, name = "name")
    private String fileName;
    @Column(nullable = false, name = "file_url")
    private String fileUrl;
    @Column(nullable = false, name = "length")
    private Integer fileLength;
    @Column(nullable = false, name = "size")
    private String fileSize;
    @Column(nullable = false, name = "extension")
    private String extension;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pro_seq")
    private Vc vc;

    public static VcTrgFile create(Vc vc, String fileName,
                            String fileUrl, Integer fileLength,
                            String fileSize, String extension){
        return VcTrgFile.builder()
                .vc(vc)
                .fileName(fileName)
                .fileUrl(fileUrl)
                .fileLength(fileLength)
                .fileSize(fileSize)
                .extension(extension)
                .build();
    }
}
