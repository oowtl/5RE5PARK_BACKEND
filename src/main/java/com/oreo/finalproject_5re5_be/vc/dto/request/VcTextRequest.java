package com.oreo.finalproject_5re5_be.vc.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VcTextRequest {
    private Long seq;
    private String text;
}
