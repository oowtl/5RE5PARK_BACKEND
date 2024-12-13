package com.oreo.finalproject_5re5_be.concat.entity;

import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "bgm_file")
public class BgmFile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bgm_file_seq")
    private Long bgmFileSeq;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "concat_tab_seq") // 외래 키: concat_tab의 ID
    private ConcatTab concatTab;

    @Setter
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concat_result_seq", nullable = true)
    private ConcatResult concatResult;

    private String audioUrl;
    private String extension;
    private Long fileSize;
    private Long fileLength;
    private String fileName;

    @CreatedDate
    @Column(updatable = false, name = "created_date")
    private LocalDateTime createdDate;
}
