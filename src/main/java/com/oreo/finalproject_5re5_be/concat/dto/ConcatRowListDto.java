package com.oreo.finalproject_5re5_be.concat.dto;

import com.oreo.finalproject_5re5_be.concat.entity.ConcatRow;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ConcatRowListDto {
    private List<ConcatRowDto> rowList;

    // concatRow 엔티티 리스트 정보로 ConcatRowDto 객체 생성하는 메서드
    public static ConcatRowListDto of(List<ConcatRow> concatRowEntityList) {
        // concatRow 엔티티 리스트를 ConcatRowDto 리스트로 변환
        List<ConcatRowDto> concatRowDtoLList =
                concatRowEntityList.stream().map(ConcatRowDto::of).toList();

        // ConcatRowDto 리스트를 넣으며 ConcatRowListDto 생성
        return ConcatRowListDto.builder().rowList(concatRowDtoLList).build();
    }
}
