package com.oreo.finalproject_5re5_be.vc.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VcActivateResponse {
    private Long seq;
    private Character activate;
}
