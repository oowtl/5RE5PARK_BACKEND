package com.oreo.finalproject_5re5_be.vc.service;

import com.oreo.finalproject_5re5_be.global.dto.response.AudioFileInfo;
import com.oreo.finalproject_5re5_be.project.entity.Project;
import com.oreo.finalproject_5re5_be.project.repository.ProjectRepository;
import com.oreo.finalproject_5re5_be.vc.dto.request.*;
import com.oreo.finalproject_5re5_be.vc.dto.response.*;
import com.oreo.finalproject_5re5_be.vc.entity.*;
import com.oreo.finalproject_5re5_be.vc.repository.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
@RequiredArgsConstructor
@Validated
@Transactional
public class VcServiceImpl implements VcService{
    private VcRepository vcRepository;
    private VcSrcFileRepository vcSrcFileRepository;
    private VcTrgFileRepository vcTrgFileRepository;
    private VcResultFileRepository vcResultFileRepository;
    private VcTextRepository vcTextRepository;
    private ProjectRepository projectRepository;
    @Autowired
    public VcServiceImpl(VcRepository vcRepository,
                         VcSrcFileRepository vcSrcFileRepository,
                         VcTrgFileRepository vcTrgFileRepository,
                         VcResultFileRepository vcResultFileRepository,
                         VcTextRepository vcTextRepository,
                         ProjectRepository projectRepository) {
        this.vcRepository = vcRepository;
        this.vcSrcFileRepository = vcSrcFileRepository;
        this.vcTrgFileRepository = vcTrgFileRepository;
        this.vcResultFileRepository = vcResultFileRepository;
        this.vcTextRepository = vcTextRepository;
        this.projectRepository = projectRepository;
    }

    /**
     * Vc SRC 파일 저장
     * @param vcSrcRequest
     * @return VcUrlResponse
     */
    @Override
    @Transactional
    public VcUrlResponse srcSave(@Valid @NotNull VcSrcRequest vcSrcRequest, Long proSeq) {

        // VC 찾기
        Vc vc = vcRepository.findById(proSeq)
                .orElseThrow(() -> new IllegalArgumentException("not found"));
        log.info("[vcService] srcSave vc 확인 : {} ", vc);

        // vcSrcFileRepository를 통해 rowOrder 값 결정
        int rowOrder = Optional.ofNullable(vcSrcFileRepository.countByVc_ProjectSeq(vcSrcRequest.getSeq()))
                .map(count -> count + 1)
                .orElse(1);
        log.info("[vcService] srcSave rowOrder 확인 : {} ", rowOrder);
        //SRC를 찾아서 있다면 1로 없다면 사이즈만큼에서 +1해서 저장
        //프로젝트 조회한 값과 입력한 값 저장을 하기 위한 SRC 객체 생
        VcSrcFile save = vcSrcFileRepository.save(VcSrcFile.create(vc, rowOrder,
                vcSrcRequest.getName(),
                vcSrcRequest.getFileUrl(),
                vcSrcRequest.getLength(),
                vcSrcRequest.getSize(),
                vcSrcRequest.getExtension()));// SRC 객체 저장
        log.info("[vcService] srcSave save 확인 : {} ", save);

        return VcUrlResponse.of(save.getSrcSeq(), save.getFileUrl());
    }

    @Override
    @Transactional
    public List<VcUrlResponse> srcSave(@Valid @NotNull List<VcSrcRequest> vcSrcRequests, Long proSeq) {
        //vcSrcRequests를 srcSave로 저장시키고 리턴
        return vcSrcRequests.stream()
                .map(vcSrcRequest -> srcSave(vcSrcRequest, proSeq))
                .collect(Collectors.toList());
    }

    /**
     * VC Trg 파일 저장
     * @param vcAudioRequest
     * @return VcUrlResponse
     */
    @Override
    public VcUrlResponse trgSave(@Valid @NotNull VcAudioRequest vcAudioRequest) {
        //프로젝트 조회, 객체 생성후 저장
        Vc vc = projectFind(vcAudioRequest.getSeq());
        log.info("[vcService] trgSave vc 확인 : {} ", vc);

        //프로젝트 조회한 값과 입력한 값을 저장하기 위한 TRG 객체 생성
        VcTrgFile save = vcTrgFileRepository.save(VcTrgFile.create(vc,
                vcAudioRequest.getName(),
                vcAudioRequest.getFileUrl(),
                vcAudioRequest.getLength(),
                vcAudioRequest.getSize(),
                vcAudioRequest.getExtension()));// TRG 객체 저장
        log.info("[vcService] trgSave save 확인 : {} ", save);
        return VcUrlResponse.of(save.getTrgSeq(), save.getFileUrl());
    }

