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

    // 재시도 복구 설정값
    // - 최대 재시도 횟수 : 10회
    // - 재시도 딜레이 : 5초
    // - 재시도 실패시 예외 발생 : RetryFailedException.class
    // - 총 소요 시간 : 50초
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

    // 1-1. 회원가입 처리 가능 유무 파악
    public Member create(MemberRegisterRequest request) {
        try {
            // 중복되는 이메일 확인
            checkDuplicatedEmail(request.getEmail());
            // 중복되는 아이디가 있는지 확인
            checkDuplicatedId(request.getId());
            // 회원 약관 유효성 확인
            request.checkValidTerms();
            request.checkValidTermsCount();
        } catch (MemberDuplicatedEmailException | MemberDuplicatedIdException | MemberMandatoryTermNotAgreedException | MemberWrongCountTermCondition e) {
            // 회원가입 처리가 불가능할 경우 컨트롤러에 비즈니스 예외 전달
            throw e;
        }

        // 회원가입 처리, 서버 내부 예외 발생시 재시도를 통한 복구 작업 진행
        return RetryableCreateMember(request);
    }

    // 1-2. 회원가입 처리, 서버 내부 예외 발생시 재시도를 통한 복구 작업 진행
    @Transactional
    @Retryable(
            value = {RuntimeException.class},
            maxAttempts = MAX_RETRY,
            backoff = @Backoff(delay = RETRY_DELAY)
    )
    public Member RetryableCreateMember(MemberRegisterRequest request) {
        // 비밀번호 암호화
        encodePassword(request);
        // 회원 엔티티 저장
        Member savedMember = saveMember(request);
        // 회원 약관 이력 엔티티 저장
        saveMemberTermsHistory(request, savedMember);
        // 회원 초기 상태 엔티티 저장
        saveInitMemberState(savedMember);
        return savedMember;
    }

    // 1-3. 재시도 복구 실패시 RetryFailedException 예외 발생
    @Recover
    public Member recover(RuntimeException e) {
        throw new RetryFailedException();
    }

    // 중복된 에메일 확인
    private void checkDuplicatedEmail(String email) {
        Member foundMember = memberRepository.findByEmail(email);
        if (foundMember != null) {
            throw new MemberDuplicatedEmailException();
        }

    }

    // 중복된 아이디 확인
    private void checkDuplicatedId(String id) {
        Member foundMember = memberRepository.findById(id);
        if (foundMember != null) {
            throw new MemberDuplicatedIdException();
        }
    }

    // 비밀번호 암호화
    private void encodePassword(MemberRegisterRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        request.setPassword(encodedPassword);
    }

    // 회원 엔티티 저장
    private Member saveMember(MemberRegisterRequest request) {
        Member member = request.createMemberEntity();
        Member savedMember = memberRepository.save(member);
        return savedMember;
    }

    // 회원 약관 이력 저장
    private MemberTermsHistory saveMemberTermsHistory(MemberRegisterRequest request, Member member) {
        MemberTermsHistory memberTermsHistory = request.createMemberTermsHistoryEntity(member);
        MemberTermsHistory savedMemberTermsHistory = memberTermsHistoryRepository.save(memberTermsHistory);
        return savedMemberTermsHistory;
    }

    // 회원 상태 업데이트
    private MemberState saveInitMemberState(Member member) {
        MemberState userState = new MemberState();
        MemberCategory memberInitState = findMemberStateCategory("신규등록");
        userState.setMember(member);
        userState.setCateCode(memberInitState);
        MemberState savedMemberState = memberStateRepository.save(userState);
        return savedMemberState;
    }

    // 회원 상태 카테고리 조회
    private MemberCategory findMemberStateCategory(String cateName) {
        MemberCategory foundMemberCategory = memberCategoryRepository.findByName(cateName);
        return foundMemberCategory;
    }


    // 2. 로그인

    // 3. 회원정보 조회

    // 4. 회원정보 수정
}
