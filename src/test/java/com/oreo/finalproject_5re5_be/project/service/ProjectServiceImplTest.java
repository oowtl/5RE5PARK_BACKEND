package com.oreo.finalproject_5re5_be.project.service;


import com.oreo.finalproject_5re5_be.member.entity.Member;
import com.oreo.finalproject_5re5_be.member.repository.MemberRepository;
import com.oreo.finalproject_5re5_be.project.entity.Project;
import com.oreo.finalproject_5re5_be.project.exception.InvalidProjectNameException;
import com.oreo.finalproject_5re5_be.project.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ProjectServiceImplTest {

    @InjectMocks
    private ProjectServiceImpl projectService;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private MemberRepository memberRepository;

    private Member mockMember;
    private Project mockProject;
    private Long memberSeq = 1L;

    @BeforeEach
    void setUp() {
        mockMember = Member.builder()
                .seq(1L)
                .build();

        mockProject = Project.builder()
                .proSeq(1L)
                .proName("Test Project").build();
    }


//    @Test
//    void projectFindAll() {
//        // given
//        when(memberRepository.findById(memberSeq)).thenReturn(Optional.of(mockMember));//member 값 세팅
//        when(projectRepository.findByMemberSeq(memberSeq)).thenReturn(List.of(mockProject));//project 값 세팅
//
//        // when
//        List<ProjectResponse> responses = projectService.projectFindAll(memberSeq);//memberSeq 로 project 확인
//
//        // then
//        assertThat(responses).hasSize(1);
//        assertThat(responses.get(0).getProjectName()).isEqualTo("Test Project");//프로젝트 결과 이름확인
//        verify(memberRepository).findById(memberSeq);//검증
//        verify(projectRepository).findByMemberSeq(memberSeq);//검증
//    }

    @Test
    void projectSave() {
        // given
        when(memberRepository.findById(memberSeq)).thenReturn(Optional.of(mockMember));//member 값 세팅
        when(projectRepository.save(any(Project.class))).thenReturn(mockProject);//project 값 세팅

        // when
        Long projectSeq = projectService.projectSave(memberSeq);//1L

        // then
        assertThat(projectSeq).isEqualTo(1L);//결과 비교
        verify(memberRepository).findById(1L);//검증
        verify(projectRepository).save(any(Project.class));//검증
    }

    @Test
    void projectUpdate() {
        // given
        when(projectRepository.findById(1L)).thenReturn(Optional.of(mockProject));//project 값 세팅

        // when
        projectService.projectUpdate(1L, "Updated Project");//Update 실행

        // then
        ArgumentCaptor<Project> projectCaptor = ArgumentCaptor.forClass(Project.class);//project 타입을 캡처
        verify(projectRepository).save(projectCaptor.capture());// 검증
        Project updatedProject = projectCaptor.getValue();
        assertThat(updatedProject.getProName()).isEqualTo("Updated Project"); // 업데이트된 값 확인
        assertThat(updatedProject.getProSeq()).isEqualTo(1L); // seq 확인 (추가 검증 가능)
    }

    @Test
    void projectDelete() {
        // given
        when(projectRepository.findById(1L)).thenReturn(Optional.of(mockProject));//project 값 셋팅

        // when
        projectService.projectDelete(List.of(1L));//1L 삭제실행

        // then
        ArgumentCaptor<Project> projectCaptor = ArgumentCaptor.forClass(Project.class);//project 타입을 캡처
        verify(projectRepository).save(projectCaptor.capture());// 검증
        Project deleteProject = projectCaptor.getValue();
        assertThat(deleteProject.getProSeq()).isEqualTo(1L);
        assertThat(deleteProject.getProActivate()).isEqualTo('N'); // Immutable; updated instance would be saved
    }
    @Test
    void projectName_InvalidProjectNameException() {
        // when // then
        assertThrows(InvalidProjectNameException.class,
                () -> projectService.projectUpdate(1L, "Hi"));
    }

    @Test
    void projectName_NullException() {
        // when // then
        assertThrows(InvalidProjectNameException.class,
                () -> projectService.projectUpdate(1L, null));
    }

    @Test
    void projectIsEmptyUpdate() {
        // given
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        // when // then
        assertThrows(IllegalArgumentException.class,
                () -> projectService.projectUpdate(1L, "Valid Name"));
    }

    @Test
    void projectNotFoundException() {
        // given
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        // when // then
        assertThrows(IllegalArgumentException.class,
                () -> projectService.projectSave(memberSeq));
    }


}