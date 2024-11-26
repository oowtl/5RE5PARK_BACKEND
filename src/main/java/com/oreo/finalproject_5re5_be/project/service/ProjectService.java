package com.oreo.finalproject_5re5_be.project.service;

import com.oreo.finalproject_5re5_be.project.dto.response.ProjectResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface ProjectService {
    /**
     * 1. 프로젝트 조회
     * 2. 프로젝트 생성
     * 3. 프로젝트 이름 변경
     * 4. 프로젝트 삭제
     */

    List<ProjectResponse> projectFindAll(Long memberSeq);
    Long projectSave(Long memberSeq);
    void projectUpdate(@Valid @NotNull Long projectSeq,@Valid @NotNull String projectName);
    void projectDelete(@Valid @NotNull List<Long> projectSeq);
    boolean projectCheck(Long memberSeq, Long projectSeq);
    boolean projectCheck(Long memberSeq, List<Long> projectSeq);
}
