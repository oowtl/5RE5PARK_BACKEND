package com.oreo.finalproject_5re5_be.project.service;

import com.oreo.finalproject_5re5_be.member.entity.Member;
import com.oreo.finalproject_5re5_be.member.repository.MemberRepository;
import com.oreo.finalproject_5re5_be.project.dto.response.ProjectResponse;
import com.oreo.finalproject_5re5_be.project.entity.Project;
import com.oreo.finalproject_5re5_be.project.exception.InvalidProjectNameException;
import com.oreo.finalproject_5re5_be.project.repository.ProjectRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository,
                              MemberRepository memberRepository) {
        this.projectRepository = projectRepository;
        this.memberRepository = memberRepository;
    }


    /**
     * 프로젝트 회원 조회
     * @return List<ProjectResponse>
     */
    @Override
    @Transactional
    public List<ProjectResponse> projectFindAll() {
        Member member = memberFind(1L);
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
                    .projectUpdateDate(p.getProUpDate())
                    .projectActivate(p.getProActivate())
                    .build();
            projectResponses.add(projectResponse);
        }
        //Response 로 추출
        return projectResponses;
    }

    /**
     * 프로젝트 생성
     * @return Long
     */
    @Override
    public Long projectSave() {
        //회원정보 추출
        Member member = memberFind(1L);
        //회원정보로 프로젝트 객체 생성
        Project project = Project.builder()
                .member(member)
                .build();
        //저장
        Project save = projectRepository.save(project);
        log.info("Save project : {}", save);
        //생성된 프로젝트 ID 정보 추출
        return save.getProSeq();
    }

    /**
     * 프로젝트 업데이트
     * @param projectSeq
     * @param projectName
     */
    @Override
    public void projectUpdate(@Valid @NotNull Long projectSeq,
                              @Valid @NotNull String projectName){
        //프로젝트 길이 제한
        validateProjectName(projectName);
        // 프로젝트 번호로 프로젝트 찾기
        Project projectFind = projectFind(projectSeq);
        //프로젝트 찾은 번호로 받은 프로젝트명으로 변경
        Project project = projectFind.toBuilder()
                .proSeq(projectSeq)
                .proName(projectName)
                .build();
        //수정
        projectRepository.save(project);
    }

    /**
     * 프로젝트 삭제(수정)
     * @param projectSeq
     */
    @Override
    @Transactional
    public void projectDelete(@Valid @NotNull List<Long> projectSeq) {
        //리스트로 받은 프로젝트 번호를 조회
        for (int i = 0; i < projectSeq.size(); i++) {
            Project projectFind = projectFind(projectSeq.get(i));
            //프로젝트들의 상태를 N으로 변경
            Project project = projectFind.toBuilder()
                    .proSeq(projectSeq.get(i))
                    .proActivate('N')
                    .build();
            //저장
            projectRepository.save(project);
        }
    }

    // 길이 제한 메서드
    private void validateProjectName(@Valid @NotNull String projectName) {
        if (projectName == null || projectName.length() < 3 || projectName.length() > 50) {
            throw new InvalidProjectNameException("프로젝트 이름은 3자 이상, 50자 이하여야 합니다.");
        }
    }

    //회원 정보 추출
    private Member getCurrentUser() {
        //시큐리티 context 에서 회원 정보 추출
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //아이디 추출
        String userId = authentication.getName();
        //아이디로 회원정보 조회
        Member member = memberRepository.findById(userId);
        if(member==null){
            throw new UsernameNotFoundException("Member not found");
        }
        //회원정보 리턴
        return member;
    }

    private Project projectFind(Long seq){
        return projectRepository.findById(seq)
                .orElseThrow(() -> new IllegalArgumentException("project not found"));
    }
    private Member memberFind(Long seq){
        return memberRepository.findById(seq)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
    }
}
