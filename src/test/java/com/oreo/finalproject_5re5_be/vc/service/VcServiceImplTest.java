package com.oreo.finalproject_5re5_be.vc.service;

import com.oreo.finalproject_5re5_be.project.entity.Project;
import com.oreo.finalproject_5re5_be.project.repository.ProjectRepository;
import com.oreo.finalproject_5re5_be.vc.dto.request.VcAudioRequest;
import com.oreo.finalproject_5re5_be.vc.dto.request.VcSrcRequest;
import com.oreo.finalproject_5re5_be.vc.dto.request.VcTextRequest;
import com.oreo.finalproject_5re5_be.vc.dto.response.VcResponse;
import com.oreo.finalproject_5re5_be.vc.entity.VcResultFile;
import com.oreo.finalproject_5re5_be.vc.entity.VcSrcFile;
import com.oreo.finalproject_5re5_be.vc.entity.VcText;
import com.oreo.finalproject_5re5_be.vc.entity.VcTrgFile;
import com.oreo.finalproject_5re5_be.vc.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@Slf4j
@SpringBootTest
class VcServiceImplTest {
    @Autowired
    private VcServiceImpl vcServiceImpl;

    @MockBean
    private VcRepository vcRepository;

    @MockBean
    private VcSrcFileRepository vcSrcFileRepository;

    @MockBean
    private VcTargetFileRepository vcTargetFileRepository;

    @MockBean
    private VcResultFileRepository vcResultFileRepository;

    @MockBean
    private VcTextRepository vcTextRepository;

    @MockBean
    private ProjectRepository projectRepository;

    @Test
    @DisplayName("[VcServiceTest] SRC 저장 테스트 - 성공")
    void srcSave() {
        // 주어진 VcSrcRequest를 사용하여 SrcSave 메서드를 테스트합니다.
        // 프로젝트를 찾아서 src 객체를 생성하고, vcSrcFileRepository에 저장하는지 검증합니다.
        Project project = createProjectBuild();

        VcSrcRequest request = createSrcFileBuildInProject(project);
        log.info("[VcServiceTest] srcSave request: {}", request);

        when(projectRepository.findById(request.getSeq())).thenReturn(Optional.of(project));//프로젝트 조회 값 설정

        vcServiceImpl.srcSave(request);//SRC 저장

        verify(vcSrcFileRepository, times(1)).save(any(VcSrcFile.class));//저장 확인
    }

    @Test
    @DisplayName("[VcServiceTest] TRG 저장 테스트 - 성공")
    void trgSave() {
        // 주어진 VcAudioRequest를 사용하여 TrgSave 메서드를 테스트합니다.
        // 프로젝트를 조회하여 trg 객체를 생성하고, vcTargetFileRepository에 저장하는지 확인합니다.
        Project project = createProjectBuild();//project 생성
        VcAudioRequest request = createAudio(project);

        log.info("[VcServiceTest] trgSave request: {}", request);
        when(projectRepository.findById(request.getSeq1())).thenReturn(Optional.of(project));//프로젝트 조회 값 설정

        vcServiceImpl.trgSave(request);//Trg 저장

        verify(vcTargetFileRepository, times(1)).save(any(VcTrgFile.class)); //저장 확인
    }

    @Test
    @DisplayName("[VcServiceTest] Result 저장 테스트 - 성공")
    void resultSave() {
        // 주어진 VcAudioRequest를 사용하여 ResultSave 메서드를 테스트합니다.
        // src 파일과 프로젝트를 조회하여 result 객체를 생성하고 vcResultFileRepository에 저장하는지 확인합니다.
        Project project = createProjectBuild();// project 객체 생성
        VcSrcFile srcFile = VcSrcFile.builder().proSeq(project).build();//src 객체 프로젝트를 담아서 생성
        VcAudioRequest request = createAudio(project);

        log.info("[VcServiceTest] resultSave request: {}", request);
        when(vcSrcFileRepository.findById(request.getSeq1())).thenReturn(Optional.of(srcFile));//SRC 조회 값 설정
        when(projectRepository.findById(srcFile.getSrcSeq())).thenReturn(Optional.of(project));//Project 조회값 설정

        vcServiceImpl.resultSave(request);//result 저장

        verify(vcResultFileRepository, times(1)).save(any(VcResultFile.class));//저장 확인
    }

