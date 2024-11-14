package com.oreo.finalproject_5re5_be.member.service;

import com.oreo.finalproject_5re5_be.member.repository.MemberTermConditionRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberTermsConditionServiceImpl {

    private final MemberTermConditionRepository memberTermConditionRepository;

    public MemberTermsConditionServiceImpl(MemberTermConditionRepository memberTermConditionRepository) {
        this.memberTermConditionRepository = memberTermConditionRepository;
    }
    // 회원 약관 항목 CRUD
    // 1-1. 단건 회원 약관 항목을 등록한다
    // 1-2. 여러개 회원 약관 항목을 등록한다

    // 2-1. 단건 회원 약관 항목을 조회한다
    // 2-2. 여러개 회원 약관 항목을 조회한다
    // 2-2. 사용 가능한 여러개 회원 약관 항목을 조회한다
    // 2-3. 사용 불가능한 여러개 회원 약관 항목을 조회한다

    // 3-1. 단건 회원 약관 항목을 수정한다
    // 3-2. 여러개 회원 약관 항목을 수정한다

    // 4-1. 단건 회원 약관 항목을 삭제한다
    // 4-2. 여러개 회원 약관 항목을 삭제한다


}
