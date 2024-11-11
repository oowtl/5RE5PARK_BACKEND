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
@AllArgsConstructor
@NoArgsConstructor
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

    private LocalDateTime histRegDate;
    private LocalDateTime histEndDate;
    private Character chkTerm1; // 'Y' or 'N'
    private Character chkTerm2;
    private Character chkTerm3;
    private Character chkTerm4;
    private Character chkTerm5;

    // 회원 약관 동의 항목 체크
    public void addMemberTermCondition(int i, Character chk) {
        switch (i) {
            case 1:
                this.chkTerm1 = chk;
                break;
            case 2:
                this.chkTerm2 = chk;
                break;
            case 3:
                this.chkTerm3 = chk;
                break;
            case 4:
                this.chkTerm4 = chk;
                break;
            case 5:
                this.chkTerm5 = chk;
                break;
            default:
                break;
        }
    }
}