    /**
     * Vc Result 파일 저장 (vc 생성 파일)
     * @param vcAudioRequest
     * @return VcUrlResponse
     */
    @Override
    @Transactional
    public VcUrlResponse resultSave(@Valid @NotNull VcAudioRequest vcAudioRequest) {
        //SRCFile 조회
        VcSrcFile srcFile = vcSrcFileFind(vcAudioRequest.getSeq());
        log.info("[vcService] resultSave srcFile 확인 : {} ", srcFile);

        //프로젝트 조회한 값과 SRC 조회한 값, 입력한 값을 저장하기 위한 ResultFile 객체 생성
        VcResultFile save = vcResultFileRepository.save(VcResultFile.create(srcFile,
                vcAudioRequest.getName(),
                vcAudioRequest.getFileUrl(),
                vcAudioRequest.getLength(),
                vcAudioRequest.getSize(),
                vcAudioRequest.getExtension()));// result 객체 저장
        log.info("[vcService] resultSave save 확인 : {} ", save);
        return VcUrlResponse.of(save.getResSeq(), save.getFileUrl());
    }

    @Transactional
    public List<VcUrlResponse> resultSave(@Valid @NotNull List<VcAudioRequest> vcAudioRequests) {
        //vcAudioRequests를 위에 resultSave를 적용
        return vcAudioRequests.stream()
                .map(this::resultSave)
                .collect(Collectors.toList());
    }

    /**
     * Text 저장 기능
     * @param vcTextRequest
     * @return VcTextResponse
     */
    @Override
    @Transactional
    public VcTextResponse textSave(@Valid @NotNull VcTextRequest vcTextRequest) {
        //SRC 조회
        VcSrcFile srcFile = vcSrcFileFind(vcTextRequest.getSeq());
        log.info("[vcService] textSave srcFile 확인 : {} ", srcFile);

        //SRC 조회한 값과 프로젝트 조회한 값, 입력 값 저장하기 위한 TextFile 객체 생성
        VcText save = vcTextRepository.save(VcText.create(srcFile, vcTextRequest.getText(),
                String.valueOf(vcTextRequest.getText().length())));//Text 객체 저장
        log.info("[vcService] textSave save 확인 : {} ", save);
        return VcTextResponse.of(save.getVtSeq(), save.getComment());
    }

    /**
     * 리스트로 텍스트 저장
     * @param vcTextRequests
     * @return
     */
    @Override
    @Transactional
    public List<VcTextResponse> textSave(@Valid @NotNull List<VcTextRequest> vcTextRequests) {
        //vcTextRequest를 가지고 위에 textSave에 적용
        return vcTextRequests.stream()
                .map(this::textSave)
                .collect(Collectors.toList());
    }


    /**
     * 프로젝트 VC 탭 조회 기능
     * @param projectSeq
     * @return List<VcResponse>
     */
    @Override
    @Transactional
    public List<VcResponse> getVcResponse(@Valid @NotNull Long projectSeq) {
        // 프로젝트 seq 조회한 값
        List<VcSrcFile> vcSrcFileList = vcSrcFileRepository.findByVcProjectSeq(projectSeq);

        // 데이터가 많을수 있으므로 병렬 처리로 변경
        return vcSrcFileList.parallelStream()
                .sorted(Comparator.comparing(VcSrcFile::getRowOrder)) //
                .map(vcSrcFile -> {
                    // src 요청 값 입력
                    VcSrcsRequest srcAudio = VcSrcsRequest.builder()
                            .seq(vcSrcFile.getSrcSeq())
                            .rowOrder(vcSrcFile.getRowOrder())
                            .name(vcSrcFile.getFileName())
                            .fileUrl(vcSrcFile.getFileUrl())
                            .build();

                    // SRC 로 제일 최근에 저장한 Result 조회, 값이 없을 경우 null 처리
                    VcResultFile vcResultFile =
                            vcResultFileRepository.findFirstBySrcSeq_SrcSeqOrderBySrcSeqDesc(vcSrcFile.getSrcSeq());
                    VcResultsRequest resultAudio = Optional.ofNullable(vcResultFile)
                            .map(file -> VcResultsRequest.builder()
                                    .seq(file.getResSeq())
                                    .name(file.getFileName())
                                    .fileUrl(file.getFileUrl())
                                    .build())
                            .orElse(null);

                    // 제일 최근에 저장한 텍스트 조회, 값이 없을 경우 null 처리
                    VcText vcText =
                            vcTextRepository.findFirstBySrcSeq_SrcSeqOrderBySrcSeqDesc(vcSrcFile.getSrcSeq());
                    VcTextRequest text = Optional.ofNullable(vcText)
                            .map(t -> VcTextRequest.of(t.getVtSeq(), t.getComment()))
                            .orElse(null);

                    // VcResponse 객체 생성 후 반환
                    return new VcResponse(vcSrcFile.getActivate(), srcAudio, resultAudio, text);
                })
                .collect(Collectors.toList());
    }

