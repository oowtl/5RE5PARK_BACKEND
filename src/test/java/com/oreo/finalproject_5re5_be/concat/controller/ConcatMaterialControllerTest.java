package com.oreo.finalproject_5re5_be.concat.controller;

import com.oreo.finalproject_5re5_be.concat.entity.ConcatRow;
import com.oreo.finalproject_5re5_be.concat.service.MaterialAudioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@WebMvcTest(ConcatMaterialController.class)
class ConcatMaterialControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MaterialAudioService materialAudioService;

    /**
     *  [ 재료 오디오 목록의 행 정보 불러오기 컨트롤러 테스트 ]
     *  1. 존재하는 seq로 불러오기 -> 상태코드 200, ConcatRowListDto 응답
     *  2. 존재하지 않는 seq로 불러오기 -> 상태코드 200, 빈 ConcatRowListDto 응답
     *  3. seq를 작성하지 않음 -> 상태코드 400?
     * */

    // 재료 오디오 목록의 행 정보 불러오기 - 1. 존재하는 seq로 불러오기
    @Test
    @DisplayName("재료 오디오 목록의 행 정보 불러오기 - 존재하는 seq로 불러오기")
    public void getMaterialRowList() throws Exception {
        // 1. given: 재료가 된 행 목록, 결과 seq 준비
        Long resultSeq = 10L;
        ConcatRow concatRow1 = createConcatRowEntity(100);
        ConcatRow concatRow2 = createConcatRowEntity(200);
        ConcatRow concatRow3 = createConcatRowEntity(300);
        List<ConcatRow> concatRowList = List.of(concatRow1, concatRow2, concatRow3);

        // 2. when: 재료가 된 row 목록 조회 서비스 동작 등록
        when(materialAudioService.findConcatRowListByResultSeq(resultSeq)).thenReturn(concatRowList);

        // 3. then: get 요청 결과 검증
        mockMvc.perform(get("/api/concat/audio/materials/rows")
                                .param("concatresultseq", String.valueOf(resultSeq))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 코드 200 이어야 함
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.response.rowList").exists()) // 응답 결과에 rowList가 있어야 함
                .andExpect(jsonPath("$.response.rowList.size()").value(concatRowList.size()))
                .andExpect(jsonPath("$.response.rowList[0].rowText").value(concatRowList.get(0).getRowText()));
    }

    // 재료 오디오 목록의 행 정보 불러오기 - 2. 존재하지 않는 seq로 불러오기
    @Test
    @DisplayName("재료 오디오 목록의 행 정보 불러오기 - 존재하지 않는 seq로 불러오기")
    public void getMaterialRowList_notExistSeq() throws Exception {
        // 1. given: 존재하지 않는 seq 정보와 빈 row 리스트(결과 값) 생성
        Long notExistSeq = -999L;
        List<ConcatRow> emptyConcatRowList = List.of();

        // 2. when: 빈 row 리스트를 반환하도록 재료 row 목록 조회 서비스 동작 설정
        when(materialAudioService.findConcatRowListByResultSeq(notExistSeq)).thenReturn(emptyConcatRowList);

        // 3. then: get 요청 결과 검증
        mockMvc.perform(get("/api/concat/audio/materials/rows")
                        .param("concatresultseq", String.valueOf(notExistSeq))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 코드 200 이어야 함
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.response.rowList").exists()) // 응답 결과에 rowList가 있어야 함
                .andExpect(jsonPath("$.response.rowList.size()").value(0)) // rowList가 비어 있어야 함
                .andExpect(jsonPath("$.response.rowList").isEmpty());
    }

    // 재료 오디오 목록의 행 정보 불러오기 - 3. seq를 작성하지 않음
    @Test
    @DisplayName("재료 오디오 목록의 행 정보 불러오기 - seq를 작성하지 않음")
    public void getMaterialRowList_notWriteSeq() throws Exception {
        // when, then: seq 작성하지 않은 채 요청을 보내면 상태 코드 400번
        mockMvc.perform(get("/api/concat/audio/materials/rows")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    // concatRow 엔티티 생성 메서드
    private ConcatRow createConcatRowEntity(int i) {
        return ConcatRow.builder()
                .concatRowSeq((long)i)
                .rowText("test-concat-row-text"+i)
                .rowIndex(i)
                .silence((float)(i+0.22))
                .status('y')
                .selected('n')
                .build();
    }
}