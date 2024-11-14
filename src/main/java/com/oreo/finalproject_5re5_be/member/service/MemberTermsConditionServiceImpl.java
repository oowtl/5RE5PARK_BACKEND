package com.oreo.finalproject_5re5_be.member.service;

import com.oreo.finalproject_5re5_be.member.dto.request.MemberTermConditionRequest;
import com.oreo.finalproject_5re5_be.member.dto.response.MemberTermConditionResponse;
import com.oreo.finalproject_5re5_be.member.dto.response.MemberTermConditionsResponse;
import com.oreo.finalproject_5re5_be.member.entity.MemberTermsCondition;
import com.oreo.finalproject_5re5_be.member.repository.MemberTermConditionRepository;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberTermsConditionServiceImpl {

    private final MemberTermConditionRepository memberTermConditionRepository;

    public MemberTermsConditionServiceImpl(MemberTermConditionRepository memberTermConditionRepository) {
        this.memberTermConditionRepository = memberTermConditionRepository;
    }
    // 회원 약관 항목 CRUD
    // 1-1. 단건 회원 약관 항목을 등록한다
    public MemberTermConditionResponse create(MemberTermConditionRequest request) {
        // 유효성 검증이 완료된 request로부터 엔티티를 생성한다
        MemberTermsCondition memberTermsConditionEntity = request.createMemberTermsConditionEntity();
        // 생성된 엔티티를 저장한다
        MemberTermsCondition savedMemberTermsCondition = memberTermConditionRepository.save(memberTermsConditionEntity);
        // 저장된 엔티티를 response로 변환하여 반환한다
        return new MemberTermConditionResponse(savedMemberTermsCondition);
    }

    // 1-2. 여러개 회원 약관 항목을 등록한다
    @Transactional
    public MemberTermConditionsResponse create(List<MemberTermConditionRequest> requests) {
        // 유효성 검증이 완료된 requests 더미로부터 이터러블 할 수 있는 엔티티 더미를 생성한다
        Stream<MemberTermsCondition> memberTermConditions = requests.stream()
                                                                    .map(MemberTermConditionRequest::createMemberTermsConditionEntity);

        // 해당 엔티티 더미를 모두 저장한다
        List<MemberTermsCondition> savedMemberTermConditions = memberTermConditionRepository.saveAll(memberTermConditions::iterator);

        // 저장된 엔티티 더미를 각각 response로 변환하여 반환한다
        List<MemberTermConditionResponse> memberTermCondtionsResponse = savedMemberTermConditions.stream()
                                                                                                 .map(MemberTermConditionResponse::new)
                                                                                                 .toList();

        // 저장된 엔티티 더미를 response로 변환하여 반환한다
        return new MemberTermConditionsResponse(memberTermCondtionsResponse);
    }

    // 2-1. 단건 회원 약관 항목을 조회한다
    // 2-2. 여러개 회원 약관 항목을 조회한다
    // 2-2. 사용 가능한 여러개 회원 약관 항목을 조회한다
    // 2-3. 사용 불가능한 여러개 회원 약관 항목을 조회한다

    // 3-1. 단건 회원 약관 항목을 수정한다
    // 3-2. 여러개 회원 약관 항목을 수정한다

    // 4-1. 단건 회원 약관 항목을 삭제한다
    // 4-2. 여러개 회원 약관 항목을 삭제한다


}
