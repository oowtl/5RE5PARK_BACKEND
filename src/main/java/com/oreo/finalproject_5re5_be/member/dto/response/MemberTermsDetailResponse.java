package com.oreo.finalproject_5re5_be.member.dto.response;

import com.oreo.finalproject_5re5_be.member.entity.MemberTerms;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class MemberTermsDetailResponse {

    private String termCode;
    private String termName;
    private Character chkTerm1;
    private Character chkTerm2;
    private Character chkTerm3;
    private Character chkTerm4;
    private Character chkTerm5;
    private Character chkUse;
    private MemberTermConditionResponses memberTermConditionResponses;

    public static MemberTermsDetailResponse of(
            MemberTerms terms, List<MemberTermConditionResponse> memberTermConditionResponseList) {
        return MemberTermsDetailResponse.builder()
                .termCode(terms.getTermCode())
                .termName(terms.getName())
                .chkTerm1(terms.getChkTerm1())
                .chkTerm2(terms.getChkTerm2())
                .chkTerm3(terms.getChkTerm3())
                .chkTerm4(terms.getChkTerm4())
                .chkTerm5(terms.getChkTerm5())
                .chkUse(terms.getChkUse())
                .memberTermConditionResponses(
                        new MemberTermConditionResponses(memberTermConditionResponseList))
                .build();
    }
}
