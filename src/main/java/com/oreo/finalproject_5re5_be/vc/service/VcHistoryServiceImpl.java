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


    /**
     * 요청 이력 저장
     * @param requestHistory
     */
    @Override
    public void requestHistorySave(VcRequestHistoryRequest requestHistory) {
        //Code 를 찾는다.
        Code code = codeRepository.findById(requestHistory.getCcSeq())
                .orElseThrow(() -> new IllegalArgumentException("Code not found"));
        //SRC 를 찾는다.
        VcSrcFile src = vcSrcFileRepository.findById(requestHistory.getSrcSeq())
                .orElseThrow(() -> new IllegalArgumentException("SrcSeq not found"));
        //TRG 를 찾는다.
        VcTrgFile trgFile = vcTrgFileRepository.findById(requestHistory.getTrgSeq())
                .orElseThrow(() -> new IllegalArgumentException("TrgSeq not found"));

        //객체 생성
        VcRequestHistory vcRequestHistory = VcRequestHistory.builder()
                .ccSeq(code)
                .trgSeq(trgFile)
                .srcSeq(src)
                .build();
        vcRequestHistoryRepository.save(vcRequestHistory);//객체 저장
    }

    /**
     * 결과 이력 저장
     * @param resultHistory
     */
    @Override
    public void resultHistorySaver(VcResultHistoryRequest resultHistory) {
        //Code 를 찾는다.
        Code code = codeRepository.findById(resultHistory.getCcSeq())
                .orElseThrow(() -> new IllegalArgumentException("Code not found"));
        //VC를 찾는다.
        Vc vc = vcRepository.findById(resultHistory.getVc())
                .orElseThrow(() -> new IllegalArgumentException("Vc not found"));
        //객체 생성
        VcResultHistory vcResultHistory = VcResultHistory.builder()
                .ccSeq(code)
                .vc(vc)
                .build();
        vcResultHistoryRepository.save(vcResultHistory);//객체 저장
    }
}
