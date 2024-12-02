package com.oreo.finalproject_5re5_be.project.service;

import com.oreo.finalproject_5re5_be.concat.repository.ConcatTabRepository;
import com.oreo.finalproject_5re5_be.global.exception.DataNotFoundException;
import com.oreo.finalproject_5re5_be.member.entity.Member;
import com.oreo.finalproject_5re5_be.member.repository.MemberRepository;
import com.oreo.finalproject_5re5_be.project.dto.response.ProjectResponse;
import com.oreo.finalproject_5re5_be.project.entity.Project;
import com.oreo.finalproject_5re5_be.project.exception.InvalidProjectNameException;
import com.oreo.finalproject_5re5_be.project.exception.ProjectNotMemberException;
import com.oreo.finalproject_5re5_be.project.repository.ProjectRepository;
import com.oreo.finalproject_5re5_be.tts.repository.TtsSentenceRepository;
import com.oreo.finalproject_5re5_be.vc.repository.VcRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Validated
@Transactional
public class ProjectServiceImpl implements ProjectService {
    private ProjectRepository projectRepository;
    private MemberRepository memberRepository;
    private TtsSentenceRepository ttsSentenceRepository;
    private VcRepository vcRepository;
    private ConcatTabRepository concatTabRepository;
    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository,
                              MemberRepository memberRepository,
                              TtsSentenceRepository ttsSentenceRepository,
                              VcRepository vcRepository,
                              ConcatTabRepository concatTabRepository) {
        this.projectRepository = projectRepository;
        this.memberRepository = memberRepository;
        this.ttsSentenceRepository = ttsSentenceRepository;
        this.vcRepository = vcRepository;
        this.concatTabRepository = concatTabRepository;
    }


    /**
     * 프로젝트 회원 조회
     * @return List<ProjectResponse>
     */
    @Override
    @Transactional
    public List<ProjectResponse> projectFindAll(Long memberSeq) {
        try {
            Member member = memberFind(memberSeq);
            //회원 정보로 전체 조회
            List<Project> project = projectRepository
                    .findByMemberSeq(member.getSeq());
            //정보를 저장할 리스트 생성
            List<ProjectResponse> projectResponses = new ArrayList<>();
            //project 정보를 모두 넣고
            for (Project p : project) {
                ProjectResponse projectResponse = ProjectResponse.builder()
                        .projectSeq(p.getProSeq())
                        .projectName(p.getProName())
                        .projectContent(p.getProName())
                        .projectDate(p.getProDate())
                        .projectUpdateDate(p.getProUpDate())
                        .tts(ttsSentenceRepository.existsByProject_ProSeq(p.getProSeq()))
                        .vc(vcRepository.existsById(p.getProSeq()))
                        .concat(concatTabRepository.existsById(p.getProSeq()))
                        .projectActivate(p.getProActivate())
                        .build();
                projectResponses.add(projectResponse);
            }
            //Response 로 추출
            return projectResponses;
        } catch (DataNotFoundException e) {
            log.error("Error in projectFindAll: memberSeq={}", memberSeq, e);
            throw e;
        } catch (Exception e){
            log.error("memberSeq: {}에 대해 프로젝트를 저장하는 동안 예상치 못한 오류가 발생했습니다.", memberSeq, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 프로젝트 생성
     * @return Long
     */
    @Override
    public Long projectSave(Long memberSeq) {
        try {
            //회원정보 추출
            Member member = memberFind(memberSeq);
            //회원정보로 프로젝트 객체 생성
            Project project = Project.builder()
                    .member(member)
                    .build();
            //저장
            Project save = projectRepository.save(project);
            log.info("프로젝트 저장 완료 : {}", save);
            //생성된 프로젝트 ID 정보 추출
            return save.getProSeq();
        } catch (DataNotFoundException e) {
            log.error("프로젝트의 회원이 아닙니다. : {}", memberSeq, e);
            throw e;
        } catch (Exception e){
            log.error("memberSeq: {}에 대해 프로젝트를 저장하는 동안 예상치 못한 오류가 발생했습니다.", memberSeq, e);
            throw new RuntimeException("프로젝트를 저장하는 동안 예상치 못한 오류가 발생했습니다.");
        }
    }

    /**
     * 프로젝트 업데이트
     * @param projectSeq
     * @param projectName
     */
    @Override
    public void projectUpdate(@Valid @NotNull Long projectSeq, @Valid @NotNull String projectName) {
        try {
            // 프로젝트 이름 유효성 검사
            validateProjectName(projectName);
            // 프로젝트 번호로 프로젝트 찾기
            Project projectFind = projectFind(projectSeq);
            // 프로젝트 정보 업데이트
            Project project = projectFind.toBuilder()
                    .proSeq(projectSeq)
                    .proName(projectName)
                    .build();
            // 저장
            projectRepository.save(project);
            log.info("프로젝트 업데이트 완료: projectSeq={}, projectName={}", projectSeq, projectName);
        } catch (InvalidProjectNameException e) {
            log.error("유효하지 않은 프로젝트 이름으로 업데이트 시도: projectName={}", projectName, e);
            throw e;
        } catch (DataNotFoundException e) {
            log.error("업데이트할 프로젝트를 찾을 수 없습니다: projectSeq={}", projectSeq, e);
            throw e;
        } catch (Exception e) {
            log.error("프로젝트 업데이트 중 예기치 않은 오류 발생: projectSeq={}", projectSeq, e);
            throw new RuntimeException("프로젝트 업데이트 중 오류가 발생했습니다.");
        }
    }

    /**
     * 프로젝트 삭제(수정)
     * @param projectSeq
     */
    @Override
    @Transactional
    public void projectDelete(@Valid @NotNull List<Long> projectSeq) {
        try {
            for (Long seq : projectSeq) {
                // 프로젝트 번호로 프로젝트 찾기
                Project projectFind = projectFind(seq);
                // 프로젝트 상태를 비활성화로 변경
                Project project = projectFind.toBuilder()
                        .proSeq(seq)
                        .proActivate('N') // N = 비활성화
                        .build();
                // 저장
                projectRepository.save(project);
                log.info("프로젝트 삭제 완료 (상태 변경): projectSeq={}", seq);
            }
        } catch (DataNotFoundException e) {
            log.error("삭제할 프로젝트를 찾을 수 없습니다: projectSeq={}", projectSeq, e);
            throw e;
        } catch (Exception e) {
            log.error("프로젝트 삭제 중 예기치 않은 오류 발생: projectSeq={}", projectSeq, e);
            throw new RuntimeException("프로젝트 삭제 중 오류가 발생했습니다.");
        }
    }

    /**
     * 회원의 프로젝트인지 확인 단일
     * @param memberSeq
     * @param projectSeq
     * @return
     */
    @Override
    public boolean projectCheck(Long memberSeq, @Valid @NotNull Long projectSeq) {
        try {
            Project project = projectFind(projectSeq);
            Long seq = project.getMember().getSeq();
            if(seq.equals(memberSeq)){
                return true;
            }
            log.warn("회원의 프로젝트가 아닙니다: memberSeq={}, projectSeq={}", memberSeq, projectSeq);
            throw new ProjectNotMemberException("해당 프로젝트는 이 회원의 프로젝트가 아닙니다.");
        } catch (DataNotFoundException | ProjectNotMemberException e) {
            log.error("프로젝트 확인 중 오류 발생: memberSeq={}, projectSeq={}", memberSeq, projectSeq, e);
            throw e;
        }
    }

    /**
     * 회원의 프로젝트인지 확인 여러개
     * @param memberSeq
     * @param projectSeq
     * @return
     */
    @Override
    public boolean projectCheck(Long memberSeq, @Valid @NotNull List<Long> projectSeq){
        for (Long pro : projectSeq) {
            projectCheck(memberSeq, pro);
        }
        return true;
    }

    // 길이 제한 메서드
    private void validateProjectName(String projectName) {
        if (projectName == null || projectName.length() < 3 || projectName.length() > 50) {
            throw new InvalidProjectNameException("프로젝트 이름은 3자 이상, 50자 이하여야 합니다.");
        }
    }

    private Project projectFind(Long seq){
        return projectRepository.findById(seq)
                .orElseThrow(() -> new DataNotFoundException("project not found"));
    }
    private Member memberFind(Long seq){
        return memberRepository.findById(seq)
                .orElseThrow(() -> new DataNotFoundException("Member not found"));
    }
}
