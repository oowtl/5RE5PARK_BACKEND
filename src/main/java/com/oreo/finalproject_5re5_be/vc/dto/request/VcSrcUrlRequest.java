package com.oreo.finalproject_5re5_be.vc.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VcSrcUrlRequest {
    @NotNull(message = "seq 필드는 null 일 수 없습니다.")
    private Long seq;
    @NotNull(message = "url 필드는 null 일 수 없습니다.")
    private String url;
}
