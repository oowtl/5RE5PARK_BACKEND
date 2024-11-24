package com.oreo.finalproject_5re5_be.vc.dto.response;

import com.oreo.finalproject_5re5_be.vc.entity.VcSrcFile;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VcUrlResponse {
    //조회, 저장 후 출력할 SEQ와 URL
    private Long seq;
    private String url;

    public static VcUrlResponse of(Long seq, String url) {
        return new VcUrlResponse(seq, url);
    }
}
