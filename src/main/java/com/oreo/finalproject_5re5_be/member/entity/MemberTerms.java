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
    @Column(name = "terms_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long termsSeq;
    private String name;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "term_code_1")
    private MemberTermsCondition termCode1;
    private Character chkTerm1;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "term_code_2")
    private MemberTermsCondition termCode2;
    private Character chkTerm2;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "term_code_3")
    private MemberTermsCondition termCode3;
    private Character chkTerm3;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "term_code_4")
    private MemberTermsCondition termCode4;
    private Character chkTerm4;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "term_code_5")
    private MemberTermsCondition termCode5;
    private Character chkTerm5;

    private LocalDateTime termRegDate;
    private Character chkUse;
}