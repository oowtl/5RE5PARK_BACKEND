package com.oreo.finalproject_5re5_be.vc.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VcRowResponse {
    private Long seq;
    private Integer rowOrder;
}
