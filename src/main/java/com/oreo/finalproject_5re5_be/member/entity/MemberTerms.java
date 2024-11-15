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
@Table(name = "member_terms")
@Getter @Setter
@ToString
public class MemberTerms extends BaseEntity  {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "terms_seq")
    private Long termsSeq;

    @Column(name = "chk_term_1", nullable = false, length = 1)
    private String chkTerm1;

    @Column(name = "chk_term_2", nullable = false, length = 1)
    private String chkTerm2;

    @Column(name = "chk_term_3", nullable = false, length = 1)
    private String chkTerm3;

    @Column(name = "chk_term_4", nullable = false, length = 1)
    private String chkTerm4;

    @Column(name = "chk_term_5", nullable = false, length = 1)
    private String chkTerm5;

    @Column(name = "term_end_date", nullable = false, length = 1)
    private String termEndDate;

    @Column(name = "reg_date")
    private LocalDateTime regDate;

    @Column(name = "reg_seq")
    private Long regSeq;

    @Column(name = "term_cond_seq_1", nullable = false)
    private Long termCondSeq1;

    @Column(name = "term_cond_seq_2", nullable = false)
    private Long termCondSeq2;

    @Column(name = "term_cond_seq_3", nullable = false)
    private Long termCondSeq3;

    @Column(name = "term_cond_seq_4", nullable = false)
    private Long termCondSeq4;

    @Column(name = "term_cond_seq_5", nullable = false)
    private Long termCondSeq5;

    @Column(name = "term_reg_date", nullable = false)
    private LocalDateTime termRegDate;

    @Column(name = "up_date")
    private LocalDateTime upDate;

    @Column(name = "up_seq")
    private Long upSeq;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "chk_use", nullable = false, length = 1)
    private String chkUse;

    // 외래키 관계 설정
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "term_cond_seq_1", referencedColumnName = "terms_cond_seq", insertable = false, updatable = false)
    private MemberTermsCondition termCondition1;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "term_cond_seq_2", referencedColumnName = "terms_cond_seq", insertable = false, updatable = false)
    private MemberTermsCondition termCondition2;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "term_cond_seq_3", referencedColumnName = "terms_cond_seq", insertable = false, updatable = false)
    private MemberTermsCondition termCondition3;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "term_cond_seq_4", referencedColumnName = "terms_cond_seq", insertable = false, updatable = false)
    private MemberTermsCondition termCondition4;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "term_cond_seq_5", referencedColumnName = "terms_cond_seq", insertable = false, updatable = false)
    private MemberTermsCondition termCondition5;
}
