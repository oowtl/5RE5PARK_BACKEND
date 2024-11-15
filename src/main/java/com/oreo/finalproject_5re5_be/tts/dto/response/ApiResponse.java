package com.oreo.finalproject_5re5_be.tts.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse <T>{
    private final int status;
    private final T response;
}
