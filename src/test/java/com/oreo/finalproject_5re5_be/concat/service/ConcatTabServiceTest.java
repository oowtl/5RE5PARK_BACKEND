package com.oreo.finalproject_5re5_be.concat.service;

import com.oreo.finalproject_5re5_be.concat.dto.request.ConcatCreateRequestDto;
import com.oreo.finalproject_5re5_be.concat.dto.response.ConcatTabResponseDto;
import com.oreo.finalproject_5re5_be.concat.entity.ConcatTab;
import com.oreo.finalproject_5re5_be.concat.repository.ConcatTabRepository;
import com.oreo.finalproject_5re5_be.concat.service.helper.ConcatTabHelper;
import com.oreo.finalproject_5re5_be.member.service.MemberServiceImpl;
import com.oreo.finalproject_5re5_be.project.entity.Project;
import com.oreo.finalproject_5re5_be.project.repository.ProjectRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConcatTabServiceTest {

    @Mock
    private ConcatTabRepository concatTabRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ConcatTabHelper concatTabHelper;

    @Mock
    private MemberServiceImpl memberService;

    @InjectMocks
    private ConcatTabService concatTabService;

    @Test
    @DisplayName("ConcatTab 생성 성공한다.")
    void createConcatTab_Success() {
        // given : 아이디가 1인 프로젝트 리턴, 컨캣탭이 존재하지 않음
        Project project = Project.builder().proSeq(1L).build();

        when(projectRepository.findById(project.getProSeq())).thenReturn(Optional.of(project));
        when(concatTabRepository.existsById(project.getProSeq())).thenReturn(false);

        // when : 컨캣탭 생성
        boolean result = concatTabService
                .createConcatTab(new ConcatCreateRequestDto(project.getProSeq(), 1L));

        // then : 생성  성공
        assertThat(result).isTrue();
        verify(concatTabRepository, times(1)).save(any(ConcatTab.class));//특정 동작이 발생했는지 확인
        verify(projectRepository, times(1)).findById(project.getProSeq());
    }

    @Test
    @DisplayName("프로젝트를 찾을수 없으면 실패한다.")
    void createConcatTab_Fail_ProjectNotFound() {
        // given : 프로젝트를 찾을수 없음
        long projectSeq = 1L;

        when(projectRepository.findById(projectSeq)).thenReturn(Optional.empty());

        // when : 프로젝트를 찾지 못하면
        boolean result = concatTabService.createConcatTab(new ConcatCreateRequestDto(projectSeq, 1L));

        // then : 실패
        assertThat(result).isFalse();
        verify(concatTabRepository, never()).save(any(ConcatTab.class));//프로젝트를 찾지 못했으므로 리포지토리는 실행되지 않는다.
    }

    @Test
    @DisplayName("ConcatTab이 이미 존재한다면 실패한다.")
    void createConcatTab_Fail_ConcatTabExists() {
        // given : 이미 ConcatTab이 존재
        Project project = Project.builder().proSeq(1L).build();

        when(projectRepository.findById(project.getProSeq())).thenReturn(Optional.of(project));
        when(concatTabRepository.existsById(project.getProSeq())).thenReturn(true);

        // when : ConcatTab이 이미 존재
        boolean result = concatTabService
                .createConcatTab(new ConcatCreateRequestDto(project.getProSeq(), 1L));

        // then : 실패
        assertThat(result).isFalse();
        verify(concatTabRepository, never()).save(any(ConcatTab.class));
    }

    @Test
    void readConcatTab_Success() {
        //given : 프로젝트와 ConcatTab이 존재
        Project project = Project.builder().proSeq(1L).build();
        Long memberSeq = 1L;
        ConcatTab concatTab = new ConcatTab(1L, project, null, 'Y', 0.5f);

        when(projectRepository.findById(project.getProSeq())).thenReturn(Optional.of(project));
        when(concatTabRepository.findById(project.getProSeq())).thenReturn(Optional.of(concatTab));
        when(concatTabHelper.prepareConcatTab(concatTab, memberSeq)).thenReturn(new ConcatTabResponseDto(
                concatTab.getProjectId(),
                null,
                concatTab.getFrontSilence(),
                concatTab.getStatus()
        ));

        //when : ConcatTab 조회
        ConcatTabResponseDto result = concatTabService.readConcatTab(project.getProSeq(), memberSeq);

        // then : 성공
        assertThat(result).isNotNull();
        verify(projectRepository, times(1)).findById(project.getProSeq());
        verify(concatTabRepository, times(1)).findById(project.getProSeq());
    }

    @Test
    void readConcatTab_CreateConcatTabAndReturn() {
        //given : 프로젝트가 존재하지만 ConcatTab이 존재하지 않음
        Project project = Project.builder().proSeq(1L).build();
        ConcatTab concatTab = new ConcatTab(1L, project, null, 'Y', 0.5f);
        Long memberSeq = 1L;

        when(projectRepository.findById(project.getProSeq())).thenReturn(Optional.of(project));
        when(concatTabRepository.findById(project.getProSeq()))
                .thenReturn(Optional.empty())//첫 번째 리턴
                .thenReturn(Optional.of(concatTab));// 두 번쨰 리턴
        when(concatTabHelper.prepareConcatTab(concatTab, memberSeq)).thenReturn(new ConcatTabResponseDto(
                concatTab.getProjectId()
                , null,
                concatTab.getFrontSilence(),
                concatTab.getStatus()
        ));
        when(concatTabRepository.existsById(project.getProSeq())).thenReturn(false);

        //when : 컨캣탭이 없다면 재생성 후 다시 조회
        ConcatTabResponseDto result = concatTabService.readConcatTab(project.getProSeq(), memberSeq);

        //then : 생성된 탭을 조회
        assertThat(result).isNotNull();
        verify(projectRepository, times(1)).findById(project.getProSeq());//프로젝트 확인시 조회 1번
        verify(concatTabRepository, times(2)).findById(project.getProSeq());//요청 받을 때, 재생성 후
    }
}