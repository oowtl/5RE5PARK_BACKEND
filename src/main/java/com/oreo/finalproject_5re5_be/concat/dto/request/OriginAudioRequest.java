package com.oreo.finalproject_5re5_be.concat.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OriginAudioRequest { //순수한 오디오 파일
    @NotNull(message = "seq 필드는 null 일 수 없습니다.")
    private Long seq;

    @Size(max = 1024, message = "fileUrl 필드는 최대 1024자까지 허용됩니다.")
    @NotNull(message = "fileUrl 필드는 null 일 수 없습니다.")
    private String audioUrl;

    @Size(max = 5, message = "extension 필드는 최대 5자까지 허용됩니다.")
    @NotNull(message = "extension 필드는 null 일 수 없습니다.")
    private String extension;

    @Positive(message = "fileSize 필드는 양수여야 합니다.")
    @NotNull(message = "fileSize 필드는 null 일 수 없습니다.")
    private Long fileSize;

    @Positive(message = "length 필드는 양수여야 합니다.")
    @NotNull(message = "length 필드는 null 일 수 없습니다.")
    private Long fileLength;

    @Size(max = 255, message = "fileName 필드는 최대 255자까지 허용됩니다.")
    @NotNull(message = "filename 필드는 null 일 수 없습니다.")
    private String fileName;

    @NotNull(message = "createdDate 필드는 null 일 수 없습니다.")
    private LocalDateTime createdDate;

}
