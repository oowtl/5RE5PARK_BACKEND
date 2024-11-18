package com.oreo.finalproject_5re5_be.member.dto.request;

import com.oreo.finalproject_5re5_be.member.entity.Member;
import com.oreo.finalproject_5re5_be.member.entity.MemberTermsCondition;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@EqualsAndHashCode // 목킹 테스트를 위해 추가
public class MemberTermConditionRequest {


    @NotBlank(message = "약관 코드를 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Z0-9]{1,20}$", message = "약관 코드는 1~20자의 영문 및 숫자만 허용됩니다.")
    private String condCode;

    private String name;

    @NotBlank(message = "약관 짧은 내용을 입력해주세요.")
    private String shortCont;

    @NotBlank(message = "약관 긴 내용을 입력해주세요.")
    private String longCont;

    @NotNull(message = "사용여부 항목의 내용을 입력해주세요.")
    private Character chkUse;

    @NotNull
    private Integer ord;

    private String law1;
    private String law2;
    private String law3;

    // Member 엔티티로 변환
    public MemberTermsCondition createMemberTermsConditionEntity() {
        // 현재 시간 조회
        LocalDateTime now = LocalDateTime.now();
        return MemberTermsCondition.builder()
                .condCode(condCode)
                .name(name)
                .shortCont(shortCont)
                .longCont(longCont)
                .chkUse(chkUse)
                .ord(ord)
                .termCondDate(now)
                .termCondUpDate(now)
                .law1(law1)
                .law2(law2)
                .law3(law3)
                .build();
    }

}
