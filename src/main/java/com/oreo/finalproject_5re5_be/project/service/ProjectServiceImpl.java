package com.oreo.finalproject_5re5_be.project.service;

import com.oreo.finalproject_5re5_be.member.entity.Member;
import com.oreo.finalproject_5re5_be.member.repository.MemberRepository;
import com.oreo.finalproject_5re5_be.project.dto.response.ProjectResponse;
import com.oreo.finalproject_5re5_be.project.entity.Project;
import com.oreo.finalproject_5re5_be.project.exception.InvalidProjectNameException;
import com.oreo.finalproject_5re5_be.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Validated
public class ProjectServiceImpl implements ProjectService {
    private ProjectRepository projectRepository;
    private MemberRepository memberRepository;
    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository,
                              MemberRepository memberRepository) {
        this.projectRepository = projectRepository;
        this.memberRepository = memberRepository;
    }


    @Override
    public List<ProjectResponse> projectFindAll() {
        List<Project> project = projectRepository.findAll();
        List<ProjectResponse> projectResponses = new ArrayList<>();
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
        return projectResponses;
    }

    @Override
    public void projectSave(String projectName) {
        validateProjectName(projectName);
        Member member = getCurrentUser();
        Project project = Project.builder()
                .member(member)
                .build();
        if(projectName.length()>0){
            project = Project.builder()
                    .member(member)
                    .proName(projectName)
                    .build();
        }
        projectRepository.save(project);
    }

    @Override
    public void projectUpdate(Long projectSeq, String projectName) {
        validateProjectName(projectName);
        Project projectFind = projectRepository.findById(projectSeq)
                .orElseThrow(() -> new IllegalArgumentException("src file not found"));
        Project project = projectFind.toBuilder()
                .proSeq(projectSeq)
                .proName(projectName)
                .build();
        projectRepository.save(project);
    }


    @Override
    public void projectDelete(Long projectSeq) {
        Project projectFind = projectRepository.findById(projectSeq)
                .orElseThrow(() -> new IllegalArgumentException("src file not found"));
        Project project = projectFind.toBuilder()
                .proSeq(projectSeq)
                .proActivate('N')
                .build();
        projectRepository.save(project);
    }

    private void validateProjectName(String projectName) {
        if (projectName == null || projectName.length() < 3 || projectName.length() > 50) {
            throw new InvalidProjectNameException("프로젝트 이름은 3자 이상, 50자 이하여야 합니다.");
        }
    }
    private Member getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        Member member = memberRepository.findById(userId);
        if(member==null){
            throw new UsernameNotFoundException("유저 정보가 없습니다.");
        }
        return member;
    }
}