    @Test
    @DisplayName("[VcServiceTest] Text 저장 테스트 - 성공")
    void textSave() {
        // 주어진 VcTextRequest를 사용하여 TextSave 메서드를 테스트합니다.
        // src 파일과 프로젝트를 조회하고 text 객체를 생성하여 vcTextRepository에 저장하는지 확인합니다.
        Project project = createProjectBuild();//project 객체 생성
        VcSrcFile srcFile = VcSrcFile.builder().proSeq(project).srcSeq(1L).build();//project를 가진 src 객체 생성
        VcTextRequest request = createTextBuildInSrc(srcFile);

        log.info("[VcServiceTest] textSave request: {}", request);
        when(vcSrcFileRepository.findById(request.getSeq())).thenReturn(Optional.of(srcFile));//src 조회 값 설정
        when(projectRepository.findById(srcFile.getSrcSeq())).thenReturn(Optional.of(project));//project 조회 값 설정

        vcServiceImpl.textSave(request);//text 저장

        verify(vcTextRepository, times(1)).save(any(VcText.class));//저장 확인
    }

    @Test
    @DisplayName("[VcServiceTest] VC 탭 조회 테스트 - 모두 조회 성공")
    void getVcResultAndTextResponse() {
        Project project = createProjectBuild();//project 객체 생성
        VcSrcFile srcFile = VcSrcFile.builder().proSeq(project).build();//src 객체 생성
        VcResultFile vcResultFile = VcResultFile.builder().srcSeq(srcFile).build();// result 객체 생성
        VcText vcText = VcText.builder().srcSeq(srcFile).build();//text 객체 생성

        when(projectRepository.findById(srcFile.getSrcSeq())).thenReturn(Optional.of(project));
        when(vcSrcFileRepository.findByProjectId(project.getProSeq())).thenReturn(List.of(srcFile));//SrcFile 조회값 설정
        when(vcResultFileRepository.findBySrcSeq_SrcSeq(srcFile.getSrcSeq())).thenReturn(vcResultFile);//result 조회값 설정
        when(vcTextRepository.findBySrcSeq_SrcSeq(srcFile.getSrcSeq())).thenReturn(vcText);// text 조회값 설정

        List<VcResponse> vcResponse = vcServiceImpl.getVcResponse(project.getProSeq());//프로젝트로 조회 호출
        log.info("[VcServiceTest] getVcResultAndTextResponse request: {}", vcResponse);

        assertEquals(1, vcResponse.size());//값 확인
    }

    @Test
    @DisplayName("[VcServiceTest] SRC 오디오 조회(다운로드, 재생) 테스트 - 성공")
    void getSrcFile() {
        // 주어진 seq를 사용하여 getSrcFile 메서드를 테스트합니다.
        // src 파일을 조회하고 해당 URL을 반환하는지 확인합니다.
        VcSrcFile srcFile = VcSrcFile.builder() // src 객체 생성
                .srcSeq(1L)
                .fileUrl("file_url")
                .build();
        //src 파일 조회시 나오는 값 설정
        when(vcSrcFileRepository.findById(srcFile.getSrcSeq())).thenReturn(Optional.of(srcFile));

        String result = vcServiceImpl.getSrcFile(srcFile.getSrcSeq());//조회

        assertEquals(srcFile.getFileUrl(), result);//값 확인
    }

