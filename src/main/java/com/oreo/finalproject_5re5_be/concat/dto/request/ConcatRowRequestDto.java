package com.oreo.finalproject_5re5_be.concat.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public class ConcatRowRequestDto {
    @NotNull private Long concatTabId;
    @NotNull private String fileName;
    @NotNull private List<ConcatRowRequest> concatRowRequests;
}
