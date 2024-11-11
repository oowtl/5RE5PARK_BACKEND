package com.oreo.finalproject_5re5_be.vc.service;


import com.oreo.finalproject_5re5_be.vc.dto.request.VcSrcRequest;
import com.oreo.finalproject_5re5_be.vc.dto.response.AudioGetResponse;
import com.oreo.finalproject_5re5_be.vc.dto.request.VcAudioRequest;
import com.oreo.finalproject_5re5_be.vc.dto.request.VcSeqRequest;
import com.oreo.finalproject_5re5_be.vc.dto.request.VcTextRequest;
import com.oreo.finalproject_5re5_be.vc.dto.response.VcResponse;

public interface VcService {
    /*
        저장
        1. SRC 파일 저장 (Long ProjectSeq, integer rowOrder,String name, String fileUrl,
         String length,String size,String extension) (VcSrcRequest)
        2. TRG 파일 저장 (Long ProjectSeq, Long SrcSeq, String name, String fileUrl,
         Long length, String size, String extension) (VcAudioRequest)
        3. Result 파일 저장 (Long ProjectSeq, Long SrcSeq, String name, String fileUrl,
         Long length, String size, String extension) (VcAudioRequest)
        4. 텍스트 저장 (SrcSeq, text) (VcTextRequest)

        조회
        5. 프로젝트 행 전체 조회 (ProjectSeq)

        6. SRC 파일 다운로드 (ProjectSeq, SrcSeq)
        7. SRC 파일 재생 (ProjectSeq, SrcSeq)

        8. Result 파일 다운로드 (ProjectSeq, ResultSeq)
        9. Result 파일 재생 (ProjectSeq, ResultSeq)

        수정
        10. 행 텍스트 수정 (SrcSeq, TextSeq, text)
        11. 행 순서 변경 (ProjectSeq, SrcSeq, rowOrder)
     */
    void SrcSave(VcSrcRequest vcSrcRequest);
    void TrgSave(VcAudioRequest vcAudioRequest);
    void ResultSave(VcAudioRequest vcAudioRequest);
    void TextSave(VcTextRequest vcTextRequest);

    VcResponse getVcResponse(Long ProjectSeq);
    AudioGetResponse getSrcFile(VcSeqRequest vcSeqRequest);
    AudioGetResponse getTrgFile(VcSeqRequest vcSeqRequest);

    void updateText(VcSeqRequest vcResponse, String text);
    void updateRowOrder(VcSeqRequest vcSeqRequest, int rowOrder);
}
