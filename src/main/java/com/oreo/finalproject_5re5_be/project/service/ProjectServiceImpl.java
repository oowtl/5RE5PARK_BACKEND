package com.oreo.finalproject_5re5_be.project.service;

import com.oreo.finalproject_5re5_be.member.entity.Member;
import com.oreo.finalproject_5re5_be.member.repository.MemberRepository;
import com.oreo.finalproject_5re5_be.project.dto.response.ProjectResponse;
import com.oreo.finalproject_5re5_be.project.entity.Project;
import com.oreo.finalproject_5re5_be.project.exception.InvalidProjectNameException;
import com.oreo.finalproject_5re5_be.project.repository.ProjectRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Long projectSave() {
        Member member = getCurrentUser();
        Project project = Project.builder()
                .member(member)
                .build();
        Project save = projectRepository.save(project);
        log.info("Save project : {}", save);
        return save.getProSeq();
    }

    @Override
    public void projectUpdate(@NotNull Long projectSeq,@NotNull String projectName) {
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
    public void projectDelete(List<Long> projectSeq) {
        for (int i = 0; i < projectSeq.size(); i++) {
            Project projectFind = projectRepository.findById(projectSeq.get(i))
                    .orElseThrow(() -> new IllegalArgumentException("src file not found"));
            Project project = projectFind.toBuilder()
                    .proSeq(projectSeq.get(i))
                    .proActivate('N')
                    .build();
            projectRepository.save(project);
        }
    }

    private void validateProjectName(@NotNull String projectName) {
        if (projectName == null || projectName.length() < 3 || projectName.length() > 50) {
            throw new InvalidProjectNameException("프로젝트 이름은 3자 이상, 50자 이하여야 합니다.");
        }
    }
    private Member getCurrentUser() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String userId = authentication.getName();
        Member member = memberRepository.findById("akbvFQMtnk2WFjgngJH");
        if(member==null){
            throw new UsernameNotFoundException("유저 정보가 없습니다.");
        }

        return member;
    }
}
