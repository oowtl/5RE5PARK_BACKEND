package com.oreo.finalproject_5re5_be.concat.service.helper;

import com.oreo.finalproject_5re5_be.concat.dto.request.OriginAudioRequest;
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

        OriginAudioRequest bgm_info = getOriginAudioRequest(concatTab);

        // 사용자 검증
        if (validateMemberCurrent(concatTab, memberSeq)) {
            return ConcatTabResponseDto.builder()
                    .tabId(concatTab.getProjectId())
                    .frontSilence(concatTab.getFrontSilence())
                    .status(concatTab.getStatus())
                    .audioInfo(bgm_info)
                    .build();
        }
        throw new IllegalArgumentException("사용자가 소유한 프로젝트가 아닙니다. 소유한 사용자 : "
                + concatTab.getProject().getMember());
    }

    private static OriginAudioRequest getOriginAudioRequest(ConcatTab concatTab) {
        OriginAudioRequest bgm_info = null;

        //getBgmAudioFile했는데 null일 수도 있다
        if (concatTab.getBgmAudioFile() != null) {
            bgm_info = OriginAudioRequest.builder()
                    .seq(concatTab.getBgmAudioFile().getAudioFileSeq())
                    .audioUrl(concatTab.getBgmAudioFile().getAudioUrl())
                    .extension(concatTab.getBgmAudioFile().getExtension())
                    .fileSize(concatTab.getBgmAudioFile().getFileSize())
                    .fileLength(concatTab.getBgmAudioFile().getFileLength())
                    .fileName(concatTab.getBgmAudioFile().getFileName())
                    .build();
        }
        return bgm_info;
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
