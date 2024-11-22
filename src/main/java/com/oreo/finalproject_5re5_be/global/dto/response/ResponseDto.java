package com.oreo.finalproject_5re5_be.global.dto.response;

import lombok.*;
import org.springframework.http.ResponseEntity;

@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto<T>{
    private int status;
    private T response;

    public ResponseEntity<ResponseDto<T>> toResponseEntity() {
        return ResponseEntity.status(this.status).body(this);
    }
}
