package com.oreo.finalproject_5re5_be.member.service;

import com.oreo.finalproject_5re5_be.code.entity.Code;
import com.oreo.finalproject_5re5_be.code.exeption.CodeNotFoundException;
import com.oreo.finalproject_5re5_be.code.repository.CodeRepository;
import com.oreo.finalproject_5re5_be.member.dto.CustomUserDetails;
import com.oreo.finalproject_5re5_be.member.dto.request.MemberChangePasswordRequest;
import com.oreo.finalproject_5re5_be.member.dto.request.MemberRegisterRequest;
import com.oreo.finalproject_5re5_be.member.dto.request.MemberRemoveRequest;
import com.oreo.finalproject_5re5_be.member.dto.request.MemberUpdateRequest;
import com.oreo.finalproject_5re5_be.member.dto.response.MemberReadResponse;
import com.oreo.finalproject_5re5_be.member.entity.Member;
import com.oreo.finalproject_5re5_be.member.entity.MemberChangeHistory;
import com.oreo.finalproject_5re5_be.member.entity.MemberConnectionHistory;
import com.oreo.finalproject_5re5_be.member.entity.MemberDelete;
import com.oreo.finalproject_5re5_be.member.entity.MemberState;
import com.oreo.finalproject_5re5_be.member.entity.MemberTerms;
import com.oreo.finalproject_5re5_be.member.entity.MemberTermsHistory;
import com.oreo.finalproject_5re5_be.member.exception.DeletedMemberException;
import com.oreo.finalproject_5re5_be.member.exception.HumanMemberException;
import com.oreo.finalproject_5re5_be.member.exception.MemberDuplicatedEmailException;
import com.oreo.finalproject_5re5_be.member.exception.MemberDuplicatedIdException;
import com.oreo.finalproject_5re5_be.member.exception.MemberDuplicatedPasswordException;
import com.oreo.finalproject_5re5_be.member.exception.MemberMandatoryTermNotAgreedException;
import com.oreo.finalproject_5re5_be.member.exception.MemberNotFoundException;
import com.oreo.finalproject_5re5_be.member.exception.MemberTermsNotFoundException;
import com.oreo.finalproject_5re5_be.member.exception.MemberWrongCountTermCondition;
import com.oreo.finalproject_5re5_be.member.exception.RestrictedMemberException;
import com.oreo.finalproject_5re5_be.member.exception.RetryFailedException;
import com.oreo.finalproject_5re5_be.member.repository.MemberChangeHistoryRepository;
import com.oreo.finalproject_5re5_be.member.repository.MemberConnectionHistoryRepository;
import com.oreo.finalproject_5re5_be.member.repository.MemberDeleteRepository;
import com.oreo.finalproject_5re5_be.member.repository.MemberRepository;
import com.oreo.finalproject_5re5_be.member.repository.MemberStateRepository;
import com.oreo.finalproject_5re5_be.member.repository.MemberTermsHistoryRepository;
import com.oreo.finalproject_5re5_be.member.repository.MemberTermsRepository;
import jakarta.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
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
    private final JavaMailSender mailSender;
    private final CodeRepository codeRepository;
    private final MemberChangeHistoryRepository memberChangeHistoryRepository;
    private final MemberDeleteRepository memberDeleteRepository;

    public MemberServiceImpl(
            MemberConnectionHistoryRepository memberConnectionHistoryRepository,
            MemberRepository memberRepository,
            MemberStateRepository memberStateRepository,
            MemberTermsHistoryRepository memberTermsHistoryRepository,
            MemberTermsRepository memberTermsRepository,
            PasswordEncoder passwordEncoder,
            JavaMailSender mailSender,
            CodeRepository codeRepository,
            MemberDeleteRepository memberDeleteRepository,
            MemberChangeHistoryRepository memberChangeHistoryRepository) {
        this.memberConnectionHistoryRepository = memberConnectionHistoryRepository;
        this.memberRepository = memberRepository;
        this.memberStateRepository = memberStateRepository;
        this.memberTermsHistoryRepository = memberTermsHistoryRepository;
        this.memberTermsRepository = memberTermsRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
        this.codeRepository = codeRepository;
        this.memberDeleteRepository = memberDeleteRepository;
        this.memberChangeHistoryRepository = memberChangeHistoryRepository;
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
        } catch (MemberDuplicatedEmailException
                | MemberDuplicatedIdException
                | MemberMandatoryTermNotAgreedException
                | MemberWrongCountTermCondition e) {
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
            backoff = @Backoff(delay = RETRY_DELAY))
    public Member retryableCreateMember(MemberRegisterRequest request) {
        // 비밀번호 암호화
        encodePassword(request);
        // 회원 엔티티 저장
        Member savedMember = saveMember(request);
        // 회원 약관 이력 엔티티 저장
        saveMemberTermsHistory(request, savedMember);
        // 회원 초기 상태 엔티티 저장
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
        MemberTerms foundTerms = memberTermsRepository.findMemberTermsByTermCode(request.getTermCode());
        if (foundTerms == null) {
            throw new MemberTermsNotFoundException();
        }

        // 입력 데이터로부터 회원 약관 이력 엔티티 생성
        MemberTermsHistory memberTermsHistory =
                request.createMemberTermsHistoryEntity(member, foundTerms);

        // 회원 약관 이력 엔티티 저장
        MemberTermsHistory savedMemberTermsHistory =
                memberTermsHistoryRepository.save(memberTermsHistory);

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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ;
        // 아이디로 회원 조회
        Member foundMember = memberRepository.findById(username);

        // 만약 해당 아이디로 조회된 회원이 없는 경우 예외 발생
        if (foundMember == null) {
            throw new UsernameNotFoundException("해당 아이디로 조회된 회원이 없습니다.");
        }

        CustomUserDetails userDetails = new CustomUserDetails(foundMember);
        // 조회된 회원 정보를 바탕으로 UserDetails 반환
        return userDetails;
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
        for (int i = 0; i < 6; i++) {
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
        return MemberReadResponse.of(foundMember);
    }

    public MemberReadResponse read(Long memberSeq) {
        // 회원 아이디로 조회
        Member foundMember = memberRepository.findBySeq(memberSeq);
        // 해당 아이디로 회원을 찾지 못한 경우 예외 발생
        if (foundMember == null) {
            throw new MemberNotFoundException();
        }

        // 조회된 회원의 상태를 확인함
        // - 휴먼회원(MBS003), 제재회원(MBS007), 탈퇴회원(MBS004)일 경우 거르기
        MemberState memberState =
                memberStateRepository.findLatestHistoryByMemberSeq(foundMember.getSeq());

        // 휴먼 회원인지 확인
        if ("MBS003".equals(memberState.getCode().getCode())) {
            // 예외 발생
            throw new HumanMemberException();
        }

        // 제재 회원인지 확인
        if ("MBS007".equals(memberState.getCode().getCode())) {
            // 예외 방생
            throw new RestrictedMemberException();
        }

        // 탈퇴 회원인지 확인
        if ("MBS004".equals(memberState.getCode().getCode())) {
            // 예외 발생
            throw new DeletedMemberException();
        }

        // 조회된 회원 정보를 바탕으로 응답 객체 생성
        return MemberReadResponse.of(foundMember);
    }

    // 5. 회원정보 수정
    @Transactional
    public void update(Long memberSeq, MemberUpdateRequest request) {
        // 5-1. 전달받은 데이터가 유효한지 검증한다
        // 5-2. 아이디, 이메일을 수정할 경우, 다른 회원과 중복된 아이디, 이메일이 있는지 확인한다
        boolean isDuplicatedId =
                memberRepository.existsByIdNotContainingMemberSeq(memberSeq, request.getId());
        if (isDuplicatedId) {
            throw new MemberDuplicatedIdException();
        }

        boolean isDuplicatedEmail =
                memberRepository.existsByEmailNotContainingMemberSeq(memberSeq, request.getEmail());
        if (isDuplicatedEmail) {
            throw new MemberDuplicatedEmailException();
        }

        // 5-3. 회원 시퀀스로 엔티티를 조회한다
        Member foundMember =
                memberRepository.findById(memberSeq).orElseThrow(MemberNotFoundException::new);

        // 5-4. 어느 부분이 변경되었는지 파악하고 이력으로 기록한다
        List<MemberChangeHistory> changeHistories = new ArrayList<>();

        // 현재 시간과 최대 시간 세팅
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.MAX;

        // DATETIME 형식으로 변환하기 위한 포맷터 생성
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 포맷팅된 문자열로 변환
        String formattedNow = now.format(formatter);
        String formattedEnd = end.format(formatter);

        boolean isChangedId = false;
        boolean isChangedEmail = false;
        boolean isChangedPassword = false;
        boolean isChangedName = false;
        boolean isChangedNormAddr = false;

        if (!foundMember.getId().equals(request.getId())) {
            isChangedId = true;
        }

        if (!foundMember.getEmail().equals(request.getEmail())) {
            isChangedEmail = true;
        }

        if (!foundMember.getPassword().equals(request.getPassword())) {
            isChangedPassword = true;
        }

        if (!foundMember.getName().equals(request.getName())) {
            isChangedName = true;
        }

        if (!foundMember.getNormAddr().equals(request.getNormAddr())) {
            isChangedNormAddr = true;
        }

        // 5-5. 변경된 부분을 이력으로 기록하기 위해 코드 테이블 값을 조회한다
        if (isChangedId) {
            Code memberIdFiledCode = codeRepository.findCodeByCode("MF001"); // 회원 아이디 필드 코드

            // 가장 최근 이력 시간 업데이트
            memberChangeHistoryRepository
                    .findLatestHistoryByIdAndCode(memberSeq, memberIdFiledCode.getCode())
                    .ifPresent(history -> history.setEndDate(formattedNow));

            MemberChangeHistory memberIdChangeHistory =
                    MemberChangeHistory.builder()
                            .member(foundMember)
                            .chngFieldCode(memberIdFiledCode)
                            .befVal(foundMember.getId())
                            .aftVal(request.getId())
                            .applDate(formattedNow)
                            .endDate(formattedEnd)
                            .build();
            changeHistories.add(memberIdChangeHistory);
        }

        if (isChangedEmail) {
            Code emailFiledCode = codeRepository.findCodeByCode("MF002"); // 회원 이메일 필드 코드
            MemberChangeHistory emailChangeHistory =
                    MemberChangeHistory.builder()
                            .member(foundMember)
                            .chngFieldCode(emailFiledCode)
                            .befVal(foundMember.getEmail())
                            .aftVal(request.getEmail())
                            .applDate(formattedNow)
                            .endDate(formattedEnd)
                            .build();
            // 가장 최근 이력 시간 업데이트
            memberChangeHistoryRepository
                    .findLatestHistoryByIdAndCode(memberSeq, emailFiledCode.getCode())
                    .ifPresent(history -> history.setEndDate(formattedNow));

            changeHistories.add(emailChangeHistory);
        }

        if (isChangedPassword) {
            Code passwordFiledCode = codeRepository.findCodeByCode("MF003"); // 회원 비밀번호 필드 코드
            String encodedPassword = passwordEncoder.encode(foundMember.getPassword());

            // 가장 최근 이력 시간 업데이트
            memberChangeHistoryRepository
                    .findLatestHistoryByIdAndCode(memberSeq, passwordFiledCode.getCode())
                    .ifPresent(history -> history.setEndDate(formattedNow));

            MemberChangeHistory passwordChangeHistory =
                    MemberChangeHistory.builder()
                            .member(foundMember)
                            .chngFieldCode(passwordFiledCode)
                            .befVal(foundMember.getPassword())
                            .aftVal(encodedPassword)
                            .applDate(formattedNow)
                            .endDate(formattedEnd)
                            .build();
            changeHistories.add(passwordChangeHistory);
        }

        if (isChangedName) {
            Code nameFiledCode = codeRepository.findCodeByCode("MF004"); // 회원 이름 필드 코드
            MemberChangeHistory nameChangeHistory =
                    MemberChangeHistory.builder()
                            .member(foundMember)
                            .chngFieldCode(nameFiledCode)
                            .befVal(foundMember.getName())
                            .aftVal(request.getName())
                            .applDate(formattedNow)
                            .endDate(formattedEnd)
                            .build();

            // 가장 최근 이력 시간 업데이트
            memberChangeHistoryRepository
                    .findLatestHistoryByIdAndCode(memberSeq, nameFiledCode.getCode())
                    .ifPresent(history -> history.setEndDate(formattedNow));

            changeHistories.add(nameChangeHistory);
        }

        if (isChangedNormAddr) {
            Code normAddrFiledCode = codeRepository.findCodeByCode("MF005"); // 회원 주소 필드 코드
            MemberChangeHistory normAddrChangeHistory =
                    MemberChangeHistory.builder()
                            .member(foundMember)
                            .chngFieldCode(normAddrFiledCode)
                            .befVal(foundMember.getNormAddr())
                            .aftVal(request.getNormAddr())
                            .applDate(formattedNow)
                            .endDate(formattedEnd)
                            .build();

            // 가장 최근 이력 시간 업데이트
            memberChangeHistoryRepository
                    .findLatestHistoryByIdAndCode(memberSeq, normAddrFiledCode.getCode())
                    .ifPresent(history -> history.setEndDate(formattedNow));

            changeHistories.add(normAddrChangeHistory);
        }

        // 5-6. 해당 엔티티를 수정한다
        foundMember.update(request);

        // 5-7. 회원 상태를 변경한다
        saveMemberState(foundMember, "MBS002"); // 변경 상태 코드 : MBS002 - 정보 변경

        // 5-8. 회원 변경 이력을 기록한다
        memberChangeHistoryRepository.saveAll(changeHistories);
    }

    // 6. 회원 탈퇴(유해기간 30일 설정, 그 이후에 삭제)
    // (1) 삭제 처리가 요청된 회원
    // - 해당 회원을 비활성 회원으로 업데이트한다
    // - 30일 유해 기간을 설정한다(현재 시간 등록)
    // - 회원 삭제 유형 코드와 사유를 기록한다
    public void remove(Long memberSeq, MemberRemoveRequest request) {
        // - 해당 회원을 조회한다
        Member foundMember =
                memberRepository.findById(memberSeq).orElseThrow(MemberNotFoundException::new);

        // - 해당 회원을 비활성 회원으로 업데이트한다
        Code removeMemberCode = codeRepository.findCodeByCode("MBS003"); // 휴먼 회원으로 등록
        MemberState memberState = MemberState.of(foundMember, removeMemberCode);
        memberStateRepository.save(memberState);

        // - 30일 유해 기간을 설정한다(현재 시간 등록)
        // - 회원 삭제 유형 코드와 사유를 기록한다
        Code removeReaseonCode = codeRepository.findCodeByCode(request.getCode());
        MemberDelete memberDelete = MemberDelete.of(memberSeq, request, removeReaseonCode);
        memberDeleteRepository.save(memberDelete);
    }

    // (2) 매일 새벽 4:00 마다 삭제 회원 중 유해기간 30일이 지난 회원들을 삭제(스프링 스케쥴러 적용)
    // - applDate가 현재와 30일 차이 나는 회원을 대상으로 한다
    // - 회원을 삭제한다
    // - 회원의 상태를 삭제한다
    // - 회원의 약관 이력을 삭제한다
    // - 회원의 접속 이력을 삭제한다
    // - 회원의 변경 이력을 삭제한다
    // - 회원 삭제 테이블에 처리 완료 체크표시 넣기
    @Scheduled(cron = "0 0 4 * * *", zone = "Asia/Seoul")
    public void checkRemovedMember() {
        // 현재 시간 조회
        LocalDateTime now = LocalDateTime.now();

        // - applDate가 현재와 30일 차이 나는 회원을 대상으로 한다
        List<MemberDelete> foundAllDeletedMembers = memberDeleteRepository.findAll();
        List<MemberDelete> candidates =
                foundAllDeletedMembers.stream()
                        .filter(
                                m -> {
                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                                    LocalDateTime applDate = LocalDateTime.parse(m.getApplDate(), formatter);
                                    return applDate.isBefore(now.minusMonths(1))
                                            || applDate.isEqual(now.minusMonths(1));
                                })
                        .toList();

        // 삭제 대상 회원을 반복해서 삭제 처리한다
        for (MemberDelete candidate : candidates) {
            // - 회원을 삭제한다
            Member foundMember =
                    memberRepository
                            .findById(candidate.getMemberSeq())
                            .orElseThrow(MemberNotFoundException::new);

            memberRepository.delete(foundMember);

            // - 회원의 상태를 삭제한다
            List<MemberState> foundMemberStates =
                    memberStateRepository.findAllByMemberSeq(candidate.getMemberSeq());
            memberStateRepository.deleteAll(foundMemberStates);

            // - 회원의 약관 이력을 삭제한다
            List<MemberTermsHistory> foundMemberTermsHistories =
                    memberTermsHistoryRepository.findByMemberSeq(candidate.getMemberSeq());
            memberTermsHistoryRepository.deleteAll(foundMemberTermsHistories);

            // - 회원의 접속 이력을 삭제한다
            List<MemberConnectionHistory> foundMemberConnectionsHistories =
                    memberConnectionHistoryRepository.findMemberConnectionHistoriesByMemberSeq(
                            candidate.getMemberSeq());
            memberConnectionHistoryRepository.deleteAll(foundMemberConnectionsHistories);

            // - 회원의 변경 이력을 삭제한다
            // - 회원 삭제 테이블에 처리 완료 체크표시 넣기
            List<MemberChangeHistory> foundMemberChangeHistories =
                    memberChangeHistoryRepository.findMemberChangeHistoriesByMemberSeq(
                            candidate.getMemberSeq());
            memberChangeHistoryRepository.deleteAll(foundMemberChangeHistories);

            // 회원 삭제 데이터 업데이트
            Code memberDeleteCode = codeRepository.findCodeByCode("MBS004"); // 탈퇴 회원으로 등록
            candidate.setCode(memberDeleteCode);
            candidate.setChkUse('Y');
        }
    }

    public void updatePassword(Long memberSeq, MemberChangePasswordRequest request) {
        // 회원 조회
        Member foundMember = memberRepository.findBySeq(memberSeq);
        if (foundMember == null) {
            throw new MemberNotFoundException();
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        String originPassword = foundMember.getPassword();

        if (encodedPassword.equals(originPassword)) {
            throw new MemberDuplicatedPasswordException();
        }

        // 비밀번호 변경
        foundMember.setPassword(encodedPassword);

        // 변경 이력 기록
        Code passwordFiledCode = codeRepository.findCodeByCode("MF003"); // 회원 비밀번호 필드 코드

        // 현재 시간과 최대 시간 세팅
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.MAX;

        // DATETIME 형식으로 변환하기 위한 포맷터 생성
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 포맷팅된 문자열로 변환
        String formattedNow = now.format(formatter);
        String formattedEnd = end.format(formatter);

        // 가장 최근 이력 시간 업데이트
        memberChangeHistoryRepository
                .findLatestHistoryByIdAndCode(memberSeq, passwordFiledCode.getCode())
                .ifPresent(history -> history.setEndDate(formattedNow));

        MemberChangeHistory passwordChangeHistory =
                MemberChangeHistory.builder()
                        .member(foundMember)
                        .chngFieldCode(passwordFiledCode)
                        .befVal(foundMember.getPassword())
                        .aftVal(encodedPassword)
                        .applDate(formattedNow)
                        .endDate(formattedEnd)
                        .build();

        // 변경 이력 저장
        memberChangeHistoryRepository.save(passwordChangeHistory);
    }

    public String findId(String email) {
        // 이메일로 회원 조회
        Member foundMember = memberRepository.findByEmail(email);
        // 조회된 회원이 없으면 예외 발생
        if (foundMember == null) {
            throw new MemberNotFoundException();
        }
        // 조회된 회원의 아이디 반환
        return foundMember.getId();
    }
}
