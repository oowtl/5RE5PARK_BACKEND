package com.oreo.finalproject_5re5_be.member.dto.request;

import com.oreo.finalproject_5re5_be.member.entity.Code;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class CodeRequest {

    @NotBlank(message = "코드 번호를 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Z0-9]{1,10}$", message = "코드 번호는 1~10자의 영문 및 숫자만 허용됩니다.")
    private String cateNum;

    @NotBlank(message = "코드를 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Z0-9]{1,20}$", message = "약관 코드는 1~20자의 영문 및 숫자만 허용됩니다.")
    private String code;

    @NotBlank(message = "코드명을 입력해주세요.")
    private String name;

    @NotBlank(message = "순서를 입력해주세요.")
    private Integer ord;

    @NotBlank(message = "사용여부를 입력해주세요.")
    private String chkUse;

    private String comt;


    public Code createCodeEntity() {
        return Code.builder()
                .cateNum(cateNum)
                .code(code)
                .name(name)
                .ord(ord)
                .chkUse(chkUse)
                .comt(comt)
                .build();
    }

}
