package com.oreo.finalproject_5re5_be.concat.dto;

import com.oreo.finalproject_5re5_be.concat.entity.ConcatRow;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Builder
public class ConcatRowDto {
    private Long concatRowSequence;
    private Long projectSequence;
    private String rowText;
    private Character selected;
    private Character status;
    private Float silence;
    private Integer rowIndex;

    // concatRow 엔티티 정보로 ConcatRowDto 객체 생성하는 메서드
    public static ConcatRowDto of(ConcatRow concatRow) {
        return ConcatRowDto.builder()
                .concatRowSequence(concatRow.getConcatRowSeq())
                .rowText(concatRow.getRowText())
                .selected(concatRow.getSelected())
                .status(concatRow.getStatus())
                .silence(concatRow.getSilence())
                .rowIndex(concatRow.getRowIndex())
                .projectSequence(concatRow.getConcatTab().getProjectId())
                .build();
    }
}
