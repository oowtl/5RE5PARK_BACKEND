package com.oreo.finalproject_5re5_be.code.dto.response;

import com.oreo.finalproject_5re5_be.code.entity.Code;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
@EqualsAndHashCode
public class CodeResponse {
    private Long codeSeq;
    private String cateNum;
    private String code;
    private String name;
    private Integer ord;
    private String chkUse;
    private String comt;

    public static CodeResponse of(Code code) {
        return CodeResponse.builder()
                .codeSeq(code.getCodeSeq())
                .cateNum(code.getCateNum())
                .code(code.getCode())
                .name(code.getName())
                .ord(code.getOrd())
                .chkUse(code.getChkUse())
                .comt(code.getComt())
                .build();
    }
}
