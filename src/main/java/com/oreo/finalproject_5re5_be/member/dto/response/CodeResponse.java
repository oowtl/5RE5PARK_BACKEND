package com.oreo.finalproject_5re5_be.member.dto.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class CodeResponse {
    private Long codeSeq;
    private String cateNum;
    private String code;
    private String name;
    private Integer ord;
    private String chkUse;
    private String comt;
}
