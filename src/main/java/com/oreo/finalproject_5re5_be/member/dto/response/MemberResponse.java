package com.oreo.finalproject_5re5_be.member.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class MemberResponse {

    private Long seq;
    private String id;
    private String email;
    private String name;
}
