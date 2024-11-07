package com.oreo.finalproject_5re5_be.vc.entity;

import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import com.oreo.finalproject_5re5_be.project.entity.Project;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "vc_resultaudiofile")
@Getter @Setter
@ToString
public class VcResultAudioFile extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "res_seq")
    private long resSeq;

    @Column(nullable = false, name = "name")
    private String fileName;
    @Column(nullable = false, name = "fileUrl")
    private String fileUrl;
    @Column(nullable = false, name = "length")
    private String fileLength;
    @Column(nullable = false, name = "extension")
    private String extension;
    @Column(nullable = false, name = "activate")
    private char activate = 'Y';
    @Column(nullable = false, name = "st_stat")
    private char StartStatus = 'Y';
    @Column(nullable = false, name = "dn_stat")
    private char DownloadStatus = 'Y';
    @Column(nullable = false, name = "res_reg_date", updatable = false)
    @CreatedDate
    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vc_seq")
    private Vc vcSeq;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pro_seq")
    private Project proSeq;

}
