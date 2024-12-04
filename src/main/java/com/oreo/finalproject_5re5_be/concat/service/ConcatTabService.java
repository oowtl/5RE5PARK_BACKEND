package com.oreo.finalproject_5re5_be.concat.service;

import com.oreo.finalproject_5re5_be.concat.dto.request.ConcatCreateRequestDto;
import com.oreo.finalproject_5re5_be.concat.dto.request.ConcatUpdateRequestDto;
import com.oreo.finalproject_5re5_be.concat.dto.response.ConcatTabResponseDto;
import com.oreo.finalproject_5re5_be.concat.entity.ConcatTab;
import com.oreo.finalproject_5re5_be.concat.repository.ConcatTabRepository;
import com.oreo.finalproject_5re5_be.concat.service.helper.ConcatTabHelper;
import com.oreo.finalproject_5re5_be.member.dto.response.MemberReadResponse;
import com.oreo.finalproject_5re5_be.member.service.MemberServiceImpl;
import com.oreo.finalproject_5re5_be.project.entity.Project;
import com.oreo.finalproject_5re5_be.project.repository.ProjectRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConcatTabService {
    private ConcatTabRepository concatTabRepository;
    private ProjectRepository projectRepository;
    private MemberServiceImpl memberService;
    private ConcatTabHelper concatTabHelper;

    /**
     * @param concatCreateRequestDto
     * @return boolean
     */
    @Transactional
    public boolean createConcatTab(ConcatCreateRequestDto concatCreateRequestDto) {
        Optional<Project> projectOpt = projectRepository.findById(concatCreateRequestDto.getProjectSequence());
        if (concatTabRepository.existsById(concatCreateRequestDto.getProjectSequence()) || projectOpt.isEmpty()) {
            return false;
        }
        Project project = projectOpt.get();
        System.out.println("project.getProSeq() = " + project.getProSeq());
        ConcatTab concatTab = ConcatTab.builder()
                .project(project) // Hibernate가 projectId를 자동으로 동기화
                .option(null)
                .status('Y')
                .frontSilence(0.0f)
                .build();

        concatTabRepository.save(concatTab);
        return true;
    }

    //중복 조회를 방지하기 위한 오버로드
    @Transactional
    public boolean createConcatTab(Project project) {
        if (concatTabRepository.existsById(project.getProSeq())) {
            return false;
        }
        ConcatTab concatTab = new ConcatTab(project.getProSeq(), project, null, 'Y', 0.0f);
        concatTabRepository.save(concatTab);
        return true;
    }

    //projectSeq가 사용자가 소유한 프로젝트의 id가 맞는지 확인 해야함
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


    @Transactional
    public boolean updateConcatTab(ConcatUpdateRequestDto concatUpdateRequestDto, Long memberSeq) {
        MemberReadResponse member = memberService.read(memberSeq);

        if (member == null) {
            throw new NoSuchElementException("회원 정보를 찾을 수 없습니다.");
        }

        // ConcatTab 및 Project 조회
        ConcatTab concatTab = concatTabRepository.findById(concatUpdateRequestDto.getTabId())
                .orElse(null);
        Project project = projectRepository.findById(concatUpdateRequestDto.getTabId())
                .orElse(null);

        // 조회 실패 처리
        if (concatTab == null || project == null) {
            throw new NoSuchElementException("수정할 프로젝트가 없습니다.");
        }

        // 권한 확인
        if (project.getMember().getId().equals(member.getId())) {
            // 저장
            ConcatTab updatedTab = new ConcatTab(
                    concatUpdateRequestDto.getTabId(),
                    project,
                    //concatUpdateRequestDto.getConcatOption(),
                    null,
                    concatUpdateRequestDto.getStatus(),
                    concatUpdateRequestDto.getFrontSilence()
            );

            concatTabRepository.save(updatedTab);
            return true;
        }
        throw new IllegalArgumentException("알 수 없는 오류가 발생했습니다.");
    }
}
