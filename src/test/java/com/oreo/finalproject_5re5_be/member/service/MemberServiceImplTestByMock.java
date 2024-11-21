package com.oreo.finalproject_5re5_be.member.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.oreo.finalproject_5re5_be.member.dto.request.MemberRegisterRequest;
import com.oreo.finalproject_5re5_be.member.dto.request.MemberRemoveRequest;
import com.oreo.finalproject_5re5_be.member.dto.request.MemberTermCheckOrNotRequest;
import com.oreo.finalproject_5re5_be.member.dto.request.MemberUpdateRequest;
import com.oreo.finalproject_5re5_be.member.dto.response.MemberReadResponse;
import com.oreo.finalproject_5re5_be.code.entity.Code;
import com.oreo.finalproject_5re5_be.member.entity.Member;
import com.oreo.finalproject_5re5_be.member.entity.MemberChangeHistory;
import com.oreo.finalproject_5re5_be.member.entity.MemberConnectionHistory;
import com.oreo.finalproject_5re5_be.member.entity.MemberDelete;
import com.oreo.finalproject_5re5_be.member.entity.MemberState;
import com.oreo.finalproject_5re5_be.member.entity.MemberTermsHistory;
import com.oreo.finalproject_5re5_be.member.exception.MemberDuplicatedEmailException;
import com.oreo.finalproject_5re5_be.member.exception.MemberDuplicatedIdException;
import com.oreo.finalproject_5re5_be.member.exception.MemberNotFoundException;
import com.oreo.finalproject_5re5_be.member.exception.MemberWrongCountTermCondition;
import com.oreo.finalproject_5re5_be.code.repository.CodeRepository;
import com.oreo.finalproject_5re5_be.member.repository.MemberChangeHistoryRepository;
import com.oreo.finalproject_5re5_be.member.repository.MemberConnectionHistoryRepository;
import com.oreo.finalproject_5re5_be.member.repository.MemberDeleteRepository;
import com.oreo.finalproject_5re5_be.member.repository.MemberRepository;
import com.oreo.finalproject_5re5_be.member.repository.MemberStateRepository;
import com.oreo.finalproject_5re5_be.member.repository.MemberTermsHistoryRepository;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTestByMock {

    @InjectMocks
    private MemberServiceImpl memberService;

    @Mock
    private MemberRepository memberRepository;


    @Mock
    private CodeRepository codeRepository;

    @Mock
    private MemberStateRepository memberStateRepository;

    @Mock
    private MemberChangeHistoryRepository memberChangeHistoryRepository;

    @Mock
    private MemberDeleteRepository memberDeleteRepository;

    @Mock
    private MemberTermsHistoryRepository memberTermsHistoryRepository;

    @Mock
    private MemberConnectionHistoryRepository memberConnectionHistoryRepository;

    private User user;

    @BeforeEach
    void setUp() {
        assertNotNull(memberService);
        user = new User("qwefghnm1212", "dqwesf1212@!", Collections.emptyList());
    }


    @DisplayName("회원가입 - 중복된 이메일")
    @Test
    public void 중복된_이메일_예외_발생() {
        // 회원 약관 정보 생성
        List<MemberTermCheckOrNotRequest> memberTermCheckOrNotRequests = retryableCreateMemberMemberTerms();
        // 회원 입력 데이터 생성
        MemberRegisterRequest request = retryableCreateMemberMemberRegisterRequest(
                memberTermCheckOrNotRequests);
        // 회원 엔티티 생성
        Member member = request.createMemberEntity();
        // 이메일 중복시 해당 회원 엔티티 반환하게 세팅
        when(memberRepository.findByEmail(request.getEmail())).thenReturn(member);
        // 실행 및 예외 발생 여부 파악
        assertThrows(MemberDuplicatedEmailException.class, () -> memberService.create(request));
    }

    @DisplayName("회원가입 - 중복된 아이디")
    @Test
    public void 중복된_아이디_예외_발생() {
        // 회원 약관 정보 생성
        List<MemberTermCheckOrNotRequest> memberTermCheckOrNotRequests = retryableCreateMemberMemberTerms();
        // 회원 입력 데이터 생성
        MemberRegisterRequest request = retryableCreateMemberMemberRegisterRequest(
                memberTermCheckOrNotRequests);
        // 회원 엔티티 생성
        Member member = request.createMemberEntity();
        // 아이디 중복시 해당 회원 엔티티 반환하게 세팅
        when(memberRepository.findById(request.getId())).thenReturn(member);
        // 실행 및 예외 발생 여부 파악
        assertThrows(MemberDuplicatedIdException.class, () -> memberService.create(request));
    }

    @DisplayName("회원가입 - 필수 약관 미동의")
    @Test
    public void 필수_약관_미동의_예외_발생() {
        // 회원 약관 정보 생성
        List<MemberTermCheckOrNotRequest> memberTermCheckOrNotRequests = retryableCreateMemberMemberTerms();
        // 필수 약관 미동의로 변경
        memberTermCheckOrNotRequests.get(0).setAgreed('N');
        // 회원 입력 데이터 생성
        MemberRegisterRequest request = retryableCreateMemberMemberRegisterRequest(
                memberTermCheckOrNotRequests);
        // 회원 엔티티 생성
        Member member = request.createMemberEntity();
        // 실행 및 예외 발생 여부 파악
//        assertThrows(MemberMandatoryTermNotAgreedException.class, () -> memberService.create(request));
    }

    @DisplayName("회원가입 - 5개의 약관 항목 보다 많은 경우")
    @Test
    public void 약관_항목_초과_예외_발생() {
        // 회원 약관 정보 생성
        List<MemberTermCheckOrNotRequest> memberTermCheckOrNotRequests = retryableCreateMemberMemberTerms();
        // 회원 약관 추가 6개로 세팅
        memberTermCheckOrNotRequests.add(MemberTermCheckOrNotRequest.builder()
                        .termCondCode(6L)
                        .agreed('Y')
                        .build());
        // 회원 입력 데이터 생성
        MemberRegisterRequest request = retryableCreateMemberMemberRegisterRequest(
                memberTermCheckOrNotRequests);
        // 회원 엔티티 생성
        Member member = request.createMemberEntity();
        // 실행 및 예외 발생 여부 파악
        assertThrows(MemberWrongCountTermCondition.class, () -> memberService.create(request));
    }

    // 추후에 해당 부분 문제 해결 : RetryFailedException 발생함. 이거 어떻게 잡을지 고민하기
    @DisplayName("재시도 복구 로직 정상적으로 동작하는지 테스트")
    @Test
    public void 재시도_복구_동작_테스트() {

    }

    @DisplayName("스프링시큐리티에서 호출하는 loadUserByUsername() 성공 테스트 ")
    @Test
    public void 스프링시큐리티_회원_조회_성공() {
        // 회원 약관 정보 생성
        List<MemberTermCheckOrNotRequest> memberTermCheckOrNotRequests = retryableCreateMemberMemberTerms();
        // 회원 입력 데이터 생성
        MemberRegisterRequest memberRegisterRequest = retryableCreateMemberMemberRegisterRequest(
                memberTermCheckOrNotRequests);
        // 입력 데이터로부터 회원 엔티티 생성
        Member foundMember = memberRegisterRequest.createMemberEntity();

        // "qwerfde2312" 조회시 위에 생성한 엔티티 반환하게 세팅
        when(memberRepository.findById("qwerfde2312")).thenReturn(foundMember);

        // 실행 및 결과 비교
        UserDetails foundMemberDetails = memberService.loadUserByUsername("qwerfde2312");
        assertEquals(foundMemberDetails.getUsername(), foundMember.getId());
    }

    @DisplayName("스프링시큐리티에서 호출하는 loadUserByUsername() 실패 테스트 ")
    @Test
    public void 스프링시큐리티_회원_조회_실패() {
        // 회원 약관 정보 생성
        List<MemberTermCheckOrNotRequest> memberTermCheckOrNotRequests = retryableCreateMemberMemberTerms();
        // 입력 데이터로부터 회원 엔티티 생성
        MemberRegisterRequest memberRegisterRequest = retryableCreateMemberMemberRegisterRequest(
                memberTermCheckOrNotRequests);
        // 입력 데이터로부터 회원 엔티티 생성
        Member foundMember = memberRegisterRequest.createMemberEntity();
        // "qwerfde2312" 조회시 null 반환하게 세팅
        when(memberRepository.findById("qwerfde2312")).thenReturn(null);
        // 실행 및 예외 발생 여부 파악
        assertThrows(UsernameNotFoundException.class,
                () -> memberService.loadUserByUsername("qwerfde2312"));
    }

    // 추후에 해당 부분 문제 해결 : 내부적으로 이메일을 전송하는 기능이 있는 메서드는 테스트 코드를 어떻게 작성할지 고민하기
    @DisplayName("이메일 전송 성공 테스트")
    @Test
    public void 이메일_전송_성공() {

    }

    // 회원 단순 조회 테스트
    @DisplayName("회원 단순 조회 테스트")
    @Test
    public void 회원_단순_조회_성공() {
        // 회원 아이디로 호출시 목객체 반환
        Member foundMember = Member.builder()
                                   .seq(1L)
                                   .id("qwerfde2312")
                                   .email("qwedr123@gmail.com")
                                   .name("홍길동")
                                   .normAddr("서울시 강남구")
                                   .detailAddr("서초대로 59-32")
                                   .build();

        when(memberRepository.findById("qwerfde2312")).thenReturn(foundMember);

        // 회원 응답 객체 생성
        MemberReadResponse response = MemberReadResponse.of(foundMember.getId(), foundMember.getEmail(),
                                                            foundMember.getName(), foundMember.getNormAddr(),
                                                            foundMember.getDetailAddr());

        // 서비스 호출
        MemberReadResponse actualResponse = memberService.read("qwerfde2312");

        // 결과 비교
        assertEquals(response, actualResponse);
    }

    @DisplayName("회원 단순 조회 실패 테스트")
    @Test
    public void 회원_단순_조회_실패() {
        // 회원 아이디로 호출시 null 반환하게 세팅
        when(memberRepository.findById("qwerfde2312")).thenReturn(null);

        // 서비스 호출 및 예외 발생 여부 확인
        assertThrows(MemberNotFoundException.class, () -> {
            memberService.read("qwerfde2312");
        });
    }

    @DisplayName("회원 수정 처리 테스트")
    @Test
    public void 회원_수정_처리() {
        // 더미 데이터 생성
        // - 1. 유효성 검증이 된 MemberUpdateRequest와 회원 시퀀스 memberSeq
        // - 2. 회원 상태 코드 : MBS002 - 회원 상태 변경, MF001 ~ MF005, 순서대로 아이디, 이메일, 비밀번호, 이름, 주소
        // - 3. 회원 변경 이력 : 저장될 회원 변경 이력
        Long memberSeq = 1L;
        MemberUpdateRequest request = MemberUpdateRequest.builder()
                                                        .id("new dwads23123")
                                                        .password("dwadaw")
                                                        .email("eqwfqws2131@gmail.com")
                                                        .name("홍만동")
                                                        .normAddr("서울시 양천구")
                                                        .build();

        Code memberIdFeildCode = Code.builder()
                .codeSeq(1L)
                .code("MF001")
                .cateNum("M")
                .name("회원 아이디 필드")
                .chkUse("Y")
                .ord(3)
                .build();

        Code memberStateCode = Code.builder()
                .codeSeq(2L)
                .code("MBS001")
                .cateNum("M")
                .name("회원정보수정")
                .chkUse("Y")
                .ord(1)
                .build();

        Member foundMember = Member.builder()
                .id("qwerfde2312")
                .password("dwadaw")
                .email("eqwfqws2131@gmail.com")
                .name("홍만동")
                .normAddr("서울시 양천구")
                .locaAddr("서울시")
                .detailAddr("서초동 123-456")
                .passAddr("서초대로 59-32")
                .chkValid('Y')
                .build();

        MemberState memberState = MemberState.of(
                foundMember, memberStateCode
        );

        MemberState savedMemberState = MemberState.of(
                foundMember, memberStateCode
        );

        // 현재 시간과 최대 시간 세팅
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.MAX;

        // DATETIME 형식으로 변환하기 위한 포맷터 생성
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 포맷팅된 문자열로 변환
        String formattedNowTime = now.format(formatter);
        String formattedEnd = end.format(formatter);

        MemberChangeHistory history = MemberChangeHistory.builder()
                                                            .member(foundMember)
                                                            .chngFieldCode(memberIdFeildCode)
                                                            .befVal(foundMember.getId())
                                                            .aftVal(request.getId())
                                                            .applDate(formattedNowTime)
                                                            .endDate(formattedEnd)
                                                            .build();
        List<MemberChangeHistory> histories = new ArrayList<>();
        histories.add(history);

        List<MemberChangeHistory> responseHistories = new ArrayList<>();
        responseHistories.add(history);


        // 회원 아이디만 변경
        // 리포지토리 목킹
        // - 1. 아이디, 이메일 수정 확인 false
        // - 2. 코드 리포지토리에서 MF001로 코드 조회시 회원 아이디 필드 의미하는 코드 반환
        // - 3. 회원 상태 MBS002로 코드 조회시 회원 변경 상태를 의미하는 코드 반환
        when(memberRepository.existsByIdNotContainingMemberSeq(memberSeq, request.getId())).thenReturn(false);
        when(memberRepository.existsByEmailNotContainingMemberSeq(memberSeq, request.getEmail())).thenReturn(false);
        when(memberRepository.findById(memberSeq)).thenReturn(Optional.of(foundMember));
        when(codeRepository.findCodeByCode("MF001")).thenReturn(memberIdFeildCode);
        when(codeRepository.findCodeByCode("MBS002")).thenReturn(memberStateCode);
        when(memberStateRepository.save(memberState)).thenReturn(savedMemberState);
        when(memberChangeHistoryRepository.findLatestHistoryByIdAndCode(memberSeq, "MF001")).thenReturn(Optional.of(history));
        when(memberChangeHistoryRepository.saveAll(any())).thenReturn(responseHistories);


        // 서비스로 수정 처리 호출
        // 결과 비교
        assertDoesNotThrow(() -> {
            memberService.update(memberSeq, request);
        });
    }

    @DisplayName("회원 삭제 처리(비활성 상태 전환)")
    @Test
    void 회원_삭제_처리_비활성_상태_전환() {
        // 유효한 데이터를 전달받음 -> memberSeq, MemberRemoveRequest
        Long memberSeq = 1L;
        MemberRemoveRequest request = MemberRemoveRequest.builder()
                                                        .code("MBD001") // 서비스 이용 불만족
                                                        .detailCont("해당 서비스가 제가 생각했던 서비스가 아니네요. 실망했습니다")
                                                        .build();

        // 응답 데이터 세팅
        Member foundMember = Member.builder()
                                    .id("qwerfde2312")
                                    .password("dwadaw")
                                    .email("eqwfqws2131@gmail.com")
                                    .name("홍만동")
                                    .normAddr("서울시 양천구")
                                    .locaAddr("서울시")
                                    .detailAddr("서초동 123-456")
                                    .passAddr("서초대로 59-32")
                                    .chkValid('Y')
                                    .build();

        Code removeMemberCode = Code.builder()
                                    .codeSeq(1L)
                                    .code("MBS003")
                                    .cateNum("M")
                                    .name("회원 비활성화")
                                    .chkUse("Y")
                                    .ord(1)
                                    .build();

        MemberState memberState = MemberState.of(foundMember, removeMemberCode);
        MemberState savedMemberState = MemberState.of(foundMember, removeMemberCode);
        MemberDelete memberDelete = MemberDelete.of(memberSeq, request, removeMemberCode);
        MemberDelete savedMemberDelete = MemberDelete.of(memberSeq, request, removeMemberCode);

        // 리포지토리 목킹
        // - 1. 시퀀스로 회원 조회 -> foundMember
        // - 2. MBS003 코드 조회(회원 비활성화) -> removeMemberCode
        // - 3. 회원 비활성 상태 엔티티 생성 -> memberState
        // - 4. 회원 30일 유해 기간 설정, 회원 삭제 유형 코드와 사유 기록 -> MemberDelete
        when(memberRepository.findById(memberSeq)).thenReturn(Optional.of(foundMember));
        when(codeRepository.findCodeByCode("MBS003")).thenReturn(removeMemberCode);
        when(memberStateRepository.save(memberState)).thenReturn(savedMemberState);
        when(memberDeleteRepository.save(any())).thenReturn(savedMemberDelete);


        // 서비스 호출
        // 결과 비교 -> 예외 발생 x
        assertDoesNotThrow(() -> {
            memberService.remove(memberSeq, request);
        });
    }


    @DisplayName("스케줄러 테스트, checkRemovedMember 메서드가 매시 새벽 4시에 실행되야 함")
    @Test
    void 스케줄러_회원_삭제_처리() throws Exception {
        // 매일 새벽 4시를 의미하는 cron 표현식
        String cronExpression = "0 0 4 * * *";

        // Cron 트리거 생성 : 위에서 작성한 cron 표현식으로 다음 실행 시간을 계산할 객체
        CronTrigger trigger = new CronTrigger(cronExpression);

        // 테스트 시작 시간 설정 (2024년 10월 20일 새벽 3시 59분 59초)
        Date startTime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse("2024/10/20 03:59:59");

        // 스케쥴링 컨텍스트 초기화
        SimpleTriggerContext context = new SimpleTriggerContext();
        context.update(startTime, startTime, startTime);

        // 예상되는 실행 시간 목록
        List<String> expectedTimes = Arrays.asList(
                "2024/10/20 04:00:00",  // 첫 실행 시간
                "2024/10/21 04:00:00",  // 두 번째 실행 시간
                "2024/10/22 04:00:00"   // 세 번째 실행 시간
        );

        // 동작 수행
        for (String expectedTime : expectedTimes) {
            Date nextExecutionTime = trigger.nextExecutionTime(context);

            // 결과 비교
            String actualTime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(nextExecutionTime);
            assertEquals(expectedTime, actualTime);

            // 다음 실행 시간 업데이트
            context.update(nextExecutionTime, nextExecutionTime, nextExecutionTime);
        }
    }

    @DisplayName("회원 스케줄링 삭제 호출 여부 확인")
    @Test
    void 회원_스케줄링_삭제_호출_처리() {
        // 더미 데이터 세팅
        Long memberSeq1 = 1L;
        Member foundMember = Member.builder()
                                    .seq(memberSeq1)
                                    .id("qwerfde2312")
                                    .password("dwadaw")
                                    .email("eqwfqws2131@gmail.com")
                                    .name("홍만동")
                                    .normAddr("서울시 양천구")
                                    .locaAddr("서울시")
                                    .detailAddr("서초동 123-456")
                                    .passAddr("서초대로 59-32")
                                    .chkValid('Y')
                                    .build();

        List<MemberConnectionHistory> connectionHistories = new ArrayList<>();
        List<MemberTermsHistory> termsHistories = new ArrayList<>();
        List<MemberChangeHistory> changeHistories = new ArrayList<>();


        MemberRemoveRequest request1 = MemberRemoveRequest.builder()
                                                        .code("MBD001") // 서비스 이용 불만족
                                                        .detailCont("해당 서비스가 제가 생각했던 서비스가 아니네요. 실망했습니다")
                                                        .build();

        Code removeReasonCode1 = Code.builder()
                                    .codeSeq(1L)
                                    .code("MBD001")
                                    .cateNum("M")
                                    .name("서비스 이용 불만족")
                                    .chkUse("Y")
                                    .ord(1)
                                    .build();



        MemberDelete memberDelete1 = MemberDelete.of(memberSeq1, request1, removeReasonCode1);

        // 현재 기준 30일 지난 시점으로 변경하기
        LocalDateTime beforeMonth = LocalDateTime.now().minusMonths(1);

        // DATETIME 형식으로 변환하기 위한 포맷터 생성
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 포맷팅된 문자열로 변환
        String formattedBeforeMonth = beforeMonth.format(formatter);

        // 적용 시간 세팅
        memberDelete1.setApplDate(formattedBeforeMonth);


        List<MemberDelete> foundDeleteMembers = Arrays.asList(
                memberDelete1
        );

        when(memberDeleteRepository.findAll()).thenReturn(foundDeleteMembers);
        when(memberRepository.findById(memberSeq1)).thenReturn(Optional.of(foundMember));
        when(memberConnectionHistoryRepository.findMemberConnectionHistoriesByMemberSeq(memberSeq1)).thenReturn(connectionHistories);
        when(memberTermsHistoryRepository.findByMemberSeq(memberSeq1)).thenReturn(termsHistories);
        when(memberChangeHistoryRepository.findMemberChangeHistoriesByMemberSeq(memberSeq1)).thenReturn(changeHistories);

        // 스케줄러 메서드 직접 호출
        memberService.checkRemovedMember();

        // 검증 - 예상되는 동작 (삭제 메서드 호출 여부 등)
        verify(memberRepository, times(1)).delete(any(Member.class));
        verify(memberStateRepository, times(1)).deleteAll(anyList());
        verify(memberTermsHistoryRepository, times(1)).deleteAll(anyList());
        verify(memberTermsHistoryRepository, times(1)).deleteAll(anyList());
        verify(memberChangeHistoryRepository, times(1)).deleteAll(anyList());
        verify(codeRepository, times(1)).findCodeByCode("MBS999");
    }

    private MemberRegisterRequest retryableCreateMemberMemberRegisterRequest(List<MemberTermCheckOrNotRequest> memberTermCheckOrNotRequests) {
        var request = MemberRegisterRequest.builder()
                                            .id("qwerfde2312")
                                            .password("asdf12341234@")
                                            .email("asdf3214@gmail.com")
                                            .name("홍길동")
                                            .userRegDate(LocalDateTime.now())
                                            .chkValid('Y')
                                            .memberTermCheckOrNotRequests(memberTermCheckOrNotRequests)
                                            .normAddr("서울시 강남구")
                                            .passAddr("서초대로 59-32")
                                            .locaAddr("서초동")
                                            .detailAddr("서초동 123-456")
                                            .build();

        return request;
    }

    private List<MemberTermCheckOrNotRequest> retryableCreateMemberMemberTerms() {
        List<MemberTermCheckOrNotRequest> memberTermCheckOrNotRequests = new ArrayList<>();
        // 약관 동의 내용 설정
        memberTermCheckOrNotRequests = new ArrayList<>();
        memberTermCheckOrNotRequests.add(
                MemberTermCheckOrNotRequest.builder()
                        .termCondCode(1L)
                        .agreed('Y')
                        .build());

        memberTermCheckOrNotRequests.add(
                MemberTermCheckOrNotRequest.builder()
                        .termCondCode(2L)
                        .agreed('Y')
                        .build());

        memberTermCheckOrNotRequests.add(
                MemberTermCheckOrNotRequest.builder()
                        .termCondCode(3L)
                        .agreed('Y')
                        .build());

        memberTermCheckOrNotRequests.add(
                MemberTermCheckOrNotRequest.builder()
                        .termCondCode(4L)
                        .agreed('N')
                        .build());

        memberTermCheckOrNotRequests.add(
                MemberTermCheckOrNotRequest.builder()
                        .termCondCode(5L)
                        .agreed('N')
                        .build());

        return memberTermCheckOrNotRequests;
    }

    private boolean isSameMemberFields(Member member, MemberRegisterRequest request) {
        // 아이디, 이름, 이메일 등 회원 정보 비교
        return member.getId().equals(request.getId()) &&
                member.getEmail().equals(request.getEmail()) &&
                member.getName().equals(request.getName()) &&
                member.getNormAddr().equals(request.getNormAddr()) &&
                member.getLocaAddr().equals(request.getLocaAddr()) &&
                member.getDetailAddr().equals(request.getDetailAddr()) &&
                member.getPassAddr().equals(request.getPassAddr());
    }

    private boolean isSameMemberTermsHistoryFields(MemberTermsHistory memberTermsHistory, List<MemberTermCheckOrNotRequest> memberTermCheckOrNotRequests) {
        // 약관 동의 내역 비교
        return memberTermsHistory.getChkTerm1().equals(memberTermCheckOrNotRequests.get(0).getAgreed()) &&
                memberTermsHistory.getChkTerm2().equals(memberTermCheckOrNotRequests.get(1).getAgreed()) &&
                memberTermsHistory.getChkTerm3().equals(memberTermCheckOrNotRequests.get(2).getAgreed()) &&
                memberTermsHistory.getChkTerm4().equals(memberTermCheckOrNotRequests.get(3).getAgreed()) &&
                memberTermsHistory.getChkTerm5().equals(memberTermCheckOrNotRequests.get(4).getAgreed());
    }

    // 추후에 해당 부분 문제 해결 : 회원 상태 어떻게 비교할지 고민하기
    private boolean isSameMemberStateFields(MemberState memberState) {
        // 회원 상태 비교(아직 데이터 세팅해 놓지 않은 상태임)
        return true;
    }
}