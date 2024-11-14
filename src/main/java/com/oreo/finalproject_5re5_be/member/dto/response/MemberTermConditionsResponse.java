package com.oreo.finalproject_5re5_be.member.dto.response;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class MemberTermConditionsResponse {

    List<MemberTermConditionResponse> memberTermConditionResponses;

    public MemberTermConditionsResponse(List<MemberTermConditionResponse> memberTermConditionResponses) {
        this.memberTermConditionResponses = memberTermConditionResponses;
    }

    public List<MemberTermConditionResponse> getMemberTermConditionResponses() {
        return memberTermConditionResponses;
    }
}
