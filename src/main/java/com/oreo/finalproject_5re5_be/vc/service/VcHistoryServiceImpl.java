package com.oreo.finalproject_5re5_be.vc.service;

import com.oreo.finalproject_5re5_be.member.entity.Code;
import com.oreo.finalproject_5re5_be.member.repository.CodeRepository;
import com.oreo.finalproject_5re5_be.vc.dto.request.VcRequestHistoryRequest;
import com.oreo.finalproject_5re5_be.vc.dto.request.VcResultHistoryRequest;
import com.oreo.finalproject_5re5_be.vc.entity.*;
import com.oreo.finalproject_5re5_be.vc.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VcHistoryServiceImpl implements VcHistoryService {
    private VcRequestHistoryRepository vcRequestHistoryRepository;
    private VcResultHistoryRepository vcResultHistoryRepository;
    private VcSrcFileRepository vcSrcFileRepository;
    private VcTrgFileRepository vcTrgFileRepository;
    private VcRepository vcRepository;
    private CodeRepository codeRepository;

    @Autowired
    public VcHistoryServiceImpl(VcRequestHistoryRepository vcRequestHistoryRepository,
                                VcResultHistoryRepository vcResultHistoryRepository){
        this.vcRequestHistoryRepository = vcRequestHistoryRepository;
        this.vcResultHistoryRepository = vcResultHistoryRepository;
    }


    @Override
    public void requestHistorySave(VcRequestHistoryRequest requestHistory) {
        Code code = codeRepository.findById(requestHistory.getCcSeq())
                .orElseThrow(() -> new IllegalArgumentException("Code not found"));
        VcSrcFile src = vcSrcFileRepository.findById(requestHistory.getSrcSeq())
                .orElseThrow(() -> new IllegalArgumentException("SrcSeq not found"));
        VcTrgFile trgFile = vcTrgFileRepository.findById(requestHistory.getTrgSeq())
                .orElseThrow(() -> new IllegalArgumentException("TrgSeq not found"));

        VcRequestHistory vcRequestHistory = VcRequestHistory.builder()
                .ccSeq(code)
                .trgSeq(trgFile)
                .srcSeq(src)
                .build();
        vcRequestHistoryRepository.save(vcRequestHistory);
    }

    @Override
    public void resultHistorySaver(VcResultHistoryRequest resultHistory) {
        Code code = codeRepository.findById(resultHistory.getCcSeq())
                .orElseThrow(() -> new IllegalArgumentException("Code not found"));
        Vc vc = vcRepository.findById(resultHistory.getVc())
                .orElseThrow(() -> new IllegalArgumentException("Vc not found"));
        VcResultHistory vcResultHistory = VcResultHistory.builder()
                .ccSeq(code)
                .vc(vc)
                .build();
        vcResultHistoryRepository.save(vcResultHistory);
    }
}