    /**
     * VC SRC 파일 조회 기능
     * @param seq
     * @return VcUrlResponse
     */

    @Override
    public VcUrlResponse getSrcFile(@Valid @NotNull Long seq) {
        //SRC seq 로 SRC 값 조회
        VcSrcFile srcFile = vcSrcFileFind(seq);
        log.info("[vcService] getSrcFile srcFile 확인 : {} ", srcFile);
        //S3 SRC URL 값 출력
        return VcUrlResponse.of(srcFile.getSrcSeq(), srcFile.getFileUrl());
    }

    /**
     * VC Result 파일 조회 기능
     * @param seq
     * @return VcUrlResponse
     */
    @Override
    public VcUrlResponse getResultFile(@Valid @NotNull Long seq) {
        //TRG seq 로 TRG 값 조회
        VcResultFile resultFile = vcResultFind(seq);
        log.info("[vcService] getResultFile resultFile 확인 : {} ", resultFile);
        //S3 TRG URL 값 출력
        return VcUrlResponse.of(resultFile.getResSeq(), resultFile.getFileUrl());
    }

    /**
     * 텍스트 수정 기능
     * @param seq
     * @param text
     */
    @Override
    public VcTextResponse updateText(@Valid @NotNull Long seq, @Valid @NotNull String text) {
        //Text seq 로 Text 값 조회 검증
        VcText vcText = vcTextFind(seq);
        log.info("[vcService] updateText vcText 확인 : {} ", vcText);

        //변경할 값과 seq 값 변경 객체 생성
        VcText save = vcTextRepository.save(vcText.toBuilder()
                .vtSeq(vcText.getVtSeq())
                .comment(text)
                .build());//텍스트 값 변경
        log.info("[vcService] updateText save 확인 : {} ", save);
        return VcTextResponse.of(save.getVtSeq(), save.getComment());
    }

    /**
     * 행 수정 기능
     * @param seq
     * @param rowOrder
     */
    @Override
    @Transactional
    public VcRowResponse updateRowOrder(@Valid @NotNull Long seq, @Valid @NotNull int rowOrder) {
        //변경할 행순서 값과 SRC seq 값 변경 객체 생성
        VcSrcFile updateSrcFile = vcSrcFileFind(seq).toBuilder()
                .rowOrder(rowOrder)
                .build();
        log.info("[vcService] updateRowOrder updateSrcFile 확인 : {} ", updateSrcFile);

        VcSrcFile save = vcSrcFileRepository.save(updateSrcFile);//행순서 변경
        log.info("[vcService] updateRowOrder save 확인 : {} ", save);
        return VcRowResponse.builder()
                .seq(save.getSrcSeq())
                .rowOrder(save.getRowOrder())
                .build();
    }

    /**
     * 행 순서 변경 리스트로
     * @param row
     * @return
     */
    @Override
    public List<VcRowResponse> updateRowOrder(List<VcRowRequest> row) {
        //row를 가지고 위에 updateRowOrder를 적용
        return row.stream()
                .map(vcRowRequest -> updateRowOrder(vcRowRequest.getSeq(), vcRowRequest.getRowOrder()))
                .collect(Collectors.toList());
    }

    /**
     * SRC 행 삭제 하는 기능(수정)
     * @param seq
     */
    @Override
    @Transactional
    public VcActivateResponse deleteSrcFile(@Valid @NotNull Long seq) {
        //활성화 상태 N로 변경
        VcSrcFile deleteSrcFile = vcSrcFileFind(seq).toBuilder()
                .activate('N')
                .build();
        log.info("[vcService] deleteSrcFile deleteSrcFile 확인 : {} ", deleteSrcFile);
        //활성화상태 변경
        VcSrcFile save = vcSrcFileRepository.save(deleteSrcFile);
        log.info("[vcService] deleteSrcFile save 확인 : {} ", save);
        return VcActivateResponse.builder()
                .seq(save.getSrcSeq())
                .activate(save.getActivate())
                .build();
    }

    /**
     * 삭제 리스트로 변경
     * @param seqs
     * @return
     */
    @Override
    @Transactional
    public List<VcActivateResponse> deleteSrcFile(@Valid @NotNull List<Long> seqs) {
        //seqs를 가지고 위에 deleteSrcFile를 적용
        return seqs.stream()
                .map(this::deleteSrcFile)
                .collect(Collectors.toList());
    }


