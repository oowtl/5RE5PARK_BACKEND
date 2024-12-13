package com.oreo.finalproject_5re5_be.vc.dto.request;

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

    public static VcTextRequest of(Long seq, String text) {
        return new VcTextRequest(seq, text);
    }
}
