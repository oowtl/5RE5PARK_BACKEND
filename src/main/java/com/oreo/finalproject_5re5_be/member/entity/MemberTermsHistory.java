package com.oreo.finalproject_5re5_be.member.entity;

import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import com.oreo.finalproject_5re5_be.member.exception.MemberWrongCountTermCondition;
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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name = "member_terms_history")
@Getter @Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class MemberTermsHistory extends BaseEntity {

    @Id
    @Column(name = "term_hist_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long termHistSeq;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_seq")
    private Member member;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "terms_seq")
    private MemberTerms terms;

    @Column(name = "hist_reg_date", nullable = false)
    private String histRegDate;
    @Column(name = "hist_end_date", nullable = false)
    private String histEndDate;
    @Column(name = "chk_term_1", nullable = false)
    private Character chkTerm1; // 'Y' or 'N'
    @Column(name = "chk_term_2", nullable = false)
    private Character chkTerm2;
    @Column(name = "chk_term_3", nullable = false)
    private Character chkTerm3;
    @Column(name = "chk_term_4", nullable = false)
    private Character chkTerm4;
    @Column(name = "chk_term_5", nullable = false)
    private Character chkTerm5;

    // 회원 약관 동의 항목 체크
    public void addMemberTermCondition(int i, Character chk) {
        if (i == 1) {
            this.chkTerm1 = chk;
        } else if (i == 2) {
            this.chkTerm2 = chk;
        } else if (i == 3) {
            this.chkTerm3 = chk;
        } else if (i == 4) {
            this.chkTerm4 = chk;
        } else if (i == 5) {
            this.chkTerm5 = chk;
        }
    }
}
