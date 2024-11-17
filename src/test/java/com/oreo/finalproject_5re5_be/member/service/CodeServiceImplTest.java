package com.oreo.finalproject_5re5_be.member.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.oreo.finalproject_5re5_be.member.dto.request.CodeRequest;
import com.oreo.finalproject_5re5_be.member.dto.request.CodeUpdateRequest;
import com.oreo.finalproject_5re5_be.member.dto.response.CodeResponse;
import com.oreo.finalproject_5re5_be.member.dto.response.CodeResponses;
import com.oreo.finalproject_5re5_be.member.entity.Code;
import com.oreo.finalproject_5re5_be.member.exception.CodeDuplicatedException;
import com.oreo.finalproject_5re5_be.member.repository.CodeRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CodeServiceImplTest {

    @Mock
    private CodeRepository codeRepository;

    @InjectMocks
    private CodeServiceImpl codeService;

    private CodeRequest request;
    private CodeResponse response;
    private List<Code> dummy;

    @BeforeEach
    void setUp() {
        // 자동 주입 확인
        assertNotNull(codeRepository);
        assertNotNull(codeService);

        // 더미 데이터 생성
        createCodeRequest();
        createCodeResponse();
        createDummy();
    }

    // 코드 등록
    @DisplayName("코드 등록 처리")
    @Test
    void 코드_생성_처리() {
        // 유효성 검증이 완료된 더미 데이터 생성
        // 기대 결과 생성
        Code requestCodeEntity = request.createCodeEntity();
        Code savedCode = Code.builder()
                            .codeSeq(1L)
                            .cateNum("MB")
                            .code("MBS01")
                            .name("신규회원")
                            .ord(1)
                            .chkUse("Y")
                            .comt("신규회원 코드입니다.")
                            .build();

        createCodeResponse();


        // 리포지토리 목킹
        // - 중복여부 false 설정
        // - 저장된 엔티티 반환
        when(codeRepository.existsByCode(anyString())).thenReturn(false);
        when(codeRepository.save(requestCodeEntity)).thenReturn(savedCode);

        // 서비스 메서드 호출
        // 기대한 결과와 반환된 결과 비교
        CodeResponse actualResponse = codeService.create(request);
        assertEquals(response, actualResponse);

    }

    @DisplayName("중복된 코드명 예외 처리")
    @Test
    void 중복된_코드명_예외_처리() {
        // 더미 데이터 생성

        // 리포지토리 목킹
        // - 중복여부 true 설정
        when(codeRepository.existsByCode(anyString())).thenReturn(true);

        // 서비스 메서드 호출
        // 예외 발생 확인
        assertThrows(CodeDuplicatedException.class, () -> codeService.create(request));

    }

    // 코드 조회
    @DisplayName("모든 코드 조회")
    @Test
    void 모든_코드_조회() {
        // 기대한 결과 생성
        List<CodeResponse> responseList = dummy.stream().map(CodeResponse::of).toList();
        CodeResponses expectedResponses = CodeResponses.of(responseList);

        // 리포지토리 목킹
        // - 여러 코드 엔티티 반환 설정
        when(codeRepository.findAll()).thenReturn(dummy);

        // 서비스 메서드 호출
        // 기대한 결과와 반환된 결과 비교
        CodeResponses actualResponses = codeService.readAll();
        assertEquals(expectedResponses, actualResponses);
    }

    @DisplayName("시퀀스로 조회")
    @Test
    void 시퀀스로_조회() {
        // 기대한 결과 생성
        Code expectedCode = dummy.get(0);
        CodeResponse expectedResponse = CodeResponse.of(expectedCode);

        // 리포지토리 목킹
        // - 시퀀스로 조회한 코드 엔티티 반환 설정
        when(codeRepository.findCodeByCodeSeq(1L)).thenReturn(expectedCode);

        // 서비스 메서드 호출
        // 기대한 결과와 반환된 결과 비교
        CodeResponse actualResponse = codeService.read(1L);
        assertEquals(expectedResponse, actualResponse);

    }


    @DisplayName("코드 번호로 특정 코드 조회")
    @Test
    void 코드_번호로_특정_코드_조회() {
        // 기대한 결과 생성
        Code expectedCode = dummy.get(0);
        CodeResponse expectedResponse = CodeResponse.of(expectedCode);

        // 리포지토리 목킹
        // - 특정 코드 번호로 조회시 반환할 코드 엔티티 설정
        when(codeRepository.findCodeByCode("MBS01")).thenReturn(expectedCode);

        // 서비스 메서드 호출
        // 기대한 결과와 반환된 결과 비교
        CodeResponse actualResponse = codeService.read("MBS01");
        assertEquals(expectedResponse, actualResponse);
    }

    @DisplayName("각 파트별 사용 가능한 코드 조회")
    @Test
    void 각_파트별_사용_가능한_코드_조회() {
        // 기대한 결과 생성
        List<Code> foundCodes = dummy.stream()
                .filter(code -> code.getCateNum().equals("MB"))
                .filter(code -> code.getChkUse().equals("Y"))
                .toList();
        List<CodeResponse> responseList = foundCodes.stream()
                .map(CodeResponse::of)
                .toList();
        CodeResponses expectedResponses = CodeResponses.of(responseList);

        // 리포지토리 목킹
        // - 특정 파트 번호(cateNum)으로 조회시 반환할 코드 엔티티 더미 설정
        when(codeRepository.findAvailableCodesByCateNum("MB")).thenReturn(foundCodes);

        // 서비스 메서드 호출
        // 기대한 결과와 반환된 결과 비교
        CodeResponses actualResponses = codeService.readAvailableCodeByCateNum("MB");
        assertEquals(expectedResponses, actualResponses);
    }

    @DisplayName("각 파트별 모든 코드 조회")
    @Test
    void 각_파트별_모든_코드_조회() {
        // 기대한 결과 생성
        List<Code> foundCodes = dummy.stream()
                .filter(code -> code.getCateNum().equals("MB"))
                .toList();
        List<CodeResponse> responseList = foundCodes.stream()
                .map(CodeResponse::of)
                .toList();
        CodeResponses expectedResponses = CodeResponses.of(responseList);

        // 리포지토리 목킹
        // - 특정 파트 번호(cateNum)으로 조회시 반환할 코드 엔티티 더미 설정
        when(codeRepository.findCodesByCateNum("MB")).thenReturn(foundCodes);

        // 서비스 메서드 호출
        // 기대한 결과와 반환된 결과 비교
        CodeResponses actualResponses = codeService.readAllByCateNum("MB");
        assertEquals(expectedResponses, actualResponses);
    }


    // 코드 수정
    @DisplayName("코드 수정 처리")
    @Test
    void 코드_수정_처리() {
        // 더미 데이터 세팅
        CodeUpdateRequest updateRequest = CodeUpdateRequest.builder()
                                                            .name("신규회원 수정")
                                                            .ord(2)
                                                            .chkUse("N")
                                                            .comt("신규회원 수정 코드입니다.")
                                                            .build();

        // 리포지토리 목킹
        // - 시퀀스로 조회한 코드 엔티티 반환 설정
        when(codeRepository.findCodeByCodeSeq(1L)).thenReturn(dummy.get(0));

        // 서비스 호출
        assertDoesNotThrow(() -> codeService.update(1L, updateRequest));
    }


    // 코드 삭제

    @DisplayName("코드 삭제 처리")
    @Test
    void 코드_삭제_처리() {
        // 더미 데이터 세팅

        // 리포지토리 목킹
        when(codeRepository.findCodeByCodeSeq(1L)).thenReturn(dummy.get(0));

        // 서비스 호출
        assertDoesNotThrow(() -> codeService.delete(1L));
    }


    private void createCodeRequest() {
        request = CodeRequest.builder()
                .cateNum("MB")
                .code("MBS01")
                .name("신규회원")
                .ord(1)
                .chkUse("Y")
                .comt("신규회원 코드입니다.")
                .build();
    }

    private void createCodeResponse() {
        response = CodeResponse.builder()
                .codeSeq(1L)
                .cateNum("MB")
                .code("MBS01")
                .name("신규회원")
                .ord(1)
                .chkUse("Y")
                .comt("신규회원 코드입니다.")
                .build();
    }

    private void createDummy() {
        dummy = List.of(
                Code.builder()
                        .codeSeq(1L)
                        .cateNum("MB")
                        .code("MBS01")
                        .name("신규회원")
                        .ord(1)
                        .chkUse("Y")
                        .comt("신규회원 코드입니다.")
                        .build(),
                Code.builder()
                        .codeSeq(2L)
                        .cateNum("MB")
                        .code("MBS02")
                        .name("기존회원")
                        .ord(2)
                        .chkUse("Y")
                        .comt("기존회원 코드입니다.")
                        .build(),
                Code.builder()
                        .codeSeq(3L)
                        .cateNum("MB")
                        .code("MBS03")
                        .name("탈퇴회원")
                        .ord(3)
                        .chkUse("Y")
                        .comt("탈퇴회원 코드입니다.")
                        .build(),
                Code.builder()
                        .codeSeq(4L)
                        .cateNum("TTS")
                        .code("TTS01")
                        .name("생성")
                        .ord(3)
                        .chkUse("Y")
                        .comt("TTS 작업 코드입니다.")
                        .build()
        );
    }

}