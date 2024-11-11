package com.oreo.finalproject_5re5_be.vc.service;

import com.oreo.finalproject_5re5_be.vc.dto.request.VcSrcRequest;
import com.oreo.finalproject_5re5_be.vc.dto.response.AudioGetResponse;
import com.oreo.finalproject_5re5_be.vc.dto.request.VcAudioRequest;
import com.oreo.finalproject_5re5_be.vc.dto.request.VcSeqRequest;
import com.oreo.finalproject_5re5_be.vc.dto.request.VcTextRequest;
import com.oreo.finalproject_5re5_be.vc.dto.response.VcResponse;
import com.oreo.finalproject_5re5_be.vc.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class VcServiceImpl implements VcService{
    private VcRepository vcRepository;
    private VcSrcFileRepository vcSrcFileRepository;
    private VcTargetFileRepository vcTargetFileRepository;
    private VcResultFileRepository vcResultFileRepository;
    private VcTextRepository vcTextRepository;
    @Autowired
    public VcServiceImpl(VcRepository vcRepository,
                         VcSrcFileRepository vcSrcFileRepository,
                         VcTargetFileRepository vcTargetFileRepository,
                         VcResultFileRepository vcResultFileRepository,
                         VcTextRepository vcTextRepository) {
        this.vcRepository = vcRepository;
        this.vcSrcFileRepository = vcSrcFileRepository;
        this.vcTargetFileRepository = vcTargetFileRepository;
        this.vcResultFileRepository = vcResultFileRepository;
        this.vcTextRepository = vcTextRepository;
    }


    @Override
    public void SrcSave(VcSrcRequest vcSrcRequest) {

    }

    @Override
    public void TrgSave(VcAudioRequest vcAudioRequest) {

    }

    @Override
    public void ResultSave(VcAudioRequest vcAudioRequest) {

    }

    @Override
    public void TextSave(VcTextRequest vcTextRequest) {

    }

    @Override
    public VcResponse getVcResponse(Long ProjectSeq) {
        return null;
    }

    @Override
    public AudioGetResponse getSrcFile(VcSeqRequest vcSeqRequest) {
        return null;
    }

    @Override
    public AudioGetResponse getTrgFile(VcSeqRequest vcSeqRequest) {
        return null;
    }

    @Override
    public void updateText(VcSeqRequest vcResponse, String text) {

    }

    @Override
    public void updateRowOrder(VcSeqRequest vcSeqRequest, int rowOrder) {

    }
}
