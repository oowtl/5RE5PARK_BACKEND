package com.oreo.finalproject_5re5_be.concat.dto.response;


import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ConcatUrlResponse {
    //조회, 저장 후 출력할 SEQ와 URL
    private Long seq;
    private String url;
}
