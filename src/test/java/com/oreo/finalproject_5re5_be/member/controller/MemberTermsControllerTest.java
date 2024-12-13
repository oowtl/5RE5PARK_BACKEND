// package com.oreo.finalproject_5re5_be.member.controller;
//
// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.Mockito.doNothing;
// import static org.mockito.Mockito.times;
// import static org.mockito.Mockito.verify;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.mockito.Mockito.when;
// import static
// org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.oreo.finalproject_5re5_be.member.dto.request.MemberTermRequest;
// import com.oreo.finalproject_5re5_be.member.dto.request.MemberTermUpdateRequest;
// import com.oreo.finalproject_5re5_be.member.dto.response.MemberTermResponse;
// import com.oreo.finalproject_5re5_be.member.dto.response.MemberTermResponses;
// import com.oreo.finalproject_5re5_be.member.entity.MemberTerms;
// import com.oreo.finalproject_5re5_be.member.service.MemberTermsServiceImpl;
// import com.oreo.finalproject_5re5_be.tts.service.TtsSentenceService;
// import java.time.LocalDateTime;
// import java.util.ArrayList;
// import java.util.List;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.junit.runner.RunWith;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.http.MediaType;
// import org.springframework.security.test.context.support.WithMockUser;
// import org.springframework.test.web.servlet.MockMvc;
//
//
// @WebMvcTest(MemberTermsController.class)
// class MemberTermsControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private MemberTermsServiceImpl memberTermsService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private MemberTermRequest requestFixture;
//    private MemberTermResponse responseFixture;
//    private MemberTermResponses responsesFixture;
//
//    @BeforeEach
//    void setUp() {
//        // 자동 주입 확인
//        assertNotNull(mockMvc);
//        assertNotNull(memberTermsService);
//        assertNotNull(objectMapper);
//
//        // 더미 데이터 생성
//        createMemberTermRequest();
//        createMemberTermResponse();
//        createMemberTermResponses();   // 약관 3개 등록, 이중 2개는 사용 가능 1개는 불가능
//    }
//
//    // 1-1. 약관 등록 성공 테스트
//    // 1-2. 약관 등록시 예외 발생 테스트
//    // 2-1. 모든 약관 조회 테스트
//    // 2-2. 사용 가능한 모든 약관 조회 테스트
//    // 2-3. 사용 불가능한 모든 약관 조회 테스트
//    // 3-1. 약관 수정 테스트
//    // 3-2. 약관 수정시 예외 발생 테스트
//    // 4. 약관 삭제 테스트
//
//    /**
//     * 알게된 사실 : 목킹이 제대로 이루어지지 않는 경우, dto에서 equals(), hashCode() 메소드를 오버라이딩하지 않은 것이 원이이 될 수 있다
//     * - 요청, 응답 dto 모두 구현해야함
//     */
//    @Test
//    @WithMockUser
//    @DisplayName("1-1. 약관 등록 성공 테스트")
//    void 약관_등록_성공_테스트() throws Exception {
//        // 약관 등록시 필요한 정상적인 요청 데이터 더미 생성
//        // 서비스 객체 목킹
//        when(memberTermsService.create(requestFixture)).thenReturn(responseFixture);
//
//        // 컨트롤러에 요청 보내기, 응답 데이터 확인
//        mockMvc.perform(post("/api/member-term/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestFixture))
//                        .with(csrf())
//                )
//               .andExpect(status().isCreated())
//               .andExpect(jsonPath("$.termSeq").value(responseFixture.getTermSeq()))
//               .andExpect(jsonPath("$.name").value(responseFixture.getName()))
//               .andExpect(jsonPath("$.chkTerm1").value(responseFixture.getChkTerm1().toString()))
//               .andExpect(jsonPath("$.chkTerm2").value(responseFixture.getChkTerm2().toString()))
//               .andExpect(jsonPath("$.chkTerm3").value(responseFixture.getChkTerm3().toString()))
//               .andExpect(jsonPath("$.chkTerm4").value(responseFixture.getChkTerm4().toString()))
//               .andExpect(jsonPath("$.chkTerm5").value(responseFixture.getChkTerm5().toString()))
//               .andExpect(jsonPath("$.termEndDate").exists())
//               .andExpect(jsonPath("$.termRegDate").exists())
//               .andExpect(jsonPath("$.chkUse").value(responseFixture.getChkUse().toString()))
//               .andExpect(jsonPath("$.termCond1").value(responseFixture.getTermCond1()))
//               .andExpect(jsonPath("$.termCond2").value(responseFixture.getTermCond2()))
//               .andExpect(jsonPath("$.termCond3").value(responseFixture.getTermCond3()))
//               .andExpect(jsonPath("$.termCond4").value(responseFixture.getTermCond4()))
//               .andExpect(jsonPath("$.termCond5").value(responseFixture.getTermCond5()));
//    }
//
//    @Test
//    @WithMockUser
//    @DisplayName("1-2. 약관 등록시 예외 발생 테스트")
//    void 약관_등록시_예외_발생_테스트() throws Exception {
//        // 약관 등록시 예외가 발생하는 요청 데이터 더미 생성
//        // 서비스 객체 목킹
//        // 컨트롤러에 요청 보내기, 응답 데이터 확인
//        MemberTermRequest wrongRequest = MemberTermRequest.builder()
//                                                          .name("24년도회원약관A")
//
// .memberTermConditionCodes(List.of("S001", "Tdawda", "213154", "dwa"))
//
// .memberTermConditionMandatoryOrNot(List.of('Y', 'Y', 'Y', 'Y'))
//                                                          .chkUse('Y')
//                                                          .build();
//        mockMvc.perform(post("/api/member-term/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(wrongRequest))
//                        .with(csrf())
//                )
//               .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    @WithMockUser
//    @DisplayName("2-1. 모든 약관 조회 테스트")
//    void 모든_약관_조회_테스트() throws Exception {
//        // 3개의 약관을 등록했다고 가정함
//        // 서비스 객체 목킹
//        // 서비스에서 모든 약관 조회시 등록한 3개의 약관 반환하게 만듦
//        when(memberTermsService.readAll()).thenReturn(responsesFixture);
//
//        // 컨트롤러에 요청 보내기, 응답 데이터 확인
//        mockMvc.perform(get("/api/member-term/all"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.memberTermResponses").isArray())
//
// .andExpect(jsonPath("$.memberTermResponses[0].termSeq").value(responsesFixture.getMemberTermResponses().get(0).getTermSeq()))
//
// .andExpect(jsonPath("$.memberTermResponses[0].name").value(responsesFixture.getMemberTermResponses().get(0).getName()))
//
// .andExpect(jsonPath("$.memberTermResponses[0].chkTerm1").value(responsesFixture.getMemberTermResponses().get(0).getChkTerm1().toString()))
//
// .andExpect(jsonPath("$.memberTermResponses[0].chkTerm2").value(responsesFixture.getMemberTermResponses().get(0).getChkTerm2().toString()))
//
// .andExpect(jsonPath("$.memberTermResponses[0].chkTerm3").value(responsesFixture.getMemberTermResponses().get(0).getChkTerm3().toString()))
//
// .andExpect(jsonPath("$.memberTermResponses[0].chkTerm4").value(responsesFixture.getMemberTermResponses().get(0).getChkTerm4().toString()))
//
// .andExpect(jsonPath("$.memberTermResponses[0].chkTerm5").value(responsesFixture.getMemberTermResponses().get(0).getChkTerm5().toString()))
//                .andExpect(jsonPath("$.memberTermResponses[0].termEndDate").exists())
//                .andExpect(jsonPath("$.memberTermResponses[0].termRegDate").exists())
//
// .andExpect(jsonPath("$.memberTermResponses[0].chkUse").value(responsesFixture.getMemberTermResponses().get(0).getChkUse().toString()))
//
// .andExpect(jsonPath("$.memberTermResponses[0].termCond1").value(responsesFixture.getMemberTermResponses().get(0).getTermCond1()))
//
// .andExpect(jsonPath("$.memberTermResponses[0].termCond2").value(responsesFixture.getMemberTermResponses().get(0).getTermCond2()))
//
// .andExpect(jsonPath("$.memberTermResponses[0].termCond3").value(responsesFixture.getMemberTermResponses().get(0).getTermCond3()))
//
// .andExpect(jsonPath("$.memberTermResponses[0].termCond4").value(responsesFixture.getMemberTermResponses().get(0).getTermCond4()))
//
// .andExpect(jsonPath("$.memberTermResponses[0].termCond5").value(responsesFixture.getMemberTermResponses().get(0).getTermCond5()));
//
//    }
//
//    @Test
//    @WithMockUser
//    @DisplayName("2-2. 사용 가능한 모든 약관 조회 테스트")
//    void 사용_가능한_모든_약관_조회_테스트() throws Exception {
//        // 3개의 약관을 등록했다고 가정함. 이때, 1개의 약관은 사용 불가능한 약관
//        // 서비스 객체 목킹
//        // 서비스에서 사용가능한 모든 약관 조회시 등록한 2개의 약관 반환하게 만듦
//        List<MemberTermResponse> memberTermResponseList =
// responsesFixture.getMemberTermResponses()
//                                                                          .stream()
//
// .filter(memberTermResponse -> memberTermResponse.getChkUse().equals('Y'))
//                                                                          .toList();
//        MemberTermResponses availableResponses = new MemberTermResponses(memberTermResponseList);
//        when(memberTermsService.readAvailable()).thenReturn(availableResponses);
//
//        // 컨트롤러에 요청 보내기, 응답 데이터 확인
//        mockMvc.perform(get("/api/member-term/available"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.memberTermResponses").isArray())
//
// .andExpect(jsonPath("$.memberTermResponses[0].termSeq").value(availableResponses.getMemberTermResponses().get(0).getTermSeq()))
//
// .andExpect(jsonPath("$.memberTermResponses[0].name").value(availableResponses.getMemberTermResponses().get(0).getName()))
//
// .andExpect(jsonPath("$.memberTermResponses[0].chkTerm1").value(availableResponses.getMemberTermResponses().get(0).getChkTerm1().toString()))
//
// .andExpect(jsonPath("$.memberTermResponses[0].chkTerm2").value(availableResponses.getMemberTermResponses().get(0).getChkTerm2().toString()))
//
// .andExpect(jsonPath("$.memberTermResponses[0].chkTerm3").value(availableResponses.getMemberTermResponses().get(0).getChkTerm3().toString()))
//
// .andExpect(jsonPath("$.memberTermResponses[0].chkTerm4").value(availableResponses.getMemberTermResponses().get(0).getChkTerm4().toString()))
//
// .andExpect(jsonPath("$.memberTermResponses[0].chkTerm5").value(availableResponses.getMemberTermResponses().get(0).getChkTerm5().toString()))
//                .andExpect(jsonPath("$.memberTermResponses[0].termEndDate").exists())
//                .andExpect(jsonPath("$.memberTermResponses[0].termRegDate").exists())
//
// .andExpect(jsonPath("$.memberTermResponses[0].chkUse").value(availableResponses.getMemberTermResponses().get(0).getChkUse().toString()))
//
// .andExpect(jsonPath("$.memberTermResponses[0].termCond1").value(availableResponses.getMemberTermResponses().get(0).getTermCond1()))
//
// .andExpect(jsonPath("$.memberTermResponses[0].termCond2").value(availableResponses.getMemberTermResponses().get(0).getTermCond2()))
//
// .andExpect(jsonPath("$.memberTermResponses[0].termCond3").value(availableResponses.getMemberTermResponses().get(0).getTermCond3()))
//
// .andExpect(jsonPath("$.memberTermResponses[0].termCond4").value(availableResponses.getMemberTermResponses().get(0).getTermCond4()))
//
// .andExpect(jsonPath("$.memberTermResponses[0].termCond5").value(availableResponses.getMemberTermResponses().get(0).getTermCond5()))
//
// .andExpect(jsonPath("$.memberTermResponses[1].termSeq").value(availableResponses.getMemberTermResponses().get(1).getTermSeq()))
//
// .andExpect(jsonPath("$.memberTermResponses[1].name").value(availableResponses.getMemberTermResponses().get(1).getName()))
//
// .andExpect(jsonPath("$.memberTermResponses[1].chkTerm1").value(availableResponses.getMemberTermResponses().get(1).getChkTerm1().toString()))
//
// .andExpect(jsonPath("$.memberTermResponses[1].chkTerm2").value(availableResponses.getMemberTermResponses().get(1).getChkTerm2().toString()))
//
// .andExpect(jsonPath("$.memberTermResponses[1].chkTerm3").value(availableResponses.getMemberTermResponses().get(1).getChkTerm3().toString()))
//
// .andExpect(jsonPath("$.memberTermResponses[1].chkTerm4").value(availableResponses.getMemberTermResponses().get(1).getChkTerm4().toString()))
//
// .andExpect(jsonPath("$.memberTermResponses[1].chkTerm5").value(availableResponses.getMemberTermResponses().get(1).getChkTerm5().toString()))
//                .andExpect(jsonPath("$.memberTermResponses[1].termEndDate").exists())
//                .andExpect(jsonPath("$.memberTermResponses[1].termRegDate").exists())
//
// .andExpect(jsonPath("$.memberTermResponses[1].chkUse").value(availableResponses.getMemberTermResponses().get(1).getChkUse().toString()))
//
// .andExpect(jsonPath("$.memberTermResponses[1].termCond1").value(availableResponses.getMemberTermResponses().get(1).getTermCond1()))
//
// .andExpect(jsonPath("$.memberTermResponses[1].termCond2").value(availableResponses.getMemberTermResponses().get(1).getTermCond2()))
//
// .andExpect(jsonPath("$.memberTermResponses[1].termCond3").value(availableResponses.getMemberTermResponses().get(1).getTermCond3()))
//
// .andExpect(jsonPath("$.memberTermResponses[1].termCond4").value(availableResponses.getMemberTermResponses().get(1).getTermCond4()))
//
// .andExpect(jsonPath("$.memberTermResponses[1].termCond5").value(availableResponses.getMemberTermResponses().get(1).getTermCond5()));
//    }
//
//    @Test
//    @WithMockUser
//    @DisplayName("2-3. 사용 불가능한 모든 약관 조회 테스트")
//    void 사용_불가능한_모든_약관_조회_테스트() throws Exception {
//        // 3개의 약관을 등록했다고 가정함. 이때, 1개의 약관은 사용 불가능한 약관
//        // 서비스 객체 목킹
//        // 서비스에서 사용 불가능한 모든 약관 조회시 등록한 1개의 약관 반환하게 만듦
//        List<MemberTermResponse> memberTermResponseList =
// responsesFixture.getMemberTermResponses()
//                                                                        .stream()
//                                                                        .filter(memberTermResponse
// -> memberTermResponse.getChkUse().equals('N'))
//                                                                        .toList();
//        MemberTermResponses notAvailableResponses = new
// MemberTermResponses(memberTermResponseList);
//        when(memberTermsService.readNotAvailable()).thenReturn(notAvailableResponses);
//
//        // 컨트롤러에 요청 보내기, 응답 데이터 확인
//        mockMvc.perform(get("/api/member-term/not-available"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.memberTermResponses").isArray())
//
// .andExpect(jsonPath("$.memberTermResponses[0].termSeq").value(notAvailableResponses.getMemberTermResponses().get(0).getTermSeq()))
//
// .andExpect(jsonPath("$.memberTermResponses[0].name").value(notAvailableResponses.getMemberTermResponses().get(0).getName()))
//
// .andExpect(jsonPath("$.memberTermResponses[0].chkTerm1").value(notAvailableResponses.getMemberTermResponses().get(0).getChkTerm1().toString()))
//
// .andExpect(jsonPath("$.memberTermResponses[0].chkTerm2").value(notAvailableResponses.getMemberTermResponses().get(0).getChkTerm2().toString()))
//
// .andExpect(jsonPath("$.memberTermResponses[0].chkTerm3").value(notAvailableResponses.getMemberTermResponses().get(0).getChkTerm3().toString()))
//
// .andExpect(jsonPath("$.memberTermResponses[0].chkTerm4").value(notAvailableResponses.getMemberTermResponses().get(0).getChkTerm4().toString()))
//
// .andExpect(jsonPath("$.memberTermResponses[0].chkTerm5").value(notAvailableResponses.getMemberTermResponses().get(0).getChkTerm5().toString()))
//                .andExpect(jsonPath("$.memberTermResponses[0].termEndDate").exists())
//                .andExpect(jsonPath("$.memberTermResponses[0].termRegDate").exists())
//
// .andExpect(jsonPath("$.memberTermResponses[0].chkUse").value(notAvailableResponses.getMemberTermResponses().get(0).getChkUse().toString()))
//
// .andExpect(jsonPath("$.memberTermResponses[0].termCond1").value(notAvailableResponses.getMemberTermResponses().get(0).getTermCond1()))
//
// .andExpect(jsonPath("$.memberTermResponses[0].termCond2").value(notAvailableResponses.getMemberTermResponses().get(0).getTermCond2()))
//
// .andExpect(jsonPath("$.memberTermResponses[0].termCond3").value(notAvailableResponses.getMemberTermResponses().get(0).getTermCond3()))
//
// .andExpect(jsonPath("$.memberTermResponses[0].termCond4").value(notAvailableResponses.getMemberTermResponses().get(0).getTermCond4()))
//
// .andExpect(jsonPath("$.memberTermResponses[0].termCond5").value(notAvailableResponses.getMemberTermResponses().get(0).getTermCond5()));
//
//
//    }
//
//    @Test
//    @WithMockUser
//    @DisplayName("3-1. 약관 수정 테스트")
//    void 약관_수정_테스트() throws Exception {
//        // 약관 수정시 필요한 정상적인 요청 데이터 더미 생성
//        // 서비스 객체 목킹
//        MemberTermUpdateRequest request = MemberTermUpdateRequest.builder()
//
// .memberTermConditionMandatoryOrNot(List.of('Y', 'Y', 'Y', 'N', 'N'))
//                                                                 .chkUse('N')
//                                                                 .build();
//
//        doNothing().when(memberTermsService).update(1L, request);
//        // 컨트롤러에 요청 보내기, 응답 데이터 확인
//        mockMvc.perform(patch("/api/member-term/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request))
//                        .with(csrf())
//                )
//               .andExpect(status().isNoContent());
//    }
//
//    @Test
//    @WithMockUser
//    @DisplayName("3-2. 약관 수정시 예외 발생 테스트")
//    void 약관_수정시_예외_발생_테스트() throws Exception{
//        // 약관 수정시 예외가 발생하는 비정상적인 요청 데이터 더미 생성
//        // 서비스 객체 목킹
//        doNothing().when(memberTermsService).update(1L,
// MemberTermUpdateRequest.builder().build());
//        // 컨트롤러에 요청 보내기, 응답 데이터 확인
//        mockMvc.perform(patch("/api/member-term/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//
// .content(objectMapper.writeValueAsString(MemberTermUpdateRequest.builder().build()))
//                        .with(csrf())
//                )
//               .andExpect(status().isBadRequest());
//    }
//
//
//    @Test
//    @WithMockUser
//    @DisplayName("4. 약관 삭제 테스트")
//    void 약관_삭제_테스트() throws Exception {
//        // 약관 삭제시 필요한 요청 데이터 더미 생성
//        // 서비스 객체 목킹
//        doNothing().when(memberTermsService).remove(1L);
//        // 컨트롤러에 요청 보내기, 응답 데이터 확인
//        mockMvc.perform(delete("/api/member-term/1")
//                        .with(csrf())
//                )
//               .andExpect(status().isNoContent());
//
//    }
//
//    private void createMemberTermRequest() {
//        requestFixture = MemberTermRequest.builder()
//                                          .name("24년도회원약관A")
//                                          .memberTermConditionCodes(List.of("TERMS001",
// "TERMS002", "TERMS003", "TERMS004", "TERMS005"))
//                                          .memberTermConditionMandatoryOrNot(List.of('Y', 'Y',
// 'Y', 'Y', 'Y'))
//                                          .chkUse('Y')
//                                          .build();
//    }
//
//    private void createMemberTermResponse() {
//        responseFixture = MemberTermResponse.builder()
//                                            .termSeq(1L)
//                                            .name("24년도회원약관A")
//                                            .chkTerm1('Y')
//                                            .chkTerm2('Y')
//                                            .chkTerm3('Y')
//                                            .chkTerm4('N')
//                                            .chkTerm5('N')
//                                            .termEndDate(LocalDateTime.now())
//                                            .termRegDate(LocalDateTime.now().minusDays(1))
//                                            .chkUse('Y')
//                                            .termCond1(1L)
//                                            .termCond2(2L)
//                                            .termCond3(3L)
//                                            .termCond4(4L)
//                                            .termCond5(5L)
//                                            .build();
//    }
//
//    private void createMemberTermResponses() {
//        // 더미 데이터 생성
//        List<MemberTermResponse> dummy = new ArrayList<>();
//
//        dummy.add(MemberTermResponse.builder()
//                                    .termSeq(1L)
//                                    .name("24년도회원약관A")
//                                    .chkTerm1('Y')
//                                    .chkTerm2('Y')
//                                    .chkTerm3('Y')
//                                    .chkTerm4('N')
//                                    .chkTerm5('N')
//                                    .termEndDate(LocalDateTime.now())
//                                    .termRegDate(LocalDateTime.now().minusDays(1))
//                                    .chkUse('Y')
//                                    .termCond1(1L)
//                                    .termCond2(2L)
//                                    .termCond3(3L)
//                                    .termCond4(4L)
//                                    .termCond5(5L)
//                                    .build());
//
//        dummy.add(MemberTermResponse.builder()
//                                    .termSeq(2L)
//                                    .name("24년도회원약관B")
//                                    .chkTerm1('Y')
//                                    .chkTerm2('Y')
//                                    .chkTerm3('Y')
//                                    .chkTerm4('N')
//                                    .chkTerm5('N')
//                                    .termEndDate(LocalDateTime.now())
//                                    .termRegDate(LocalDateTime.now().minusDays(1))
//                                    .chkUse('Y')
//                                    .termCond1(1L)
//                                    .termCond2(2L)
//                                    .termCond3(3L)
//                                    .termCond4(4L)
//                                    .termCond5(5L)
//                                    .build());
//
//        dummy.add(MemberTermResponse.builder()
//                                    .termSeq(3L)
//                                    .name("24년도회원약관C")
//                                    .chkTerm1('Y')
//                                    .chkTerm2('Y')
//                                    .chkTerm3('Y')
//                                    .chkTerm4('N')
//                                    .chkTerm5('N')
//                                    .termEndDate(LocalDateTime.now())
//                                    .termRegDate(LocalDateTime.now().minusDays(1))
//                                    .chkUse('N')
//                                    .termCond1(1L)
//                                    .termCond2(2L)
//                                    .termCond3(3L)
//                                    .termCond4(4L)
//                                    .termCond5(5L)
//                                    .build());
//
//        responsesFixture = new MemberTermResponses(dummy);
//    }
//
//
//
// }
