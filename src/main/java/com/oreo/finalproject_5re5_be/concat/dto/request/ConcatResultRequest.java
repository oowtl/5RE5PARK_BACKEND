package com.oreo.finalproject_5re5_be.concat.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ConcatResultRequest {

    @NotNull(message = "ConcatTab Seq는 null 일 수 없습니다.")
    private Long concatTabSeq; // ConcatTab의 Seq (Project와 연결된)

    private Long optionSeq; // ConcatOption의 Seq. option 안 쓰는 방법도 생각중이라 일단 null가능

    @NotNull(message = "AudioFormat Seq는 null 일 수 없습니다.")
    private Long audioFormatSeq; // AudioFormat의 Seq

    @NotNull(message = "url 필드는 null 일 수 없습니다.")
    private String ResultUrl;

    @NotNull(message = "extension 필드는 null 일 수 없습니다.")
    private String ResultExtension;

    @NotNull(message = "fileSize 필드는 null 일 수 없습니다.")
    private Long ResultFileSize;

    @NotNull(message = "fileLength 필드는 null 일 수 없습니다.")
    private Float ResultFileLength;

    @NotNull(message = "fileName 필드는 null 일 수 없습니다.")
    @Size(max = 255, message = "FileName 필드는 최대 255자까지 허용됩니다.")
    private String ResultFileName;

    @NotNull(message = "createdDateTime 필드는 null 일 수 없습니다.")
    private LocalDateTime CreatedDateTime;

}
