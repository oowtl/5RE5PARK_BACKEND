package com.oreo.finalproject_5re5_be.vc.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VcTextResponse {
    // 텍스트 저장시 사용할 DTO
    private Long seq;
    private String text;

    public static VcTextResponse of(Long seq, String text) {
        return new VcTextResponse(seq, text);
    }
}
