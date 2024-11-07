package com.oreo.finalproject_5re5_be.vc.entity;

import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import com.oreo.finalproject_5re5_be.project.entity.Project;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "vc_srcfile")
@Getter @Setter
@ToString
public class VcSrcFile extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "src_seq")
    private long srcSeq;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vc_seq")
    private Vc vcSeq;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pro_seq")
    private Project proSeq;

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
    @Column(nullable = false, name = "activate")
    private char activate='Y';
    @Column(nullable = false, name = "st_stat")
    private char StartStatus='Y';
    @Column(nullable = false, name = "dn_stat")
    private char DownloadStatus='Y';

}
