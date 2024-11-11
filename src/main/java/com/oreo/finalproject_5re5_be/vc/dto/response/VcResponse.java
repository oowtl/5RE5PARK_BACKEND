package com.oreo.finalproject_5re5_be.vc.dto.response;

import com.oreo.finalproject_5re5_be.vc.entity.VcResultFile;
import com.oreo.finalproject_5re5_be.vc.entity.VcSrcFile;
import com.oreo.finalproject_5re5_be.vc.entity.VcText;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VcResponse {
    private VcSrcFile vcSrcFile;
    private VcResultFile vcResultFile;
    private VcText vcText;
}
