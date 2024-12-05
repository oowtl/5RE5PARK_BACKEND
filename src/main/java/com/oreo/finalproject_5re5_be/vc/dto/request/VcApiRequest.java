package com.oreo.finalproject_5re5_be.vc.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VcApiRequest {
    private List<String> srcUrls;
    private String trgUrl;

    public static VcApiRequest of(List<String> srcUrls, String trgUrl){
        return new VcApiRequest(srcUrls, trgUrl);
    }
}