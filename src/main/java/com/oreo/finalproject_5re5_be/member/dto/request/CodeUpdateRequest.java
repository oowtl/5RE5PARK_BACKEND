package com.oreo.finalproject_5re5_be.member.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class CodeUpdateRequest {

    @NotBlank(message = "코드명을 입력해주세요.")
    private String name;

    @NotNull(message = "정렬 순서를 입력해주세요.")
    private Integer ord;

    @NotBlank(message = "사용여부를 입력해주세요.")
    private String chkUse;

    private String comt;
}
