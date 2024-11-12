package com.oreo.finalproject_5re5_be.vc.dto.request;

import com.oreo.finalproject_5re5_be.project.entity.Project;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VcSrcRequest {
    private Long seq1;
    private Integer rowOrder;
    private String name;
    private String fileUrl;
    private Integer length;
    private String  size;
    private String extension;
}
