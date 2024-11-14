package com.oreo.finalproject_5re5_be.member.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class MemberTermConditionResponse {
    private String condCode;
    private String shortCont;
    private String longCont;
    private Character chkUse;
    private Integer ord;
    private String law1;
    private String law2;
    private String law3;
}
