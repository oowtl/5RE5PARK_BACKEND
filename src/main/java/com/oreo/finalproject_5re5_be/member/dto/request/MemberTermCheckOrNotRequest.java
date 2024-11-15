package com.oreo.finalproject_5re5_be.member.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class MemberTermCheckOrNotRequest {
    private Long termCondCode;
    private Character agreed;
    private Boolean isMandatory;

    // 서비스 정책 사항에 부합한 약관 동의 여부 확인
    public boolean isValid() {
        if (isMandatory) {
            return agreed != null && agreed.equals('Y');
        }

        return agreed != null;
    }
}
