package com.oreo.finalproject_5re5_be.member.dto.response;

import com.oreo.finalproject_5re5_be.member.entity.MemberTermsCondition;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class MemberTermConditionResponse {
    private String condCode;
    private String shortCont;
    private String longCont;
    private Character chkUse;
    private Integer ord;
    private String law1;
    private String law2;
    private String law3;

    public MemberTermConditionResponse(MemberTermsCondition memberTermsCondition) {
        this.condCode = memberTermsCondition.getCondCode();
        this.shortCont = memberTermsCondition.getShortCont();
        this.longCont = memberTermsCondition.getLongCont();
        this.chkUse = memberTermsCondition.getChkUse();
        this.ord = memberTermsCondition.getOrd();
        this.law1 = memberTermsCondition.getLaw1();
        this.law2 = memberTermsCondition.getLaw2();
        this.law3 = memberTermsCondition.getLaw3();
    }
}
