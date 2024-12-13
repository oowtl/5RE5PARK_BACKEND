package com.oreo.finalproject_5re5_be.member.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/** 로그인, 로그아웃 테스트용 임시 컨트롤러입니다. 추후 삭제될 예정입니다. */
@RestController
public class TestController {

    @GetMapping("/")
    public String test() {
        return "로그인, 로그아웃 성공";
    }

    @GetMapping("/fail")
    public String fail() {
        return "로그인 실패";
    }
}
