package com.oreo.finalproject_5re5_be.concat.service;

import com.oreo.finalproject_5re5_be.concat.dto.request.ConcatCreateRequestDto;
import com.oreo.finalproject_5re5_be.concat.dto.request.ConcatUpdateRequestDto;
import com.oreo.finalproject_5re5_be.concat.dto.response.ConcatTabResponseDto;
import com.oreo.finalproject_5re5_be.concat.entity.BgmFile;
import com.oreo.finalproject_5re5_be.concat.entity.ConcatTab;
import com.oreo.finalproject_5re5_be.concat.repository.BgmFileRepository;
import com.oreo.finalproject_5re5_be.concat.repository.ConcatTabRepository;
import com.oreo.finalproject_5re5_be.concat.service.helper.ConcatTabHelper;
import com.oreo.finalproject_5re5_be.member.dto.response.MemberReadResponse;
import com.oreo.finalproject_5re5_be.member.service.MemberServiceImpl;
import com.oreo.finalproject_5re5_be.project.entity.Project;
import com.oreo.finalproject_5re5_be.project.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ConcatTabService {
    private ConcatTabRepository concatTabRepository;
    private ProjectRepository projectRepository;
    private MemberServiceImpl memberService;
    private ConcatTabHelper concatTabHelper;
    private BgmFileRepository bgmFileRepository;

    /**
     * @param concatCreateRequestDto
     * @return boolean
     */
    @Transactional
    public boolean createConcatTab(ConcatCreateRequestDto concatCreateRequestDto) {
        Optional<Project> projectOpt =
                projectRepository.findById(concatCreateRequestDto.getProjectSequence());
        if (concatTabRepository.existsById(concatCreateRequestDto.getProjectSequence())
                || projectOpt.isEmpty()) {
            return false;
        }
        Project project = projectOpt.get();
        System.out.println("project.getProSeq() = " + project.getProSeq());
        ConcatTab concatTab =
                ConcatTab.builder()
                        .project(project) // Hibernate가 projectId를 자동으로 동기화
                        .status('Y')
                        .frontSilence(0.0f)
                        .bgmFiles(null) // create라서 처음엔 null
                        .build();

        concatTabRepository.save(concatTab);
        return true;
    }

    // 중복 조회를 방지하기 위한 오버로드
    @Transactional
    public boolean createConcatTab(Project project) {
        if (concatTabRepository.existsById(project.getProSeq())) {
            return false;
        }

        ConcatTab concatTab =
                ConcatTab.builder()
                        .project(project) // Hibernate가 projectId를 자동으로 동기화
                        .status('Y')
                        .frontSilence(0.0f)
                        .bgmFiles(null) // create라서 처음엔 null
                        .build();

        System.out.println("concatTab = " + concatTab);
        concatTabRepository.save(concatTab);
        return true;
    }

    // projectSeq가 사용자가 소유한 프로젝트의 id가 맞는지 확인 해야함
    @Transactional
    public ConcatTabResponseDto readConcatTab(long projectSeq, Long memberSeq) {
        // ConcatTab, Project 조회
        Optional<ConcatTab> concatOpt = concatTabRepository.findById(projectSeq);
        Optional<Project> projectOpt = projectRepository.findById(projectSeq);

        if (projectOpt.isEmpty()) {
            throw new NoSuchElementException("프로젝트를 찾을 수 없습니다.");
        }

        // 데이터가 존재할 경우 처리
        if (concatOpt.isPresent()) {
            return concatTabHelper.prepareConcatTab(concatOpt.get(), memberSeq);
        }

        // ConcatTab 생성 시도
        if (createConcatTab(projectOpt.get())) {
            // 생성 후 다시 조회
            Optional<ConcatTab> newConcatTab = concatTabRepository.findById(projectSeq);
            if (newConcatTab.isPresent()) {
                return concatTabHelper.prepareConcatTab(newConcatTab.get(), memberSeq);
            }
        }

        // 데이터가 없으면 예외 처리
        throw new NoSuchElementException("해당하는 구성요소를 찾을 수 없습니다.");
    }

    // concatTab에 대한 정보들 모두 수정
    @Transactional
    public boolean updateConcatTab(ConcatUpdateRequestDto concatUpdateRequestDto, Long memberSeq) {
        // 회원 조회
        MemberReadResponse member = memberService.read(memberSeq);

        if (member == null) {
            throw new NoSuchElementException("회원 정보를 찾을 수 없습니다.");
        }

        // ConcatTab 및 Project 조회
        ConcatTab existingTab =
                concatTabRepository
                        .findById(concatUpdateRequestDto.getTabId())
                        .orElseThrow(() -> new NoSuchElementException("수정할 프로젝트가 없습니다."));

        Project project =
                projectRepository
                        .findById(concatUpdateRequestDto.getTabId())
                        .orElseThrow(() -> new NoSuchElementException("수정할 프로젝트가 없습니다."));

        // Builder로 새로운 ConcatTab 생성
        ConcatTab updatedTab =
                ConcatTab.builder()
                        .projectId(existingTab.getProjectId()) // 기존 값 유지
                        .project(existingTab.getProject()) // 기존 값 유지
                        .frontSilence(concatUpdateRequestDto.getFrontSilence()) // 업데이트된 값
                        .status(concatUpdateRequestDto.getStatus()) // 업데이트된 값
                        .build();

        concatTabRepository.save(updatedTab);
        return true;
    }

    // bgmFile들만 수정
    @Transactional
    public boolean updateBgmAudioFiles(Long tabSeq, List<Long> bgmFileSeqs) {
        // ConcatTab 찾기
        ConcatTab concatTab =
                concatTabRepository
                        .findById(tabSeq)
                        .orElseThrow(
                                () -> new NoSuchElementException("ConcatTab not found with ID: " + tabSeq));

        if (bgmFileSeqs == null || bgmFileSeqs.isEmpty()) {
            // bgmFileSeqs가 비어있으면 기존 bgmFiles 제거
            concatTab.addBgmFile(new BgmFile());
        } else {
            // bgmFileSeqs를 통해 BgmFile 리스트 생성
            List<BgmFile> bgmFiles =
                    bgmFileSeqs.stream()
                            .map(
                                    seq ->
                                            bgmFileRepository
                                                    .findById(seq)
                                                    .orElseThrow(
                                                            () ->
                                                                    new NoSuchElementException("BgmFile not found with ID: " + seq)))
                            .toList();

            // ConcatTab에 새로운 BgmFile 리스트 설정
            concatTab.addBgmFile(bgmFiles);
        }

        // 업데이트된 ConcatTab 저장
        concatTabRepository.save(concatTab);
        return true;
    }

    @Transactional
    public ConcatTab getConcatTabBySeq(Long tabSeq) {
        return concatTabRepository
                .findById(tabSeq)
                .orElseThrow(() -> new IllegalArgumentException("Invalid ConcatTab ID: " + tabSeq));
    }
}
