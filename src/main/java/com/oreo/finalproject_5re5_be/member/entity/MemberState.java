package com.oreo.finalproject_5re5_be.member.entity;

import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "member_state")
@Getter @Setter
@ToString
public class MemberState extends BaseEntity {

    @Id
    @Column(name = "state_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long StateSeq;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_seq")
    private Member member;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cate_code")
    private MemberCategory cateCode;

    private LocalDateTime applDate;
    private LocalDateTime endDate;
}