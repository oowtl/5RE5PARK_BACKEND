package com.oreo.finalproject_5re5_be.member.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class MemberRegisterResponse {

    private String content;

    // 정적 팩토리 메서드
    public static MemberRegisterResponse of(String content) {
        return MemberRegisterResponse.builder()
                .content(content)
                .build();
    }
}