    /**
     * VCSrcRequest 객체 생성
     * @param audioFileInfos
     * @param upload
     * @param proSeq
     * @return
     */
    @Override
    public List<VcSrcRequest> vcSrcRequestBuilder(List<AudioFileInfo> audioFileInfos,
                                                          List<String> upload,
                                                          Long proSeq){
        //audioFileInfos 개수 만큼 VcSrcRequest audioinfo, url, proSeq 적용해서 builder 패턴으로 리턴
        return IntStream.range(0, audioFileInfos.size())
                .mapToObj(i -> VcSrcRequest.builder()
                        .seq(proSeq)
                        .rowOrder(1)
                        .name(audioFileInfos.get(i).getName())
                        .fileUrl(upload.get(i))
                        .length(audioFileInfos.get(i).getLength())
                        .size(audioFileInfos.get(i).getSize())
                        .extension(audioFileInfos.get(i).getExtension())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * VcAudioRequest 객체 생성
     * @param proSeq
     * @param info
     * @param url
     * @return
     */
    @Override
    public VcAudioRequest audioRequestBuilder(Long proSeq, AudioFileInfo info, String url) {
        //static 정적 메서드로 리턴
        return VcAudioRequest.of(proSeq,
                info.getName(),
                url,
                info.getLength(),
                info.getSize(),
                info.getExtension());
    }

    /**
     * VCAudioRequest 객체 생성
     * @param vcSrcUrlRequest
     * @param info
     * @param url
     * @return
     */
    @Override
    public List<VcAudioRequest> audioRequestBuilder(List<VcSrcUrlRequest> vcSrcUrlRequest,
                                                    List<AudioFileInfo> info,
                                                    List<String> url) {
        //vcSrcUrlRequest 사이즈 만큼 반복 하여 생성자 리턴
        return IntStream.range(0, vcSrcUrlRequest.size())
                .mapToObj(i -> VcAudioRequest.of(vcSrcUrlRequest.get(i).getSeq(),
                        info.get(i).getName(),
                        url.get(i),
                        info.get(i).getLength(),
                        info.get(i).getSize(),
                        info.get(i).getExtension()))
                .collect(Collectors.toList());
    }

    /**
     * VcTextRequest 객체 생성
     * @param srcSeq
     * @param text
     * @return
     */
    @Override
    public List<VcTextRequest> vcTextResponses(List<Long> srcSeq, List<String> text) {
        //srcSeq 개수 가지고 생성자 리턴
        return IntStream.range(0, srcSeq.size())
                .mapToObj(i -> VcTextRequest.of(srcSeq.get(i), text.get(i)))
                .collect(Collectors.toList());
    }

    /**
     * Srcseq로 url을 찾는 메서드
     * @param srcSeq
     * @return
     */
    @Override
    public List<VcSrcUrlRequest> vcSrcUrlRequests(List<Long> srcSeq){
        //srcSeq를 가지고 vcSrcFile 찾고 없으면 예외던지고 있다면 생성자로 리턴
        return srcSeq.stream()
                .map(seq -> {
                    VcSrcFile vcSrcFile = vcSrcFileRepository.findById(seq)
                            .orElseThrow(() -> new IllegalArgumentException("srcSeq not found"));
                    return VcSrcUrlRequest.builder()
                            .url(vcSrcFile.getFileUrl())
                            .seq(vcSrcFile.getSrcSeq())
                            .build();
                })
                .collect(Collectors.toList());
    }



    //VcSrcFile 찾는 메서드
    private VcSrcFile vcSrcFileFind(Long seq){
        return vcSrcFileRepository.findById(seq)
                .orElseThrow(() -> new IllegalArgumentException("Src file not found"));
    }
    //Project 찾고 vc생성 저장하는 메서드
    private Vc projectFind(Long seq){
        return projectRepository.findById(seq)
                .map(project -> vcRepository.findById(project.getProSeq()) //VC를 찾고 존재하지 않으면 vcSave 실행
                        .orElseGet(() -> vcRepository.save(Vc.builder()
                                .proSeq(project)
                                .build())))
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
    }
    //VcResultFile 찾는 메서드
    private VcResultFile vcResultFind(Long seq){
        return vcResultFileRepository.findById(seq)
                .orElseThrow(() -> new IllegalArgumentException("ResultFile not found"));
    }
    //VcText 찾는 메서드
    private VcText vcTextFind(Long seq){
        return vcTextRepository.findById(seq)
                .orElseThrow(() -> new IllegalArgumentException("Text not found"));
    }
}