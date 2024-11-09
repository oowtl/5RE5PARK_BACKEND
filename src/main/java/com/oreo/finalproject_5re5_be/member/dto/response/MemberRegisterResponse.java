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

    public static MemberRegisterResponse of(String content) {
        return MemberRegisterResponse.builder()
                .content(content)
                .build();
    }
}
