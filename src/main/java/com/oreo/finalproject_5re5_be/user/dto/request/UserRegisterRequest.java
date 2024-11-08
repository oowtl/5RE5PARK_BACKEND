package com.oreo.finalproject_5re5_be.user.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class UserRegisterRequest {

    private String id;
    private String email;
    private String password;
    private String name;
    private String normAddr;
    private String birthDate;
    private String locaAddr;
    private String detailAddr;
    private String passAddr;
    private String chkValid;

}
