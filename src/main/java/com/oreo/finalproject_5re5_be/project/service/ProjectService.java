package com.oreo.finalproject_5re5_be.project.service;

import com.oreo.finalproject_5re5_be.project.dto.response.ProjectResponse;

import java.util.List;

public interface ProjectService {
    /**
     * 1. 프로젝트 조회
     * 2. 프로젝트 생성
     * 3. 프로젝트 이름 변경
     * 4. 프로젝트 삭제
     */
    List<ProjectResponse> projectFindAll(Long memberSeq);
    Long projectSave(Long memberSeq);
    void projectUpdate(Long projectSeq, String projectName);
    void projectDelete(List<Long> projectSeq);
}
