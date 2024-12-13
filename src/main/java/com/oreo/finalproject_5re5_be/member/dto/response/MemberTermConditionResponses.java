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
public class MemberTermConditionResponses {

    List<MemberTermConditionResponse> memberTermConditionResponses;

    public MemberTermConditionResponses(
            List<MemberTermConditionResponse> memberTermConditionResponses) {
        this.memberTermConditionResponses = memberTermConditionResponses;
    }
}
