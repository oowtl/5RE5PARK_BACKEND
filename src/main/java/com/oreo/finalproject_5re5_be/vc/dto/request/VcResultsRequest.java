package com.oreo.finalproject_5re5_be.vc.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VcResultsRequest {
    private Long seq;

    @Size(max = 255, message = "name 필드는 최대 255자 까지 가능합니다.")
    private String name;

    @Size(max = 1024, message = "fileUrl 필드는 최대 1024자까지 허용됩니다.")
    private String fileUrl;
}
