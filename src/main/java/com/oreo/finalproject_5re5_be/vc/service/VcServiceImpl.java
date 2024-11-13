package com.oreo.finalproject_5re5_be.vc.service;

import com.oreo.finalproject_5re5_be.project.entity.Project;
import com.oreo.finalproject_5re5_be.project.repository.ProjectRepository;
import com.oreo.finalproject_5re5_be.vc.dto.request.*;
import com.oreo.finalproject_5re5_be.vc.dto.response.VcResponse;
import com.oreo.finalproject_5re5_be.vc.entity.VcResultFile;
import com.oreo.finalproject_5re5_be.vc.entity.VcSrcFile;
import com.oreo.finalproject_5re5_be.vc.entity.VcText;
import com.oreo.finalproject_5re5_be.vc.entity.VcTrgFile;
import com.oreo.finalproject_5re5_be.vc.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class VcServiceImpl implements VcService{
    private VcRepository vcRepository;
    private VcSrcFileRepository vcSrcFileRepository;
    private VcTargetFileRepository vcTargetFileRepository;
    private VcResultFileRepository vcResultFileRepository;
    private VcTextRepository vcTextRepository;
    private ProjectRepository projectRepository;
    @Autowired
    public VcServiceImpl(VcRepository vcRepository,
                         VcSrcFileRepository vcSrcFileRepository,
                         VcTargetFileRepository vcTargetFileRepository,
                         VcResultFileRepository vcResultFileRepository,
                         VcTextRepository vcTextRepository,
                         ProjectRepository projectRepository) {
        this.vcRepository = vcRepository;
        this.vcSrcFileRepository = vcSrcFileRepository;
        this.vcTargetFileRepository = vcTargetFileRepository;
        this.vcResultFileRepository = vcResultFileRepository;
        this.vcTextRepository = vcTextRepository;
        this.projectRepository = projectRepository;
    }


    @Override
    public void srcSave(VcSrcRequest vcSrcRequest) {
        //프로젝트 조회
        Project project = projectRepository.findById(vcSrcRequest.getSeq1())
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
        log.info("[vcService] SrcSave Project find : {} ", project); // 프로젝트 확인
        //프로젝트 조회한 값과 입력한 값 저장을 하기 위한 SRC 객체 생성
        VcSrcFile src = VcSrcFile.builder()
                .proSeq(project)
                .rowOrder(vcSrcRequest.getRowOrder())
                .fileName(vcSrcRequest.getName())
                .fileUrl(vcSrcRequest.getFileUrl())
                .fileLength(vcSrcRequest.getLength())
                .fileSize(vcSrcRequest.getSize())
                .extension(vcSrcRequest.getExtension()).build();
        log.info("[vcService] Save src 생성  : {}", src); //SRC 객체 생성 확인
        vcSrcFileRepository.save(src);// SRC 객체 저장
    }

    @Override
    public void trgSave(VcAudioRequest vcAudioRequest) {
        //프로젝트 조회
        Project project = projectRepository.findById(vcAudioRequest.getSeq1())
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
        log.info("[vcService] TrgSave Project find : {} ", project); //프로젝트 확인
        //프로젝트 조회한 값과 입력한 값을 저장하기 위한 TRG 객체 생성
        VcTrgFile trg = VcTrgFile.builder()
                .proSeq(project)
                .fileName(vcAudioRequest.getName())
                .fileUrl(vcAudioRequest.getFileUrl())
                .fileLength(vcAudioRequest.getLength())
                .fileSize(vcAudioRequest.getSize())
                .extension(vcAudioRequest.getExtension()).build();
        log.info("[vcService] Save trg 생성  : {}", trg); //TRG 객체 생성 확인
        vcTargetFileRepository.save(trg);// TRG 객체 저장
    }

    @Override
    public void resultSave(VcAudioRequest vcAudioRequest) {
        //SRCFile 조회
        VcSrcFile srcFile = vcSrcFileRepository.findById(vcAudioRequest.getSeq1())
                .orElseThrow(() -> new IllegalArgumentException("SrcFile not found"));
        log.info("[vcService] ResultSave srcFile find : {} ", srcFile);// SRC 확인
        //프로젝트 조회
        Project project = projectRepository.findById(srcFile.getSrcSeq())
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
        log.info("[vcService] ResultSave Project find : {} ", project);// 프로젝트 확인
        //프로젝트 조회한 값과 SRC 조회한 값, 입력한 값을 저장하기 위한 ResultFile 객체 생성
        VcResultFile result = VcResultFile.builder()
                .proSeq(project)
                .srcSeq(srcFile)
                .fileName(vcAudioRequest.getName())
                .fileUrl(vcAudioRequest.getFileUrl())
                .fileLength(vcAudioRequest.getLength())
                .fileSize(vcAudioRequest.getSize())
                .extension(vcAudioRequest.getExtension()).build();
        log.info("[vcService] Save result 생성 : {}", result); // Result 객체 생성 확인
        vcResultFileRepository.save(result); // result 객체 저장
    }


    @Override
    public void textSave(VcTextRequest vcTextRequest) {
        //SRC 조회
        VcSrcFile srcFile = vcSrcFileRepository.findById(vcTextRequest.getSrcSeq())
                .orElseThrow(() -> new IllegalArgumentException("SrcFile not found"));
        log.info("[vcService] TextSave srcFile find : {} ", srcFile);//SRC 확인
        //프로젝트 조회
        Project project = projectRepository.findById(srcFile.getSrcSeq())
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
        log.info("[vcService] TextSave Project find : {} ", project);//프로젝트 확인
        //SRC 조회한 값과 프로젝트 조회한 값, 입력 값 저장하기 위한 TextFile 객체 생성
        VcText text = VcText.builder()
                .proSeq(project)
                .srcSeq(srcFile)
                .comment(vcTextRequest.getText())
                .length(String.valueOf(vcTextRequest.getText().length()))
                .build();
        log.info("[vcService] Save text 생성 : {}", text);//Text 객체 생성 값 확인
        vcTextRepository.save(text);//Text 객체 저장
    }

    @Override
    public List<VcResponse> getVcResponse(Long ProjectSeq) {
        //프로젝트 seq 조회한 값
        List<VcSrcFile> vcSrcFileList = vcSrcFileRepository.findByProjectId(ProjectSeq);
        log.info("[vcService] GetVcResponse vcSrcFileList find : {} ", vcSrcFileList);
        //src, result, text 값 저장하기 위한 배열 생성
        List<VcResponse> vcResponseList = new ArrayList<>();

        for (VcSrcFile vcSrcFile : vcSrcFileList) {
            // SRC 로 Result, Text 조회 값이 없을경우 null 출력
            VcResultFile vcResultFile = vcResultFileRepository.findBySrcSeq_SrcSeq(vcSrcFile.getSrcSeq())
                    != null ? vcResultFileRepository.findBySrcSeq_SrcSeq(vcSrcFile.getSrcSeq()) : null;
            log.info("[vcService] GetVcResponse vcResultFile find : {} ", vcResultFile);
            VcText vcText = vcTextRepository.findBySrcSeq_SrcSeq(vcSrcFile.getSrcSeq())
                    != null ? vcTextRepository.findBySrcSeq_SrcSeq(vcSrcFile.getSrcSeq()) : null;
            log.info("[vcService] GetVcText find : {} ", vcText);
            // VcResponse 객체 생성 후 리스트에 추가
            VcResponse vcResponse = new VcResponse(vcSrcFile, vcResultFile, vcText);
            vcResponseList.add(vcResponse);
        }
        log.info("[vcService] GetVcResponseList find : {} ", vcResponseList);
        return vcResponseList;
    }

    @Override
    public String getSrcFile(Long seq) {
        //SRC seq 로 SRC 값 조회
        VcSrcFile srcFile = vcSrcFileRepository.findById(seq)
                .orElseThrow(() -> new IllegalArgumentException("SrcFile not found"));
        log.info("[vcService] getSrcFile VcSRcFile find : {} ", srcFile);//SRC 값 확인
        //S3 SRC URL 값 출력
        return srcFile.getFileUrl();
    }

    @Override
    public String getResultFile(Long seq) {
        //TRG seq 로 TRG 값 조회
        VcResultFile resultFile = vcResultFileRepository.findById(seq)
                .orElseThrow(() -> new IllegalArgumentException("TrgFile not found"));
        log.info("[vcService] getResultFile ResultFile find : {} ", resultFile);//TRG 값 확인
        //S3 TRG URL 값 출력
        return resultFile.getFileUrl();
    }

    @Override
    public void updateText(Long seq, String text) {
        //Text seq 로 Text 값 조회 검증
        VcText vcText = vcTextRepository.findById(seq)
                .orElseThrow(() -> new IllegalArgumentException("text not found"));
        log.info("[vcService] updateText VcText find : {} ", vcText); //Text 값 확인
        //변경할 값과 seq 값 변경 객체 생성
        VcText updateText = vcText.toBuilder()
                .vtSeq(vcText.getVtSeq())
                .comment(text)
                .build();
        log.info("[vcService] updateText updateText find : {} ", updateText); //변경 객체 값 확인
        vcTextRepository.save(updateText);//텍스트 값 변경
    }

    @Override
    public void updateRowOrder(Long seq, int rowOrder) {
        //SRC seq 로 SRC 값 조회 검증
        VcSrcFile vcSrcFile = vcSrcFileRepository.findById(seq)
                .orElseThrow(() -> new IllegalArgumentException("src file not found"));
        log.info("[vcService] updateRowOrder vcSrcFile find : {} ", vcSrcFile);//SRC 값 확인
        //변경할 행순서 값과 SRC seq 값 변경 객체 생성
        VcSrcFile updateSrcFile = vcSrcFile.toBuilder()
                .srcSeq(vcSrcFile.getSrcSeq())
                .rowOrder(rowOrder)
                .build();
        log.info("[vcService] updateRowOrder updateSrcFile find : {} ", updateSrcFile);// 변경 객체 확인
        vcSrcFileRepository.save(updateSrcFile);//행순서 변경
    }

    @Override
    public void deleteSrcFile(Long seq) {
        //SRC seq 로 SRC 값 조회 검증
        VcSrcFile vcSrcFile = vcSrcFileRepository.findById(seq)
                .orElseThrow(() -> new IllegalArgumentException("src file not found"));
        //활성화 상태 N로 변경
        VcSrcFile deleteSrcFile = vcSrcFile.toBuilder()
                .srcSeq(vcSrcFile.getSrcSeq())
                .activate('N')
                .build();
        log.info("[vcService] deleteSrcFile vcSrcFile find : {} ", deleteSrcFile);//변경 확인
        vcSrcFileRepository.save(deleteSrcFile);//활성화상태 변경
    }
}
