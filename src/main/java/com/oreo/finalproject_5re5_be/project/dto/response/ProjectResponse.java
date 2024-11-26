package com.oreo.finalproject_5re5_be.project.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponse {
    private Long projectSeq;
    private String projectName;
    private String projectContent;
    private LocalDateTime projectDate;
    private LocalDateTime projectUpdateDate;
    private Boolean tts;
    private Boolean vc;
    private Boolean concat;
    private Character projectActivate;
}
