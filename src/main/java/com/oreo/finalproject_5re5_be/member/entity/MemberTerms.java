package com.oreo.finalproject_5re5_be.member.entity;

import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import com.oreo.finalproject_5re5_be.member.dto.request.MemberTermUpdateRequest;
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
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name = "member_terms")
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
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
    private LocalDateTime termEndDate;

    @Column(name = "term_reg_date", nullable = false)
    private LocalDateTime termRegDate;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "chk_use", nullable = false)
    private Character chkUse;

    // term_cond_seq_1~5 외래 키를 ManyToOne 관계로 처리
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "term_cond_seq_1", referencedColumnName = "terms_cond_seq")
    private MemberTermsCondition termCond1;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "term_cond_seq_2", referencedColumnName = "terms_cond_seq")
    private MemberTermsCondition termCond2;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "term_cond_seq_3", referencedColumnName = "terms_cond_seq")
    private MemberTermsCondition termCond3;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "term_cond_seq_4", referencedColumnName = "terms_cond_seq")
    private MemberTermsCondition termCond4;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "term_cond_seq_5", referencedColumnName = "terms_cond_seq")
    private MemberTermsCondition termCond5;

    public void update(MemberTermUpdateRequest request) {
        // 사용 가능 여부를 세팅함
        Character chk = request.getChkUse();
        this.setChkUse(chk);

        // 약관 필수 여부 변경
        List<Character> memberTermConditionMandatoryOrNot = request.getMemberTermConditionMandatoryOrNot();
        this.setChkTerm1(memberTermConditionMandatoryOrNot.get(0));
        this.setChkTerm2(memberTermConditionMandatoryOrNot.get(1));
        this.setChkTerm3(memberTermConditionMandatoryOrNot.get(2));
        this.setChkTerm4(memberTermConditionMandatoryOrNot.get(3));
        this.setChkTerm5(memberTermConditionMandatoryOrNot.get(4));

        // 시간 세팅함
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime max = LocalDateTime.MAX;

        this.setTermRegDate(now);
        this.setTermEndDate(max);
    }

}
