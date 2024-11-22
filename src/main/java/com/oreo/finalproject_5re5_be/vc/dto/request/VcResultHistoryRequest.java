package com.oreo.finalproject_5re5_be.vc.dto.request;


import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VcResultHistoryRequest {
    private Long ccSeq;
    private Long vc;
}
