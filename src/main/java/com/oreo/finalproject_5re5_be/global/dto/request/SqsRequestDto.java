package com.oreo.finalproject_5re5_be.global.dto.request;

import com.oreo.finalproject_5re5_be.global.constant.MessageType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SqsRequestDto {
    MessageType messageType;
    String message;
}
