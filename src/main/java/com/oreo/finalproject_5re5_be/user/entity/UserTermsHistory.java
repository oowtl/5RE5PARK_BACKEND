package com.oreo.finalproject_5re5_be.user.entity;

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
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name = "user_terms_history")
@Getter @Setter
@ToString
public class UserTermsHistory extends BaseEntity {

    @Id
    @Column(name = "term_hist_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long termHistSeq;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_seq")
    private User user;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "terms_seq")
    private UserTerms terms;

    private LocalDateTime histRegDate;
    private LocalDateTime histEndDate;
    private Character chkTerm1; // 'Y' or 'N'
    private Character chkTerm2;
    private Character chkTerm3;
    private Character chkTerm4;
    private Character chkTerm5;
}
