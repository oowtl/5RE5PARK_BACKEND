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
@Table(name = "member_change_history")
@Getter @Setter
@ToString
public class MemberChangeHistory extends BaseEntity {
    @Id
    @Column(name = "chng_hist_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chngHistSeq;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_seq")
    private Member member;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "code")
    private Code chngFieldCode;

    @Column(name = "bef_val", nullable = false)
    private String befVal;
    @Column(name = "aft_val", nullable = false)
    private String aftVal;
    @Column(name = "appl_date", nullable = false)
    private LocalDateTime applDate;
    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;
}
