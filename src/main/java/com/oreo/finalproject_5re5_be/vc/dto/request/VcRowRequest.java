package com.oreo.finalproject_5re5_be.vc.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VcRowRequest {
    @NotNull(message = "변경할 번호를 입력해주세요.")
    private Long seq;

    @NotNull(message = "변경할 행순서를 입력해주세요.")
    private Integer rowOrder;
}
