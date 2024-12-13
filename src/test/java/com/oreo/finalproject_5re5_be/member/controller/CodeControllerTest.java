// package com.oreo.finalproject_5re5_be.member.controller;
//
//
// import static org.junit.jupiter.api.Assertions.assertNotNull;
// import static org.mockito.Mockito.doNothing;
// import static org.mockito.Mockito.when;
// import static
// org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.oreo.finalproject_5re5_be.code.dto.request.CodeRequest;
// import com.oreo.finalproject_5re5_be.code.dto.request.CodeUpdateRequest;
// import com.oreo.finalproject_5re5_be.code.dto.response.CodeResponse;
// import com.oreo.finalproject_5re5_be.code.dto.response.CodeResponses;
// import com.oreo.finalproject_5re5_be.code.service.CodeServiceImpl;
// import java.util.ArrayList;
// import java.util.List;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.http.MediaType;
// import org.springframework.security.test.context.support.WithMockUser;
// import org.springframework.test.web.servlet.MockMvc;
//
//
// @WebMvcTest(CodeController.class)
// class CodeControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockBean
//    private CodeServiceImpl codeService;
//
//    private CodeRequest request;
//    private CodeUpdateRequest updateRequest;
//    private CodeResponse response;
//    private CodeResponses responses;
//
//
//    @BeforeEach
//    void setUp() {
//        // 자동 주입 확인
//        assertNotNull(codeService);
//        assertNotNull(mockMvc);
//        assertNotNull(objectMapper);
//
//        // 더미 데이터 생성
//        createCodeRequest();
//        createCodeUpdateRequest();
//        createCodeResponse();
//        createCodeResponses();
//    }
//
//    @Test
//    @WithMockUser
//    @DisplayName("코드 등록 처리")
//    void 코드_등록_처리() throws Exception {
//        // 더미 데이터 생성
//
//        // 서비스 목킹
//        // - 서비스 생성 메서드 호출시 response 데이터 반환하도록 설정
//        when(codeService.create(request)).thenReturn(response);
//
//        // 컨트롤러 요청 보내기 및 결과 비교
//        mockMvc.perform(post("/api/code/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request))
//                        .with(csrf())
//                )
//               .andExpect(status().isCreated())
//               .andExpect(jsonPath("$.cateNum").value(response.getCateNum()))
//               .andExpect(jsonPath("$.code").value(response.getCode()))
//               .andExpect(jsonPath("$.name").value(response.getName()))
//               .andExpect(jsonPath("$.ord").value(response.getOrd()));
//
//    }
//
//
//    @Test
//    @WithMockUser
//    @DisplayName("모든 코드 조회 처리")
//    void 모든_코드_조회_처리() throws Exception {
//        // 더미 데이터 생성
//
//        // 서비스 목킹
//        // - 서비스 모든 코드 조회 메서드 호출시 responses 데이터 반환하도록 설정
//        when(codeService.readAll()).thenReturn(responses);
//
//        // 컨트롤러 요청 보내기 및 결과 비교
//        mockMvc.perform(get("/api/code/all"))
//               .andExpect(status().isOk())
//
// .andExpect(jsonPath("$.codeResponses[0].cateNum").value(responses.getCodeResponses().get(0).getCateNum()))
//
// .andExpect(jsonPath("$.codeResponses[0].code").value(responses.getCodeResponses().get(0).getCode()))
//
// .andExpect(jsonPath("$.codeResponses[0].name").value(responses.getCodeResponses().get(0).getName()))
//
// .andExpect(jsonPath("$.codeResponses[0].ord").value(responses.getCodeResponses().get(0).getOrd()));
//
//    }
//
//    @Test
//    @WithMockUser
//    @DisplayName("시퀀스로 특정 코드 조회")
//    void 시퀀스로_특정_코드_조회() throws Exception {
//        // 더미 데이터 생성
//
//        // 서비스 목킹
//        // - 서비스 시퀀스로 조회 메서드 호출시 response 데이터 반환하도록 설정
//        when(codeService.read(1L)).thenReturn(response);
//
//        // 컨트롤러 요청 보내기 및 결과 비교
//        mockMvc.perform(get("/api/code/seq/1"))
//               .andExpect(status().isOk())
//               .andExpect(jsonPath("$.cateNum").value(response.getCateNum()))
//               .andExpect(jsonPath("$.code").value(response.getCode()))
//               .andExpect(jsonPath("$.name").value(response.getName()))
//               .andExpect(jsonPath("$.ord").value(response.getOrd()));
//
//    }
//
//    @Test
//    @WithMockUser
//    @DisplayName("코드 번호로 특정 코드 조회")
//    void 코드_번호로_특정_코드_조회() throws Exception {
//        // 더미 데이터 생성
//        String code = request.getCode();
//
//        // 서비스 목킹
//        // - 서비스 코드 번호로 조회 메서드 호출시 response 데이터 반환하도록 설정
//        when(codeService.read(code)).thenReturn(response);
//
//        // 컨트롤러 요청 보내기 및 결과 비교
//        mockMvc.perform(get("/api/code/" + code))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.cateNum").value(response.getCateNum()))
//                .andExpect(jsonPath("$.code").value(response.getCode()))
//                .andExpect(jsonPath("$.name").value(response.getName()))
//                .andExpect(jsonPath("$.ord").value(response.getOrd()));
//
//    }
//
//    @Test
//    @WithMockUser
//    @DisplayName("각 파트(cateNum)으로 사용 가능한 코드 조회")
//    void 각_파트_cateNum_으로_사용_가능한_코드_조회() throws Exception {
//        // 더미 데이터 생성
//
//        // 서비스 목킹
//        // - 서비스 각 파트별 사용 가능한 코드 조회 메서드 호출시 responses 데이터 반환하도록 설정
//        when(codeService.readAvailableCodeByCateNum("MB")).thenReturn(responses);
//
//        // 컨트롤러 요청 보내기 및 결과 비교
//        mockMvc.perform(get("/api/code/MB/available"))
//               .andExpect(status().isOk())
//
// .andExpect(jsonPath("$.codeResponses[0].cateNum").value(responses.getCodeResponses().get(0).getCateNum()))
//
// .andExpect(jsonPath("$.codeResponses[0].code").value(responses.getCodeResponses().get(0).getCode()))
//
// .andExpect(jsonPath("$.codeResponses[0].name").value(responses.getCodeResponses().get(0).getName()))
//
// .andExpect(jsonPath("$.codeResponses[0].ord").value(responses.getCodeResponses().get(0).getOrd()));
//
//    }
//
//    @Test
//    @WithMockUser
//    @DisplayName("각 파트(cateNum)으로 모든 코드 조회")
//    void 각_파트_cateNum_으로_모든_코드_조회() throws Exception {
//        // 더미 데이터 생성
//
//        // 서비스 목킹
//        // - 서비스 각 파트별 모든 코드 조회 메서드 호출시 responses 데이터 반환하도록 설정
//        when(codeService.readAllByCateNum("MB")).thenReturn(responses);
//
//        // 컨트롤러 요청 보내기 및 결과 비교
//        mockMvc.perform(get("/api/code/MB/all"))
//                .andExpect(status().isOk())
//
// .andExpect(jsonPath("$.codeResponses[0].cateNum").value(responses.getCodeResponses().get(0).getCateNum()))
//
// .andExpect(jsonPath("$.codeResponses[0].code").value(responses.getCodeResponses().get(0).getCode()))
//
// .andExpect(jsonPath("$.codeResponses[0].name").value(responses.getCodeResponses().get(0).getName()))
//
// .andExpect(jsonPath("$.codeResponses[0].ord").value(responses.getCodeResponses().get(0).getOrd()));
//
//
//    }
//
//    @Test
//    @WithMockUser
//    @DisplayName("코드 수정")
//    void 코드_수정() throws Exception {
//        // 더미 데이터 생성
//
//        // 서비스 목킹
//        // - 서비스 시퀀스와 updateRequest 데이터로 update 메서드 호출시 목킹
//        doNothing().when(codeService).update(1L, updateRequest);
//
//        // 컨트롤러 요청 보내기 및 결과 비교
//        mockMvc.perform(patch("/api/code/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updateRequest))
//                        .with(csrf())
//                )
//               .andExpect(status().isNoContent());
//    }
//
//
//    @Test
//    @WithMockUser
//    @DisplayName("코드 삭제 처리")
//    void 코드_수정_실패() throws Exception {
//        // 더미 데이터 생성
//
//        // 서비스 목킹
//        // - 서비스 시퀀스로 삭제 목킹
//        doNothing().when(codeService).delete(1L);
//
//        // 컨트롤러 요청 보내기 및 결과 비교
//        mockMvc.perform(delete("/api/code/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updateRequest))
//                        .with(csrf())
//                )
//               .andExpect(status().isNoContent());
//
//    }
//
//    private void createCodeRequest() {
//        request = CodeRequest.builder()
//                             .cateNum("MB")
//                             .code("MBS01")
//                             .name("신규회원")
//                             .ord(1)
//                             .chkUse("Y")
//                             .build();
//    }
//
//    private void createCodeUpdateRequest() {
//        updateRequest = CodeUpdateRequest.builder()
//                                         .name("변경된 신규회원")
//                                         .ord(1)
//                                         .chkUse("Y")
//                                         .build();
//    }
//
//    private void createCodeResponse() {
//        response = CodeResponse.builder()
//                               .cateNum("MB")
//                               .code("MBS01")
//                               .name("신규회원")
//                               .ord(1)
//                               .chkUse("Y")
//                               .build();
//    }
//
//    private void createCodeResponses() {
//        List<CodeResponse> dummy = new ArrayList<>();
//
//        dummy.add(CodeResponse.builder()
//                              .cateNum("MB")
//                              .code("MBS01")
//                              .name("신규회원")
//                              .ord(1)
//                              .chkUse("Y")
//                              .build());
//
//        dummy.add(CodeResponse.builder()
//                                .cateNum("MB")
//                                .code("MBS02")
//                                .name("기존회원")
//                                .ord(2)
//                                .build());
//
//
//        dummy.add(CodeResponse.builder()
//                                .cateNum("MB")
//                                .code("MBS03")
//                                .name("탈퇴회원")
//                                .ord(3)
//                                .chkUse("N")
//                                .build());
//
//
//        responses = new CodeResponses(dummy);
//    }
// }
