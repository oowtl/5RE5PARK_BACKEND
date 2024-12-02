package com.oreo.finalproject_5re5_be.global.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SqsRequestDto {
    String message;
}
