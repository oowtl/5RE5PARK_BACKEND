package com.oreo.finalproject_5re5_be.member.entity;

import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import com.oreo.finalproject_5re5_be.member.dto.request.MemberTermConditionUpdateRequest;
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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "member_terms_condition")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@ToString
@EqualsAndHashCode(callSuper = false)
public class MemberTermsCondition extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "terms_cond_seq")
    private Long termsCondSeq;

    @Column(name = "chk_use", nullable = false)
    private Character chkUse;

    @Column(name = "ord", nullable = false)
    private Integer ord;

    @Column(name = "reg_date")
    private LocalDateTime regDate;

    @Column(name = "reg_seq")
    private Long regSeq;

    @Column(name = "term_cond_date", nullable = false)
    private LocalDateTime termCondDate;

    @Column(name = "term_cond_up_date")
    private LocalDateTime termCondUpDate;

    @Column(name = "up_date")
    private LocalDateTime upDate;

    @Column(name = "up_seq")
    private Long upSeq;

    @Column(name = "cond_code", nullable = false, length = 255, unique = true)
    private String condCode;

    @Column(name = "law1", length = 255)
    private String law1;

    @Column(name = "law2", length = 255)
    private String law2;

    @Column(name = "law3", length = 255)
    private String law3;

    @Column(name = "long_cont", nullable = false, length = 255)
    private String longCont;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "short_cont", nullable = false, length = 255)
    private String shortCont;

    public void update(MemberTermConditionUpdateRequest updateRequest) {
        this.shortCont = updateRequest.getShortCont();
        this.longCont = updateRequest.getLongCont();
        this.chkUse = updateRequest.getChkUse();
        this.ord = updateRequest.getOrd();
        this.termCondUpDate = LocalDateTime.now();
    }
}
