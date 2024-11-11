package com.oreo.finalproject_5re5_be.project.entity;


import com.oreo.finalproject_5re5_be.member.entity.Member;
import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
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
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "project")
@Getter
@Setter
@ToString
public class Project extends BaseEntity {

    @Id
    @Column(name = "pro_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long proSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private Member member;

    private String proName;
    private String proCmt;
    private LocalDateTime proUpDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Project project)) return false;
        return Objects.equals(proSeq, project.proSeq) && Objects.equals(member, project.member) && Objects.equals(proName, project.proName) && Objects.equals(proCmt, project.proCmt) && Objects.equals(proUpDate, project.proUpDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(proSeq, member, proName, proCmt, proUpDate);
    }
}
