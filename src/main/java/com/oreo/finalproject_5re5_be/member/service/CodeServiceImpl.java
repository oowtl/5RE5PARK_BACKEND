package com.oreo.finalproject_5re5_be.member.service;


import com.oreo.finalproject_5re5_be.member.repository.CodeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CodeServiceImpl {

    private final CodeRepository codeRepository;

    public CodeServiceImpl(CodeRepository codeRepository) {
        this.codeRepository = codeRepository;
    }

    // 코드 등록

    // 코드 조회

    // 코드 수정

    // 코드 삭제

}
