package com.oreo.finalproject_5re5_be.member.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class MemberReadResponse {

    private String memberId;
    private String email;
    private String name;
    private String normAddr;
    private String detailAddr;

    public static MemberReadResponse of(String memberId, String email, String name, String normAddr, String detailAddr) {
        return MemberReadResponse.builder()
                .memberId(memberId)
                .email(email)
                .name(name)
                .normAddr(normAddr)
                .detailAddr(detailAddr)
                .build();
    }
}
