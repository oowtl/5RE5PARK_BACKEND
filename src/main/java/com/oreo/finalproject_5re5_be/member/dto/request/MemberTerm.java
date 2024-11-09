package com.oreo.finalproject_5re5_be.member.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class MemberTerm {
    private Long termCondCode; // 추후에 varchar로 변경
    private Character agreed;
    private Boolean isMandatory;

    public boolean isValid() {
        if (isMandatory) {
            return agreed != null && agreed.equals('Y');
        }

        return agreed != null;
    }
}
