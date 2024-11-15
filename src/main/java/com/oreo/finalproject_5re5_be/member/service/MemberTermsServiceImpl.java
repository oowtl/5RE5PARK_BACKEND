package com.oreo.finalproject_5re5_be.member.service;

import com.oreo.finalproject_5re5_be.member.repository.MemberTermsRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberTermsServiceImpl {

    private final MemberTermsRepository memberTermsRepository;
    private final MemberTermsConditionServiceImpl memberTermsConditionService;

    public MemberTermsServiceImpl(MemberTermsRepository memberTermsRepository, MemberTermsConditionServiceImpl memberTermsConditionService) {
        this.memberTermsRepository = memberTermsRepository;
        this.memberTermsConditionService = memberTermsConditionService;
    }

    // 회원 약관 항목 CRUD 구현

    // 1.회원 약관 생성
    // 1-1. 단건 회원 약관 항목들을 바탕으로 회원 약관을 등록한다

    // 2. 회원 약관 조회
    // 2-1. 사용 가능한 가장 최근 약관 조회
    // 2-2. 사용 가능한 모든 약관 조회
    // 2-2. 사용 불가능한 모든 약관 조회

    // 3. 회원 약관 수정


    // 4. 회원 약관 삭제

}
