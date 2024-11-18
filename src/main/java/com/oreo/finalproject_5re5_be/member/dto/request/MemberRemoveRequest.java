package com.oreo.finalproject_5re5_be.member.dto.request;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class MemberRemoveRequest {

    private String code;
    private String detailCont;

}
