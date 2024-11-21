package com.oreo.finalproject_5re5_be.project.entity;


import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import com.oreo.finalproject_5re5_be.member.entity.Code;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "project_status")
@Getter @Setter
@ToString
public class ProjectStatus extends BaseEntity {

    @Id
    @Column(name = "ps_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long psSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pro_seq")
    private Project project;

    /** 추후에 통합 코드 엔티티와 연관관계 맺기 **/
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "code")
    private Code ccSeq;

    private LocalDateTime applDate;//적용 시점일
    private LocalDateTime endDate;//적용 종료일

    public void prePersist() {
        applDate = LocalDateTime.now();
    }
}
