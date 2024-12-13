package com.oreo.finalproject_5re5_be.project.entity;

import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import com.oreo.finalproject_5re5_be.member.entity.Member;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name = "project")
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
public class Project extends BaseEntity {

    @Id
    @Column(name = "pro_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long proSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_seq")
    private Member member;

    @Builder.Default
    @Column(nullable = false, name = "pro_name")
    private String proName = "ProjectName";

    @Column(name = "pro_cmt")
    private String proCmt;

    @Column(updatable = false, name = "pro_date")
    @CreatedDate
    @EqualsAndHashCode.Exclude
    private LocalDateTime proDate;

    @Column(nullable = false, name = "pro_up_date")
    @LastModifiedDate
    @EqualsAndHashCode.Exclude
    private LocalDateTime proUpDate;

    @Builder.Default
    @Column(nullable = false, name = "activate")
    private Character proActivate = 'Y';

    @PrePersist
    public void prePersist() {
        proUpDate = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        proUpDate = LocalDateTime.now();
    }
}
