package com.oreo.finalproject_5re5_be.vc.entity;

import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import com.oreo.finalproject_5re5_be.project.entity.Project;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vc_target")
@Getter
@ToString
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
public class VcTargetFile extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trg_seq")
    private Long trgSeq;
    @Column(nullable = false, name = "name")
    private String fileName;
    @Column(nullable = false, name = "file_url")
    private String fileUrl;
    @Column(nullable = false, name = "length")
    private String fileLength;
    @Column(nullable = false, name = "size")
    private String fileSize;
    @Column(nullable = false, name = "extension")
    private String extension;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pro_seq")
    private Project proSeq;
}
