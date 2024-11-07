package com.oreo.finalproject_5re5_be.vc.entity;

import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import com.oreo.finalproject_5re5_be.project.entity.Project;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "vc_target")
@Getter
@Setter
@ToString
public class VcTarget extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trg_seq")
    private Long trgSeq;
    @Column(nullable = false, name = "name")
    private String fileName;
    @Column(nullable = false, name = "fileurl")
    private String fileUrl;
    @Column(name = "localurl")
    private String localUrl;
    @Column(nullable = false, name = "length")
    private String fileLength;
    @Column(nullable = false, name = "extension")
    private String extension;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vc_seq")
    private Vc vcSeq;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pro_seq")
    private Project proSeq;
}
