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
public class MemberTermUpdateRequest {

    @NotEmpty(message = "필수 여부는 비어 있을 수 없습니다.")
    private List<@Pattern(regexp = "[YN]", message = "필수 여부는 'Y' 또는 'N'만 가능합니다.") Character> memberTermConditionMandatoryOrNot;

    @NotEmpty(message = "사용 가능 여부는 비어 있을 수 없습니다.")
    @Pattern(regexp = "[YN]", message = "사용 여부는 'Y' 또는 'N'만 가능합니다.")
    private Character chkUse;
}
