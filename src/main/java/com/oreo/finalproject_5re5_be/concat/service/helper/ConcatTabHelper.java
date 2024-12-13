package com.oreo.finalproject_5re5_be.concat.service.helper;

import com.oreo.finalproject_5re5_be.concat.dto.request.OriginAudioRequest;
import com.oreo.finalproject_5re5_be.concat.dto.response.ConcatTabResponseDto;
import com.oreo.finalproject_5re5_be.concat.entity.BgmFile;
import com.oreo.finalproject_5re5_be.concat.entity.ConcatTab;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * @apiNote ConcatTabService의 로직을 밖으로 분리한 클래스입니다.
 */
@Component
public class ConcatTabHelper {

    // ConcatTab의 구성요소를 Dto에 담아 리턴
    public ConcatTabResponseDto prepareConcatTab(ConcatTab concatTab, Long memberSeq) {

        // 사용자 검증
        if (validateMemberCurrent(concatTab, memberSeq)) {
            // bgmFile객체들을 그대로 프론트에게 줄 수 없으니 bgmFiles를 OriginAudioRequest(DTO)로 변환
            List<OriginAudioRequest> bgmList =
                    Optional.ofNullable(concatTab.getBgmFiles()).orElse(new ArrayList<>()).stream()
                            .map(this::convertToOriginAudioRequest)
                            .toList();

            return ConcatTabResponseDto.builder()
                    .tabId(concatTab.getProjectId())
                    .frontSilence(concatTab.getFrontSilence())
                    .status(concatTab.getStatus())
                    .bgmFileList(bgmList)
                    .build();
        }
        throw new IllegalArgumentException("사용자가 소유한 프로젝트가 아닙니다.");
    }

    public boolean validateMemberCurrent(ConcatTab concatTab, Long memberSeq) {
        if (Objects.equals(concatTab.getProject().getMember().getSeq(), memberSeq)) {
            return true;
        }

        throw new IllegalArgumentException(
                "사용자가 소유한 프로젝트가 아닙니다. 소유한 사용자 : " + concatTab.getProject().getMember());
    }

    // BgmFile을 OriginAudioRequest로 변환
    private OriginAudioRequest convertToOriginAudioRequest(BgmFile bgmFile) {
        return OriginAudioRequest.builder()
                .seq(bgmFile.getBgmFileSeq())
                .audioUrl(bgmFile.getAudioUrl())
                .extension(bgmFile.getExtension())
                .fileSize(bgmFile.getFileSize())
                .fileLength(bgmFile.getFileLength())
                .fileName(bgmFile.getFileName())
                .build();
    }
}
