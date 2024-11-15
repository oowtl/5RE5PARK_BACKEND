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
public class MemberTerms extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "terms_seq")
    private Long termsSeq;

    @Column(name = "chk_term_1")
    private Character chkTerm1;

    @Column(name = "chk_term_2")
    private Character chkTerm2;

    @Column(name = "chk_term_3")
    private Character chkTerm3;

    @Column(name = "chk_term_4")
    private Character chkTerm4;

    @Column(name = "chk_term_5")
    private Character chkTerm5;

    @Column(name = "term_end_date")
    private Character termEndDate;

    @Column(name = "term_reg_date", nullable = false)
    private LocalDateTime termRegDate;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "chk_use", nullable = false)
    private Character chkUse;

    // term_cond_seq_1~5 외래 키를 OneToOne 관계로 처리
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "term_cond_seq_1", referencedColumnName = "terms_cond_seq")
    private MemberTermsCondition termCond1;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "term_cond_seq_2", referencedColumnName = "terms_cond_seq")
    private MemberTermsCondition termCond2;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "term_cond_seq_3", referencedColumnName = "terms_cond_seq")
    private MemberTermsCondition termCond3;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "term_cond_seq_4", referencedColumnName = "terms_cond_seq")
    private MemberTermsCondition termCond4;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "term_cond_seq_5", referencedColumnName = "terms_cond_seq")
    private MemberTermsCondition termCond5;

}
