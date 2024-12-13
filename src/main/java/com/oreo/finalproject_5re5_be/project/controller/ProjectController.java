package com.oreo.finalproject_5re5_be.project.controller;

import com.oreo.finalproject_5re5_be.global.dto.response.ResponseDto;
import com.oreo.finalproject_5re5_be.project.dto.request.ProjectTextRequest;
import com.oreo.finalproject_5re5_be.project.dto.response.ProjectResponse;
import com.oreo.finalproject_5re5_be.project.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Project", description = "Project 관련 API")
@RestController
@Slf4j
@Validated
@CrossOrigin(origins = "*")
@RequestMapping("/api/project")
public class ProjectController {
    private ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Operation(summary = "Project 정보 검색", description = "회원 Seq로 프로젝트 정보를 가지고옵니다.")
    @GetMapping("")
    public ResponseEntity<ResponseDto<Map<String, List<Object>>>> projectGet(HttpSession session) {
        //            @PathVariable Long memSeq){//session memberSeq값
        //        List<ProjectResponse> projectResponses =
        Long memberSeq = (Long) session.getAttribute("memberSeq");
        projectService.projectFindAll(memberSeq);
        List<ProjectResponse> projectResponses = projectService.projectFindAll(memberSeq);
        log.info(
                "[ProjectController] projectGet - projectResponses : {} ", projectResponses.toString());

        Map<String, List<Object>> map = new HashMap<>(); // 맵 생성
        map.put("row", Collections.singletonList(projectResponses)); // row : [] 로 응답
        return ResponseEntity.ok().body(new ResponseDto<>(HttpStatus.OK.value(), map));
    }

    @Operation(summary = "Project 생성", description = "회원 Seq로 프로젝트를 생성 합니다.")
    @PostMapping("")
    public ResponseEntity<ResponseDto<Map<String, Object>>> projectSave(
            HttpSession session) { // session memberSeq값
        // project 생성
        Long projectSeq = projectService.projectSave((Long) session.getAttribute("memberSeq"));
        Map<String, Object> map = new HashMap<>();
        map.put("projectSeq", projectSeq); // 프로젝트seq 응답에 추가
        map.put("msg", "프로젝트 생성 완료되었습니다."); // 메시지 추가
        return ResponseEntity.ok().body(new ResponseDto<>(HttpStatus.OK.value(), map));
    }

    @Operation(summary = "Project 이름 수정(저장)", description = "프로젝트 Seq와 변경할 이름을 받아 수정합니다.")
    @PutMapping("")
    public ResponseEntity<ResponseDto<String>> projectUpdate(
            HttpSession session, @Valid @RequestBody ProjectTextRequest request) {
        projectService.projectCheck(
                (Long) session.getAttribute("memberSeq"), request.getProSeq()); // 회원의 프로젝트인지 확인
        projectService.projectUpdate(request.getProSeq(), request.getProjectName()); // 프로젝트 수정
        return ResponseEntity.ok()
                .body(new ResponseDto<>(HttpStatus.OK.value(), "Project 이름 변경 완료되었습니다.")); // 응답
    }

    @Operation(summary = "Project 삭제", description = "프로젝트 Seq를 받아 activate 상태를 'N'으로 변경합니다.")
    @DeleteMapping("")
    public ResponseEntity<ResponseDto<String>> projectDelete(
            @RequestParam List<Long> proSeq, HttpSession session) {
        projectService.projectCheck((Long) session.getAttribute("memberSeq"), proSeq); // 회원의 프로젝트인지 확인
        projectService.projectDelete(proSeq); // 프로젝트 삭제 배열로 받음
        return ResponseEntity.ok()
                .body(new ResponseDto<>(HttpStatus.OK.value(), "Project 삭제 완료되었습니다.")); // 모두 삭제
    }
}
