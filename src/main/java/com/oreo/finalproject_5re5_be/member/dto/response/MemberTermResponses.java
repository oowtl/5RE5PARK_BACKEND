package com.oreo.finalproject_5re5_be.member.dto.response;

import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class MemberTermResponses {

    List<MemberTermResponse> memberTermResponses;

    public MemberTermResponses(List<MemberTermResponse> memberTermResponses) {
        this.memberTermResponses = memberTermResponses;
    }
}
