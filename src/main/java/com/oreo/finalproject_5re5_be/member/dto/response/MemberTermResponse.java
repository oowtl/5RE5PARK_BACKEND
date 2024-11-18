package com.oreo.finalproject_5re5_be.member.dto.response;


import com.oreo.finalproject_5re5_be.member.entity.MemberTerms;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class MemberTermResponse {

    private Long termSeq;
    private Character chkTerm1;
    private Character chkTerm2;
    private Character chkTerm3;
    private Character chkTerm4;
    private Character chkTerm5;
    private LocalDateTime termEndDate;
    private LocalDateTime termRegDate;
    private String name;
    private Character chkUse;
    private Long termCond1;
    private Long termCond2;
    private Long termCond3;
    private Long termCond4;
    private Long termCond5;

    public MemberTermResponse(MemberTerms memberTerms) {
        this.termSeq = memberTerms.getTermsSeq();
        this.chkTerm1 = memberTerms.getChkTerm1();
        this.chkTerm2 = memberTerms.getChkTerm2();
        this.chkTerm3 = memberTerms.getChkTerm3();
        this.chkTerm4 = memberTerms.getChkTerm4();
        this.chkTerm5 = memberTerms.getChkTerm5();
        this.termEndDate = memberTerms.getTermEndDate();
        this.termRegDate = memberTerms.getTermRegDate();
        this.name = memberTerms.getName();
        this.chkUse = memberTerms.getChkUse();
        this.termCond1 = memberTerms.getTermCond1().getTermsCondSeq();
        this.termCond2 = memberTerms.getTermCond2().getTermsCondSeq();
        this.termCond3 = memberTerms.getTermCond3().getTermsCondSeq();
        this.termCond4 = memberTerms.getTermCond4().getTermsCondSeq();
        this.termCond5 = memberTerms.getTermCond5().getTermsCondSeq();
    }
}
