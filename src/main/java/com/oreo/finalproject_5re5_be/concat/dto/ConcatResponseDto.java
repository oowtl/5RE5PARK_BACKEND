package com.oreo.finalproject_5re5_be.concat.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConcatResponseDto {
    private String audioUrl;
    private List<RowInfoDto> rows;
}