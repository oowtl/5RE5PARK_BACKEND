package com.oreo.finalproject_5re5_be.concat.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@AllArgsConstructor
public class ConcatRowRequestDto {
    @NotNull
    private Long concatTabId;
    @NotNull
    private Long memberSeq;
    @NotNull
    private String fileName;
    @NotNull
    private List<ConcatRowRequest> concatRowRequests;
}
