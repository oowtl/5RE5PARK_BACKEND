package com.oreo.finalproject_5re5_be.member.entity;

import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name = "member_delete")
@Getter @Setter
@ToString
public class MemberDelete extends BaseEntity {

    @Id
    @Column(name = "member_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userSeq;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cate_code")
    private MemberCategory cateCode;

    @Column(name = "detail_cont", nullable = false)
    private String detailCont;
    @Column(name = "chk_use", nullable = false)
    private Character chkUse;
    @Column(name = "appl_date", nullable = false)
    private LocalDateTime applDate;
}
