package com.oreo.finalproject_5re5_be.vc.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VcSrcsRequest {
    @NotNull(message = "seq 필드는 null 일 수 없습니다.")
    private Long seq;
    @Positive(message = "rowOrder 필드는 1 이상의 양수여야 합니다.")
    @NotNull(message = "orwOrder 필드는 null 일 수 없습니다.")
    private Integer rowOrder;
    @Size(max = 255, message = "name 필드는 최대 255자 까지 가능합니다.")
    @NotNull(message = "name 필드는 null 일 수 없습니다.")
    private String name;
    @Size(max = 1024, message = "fileUrl 필드는 최대 1024자까지 허용됩니다.")
    @NotNull(message = "fileUrl 필드는 null 일 수 없습니다.")
    private String fileUrl;
}
