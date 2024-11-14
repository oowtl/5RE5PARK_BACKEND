package com.oreo.finalproject_5re5_be.global.dto.response;

import lombok.*;

@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto<T>{
    private int status;
    private T response;
}
