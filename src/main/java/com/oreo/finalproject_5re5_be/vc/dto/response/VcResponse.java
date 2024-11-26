package com.oreo.finalproject_5re5_be.vc.dto.response;

import com.oreo.finalproject_5re5_be.vc.dto.request.VcResultsRequest;
import com.oreo.finalproject_5re5_be.vc.dto.request.VcSrcsRequest;
import com.oreo.finalproject_5re5_be.vc.dto.request.VcTextRequest;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VcResponse {
    private Character activate;
    private VcSrcsRequest vcSrcFile;
    private VcResultsRequest vcResultFile;
    private VcTextRequest vcText;
}
