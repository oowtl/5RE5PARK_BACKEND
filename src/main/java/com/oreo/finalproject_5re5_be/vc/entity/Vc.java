package com.oreo.finalproject_5re5_be.vc.entity;

import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import com.oreo.finalproject_5re5_be.project.entity.Project;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "vc_table")
@Getter
@ToString
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
public class Vc extends BaseEntity {
    @Id private Long projectSeq;

    @MapsId // Project 엔티티의 ID를 ConcatTab의 ID로 사용
    @OneToOne
    @JoinColumn(name = "pro_seq")
    private Project proSeq;

    @Builder.Default
    @Column(nullable = false, name = "activate")
    private Character activate = 'Y';

    @Column(nullable = false, name = "vc_up_date")
    private LocalDateTime upDateDate;

    @PrePersist
    public void prePersist() {
        if (upDateDate == null) {
            upDateDate = LocalDateTime.now(); // INSERT 시점에 현재 시간 설정
        }
    }

    @PreUpdate
    public void preUpdate() {
        upDateDate = LocalDateTime.now(); // UPDATE 시점에 현재 시간 설정
    }
}
