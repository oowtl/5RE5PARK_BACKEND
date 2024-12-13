package com.oreo.finalproject_5re5_be.vc.entity;

import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name = "vc_text")
@Getter
@ToString
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
public class VcText extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vt_seq")
    private Long vtSeq;

    @Column(nullable = false, name = "comment")
    private String comment;

    @Column(nullable = false, name = "length")
    private String length;

    @Column(name = "vt_up_date") // 수정날짜
    @LastModifiedDate
    private LocalDateTime vtUpDate;

    @Column(name = "vt_date", updatable = false) // 생성날짜
    @CreatedDate
    private LocalDateTime vtDate;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "src_seq")
    private VcSrcFile srcSeq;

    @PrePersist
    public void prePersist() {
        vtDate = LocalDateTime.now();
        vtUpDate = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        vtUpDate = LocalDateTime.now();
    }

    public static VcText create(VcSrcFile srcFile, String comment, String length) {
        return VcText.builder().srcSeq(srcFile).comment(comment).length(length).build();
    }
}
