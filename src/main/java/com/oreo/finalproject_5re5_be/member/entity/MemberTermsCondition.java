package com.oreo.finalproject_5re5_be.member.entity;

import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name = "member_terms_condition")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@ToString
public class MemberTermsCondition extends BaseEntity  {

    @Id
    @Column(name = "terms_cond_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long termCondSeq;

    @Column(name = "terms_cond_code", nullable = false, unique = true)
    private String condCode;

    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "short_cont", nullable = false)
    private String shortCont;
    @Column(name = "long_cont", nullable = false)
    private String longCont;
    @Column(name = "chk_use", nullable = false)
    private Character chkUse;
    @Column(name = "term_cond_date", nullable = false)
    private LocalDateTime termCondDate;
    @Column(name = "term_cond_up_date")
    private LocalDateTime termCondUpDate;
    @Column(name = "law1")
    private String law1;
    @Column(name = "law2")
    private String law2;
    @Column(name = "law3")
    private String law3;
}
