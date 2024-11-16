package com.oreo.finalproject_5re5_be.member.controller;


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.oreo.finalproject_5re5_be.member.config.MemberSecurityConfig;
import com.oreo.finalproject_5re5_be.member.dto.request.MemberTermConditionRequest;
import com.oreo.finalproject_5re5_be.member.dto.request.MemberTermConditionUpdateRequest;
import com.oreo.finalproject_5re5_be.member.dto.response.MemberTermConditionResponse;
import com.oreo.finalproject_5re5_be.member.dto.response.MemberTermConditionResponses;
import com.oreo.finalproject_5re5_be.member.entity.MemberTermsCondition;
import com.oreo.finalproject_5re5_be.member.exception.MemberTermsConditionNotFoundException;
import com.oreo.finalproject_5re5_be.member.service.MemberTermsConditionServiceImpl;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
class MemberTermConditionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberTermsConditionServiceImpl memberTermsConditionService;

    @BeforeEach
    void setUp() {
        assertNotNull(mockMvc);
        assertNotNull(objectMapper);
        assertNotNull(memberTermsConditionService);
    }

    @DisplayName("회원 약관 등록 처리")
    @Test
    void 회원_약관_등록_처리() throws Exception {
        // 요청 데이터 생성
        MemberTermConditionRequest request = MemberTermConditionRequest.builder()
                .condCode("mtc01")
                .name("이용약관")
                .shortCont("짧은 이용약관 설명")
                .longCont("긴 이용약관 설명")
                .chkUse('Y')
                .ord(1)
                .law1("법률1")
                .law2("법률2")
                .law3("법률3")
                .build();

        MemberTermsCondition savedMemberTermCondition = request.createMemberTermsConditionEntity();
        MemberTermConditionResponse response = new MemberTermConditionResponse(savedMemberTermCondition);

        // 요청 데이터 서비스에 전달하면 생성됐다고 반환하게 설정
        // 목킹 제대로 안되고 null 반환하는 경우 있음. 이 부분 equals, hashcode 구현해서 잡을 수 있음
        when(memberTermsConditionService.create(request)).thenReturn(response);

        // 요청 데이터 컨트롤러에 전달
        // 응답 데이터 확인
        mockMvc.perform(post("/api/member-term-condition/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.condCode").value(response.getCondCode()))
                .andExpect(jsonPath("$.shortCont").value(response.getShortCont()))
                .andExpect(jsonPath("$.longCont").value(response.getLongCont()))
                .andExpect(jsonPath("$.chkUse").value(response.getChkUse().toString()))
                .andExpect(jsonPath("$.ord").value(response.getOrd()))
                .andExpect(jsonPath("$.law1").value(response.getLaw1()))
                .andExpect(jsonPath("$.law2").value(response.getLaw2()))
                .andExpect(jsonPath("$.law3").value(response.getLaw3()));
    }

    @DisplayName("회원 약관 여러개 등록 처리")
    @Test
    void 회원_여러개_약관_등록_처리() throws Exception {
        // 요청 데이터 여러개 생성
        List<MemberTermConditionRequest> requests = new ArrayList<>();
        requests.add(MemberTermConditionRequest.builder()
                .condCode("mtc01")
                .name("이용약관")
                .shortCont("짧은 이용약관 설명")
                .longCont("긴 이용약관 설명")
                .chkUse('Y')
                .ord(1)
                .law1("법률1")
                .law2("법률2")
                .law3("법률3")
                .build());

        requests.add(MemberTermConditionRequest.builder()
                .condCode("mtc02")
                .name("개인정보처리방침")
                .shortCont("짧은 개인정보처리방침 설명")
                .longCont("긴 개인정보처리방침 설명")
                .chkUse('Y')
                .ord(2)
                .law1("법률1")
                .law2("법률2")
                .law3("법률3")
                .build());

        requests.add(MemberTermConditionRequest.builder()
                .condCode("mtc03")
                .name("위치기반서비스 이용약관")
                .shortCont("짧은 위치기반서비스 이용약관 설명")
                .longCont("긴 위치기반서비스 이용약관 설명")
                .chkUse('Y')
                .ord(3)
                .law1("법률1")
                .law2("법률2")
                .law3("법률3")
                .build());

        // 기대 결과 데이터 생성
        List<MemberTermConditionResponse> savedMemberTermsConditions = new ArrayList<>();
        for (MemberTermConditionRequest request : requests) {
            MemberTermsCondition memberTermsConditionEntity = request.createMemberTermsConditionEntity();
            MemberTermConditionResponse memberTermConditionResponse = new MemberTermConditionResponse(memberTermsConditionEntity);
            savedMemberTermsConditions.add(memberTermConditionResponse);
        }

        MemberTermConditionResponses memberTermConditionResponses = new MemberTermConditionResponses(savedMemberTermsConditions);
        when(memberTermsConditionService.create(requests)).thenReturn(memberTermConditionResponses);


        // 컨트롤러에 요청 보내고 응답 확인
        mockMvc.perform(post("/api/member-term-condition/register-all")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requests))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.memberTermConditionResponses[0].condCode").value(savedMemberTermsConditions.get(0).getCondCode()));
    }

    @DisplayName("회원 약관 조회 처리")
    @Test
    void 회원_약관_조회_처리() throws Exception {
        // 서비스에서 응답할 데이터 생성
        MemberTermConditionRequest request = MemberTermConditionRequest.builder()
                .condCode("mtc01")
                .name("이용약관")
                .shortCont("짧은 이용약관 설명")
                .longCont("긴 이용약관 설명")
                .chkUse('Y')
                .ord(1)
                .law1("법률1")
                .law2("법률2")
                .law3("법률3")
                .build();

        MemberTermsCondition savedMemberTermCondition = request.createMemberTermsConditionEntity();
        MemberTermConditionResponse response = new MemberTermConditionResponse(savedMemberTermCondition);

        // 서비스 세팅
        when(memberTermsConditionService.read("mtc01")).thenReturn(response);

        // 컨트롤러 요청 보내기 및 내용 비교
        mockMvc.perform(get("/api/member-term-condition/mtc01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.condCode").value(response.getCondCode()))
                .andExpect(jsonPath("$.shortCont").value(response.getShortCont()))
                .andExpect(jsonPath("$.longCont").value(response.getLongCont()))
                .andExpect(jsonPath("$.chkUse").value(response.getChkUse().toString()))
                .andExpect(jsonPath("$.ord").value(response.getOrd()))
                .andExpect(jsonPath("$.law1").value(response.getLaw1()))
                .andExpect(jsonPath("$.law2").value(response.getLaw2()))
                .andExpect(jsonPath("$.law3").value(response.getLaw3()));
    }

    @DisplayName("회원 약관 모두 조회 처리")
    @Test
    void 회원_약관_모두_조회_처리() throws Exception {
        // 조회될 데이터 생성
        List<MemberTermConditionRequest> dummy = new ArrayList<>();
        dummy.add(MemberTermConditionRequest.builder()
                .condCode("mtc01")
                .name("이용약관")
                .shortCont("짧은 이용약관 설명")
                .longCont("긴 이용약관 설명")
                .chkUse('Y')
                .ord(1)
                .law1("법률1")
                .law2("법률2")
                .law3("법률3")
                .build());

        dummy.add(MemberTermConditionRequest.builder()
                .condCode("mtc02")
                .name("개인정보처리방침")
                .shortCont("짧은 개인정보처리방침 설명")
                .longCont("긴 개인정보처리방침 설명")
                .chkUse('Y')
                .ord(2)
                .law1("법률1")
                .law2("법률2")
                .law3("법률3")
                .build());

        dummy.add(MemberTermConditionRequest.builder()
                .condCode("mtc03")
                .name("위치기반서비스 이용약관")
                .shortCont("짧은 위치기반서비스 이용약관 설명")
                .longCont("긴 위치기반서비스 이용약관 설명")
                .chkUse('Y')
                .ord(3)
                .law1("법률1")
                .law2("법률2")
                .law3("법률3")
                .build());

        // 서비스에서 응답할 데이터 생성
        List<MemberTermConditionResponse> savedMemberTermsConditions = new ArrayList<>();
        for (MemberTermConditionRequest request : dummy) {
            MemberTermsCondition memberTermsConditionEntity = request.createMemberTermsConditionEntity();
            MemberTermConditionResponse response = new MemberTermConditionResponse(
                    memberTermsConditionEntity);
            savedMemberTermsConditions.add(response);
        }
        MemberTermConditionResponses response = new MemberTermConditionResponses(savedMemberTermsConditions);
        when(memberTermsConditionService.readAll()).thenReturn(response);

        // 컨트롤러에 요청 보내기
        // 응답 데이터 확인
        mockMvc.perform(get("/api/member-term-condition/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberTermConditionResponses[0].condCode").value(savedMemberTermsConditions.get(0).getCondCode()));
    }

    @DisplayName("사용 가능한 약관 항목 조회 처리")
    @Test
    void 사용_가능한_약관_항목_조회_처리() throws Exception {
        // 더미 데이터 생성
        List<MemberTermConditionRequest> dummy = new ArrayList<>();
        dummy.add(MemberTermConditionRequest.builder()
                .condCode("mtc01")
                .name("이용약관")
                .shortCont("짧은 이용약관 설명")
                .longCont("긴 이용약관 설명")
                .chkUse('Y')
                .ord(1)
                .law1("법률1")
                .law2("법률2")
                .law3("법률3")
                .build());

        dummy.add(MemberTermConditionRequest.builder()
                .condCode("mtc02")
                .name("개인정보처리방침")
                .shortCont("짧은 개인정보처리방침 설명")
                .longCont("긴 개인정보처리방침 설명")
                .chkUse('Y')
                .ord(2)
                .law1("법률1")
                .law2("법률2")
                .law3("법률3")
                .build());

        dummy.add(MemberTermConditionRequest.builder()
                .condCode("mtc03")
                .name("위치기반서비스 이용약관")
                .shortCont("짧은 위치기반서비스 이용약관 설명")
                .longCont("긴 위치기반서비스 이용약관 설명")
                .chkUse('N')
                .ord(3)
                .law1("법률1")
                .law2("법률2")
                .law3("법률3")
                .build());

        // 서비스에서 응답될 데이터 세팅
        List<MemberTermConditionResponse> savedMemberTermsConditions = new ArrayList<>();
        for (MemberTermConditionRequest request : dummy) {
            if (request.getChkUse().equals('N')) continue;

            MemberTermsCondition memberTermsConditionEntity = request.createMemberTermsConditionEntity();
            MemberTermConditionResponse response = new MemberTermConditionResponse(memberTermsConditionEntity);
            savedMemberTermsConditions.add(response);
        }

        MemberTermConditionResponses response = new MemberTermConditionResponses(savedMemberTermsConditions);
        when(memberTermsConditionService.readAvailable()).thenReturn(response);


        // 컨트롤러에 요청 보내기
        // 응답 데이터 확인
        mockMvc.perform(get("/api/member-term-condition/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberTermConditionResponses[0].condCode").value(savedMemberTermsConditions.get(0).getCondCode()));

    }

    @DisplayName("사용 불가능한 약관 항목 조회 처리")
    @Test
    void 사용_불가능한_약관_항목_조회_처리() throws Exception {
        // 더미 데이터 생성
        List<MemberTermConditionRequest> dummy = new ArrayList<>();
        dummy.add(MemberTermConditionRequest.builder()
                .condCode("mtc01")
                .name("이용약관")
                .shortCont("짧은 이용약관 설명")
                .longCont("긴 이용약관 설명")
                .chkUse('Y')
                .ord(1)
                .law1("법률1")
                .law2("법률2")
                .law3("법률3")
                .build());

        dummy.add(MemberTermConditionRequest.builder()
                .condCode("mtc02")
                .name("개인정보처리방침")
                .shortCont("짧은 개인정보처리방침 설명")
                .longCont("긴 개인정보처리방침 설명")
                .chkUse('Y')
                .ord(2)
                .law1("법률1")
                .law2("법률2")
                .law3("법률3")
                .build());

        dummy.add(MemberTermConditionRequest.builder()
                .condCode("mtc03")
                .name("위치기반서비스 이용약관")
                .shortCont("짧은 위치기반서비스 이용약관 설명")
                .longCont("긴 위치기반서비스 이용약관 설명")
                .chkUse('N')
                .ord(3)
                .law1("법률1")
                .law2("법률2")
                .law3("법률3")
                .build());

        // 서비스에서 응답될 데이터 세팅
        List<MemberTermConditionResponse> savedMemberTermsConditions = new ArrayList<>();
        for (MemberTermConditionRequest request : dummy) {
            if (request.getChkUse().equals('Y')) continue;

            MemberTermsCondition memberTermsConditionEntity = request.createMemberTermsConditionEntity();
            MemberTermConditionResponse response = new MemberTermConditionResponse(memberTermsConditionEntity);
            savedMemberTermsConditions.add(response);
        }

        MemberTermConditionResponses response = new MemberTermConditionResponses(savedMemberTermsConditions);
        when(memberTermsConditionService.readNotAvailable()).thenReturn(response);


        // 컨트롤러에 요청 보내기
        // 응답 데이터 확인
        mockMvc.perform(get("/api/member-term-condition/not-available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberTermConditionResponses[0].condCode").value(savedMemberTermsConditions.get(0).getCondCode()));

    }

    @DisplayName("특정 회원 약관 항목 수정 처리")
    @Test
    void 특정_회원_약관_항목_수정_처리() throws Exception {
        // 업데이트될 더미 데이터 생성
        String condCode = "mtc01";
        MemberTermConditionUpdateRequest request = MemberTermConditionUpdateRequest.builder()
                .shortCont("짧은 이용약관 설명 수정")
                .longCont("긴 이용약관 설명 수정")
                .chkUse('N')
                .ord(2)
                .build();

        // 서비스에서 응답할 데이터 생성
        doNothing().when(memberTermsConditionService).update(condCode, request);

        // 컨트롤러에 요청 보내기
        // 응답 데이터 확인
        mockMvc.perform(patch("/api/member-term-condition/mtc01")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isNoContent());
    }

    @DisplayName("특정 회원 약관 항목 삭제 처리")
    @Test
    void 특정_회원_약관_항목_삭제_처리() throws Exception {
        // 삭제될 더미 데이터 생성 및 세팅
        String condCode = "mtc01";

        // 서비스에서 응답 동작 세팅
        doNothing().when(memberTermsConditionService).remove(condCode);

        // 컨트롤러에 요청 보내기
        // 응답 데이터 확인
        mockMvc.perform(delete("/api/member-term-condition/mtc01"))
                .andExpect(status().isNoContent());
    }

    @DisplayName("회원 약관 항목 등록 실패시 예외 처리")
    @Test
    void 회원_약관_항목_등록_실패시_예외_처리() throws Exception{
        // 예외 발생 시킬 더미 데이터 세팅
        MemberTermConditionRequest request = MemberTermConditionRequest.builder()
                .shortCont("짧은 이용약관 설명 수정")
                .longCont("긴 이용약관 설명 수정")
                .chkUse(null)
                .ord(null)
                .build();

        // 컨트롤러에 요청 보내기
        // 예외 처리 확인
        mockMvc.perform(post("/api/member-term-condition/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isBadRequest());
    }

    @DisplayName("회원 약관 항목 수정 실패시 예외 처리")
    @Test
    void 회원_약관_항목_수정_실패시_예외_처리() throws Exception {
        // 예외 발생 시킬 더미 데이터 세팅
        MemberTermConditionUpdateRequest request = MemberTermConditionUpdateRequest.builder()
                .shortCont("짧은 이용약관 설명 수정")
                .longCont("긴 이용약관 설명 수정")
                .chkUse(null)
                .ord(null)
                .build();

        // 컨트롤러에 요청 보내기
        // 예외 처리 확인
        mockMvc.perform(post("/api/member-term-condition/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());
    }

    @DisplayName("회원 약관 항목 조회 실패시 예외 처리")
    @Test
    void 회원_약관_항목_조회_실패시_예외_처리() throws Exception {
        // 예외 발생 시킬 더미 데이터 세팅
        String condCode = "mtc01";
        // 서비스에서 예외 발생하게 설정
        when(memberTermsConditionService.read(condCode)).thenThrow(MemberTermsConditionNotFoundException.class);

        // 컨트롤러에 요청 보내기
        // 예외 처리 확인
        mockMvc.perform(get("/api/member-term-condition/mtc01"))
                .andExpect(status().isNotFound());
    }
}