    @Test
    @DisplayName("[VcServiceTest] Result 오디오 조회(다운로드, 재생) 테스트 - 성공")
    void getResultFile() {
        // 주어진 seq를 사용하여 getTrgFile 메서드를 테스트합니다.
        // trg 파일을 조회하고 해당 URL을 반환하는지 확인합니다.
        VcResultFile resultFile = VcResultFile.builder()//trg 객체 생성
                .resSeq(1L)
                .fileUrl("file_url")
                .build();

        when(vcResultFileRepository.findById(resultFile.getResSeq())).thenReturn(Optional.of(resultFile));//trg조회 값 설정

        String result = vcServiceImpl.getResultFile(resultFile.getResSeq());//조회

        assertEquals(resultFile.getFileUrl(), result);//값 확인
    }

    @Test
    @DisplayName("[VcServiceTest] Text 수정 테스트 - 성공")
    void updateText() {
        // 주어진 seq와 텍스트를 사용하여 updateText 메서드를 테스트합니다.
        // 텍스트를 조회하고 업데이트된 텍스트로 저장하는지 확인합니다.

        String newText = "updated_text";//변경할 텍스트
        VcText vcText = VcText.builder()//text 객체 생성
                .vtSeq(1L)
                .comment("old_text")
                .build();

        when(vcTextRepository.findById(vcText.getVtSeq())).thenReturn(Optional.of(vcText));//text 조회 값 설정

        vcServiceImpl.updateText(vcText.getVtSeq(), newText);//수정

        verify(vcTextRepository, times(1)).save(any(VcText.class));//업데이트 확인
    }

    @Test
    @DisplayName("[VcServiceTest] 행순서 변경 테스트 - 성공")
    void updateRowOrder() {
        // 주어진 seq와 새로운 rowOrder를 사용하여 updateRowOrder 메서드를 테스트합니다.
        // src 파일을 조회하고 새로운 행 순서로 업데이트하여 저장하는지 확인합니다.

        int newRowOrder = 2;// 행 순서 변경 값 설정
        VcSrcFile srcFile = VcSrcFile.builder()
                .srcSeq(1L)
                .rowOrder(1)
                .build();// src 객체 생성

        when(vcSrcFileRepository.findById(srcFile.getSrcSeq())).thenReturn(Optional.of(srcFile));//src 조회 값 설정
        vcServiceImpl.updateRowOrder(srcFile.getSrcSeq(), newRowOrder);//수정
        verify(vcSrcFileRepository, times(1)).save(any(VcSrcFile.class));//수정확인
    }

    @Test
    @DisplayName("[VcServiceTest] SRC 행 삭제 (변경) 테스트 - 성공")
    void deleteSrcFile(){
        VcSrcFile srcFile = VcSrcFile.builder().srcSeq(1L).activate('N').build();

        when(vcSrcFileRepository.findById(srcFile.getSrcSeq())).thenReturn(Optional.of(srcFile));
        vcServiceImpl.deleteSrcFile(srcFile.getSrcSeq());
        verify(vcSrcFileRepository, times(1)).save(any(VcSrcFile.class));
    }

    private static Project createProjectBuild(){
        return Project.builder()
                .proSeq(1L)
                .proName("test project")
                .proCmt("project 설명")
                .proUpDate(LocalDateTime.now())
                .build();
    }
    private static VcSrcRequest createSrcFileBuildInProject(Project project){
        return VcSrcRequest.builder()//Src 입력
                .seq(project.getProSeq())
                .rowOrder(1)
                .name("src_file")
                .fileUrl("file_url")
                .length(100)
                .size("100MB")
                .extension("wav")
                .build();
    }
    private static VcAudioRequest createAudio(Project project){
        return VcAudioRequest.builder()//trg 입력
                .seq1(project.getProSeq()) //proSeq
                .name("VcAudioTest")
                .fileUrl("file_url")
                .length(200)
                .size("100KB")
                .extension("wav")
                .build();
    }
    private VcTextRequest createTextBuildInSrc(VcSrcFile srcFile){
        return VcTextRequest.builder()//text 객체 생성
                .seq(srcFile.getSrcSeq())
                .text("Sample text")
                .build();
    }

}