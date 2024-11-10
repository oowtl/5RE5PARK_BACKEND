package com.oreo.finalproject_5re5_be.member.service;

import com.oreo.finalproject_5re5_be.member.dto.request.MemberRegisterRequest;
import com.oreo.finalproject_5re5_be.member.entity.Member;
import com.oreo.finalproject_5re5_be.member.entity.MemberCategory;
import com.oreo.finalproject_5re5_be.member.entity.MemberState;
import com.oreo.finalproject_5re5_be.member.entity.MemberTermsHistory;
import com.oreo.finalproject_5re5_be.member.exception.MemberDuplicatedEmailException;
import com.oreo.finalproject_5re5_be.member.exception.MemberDuplicatedIdException;
import com.oreo.finalproject_5re5_be.member.exception.MemberMandatoryTermNotAgreedException;
import com.oreo.finalproject_5re5_be.member.exception.MemberWrongCountTermCondition;
import com.oreo.finalproject_5re5_be.member.exception.RetryFailedException;
import com.oreo.finalproject_5re5_be.member.repository.MemberCategoryRepository;
import com.oreo.finalproject_5re5_be.member.repository.MemberConnectionHistoryRepository;
import com.oreo.finalproject_5re5_be.member.repository.MemberRepository;
import com.oreo.finalproject_5re5_be.member.repository.MemberStateRepository;
import com.oreo.finalproject_5re5_be.member.repository.MemberTermsHistoryRepository;
import com.oreo.finalproject_5re5_be.member.repository.MemberTermsRepository;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MemberServiceImpl {

    private static final int MAX_RETRY = 10;
    private static final int RETRY_DELAY = 5_000;

    private final MemberConnectionHistoryRepository memberConnectionHistoryRepository;
    private final MemberRepository memberRepository;
    private final MemberStateRepository memberStateRepository;
    private final MemberTermsHistoryRepository memberTermsHistoryRepository;
    private final MemberTermsRepository memberTermsRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberCategoryRepository memberCategoryRepository;

    public MemberServiceImpl(MemberConnectionHistoryRepository memberConnectionHistoryRepository,
            MemberRepository memberRepository, MemberStateRepository memberStateRepository, MemberTermsHistoryRepository memberTermsHistoryRepository,
            MemberTermsRepository memberTermsRepository, PasswordEncoder passwordEncoder, MemberCategoryRepository memberCategoryRepository) {
        this.memberConnectionHistoryRepository = memberConnectionHistoryRepository;
        this.memberRepository = memberRepository;
        this.memberStateRepository = memberStateRepository;
        this.memberTermsHistoryRepository = memberTermsHistoryRepository;
        this.memberTermsRepository = memberTermsRepository;
        this.passwordEncoder = passwordEncoder;
        this.memberCategoryRepository = memberCategoryRepository;
    }

    // 1. 회원가입 : 유효성 검증이 완료된 회원 정보를 통해 회원가입을 처리한다.
    /**
     * - 중복 이메일/아이디 확인
     * - 비밀번호 암호화
     * - 회원, 약관 이력, 상태 저장
     *
     */

    public Member create(MemberRegisterRequest request) {
        try {
            // 1. 중복되는 이메일 확인 -> MemberDuplicatedEmailException.class
            checkDuplicatedEmail(request.getEmail());
            // 2. 중복되는 아이디가 있는지 확인 -> MemberDuplicatedIdException.class
            checkDuplicatedId(request.getId());
            // 3. 회원 약관 유효성 확인 -> MemberMandatoryTermNotAgreedException.class, MemberWrongCountTermCondition.class
            request.checkValidTerms();
            request.checkValidTermsCount();
        } catch (MemberDuplicatedEmailException | MemberDuplicatedIdException | MemberMandatoryTermNotAgreedException | MemberWrongCountTermCondition e) {
            throw e;
        }

        // 4. 회원가입 처리(재시도를 통한 복구 작업 설정)
        return RetryableCreateMember(request);
    }


    @Transactional
    @Retryable(
            value = {RuntimeException.class},
            maxAttempts = MAX_RETRY,
            backoff = @Backoff(delay = RETRY_DELAY)
    )
    public Member RetryableCreateMember(MemberRegisterRequest request) {
        // 1. 비밀번호 암호화
        encodePassword(request);
        // 2. 회원 엔티티 저장
        Member savedMember = saveMember(request);
        // 3. 회원 약관 이력 저장
        saveMemberTermsHistory(request, savedMember);
        // 4. 회원 상태 업데이트
        saveInitMemberState(savedMember);
        return savedMember;
    }

    @Recover
    public Member recover(RuntimeException e) {
        throw new RetryFailedException();
    }

    private void checkDuplicatedEmail(String email) {
        Member foundMember = memberRepository.findByEmail(email);
        if (foundMember != null) {
            throw new MemberDuplicatedEmailException();
        }

    }

    private void checkDuplicatedId(String id) {
        Member foundMember = memberRepository.findById(id);
        if (foundMember != null) {
            throw new MemberDuplicatedIdException();
        }
    }

    private void encodePassword(MemberRegisterRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        request.setPassword(encodedPassword);
    }

    private Member saveMember(MemberRegisterRequest request) {
        Member member = request.createMemberEntity();
        Member savedMember = memberRepository.save(member);
        return savedMember;
    }

    private MemberTermsHistory saveMemberTermsHistory(MemberRegisterRequest request, Member member) {
        MemberTermsHistory memberTermsHistory = request.createMemberTermsHistoryEntity(member);
        MemberTermsHistory savedMemberTermsHistory = memberTermsHistoryRepository.save(memberTermsHistory);
        return savedMemberTermsHistory;
    }

    private MemberState saveInitMemberState(Member member) {
        MemberState userState = new MemberState();
        MemberCategory memberInitState = findMemberStateCategory("신규등록");
        userState.setMember(member);
        userState.setCateCode(memberInitState);
        MemberState savedMemberState = memberStateRepository.save(userState);
        return savedMemberState;
    }

    private MemberCategory findMemberStateCategory(String cateName) {
        MemberCategory foundMemberCategory = memberCategoryRepository.findByName(cateName);
        return foundMemberCategory;
    }


    // 2. 로그인

    // 3. 회원정보 조회

    // 4. 회원정보 수정
}
