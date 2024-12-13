package com.oreo.finalproject_5re5_be.member.dto.response;

import com.oreo.finalproject_5re5_be.member.entity.Member;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class MemberReadResponse {

    private String id;
    private String email;
    private String name;
    private String normAddr;
    private String detailAddr;

    public static MemberReadResponse of(Member member) {
        return MemberReadResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .normAddr(member.getNormAddr())
                .detailAddr(member.getDetailAddr())
                .build();
    }
}
