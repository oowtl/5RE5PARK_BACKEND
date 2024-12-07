package com.oreo.finalproject_5re5_be.project.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProjectTextRequest {
    private Long proSeq;
    private String projectName;
}
