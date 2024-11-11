package com.oreo.finalproject_5re5_be.vc.entity;

import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import com.oreo.finalproject_5re5_be.project.entity.Project;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "vc_resultaudiofile")
@Getter
@ToString
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
public class VcResultFile extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "res_seq")
    private Long resSeq;

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
    @Builder.Default
    @Column(nullable = false, name = "activate")
    private Character activate = 'Y';
    @Builder.Default
    @Column(nullable = false, name = "st_stat")
    private Character startStatus = 'Y';
    @Builder.Default
    @Column(nullable = false, name = "dn_stat")
    private Character downloadStatus = 'Y';
    @Column(nullable = false, name = "res_reg_date", updatable = false)
    @CreatedDate
    private LocalDateTime date;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "src_seq")
    private VcSrcFile src_seq;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pro_seq")
    private Project proSeq;

}
