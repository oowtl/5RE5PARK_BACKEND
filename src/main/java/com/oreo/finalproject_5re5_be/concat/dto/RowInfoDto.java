package com.oreo.finalproject_5re5_be.concat.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RowInfoDto {
    private String audioUrl;
    private float silenceInterval;
}
