package com.oreo.finalproject_5re5_be.vc.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VcAudioRequest {
    private Long seq1;
    private String name;
    private String fileUrl;
    private Integer length;
    private String size;
    private String extension;
}
