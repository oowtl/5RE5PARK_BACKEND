package com.oreo.finalproject_5re5_be.member.service;

import com.oreo.finalproject_5re5_be.member.dto.request.MemberTermRequest;
import com.oreo.finalproject_5re5_be.member.dto.request.MemberTermUpdateRequest;
import com.oreo.finalproject_5re5_be.member.dto.response.MemberTermConditionResponse;
import com.oreo.finalproject_5re5_be.member.dto.response.MemberTermResponse;
import com.oreo.finalproject_5re5_be.member.dto.response.MemberTermResponses;
import com.oreo.finalproject_5re5_be.member.dto.response.MemberTermsDetailResponse;
import com.oreo.finalproject_5re5_be.member.entity.MemberTerms;
import com.oreo.finalproject_5re5_be.member.entity.MemberTermsCondition;
import com.oreo.finalproject_5re5_be.member.exception.MemberTermsConditionNotFoundException;
import com.oreo.finalproject_5re5_be.member.exception.MemberTermsNotFoundException;
import com.oreo.finalproject_5re5_be.member.repository.MemberTermConditionRepository;
import com.oreo.finalproject_5re5_be.member.repository.MemberTermsRepository;
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

    public MemberTermsServiceImpl(
            MemberTermsRepository memberTermsRepository,
            MemberTermConditionRepository memberTermConditionRepository) {
        this.memberTermsRepository = memberTermsRepository;
        this.memberTermConditionRepository = memberTermConditionRepository;
    }

    // 회원 약관 생성
    public MemberTermResponse create(MemberTermRequest request) {
        // 약관 엔티티를 생성함
        MemberTerms terms = new MemberTerms();
        List<MemberTermsCondition> foundMemberTermConditions =
                findMemberTermsConditions(request.getMemberTermConditionCodes());

        // 각 코드에 맞는 회원 약관 항목들을 찾아서 리스트에 담음

        // 각 약관 항목과 필수 여부를 세팅함
        List<Character> memberTermConditionMandatoryOrNot =
                request.getMemberTermConditionMandatoryOrNot();
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
        String name = request.getName();
        terms.setName(name);

        // 약관 코드 세팅함
        String termCode = request.getTermCode();
        terms.setTermCode(termCode);

        // 시간 세팅함
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime max = LocalDateTime.MAX;
        terms.setTermRegDate(now);

        // 생성한 값을 반환함
        MemberTerms savedMemberTerm = memberTermsRepository.save(terms);
        return new MemberTermResponse(savedMemberTerm);
    }

    // 회원 약관 시퀀스로 조회
    public MemberTermResponse read(Long termSeq) {
        MemberTerms foundMemberTerm = findMemberTerm(termSeq);
        return new MemberTermResponse(foundMemberTerm);
    }

    // 회원 약관 코드로 조회
    public MemberTermResponse read(String name) {
        MemberTerms foundMemberTerm = findMemberTerm(name);
        return new MemberTermResponse(foundMemberTerm);
    }

    public MemberTermResponse readLatestAvailable() {
        MemberTerms foundMemberTerms = memberTermsRepository.findTopByChkUseOrderByTermRegDateDesc();
        return new MemberTermResponse(foundMemberTerms);
    }

    /**
     * 밑에 코드들 중복, 추후에 리팩토링 처리[]
     *
     * @return
     */
    // 추후에 페이징 처리 필요
    public MemberTermResponses readAll() {
        // 모든 회원 약관을 조회한다
        List<MemberTerms> foundMemberTerms = memberTermsRepository.findAll();

        // 조회된 모든 엔티티를 response로 변환한다
        List<MemberTermResponse> memberTermResponseList =
                foundMemberTerms.stream().map(MemberTermResponse::new).toList();

        // 변환된 모든 response를 하나로 묶은 response에 담아서 반환한다
        return new MemberTermResponses(memberTermResponseList);
    }

    // 추후에 페이징 처리 필요
    public MemberTermResponses readAvailable() {
        List<MemberTerms> foundMemberTerms = memberTermsRepository.findAvailableMemberTerms();

        // 조회된 모든 엔티티를 response로 변환한다
        List<MemberTermResponse> memberTermResponseList =
                foundMemberTerms.stream().map(MemberTermResponse::new).toList();

        // 변환된 모든 response를 하나로 묶은 response에 담아서 반환한다
        return new MemberTermResponses(memberTermResponseList);
    }

    // 추후에 페이징 처리 필요
    public MemberTermResponses readNotAvailable() {
        List<MemberTerms> foundMemberTerms = memberTermsRepository.findNotAvailableMemberTerms();

        // 조회된 모든 엔티티를 response로 변환한다
        List<MemberTermResponse> memberTermResponseList =
                foundMemberTerms.stream().map(MemberTermResponse::new).toList();

        // 변환된 모든 response를 하나로 묶은 response에 담아서 반환한다
        return new MemberTermResponses(memberTermResponseList);
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

    private MemberTerms findMemberTerm(String name) {
        MemberTerms foundMemberTerms = memberTermsRepository.findMemberTermsByName(name);
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
        MemberTermsCondition foundMemberTermCondition =
                memberTermConditionRepository.findMemberTermsConditionByCondCode(condCode);
        if (foundMemberTermCondition == null) {
            throw new MemberTermsConditionNotFoundException();
        }
        return foundMemberTermCondition;
    }

    // 특정 코드로 호원 약관 상세 조회
    public MemberTermsDetailResponse readByTermCode(String termCode) {
        // 특정 회원 약관 코드로 조회
        MemberTerms foundMemberTerms = memberTermsRepository.findMemberTermsByTermCode(termCode);
        if (foundMemberTerms == null) {
            throw new MemberTermsNotFoundException();
        }

        // 각 약관 정보 조회해서 응답 데이터에 넣어주기
        List<MemberTermConditionResponse> memberTermConditionResponseList = new ArrayList<>();
        MemberTermsCondition termCond1 = foundMemberTerms.getTermCond1();
        if (termCond1 == null) {
            throw new MemberTermsConditionNotFoundException();
        }

        MemberTermsCondition termCond2 = foundMemberTerms.getTermCond2();
        if (termCond2 == null) {
            throw new MemberTermsConditionNotFoundException();
        }

        MemberTermsCondition termCond3 = foundMemberTerms.getTermCond3();
        if (termCond3 == null) {
            throw new MemberTermsConditionNotFoundException();
        }

        MemberTermsCondition termCond4 = foundMemberTerms.getTermCond4();
        if (termCond4 == null) {
            throw new MemberTermsConditionNotFoundException();
        }

        MemberTermsCondition termCond5 = foundMemberTerms.getTermCond5();
        if (termCond5 == null) {
            throw new MemberTermsConditionNotFoundException();
        }

        // 응답 데이터로 전환
        MemberTermConditionResponse memberTermConditionResponse1 =
                MemberTermConditionResponse.of(termCond1);
        MemberTermConditionResponse memberTermConditionResponse2 =
                MemberTermConditionResponse.of(termCond2);
        MemberTermConditionResponse memberTermConditionResponse3 =
                MemberTermConditionResponse.of(termCond3);
        MemberTermConditionResponse memberTermConditionResponse4 =
                MemberTermConditionResponse.of(termCond4);
        MemberTermConditionResponse memberTermConditionResponse5 =
                MemberTermConditionResponse.of(termCond5);

        // 리스트에 추가
        memberTermConditionResponseList.add(memberTermConditionResponse1);
        memberTermConditionResponseList.add(memberTermConditionResponse2);
        memberTermConditionResponseList.add(memberTermConditionResponse3);
        memberTermConditionResponseList.add(memberTermConditionResponse4);
        memberTermConditionResponseList.add(memberTermConditionResponse5);

        // 응답 데이터 반환
        return MemberTermsDetailResponse.of(foundMemberTerms, memberTermConditionResponseList);
    }
}
