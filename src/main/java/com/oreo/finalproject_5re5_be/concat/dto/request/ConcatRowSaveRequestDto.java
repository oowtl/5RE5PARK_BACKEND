package com.oreo.finalproject_5re5_be.concat.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@AllArgsConstructor
public class ConcatRowSaveRequestDto {
    private Long concatTabId;
    private List<ConcatRowRequest> concatRowRequests;
}
