package com.oreo.finalproject_5re5_be.concat.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public class ConcatRowSaveRequestDto {
    private Long concatTabId;
    private List<ConcatRowRequest> concatRowRequests;
}
