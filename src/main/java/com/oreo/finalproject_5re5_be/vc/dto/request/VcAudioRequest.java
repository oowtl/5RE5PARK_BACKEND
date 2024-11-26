package com.oreo.finalproject_5re5_be.vc.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VcAudioRequest {
    @NotNull(message = "seq 필드는 null 일 수 없습니다.")
    private Long seq;
    @Size(max = 255, message = "name 필드는 최대 255자 까지 가능합니다.")
    @NotNull(message = "name 필드는 null 일 수 없습니다.")
    private String name;

    @Size(max = 1024, message = "fileUrl 필드는 최대 1024자까지 허용됩니다.")
    @NotNull(message = "fileUrl 필드는 null 일 수 없습니다.")
    private String fileUrl;

//    @Positive(message = "length 필드는 양수여야 합니다.") //길이가 없을경우 오류나서 잠시 닫음
    @NotNull(message = "length 필드는 null 일 수 없습니다.")
    private Integer length;

    @Size(max = 50, message = "size 필드는 최대 50자까지 허용됩니다.")
    @NotNull(message = "size 필드는 null 일 수 없습니다.")
    private String size;

    @Size(max = 5, message = "extension 필드는 최대 5자까지 허용됩니다.")
    @NotNull(message = "extension 필드는 null 일 수 없습니다.")
    private String extension;

    public static VcAudioRequest of(Long seq, String name, String fileUrl, Integer length,
                                    String size, String extension){
        return new VcAudioRequest(seq, name, fileUrl, length, size, extension);
    }
}
