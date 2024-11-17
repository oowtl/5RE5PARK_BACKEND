package com.oreo.finalproject_5re5_be.project.controller;

import com.oreo.finalproject_5re5_be.global.dto.response.ResponseDto;
import com.oreo.finalproject_5re5_be.member.entity.Member;
import com.oreo.finalproject_5re5_be.project.dto.response.ProjectResponse;
import com.oreo.finalproject_5re5_be.project.service.ProjectService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@Validated
@CrossOrigin(origins = "*")
@RequestMapping("/api/project")
public class ProjectController {
    private ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService){
        this.projectService = projectService;
    }

    @GetMapping("")
    public ResponseEntity<ResponseDto<Map<String, List<ProjectResponse>>>> projectGet(
            @SessionAttribute("memberSeq") Long memberSeq){
        List<ProjectResponse> projectResponses = projectService.projectFindAll(memberSeq);
        Map<String, List<ProjectResponse>> map = new HashMap<>();
        map.put("row", projectResponses);
        return ResponseEntity.ok()
                .body(new ResponseDto<>(HttpStatus.OK.value(), map));
    }

    @PostMapping("")
    public ResponseEntity<ResponseDto<Map<String,Object>>> projectSave(
            @SessionAttribute("memberSeq") Long memberSeq){
        Long projectSeq = projectService.projectSave(memberSeq);
        Map<String, Object> map = new HashMap<>();
        map.put("projectSeq", projectSeq);
        map.put("msg", "프로젝트 생성 완료되었습니다.");
        return ResponseEntity.ok()
                .body(new ResponseDto<>(HttpStatus.OK.value(),map));
    }

    @PutMapping("/{projectSeq}")
    public ResponseEntity<ResponseDto<String>> projectUpdate(@Valid @PathVariable Long projectSeq,
                                                             @Valid @RequestBody String text){
        projectService.projectUpdate(projectSeq, text);
        return ResponseEntity.ok()
                .body(new ResponseDto<>(HttpStatus.OK.value(),
                        "Project 이름 변경 완료되었습니다."));
    }

    @DeleteMapping("")
    public ResponseEntity<ResponseDto<String>> projectDelete(@RequestParam List<Long> projectSeq){
        projectService.projectDelete(projectSeq);
        return ResponseEntity.ok()
                .body(new ResponseDto<>(HttpStatus.OK.value(),
                        "Project 삭제 완료되었습니다."));
    }
}
