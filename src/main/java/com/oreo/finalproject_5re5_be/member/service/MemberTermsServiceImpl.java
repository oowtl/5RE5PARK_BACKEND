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
    /**
     * -- 생성 쿼리
     * -- 약관 1개 만들기
     * -- 회원 약관 생성 (외래 키 값 참조)
     * INSERT INTO `member_terms` (
     *   `chk_term_1`, `chk_term_2`, `chk_term_3`, `chk_term_4`, `chk_term_5`, `chk_use`,
     *   `term_end_date`, `term_reg_date`, `name`, `reg_date`,
     *   `term_cond_seq_1`, `term_cond_seq_2`, `term_cond_seq_3`, `term_cond_seq_4`, `term_cond_seq_5`
     * )
     * VALUES(
     *   'Y', 'Y', 'N', 'N', 'Y', 'Y',                 -- 체크된 약관 (Y로 설정)
     *   now(), now(), '2024상반기회원약관', now(),
     *   (SELECT terms_cond_seq FROM member_terms_condition WHERE cond_code = 'TERMS001'), -- 첫번째 약관 항목 넣기
     *   (SELECT terms_cond_seq FROM member_terms_condition WHERE cond_code = 'TERMS002'), -- 두번째 약관 항목 넣기
     *   (SELECT terms_cond_seq FROM member_terms_condition WHERE cond_code = 'TERMS003'), -- 세번째 약관 항목 넣기
     *   (SELECT terms_cond_seq FROM member_terms_condition WHERE cond_code = 'TERMS004'), -- 네번째 약관 항목 넣기
     *   (SELECT terms_cond_seq FROM member_terms_condition WHERE cond_code = 'TERMS005') -- 다섯번째 약관 항목 넣기
     * );
     */

    // 1.회원 약관 생성
    // 1-1. 단건 회원 약관 항목들을 바탕으로 회원 약관을 등록한다

    // 2. 회원 약관 조회
    // 2-1. 사용 가능한 가장 최근 약관 조회
    // 2-2. 사용 가능한 모든 약관 조회
    // 2-2. 사용 불가능한 모든 약관 조회

    // 3. 회원 약관 수정


    // 4. 회원 약관 삭제

}
