package com.oreo.finalproject_5re5_be.user.entity;

import com.oreo.finalproject_5re5_be.utils.entity.BaseEntity;
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
@Table(name = "user_terms")
@Getter @Setter
@ToString
public class UserTerms extends BaseEntity  {

    @Id
    @Column(name = "terms_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long termsSeq;
    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "term_code_1")
    private UserTermsCondition termCode1;
    private Character chkTerm1;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "term_code_2")
    private UserTermsCondition termCode2;
    private Character chkTerm2;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "term_code_3")
    private UserTermsCondition termCode3;
    private Character chkTerm3;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "term_code_4")
    private UserTermsCondition termCode4;
    private Character chkTerm4;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "term_code_5")
    private UserTermsCondition termCode5;
    private Character chkTerm5;

    private LocalDateTime termRegDate;
    private Character chkUse;
}
