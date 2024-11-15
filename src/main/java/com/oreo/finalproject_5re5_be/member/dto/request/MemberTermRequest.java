package com.oreo.finalproject_5re5_be.member.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class MemberTermRequest {

    @NotEmpty(message = "회원 약관 조건 코드는 비어 있을 수 없습니다.")
    @Size(min = 5, max = 5, message = "회원 약관 항목 코드는 5개여야 합니다.")
    private List<@Pattern(regexp = "TERMS[0-9]{3}", message = "코드 형식은 'TERMS'로 시작하고 세 자리 숫자여야 합니다.") String> memberTermConditionCodes;

    @NotEmpty(message = "필수 여부는 비어 있을 수 없습니다.")
    private List<@Pattern(regexp = "[YN]", message = "필수 여부는 'Y' 또는 'N'만 가능합니다.") Character> memberTermConditionMandatoryOrNot;

    @NotBlank(message = "약관 이름을 입력해 주세요.")
    private String termName;
}
