package com.oreo.finalproject_5re5_be.vc.entity;

import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vc_srcfile")
@Getter
@ToString
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
public class VcSrcFile extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "src_seq")
    private Long srcSeq;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pro_seq")
    private Vc vc;

    @Column(nullable = false, name = "row_order")
    @Builder.Default
    private Integer rowOrder = 1;

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

    @Builder.Default // builder사용시 default값 적용
    @Column(nullable = false, name = "activate")
    private Character activate = 'Y';

    @Builder.Default
    @Column(nullable = false, name = "st_stat")
    private Character startStatus = 'Y';

    @Builder.Default
    @Column(nullable = false, name = "dn_stat")
    private Character downloadStatus = 'Y';

    public static VcSrcFile create(
            Vc vc,
            int rowOrder,
            String fileName,
            String fileUrl,
            Integer fileLength,
            String fileSize,
            String extension) {
        return VcSrcFile.builder()
                .vc(vc)
                .rowOrder(rowOrder)
                .fileName(fileName)
                .fileUrl(fileUrl)
                .fileLength(fileLength)
                .fileSize(fileSize)
                .extension(extension)
                .build();
    }
}
