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
@Table(name = "member_delete")
@Getter @Setter
@ToString
public class MemberDelete extends BaseEntity {

    @Id
    @Column(name = "del_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long delSeq;

    private Long memberSeq; // 기존의 회원 시퀀스

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "code")
    private Code code; // 회원 삭제 유형 코드

    @Column(name = "detail_cont", nullable = false)
    private String detailCont; // 회원 삭제 사유

    @Column(name = "chk_use")
    private Character chkUse; // 적용 유무

    @Column(name = "appl_date", nullable = false)
    private String applDate;
}
