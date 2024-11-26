package com.oreo.finalproject_5re5_be.concat.service.helper;

import com.oreo.finalproject_5re5_be.concat.dto.response.ConcatTabResponseDto;
import com.oreo.finalproject_5re5_be.concat.entity.ConcatTab;
import org.springframework.stereotype.Component;

import java.util.Objects;


/**
 * @apiNote ConcatTabService의 로직을 밖으로 분리한 클래스입니다.
 */
@Component
public class ConcatTabHelper {

    //ConcatTab의 구성요소를 Dto에 담아 리턴
    public ConcatTabResponseDto prepareConcatTab(ConcatTab concatTab, Long memberSeq) {
        if (validateMemberCurrent(concatTab, memberSeq)) {
            return ConcatTabResponseDto.builder()
                    .tabId(concatTab.getProjectId())
                    .concatOptionDto(null)
                    .frontSilence(concatTab.getFrontSilence())
                    .status(concatTab.getStatus())
                    .build();
        }
        throw new IllegalArgumentException("사용자가 소유한 프로젝트가 아닙니다. 소유한 사용자 : "
                + concatTab.getProject().getMember());
    }

    //
    public boolean validateMemberCurrent(ConcatTab concatTab, Long memberSeq) {
        if (Objects.equals(concatTab.getProject().getMember().getSeq(), memberSeq)) {
            return true;
        }

        throw new IllegalArgumentException("사용자가 소유한 프로젝트가 아닙니다. 소유한 사용자 : "
                + concatTab.getProject().getMember());
    }


}
