package com.oreo.finalproject_5re5_be.user.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class UserTerm {
    private Long termCondCode;
    private Boolean agreed;
    private Boolean isMandatory;

    public boolean isValid() {
        if (isMandatory) return agreed != null && agreed;
        return agreed != null;
    }
}
