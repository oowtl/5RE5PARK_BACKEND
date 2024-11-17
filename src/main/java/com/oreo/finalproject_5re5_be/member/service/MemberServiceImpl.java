package com.oreo.finalproject_5re5_be.member.service;

import com.oreo.finalproject_5re5_be.member.dto.request.MemberRegisterRequest;
import com.oreo.finalproject_5re5_be.member.dto.response.MemberReadResponse;
import com.oreo.finalproject_5re5_be.member.entity.Code;
import com.oreo.finalproject_5re5_be.member.entity.Member;
import com.oreo.finalproject_5re5_be.member.entity.MemberCategory;
import com.oreo.finalproject_5re5_be.member.entity.MemberState;
import com.oreo.finalproject_5re5_be.member.entity.MemberTerms;
import com.oreo.finalproject_5re5_be.member.entity.MemberTermsHistory;
import com.oreo.finalproject_5re5_be.member.exception.CodeNotFoundException;
import com.oreo.finalproject_5re5_be.member.exception.MemberDuplicatedEmailException;
import com.oreo.finalproject_5re5_be.member.exception.MemberDuplicatedIdException;
import com.oreo.finalproject_5re5_be.member.exception.MemberMandatoryTermNotAgreedException;
import com.oreo.finalproject_5re5_be.member.exception.MemberNotFoundException;
import com.oreo.finalproject_5re5_be.member.exception.MemberWrongCountTermCondition;
import com.oreo.finalproject_5re5_be.member.exception.RetryFailedException;
import com.oreo.finalproject_5re5_be.member.repository.CodeRepository;
import com.oreo.finalproject_5re5_be.member.repository.MemberCategoryRepository;
import com.oreo.finalproject_5re5_be.member.repository.MemberConnectionHistoryRepository;
import com.oreo.finalproject_5re5_be.member.repository.MemberRepository;
import com.oreo.finalproject_5re5_be.member.repository.MemberStateRepository;
import com.oreo.finalproject_5re5_be.member.repository.MemberTermsHistoryRepository;
import com.oreo.finalproject_5re5_be.member.repository.MemberTermsRepository;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberServiceImpl implements UserDetailsService {

    // 서비스 이름
    @Value("${SERVICE_NAME}")
    private String SERVICE_NAME;

    // 이메일 전송자 이메일 주소
    @Value("${EMAIL_USERNAME}")
    private String EMAIL_USERNAME;

    // 이메일 제목
    @Value("${EMAIL_TITLE}")
    private String EMAIL_TITLE;

    // 이메일 내용 템플릿
    @Value("${EMAIL_CONTENT_TEMPLATE}")
    private String EMAIL_CONTENT_TEMPLATE;

    // 재시도 복구 설정값 -> DB로부터 알수없는 에러가 발생할시 재시도 설정 규칙에 따라 재시도를 통해 복구 작업을 처리한다.
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
    private final JavaMailSender mailSender;
    private final CodeRepository codeRepository;

    public MemberServiceImpl(MemberConnectionHistoryRepository memberConnectionHistoryRepository, MemberRepository memberRepository, MemberStateRepository memberStateRepository,
                             MemberTermsHistoryRepository memberTermsHistoryRepository, MemberTermsRepository memberTermsRepository, PasswordEncoder passwordEncoder,
            MemberCategoryRepository memberCategoryRepository, JavaMailSender mailSender, CodeRepository codeRepository) {
        this.memberConnectionHistoryRepository = memberConnectionHistoryRepository;
        this.memberRepository = memberRepository;
        this.memberStateRepository = memberStateRepository;
        this.memberTermsHistoryRepository = memberTermsHistoryRepository;
        this.memberTermsRepository = memberTermsRepository;
        this.passwordEncoder = passwordEncoder;
        this.memberCategoryRepository = memberCategoryRepository;
        this.mailSender = mailSender;
        this.codeRepository = codeRepository;
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
        return retryableCreateMember(request);
    }

    // 1-2. 회원가입 처리, 서버 내부 예외 발생시 재시도를 통한 복구 작업 진행
    @Transactional
    @Retryable(
            value = {RuntimeException.class},
            maxAttempts = MAX_RETRY,
            backoff = @Backoff(delay = RETRY_DELAY)
    )
    public Member retryableCreateMember(MemberRegisterRequest request) {
        // 비밀번호 암호화
        encodePassword(request);
        // 회원 엔티티 저장
        Member savedMember = saveMember(request);
//        // 회원 약관 이력 엔티티 저장
        saveMemberTermsHistory(request, savedMember);
//        // 회원 초기 상태 엔티티 저장
        saveMemberState(savedMember, "MBS001"); // 초기 상태 코드 : MBS001 - 신규 등록
        return savedMember;
    }

    // 1-3. 재시도 복구 실패시 RetryFailedException 예외 발생
    @Recover
    public Member recover(RuntimeException e) {
        throw new RetryFailedException();
    }

    // 중복된 에메일 확인
    private void checkDuplicatedEmail(String email) {
        // 이메일로 회원 조회
        Member foundMember = memberRepository.findByEmail(email);
        // 조회된 회원이 있으면 중복된 이메일로 판단하고 비즈니스 예외 반환
        if (foundMember != null) {
            throw new MemberDuplicatedEmailException();
        }

    }

    // 중복된 아이디 확인
    private void checkDuplicatedId(String id) {
        // 아이디로 회원 조회
        Member foundMember = memberRepository.findById(id);
        // 조회된 회원이 있으면 중복된 아이디로 판단하고 비즈니스 예외 반환
        if (foundMember != null) {
            throw new MemberDuplicatedIdException();
        }
    }

    // 비밀번호 암호화
    private void encodePassword(MemberRegisterRequest request) {
        // 비밀번호 암호화 처리
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        // 암호화된 비밀번호로 변경
        request.setPassword(encodedPassword);
    }

    // 회원 엔티티 저장
    private Member saveMember(MemberRegisterRequest request) {
        // 입력 데이터로부터 회원 엔티티 생성
        Member member = request.createMemberEntity();
        // 회원 엔티티 저장
        Member savedMember = memberRepository.save(member);
        return savedMember;
    }

    // 회원 약관 이력 저장
    private MemberTermsHistory saveMemberTermsHistory(MemberRegisterRequest request, Member member) {
        // 회원 약관 조회
        MemberTerms foundTerms = memberTermsRepository.findMemberTermsByTermsSeq(request.getTermSeq());
        if (foundTerms == null) {
            throw new MemberNotFoundException();
        }

        // 입력 데이터로부터 회원 약관 이력 엔티티 생성
        MemberTermsHistory memberTermsHistory = request.createMemberTermsHistoryEntity(member, foundTerms);

        // 회원 약관 이력 엔티티 저장
        MemberTermsHistory savedMemberTermsHistory = memberTermsHistoryRepository.save(memberTermsHistory);

        return savedMemberTermsHistory;
    }

    // 회원 상태 업데이트
    private MemberState saveMemberState(Member member, String code) {
        // 신규 등록 회원 상태 조회
        Code foundCode = codeRepository.findCodeByCode(code);
        if (foundCode == null) {
            throw new CodeNotFoundException();
        }

        // 신규 등록 회원 상태 생성
        MemberState memberState = MemberState.of(member, foundCode);
        // 회원 상태 엔티티 저장

        MemberState savedMemberState = memberStateRepository.save(memberState);
        return savedMemberState;
    }



    // 2. 로그인 : 아이디로 회원 조회하여 UserDetails 반환, 스프링 시큐리티 내부적으로 호출하여 로그인 처리
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {;
        // 아이디로 회원 조회
        Member foundMember = memberRepository.findById(username);

        // 만약 해당 아이디로 조회된 회원이 없는 경우 예외 발생
        if (foundMember == null) {
            throw new UsernameNotFoundException("해당 아이디로 조회된 회원이 없습니다.");
        }

        // 조회된 회원 정보를 바탕으로 UserDetails 반환
        return User.withUsername(foundMember.getId())
                .password(foundMember.getPassword())
                .build();
    }

    // 3. 비회원 이메일 인증번호 전송 : 회원 가입시에 이메일 인증번호 전송
    public String sendVerificationCode(String email) {
        // 인증번호 생성
        String verificationCode = createVerificationCode();
        // 이메일 내용 작성
        String emailContent = createEmailContent(verificationCode);
        // 이메일 전송
        sendEmail(email, emailContent);
        // 인증번호 반환
        return verificationCode;
    }

    // 인증번호 생성
    private String createVerificationCode() {
        StringBuilder sb = new StringBuilder();

        // 6자리 랜덤 숫자 코드 생성
        for (int i=0; i<6; i++) {
            int random = (int) (Math.random() * 10);
            sb.append(random);
        }
        // 문자열로 변환하여 반환
        return sb.toString();
    }

    // 이메일 내용 작성
    private String createEmailContent(String verificationCode) {
        String emailContent = String.format(EMAIL_CONTENT_TEMPLATE, SERVICE_NAME, verificationCode);
        return emailContent;
    }

    // 이메일 전송
    private void sendEmail(String email, String emailContent) {
        try {
            // 메일 내용 넣을 객체와, 이를 도와주는 Helper 객체
            MimeMessage mail = mailSender.createMimeMessage();
            MimeMessageHelper mailHelper = new MimeMessageHelper(mail, "UTF-8");

            // 메일 내용 및 설정값 세팅
            mailHelper.setFrom(EMAIL_USERNAME); // 보내는 사람
            mailHelper.setTo(email); // 받는 사람
            mailHelper.setSubject(EMAIL_TITLE); // 제목
            mailHelper.setText(emailContent); // 내용

            // 이메일 전송
            mailSender.send(mail);
        } catch (Exception e) {
            // 이메일 전송 실패시 예외 발생
            throw new MailSendException("이메일 전송에 실패했습니다");
        }

    }

    // 4. 회원정보 상세 조회
    // - 기본적인 회원 정보만 조회
    public MemberReadResponse read(String memberId) {
        // 회원 아이디로 조회
        Member foundMember = memberRepository.findById(memberId);
        // 해당 아이디로 회원을 찾지 못한 경우 예외 발생
        if (foundMember == null) {
            throw new MemberNotFoundException();
        }
        // 조회된 회원 정보를 바탕으로 응답 객체 생성
        return MemberReadResponse.of(foundMember.getId(), foundMember.getEmail(),
                                     foundMember.getName(), foundMember.getNormAddr(),
                                     foundMember.getDetailAddr());
    }

    // - 회원에 대한 상세 정보 모두 조회


    // - 기본적인 회원 정보와 프로젝트 이력 조회


    // 5. 회원정보 수정

    // 6. 회원 탈퇴(유해기간 30일 설정, 그 이후에 삭제)

}
