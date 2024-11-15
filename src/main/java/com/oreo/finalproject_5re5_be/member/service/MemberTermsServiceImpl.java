package com.oreo.finalproject_5re5_be.member.service;

import com.oreo.finalproject_5re5_be.member.dto.request.MemberTermRequest;
import com.oreo.finalproject_5re5_be.member.dto.request.MemberTermUpdateRequest;
import com.oreo.finalproject_5re5_be.member.dto.response.MemberTermConditionResponse;
import com.oreo.finalproject_5re5_be.member.entity.Member;
import com.oreo.finalproject_5re5_be.member.entity.MemberTerms;
import com.oreo.finalproject_5re5_be.member.entity.MemberTermsCondition;
import com.oreo.finalproject_5re5_be.member.exception.MemberTermsConditionNotFoundException;
import com.oreo.finalproject_5re5_be.member.exception.MemberTermsNotFoundException;
import com.oreo.finalproject_5re5_be.member.exception.RetryFailedException;
import com.oreo.finalproject_5re5_be.member.repository.MemberTermConditionRepository;
import com.oreo.finalproject_5re5_be.member.repository.MemberTermsRepository;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberTermsServiceImpl {

    private final MemberTermsRepository memberTermsRepository;
    private final MemberTermConditionRepository memberTermConditionRepository;

    public MemberTermsServiceImpl(MemberTermsRepository memberTermsRepository, MemberTermConditionRepository memberTermConditionRepository) {
        this.memberTermsRepository = memberTermsRepository;
        this.memberTermConditionRepository = memberTermConditionRepository;
    }


    // 회원 약관 생성
    public MemberTerms create(MemberTermRequest request) {
        // 약관 엔티티를 생성함
        MemberTerms terms = new MemberTerms();
        List<MemberTermsCondition> foundMemberTermConditions = findMemberTermsConditions(request.getMemberTermConditionCodes());

        // 각 코드에 맞는 회원 약관 항목들을 찾아서 리스트에 담음

        // 각 약관 항목과 필수 여부를 세팅함
        List<Character> memberTermConditionMandatoryOrNot = request.getMemberTermConditionMandatoryOrNot();
        terms.setTermCond1(foundMemberTermConditions.get(0));
        terms.setChkTerm1(memberTermConditionMandatoryOrNot.get(0));

        terms.setTermCond2(foundMemberTermConditions.get(1));
        terms.setChkTerm2(memberTermConditionMandatoryOrNot.get(1));

        terms.setTermCond3(foundMemberTermConditions.get(2));
        terms.setChkTerm3(memberTermConditionMandatoryOrNot.get(2));

        terms.setTermCond4(foundMemberTermConditions.get(3));
        terms.setChkTerm4(memberTermConditionMandatoryOrNot.get(3));

        terms.setTermCond5(foundMemberTermConditions.get(4));
        terms.setChkTerm5(memberTermConditionMandatoryOrNot.get(4));

        // 사용 가능 여부를 세팅함
        Character chk = request.getChkUse();
        terms.setChkUse(chk);

        // 약관 이름 세팅함
        String name = request.getTermName();
        terms.setName(name);

        // 시간 세팅함
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime max = LocalDateTime.MAX;
        terms.setTermRegDate(now);
        terms.setTermEndDate(max);

        System.out.println("terms = " + terms);

        // 생성한 값을 반환함
        MemberTerms savedMemberTerm = memberTermsRepository.save(terms);
        return savedMemberTerm;
    }



    // 회원 약관 시퀀스로 조회
    public MemberTerms read(Long termSeq) {
        return findMemberTerm(termSeq);
    }

    // 회원 약관 코드로 조회
    public MemberTerms read(String name) {
        return memberTermsRepository.findMemberTermsByName(name);
    }


    public List<MemberTerms> readAll() {
        return memberTermsRepository.findAll();
    }

    public MemberTerms readLatestAvailable() {
        return memberTermsRepository.findTopByChkUseOrderByTermRegDateDesc();
    }

    public List<MemberTerms> readAvailable() {
        return memberTermsRepository.findAvailableMemberTerms();
    }

    public List<MemberTerms> readNotAvailable() {
        return memberTermsRepository.findNotAvailableMemberTerms();
    }

    // 회원 약관 수정
    public void update(Long termSeq, MemberTermUpdateRequest request) {
        // 약관 시퀀스로 해당 약관 조회
        MemberTerms foundMemberTerms = findMemberTerm(termSeq);

        // 업데이트 처리
        foundMemberTerms.update(request);
    }


    // 회원 약관 삭제
    public void remove(Long termSeq) {
        // 약관 시퀀스로 해당 약관 조회
        MemberTerms foundMemberTerms = findMemberTerm(termSeq);

        // 삭제 처리
        memberTermsRepository.delete(foundMemberTerms);
    }

    // 약관 시퀀스로 약관을 찾아서 반환함
    private MemberTerms findMemberTerm(Long termSeq) {
        MemberTerms foundMemberTerms = memberTermsRepository.findMemberTermsByTermsSeq(termSeq);
        if (foundMemberTerms == null) {
            throw new MemberTermsNotFoundException();
        }
        return foundMemberTerms;
    }

    // 약관 항목 코드로 약관 항목을 찾아서 리스트로 반환함
    private List<MemberTermsCondition> findMemberTermsConditions(List<String> condCodes) {
        List<MemberTermsCondition> foundMemberTermConditions = new ArrayList<>();
        for (String condCode : condCodes) {
            MemberTermsCondition foundMemberTermCondition = findMemberTermsCondition(condCode);
            foundMemberTermConditions.add(foundMemberTermCondition);
        }

        return foundMemberTermConditions;
    }

    // 각 코드에 맞는 약관 항목을 찾아서 반환함
    private MemberTermsCondition findMemberTermsCondition(String condCode) {
        MemberTermsCondition foundMemberTermCondition = memberTermConditionRepository.findMemberTermsConditionByCondCode(condCode);
        if (foundMemberTermCondition == null) {
            throw new MemberTermsConditionNotFoundException();
        }
        return foundMemberTermCondition;
    }

}
