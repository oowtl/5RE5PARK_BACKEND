package com.oreo.finalproject_5re5_be.vc.entity;

import com.oreo.finalproject_5re5_be.project.entity.Project;
import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "vc_table")
@Getter
@ToString
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
public class Vc extends BaseEntity {
    @Id
    private Long projectId;

    @MapsId // Project 엔티티의 ID를 ConcatTab의 ID로 사용
    @OneToOne
    @JoinColumn(name = "pro_seq")
    private Project project;

    @Builder.Default
    @Column(nullable = false, name = "activate")
    private Character activate = 'Y';

    @Column(nullable = false, name = "vc_up_date")
    @LastModifiedDate
    private LocalDateTime update;
}
