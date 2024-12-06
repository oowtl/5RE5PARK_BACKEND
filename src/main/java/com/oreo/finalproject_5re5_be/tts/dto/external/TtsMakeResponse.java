package com.oreo.finalproject_5re5_be.tts.dto.external;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TtsMakeResponse {
    private String fileName;
    private String fileExtension;
    private Integer fileLength;
    private String fileSize;
    private String url;
}
