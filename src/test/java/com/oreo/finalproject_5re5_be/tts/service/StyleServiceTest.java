package com.oreo.finalproject_5re5_be.tts.service;

import com.oreo.finalproject_5re5_be.tts.dto.response.StyleListDto;
import com.oreo.finalproject_5re5_be.tts.entity.Style;
import com.oreo.finalproject_5re5_be.tts.repository.StyleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class StyleServiceTest {
    @Autowired
    StyleService styleService;

    @MockBean
    StyleRepository styleRepository;

    /**
     *  [ 스타일 조회 서비스 테스트 ]
     *  1. 여러 개의 데이터가 있는 목록 조회 테스트
     *  2. 빈 목록 조회 테스트
     * */
    @Test
    @DisplayName("스타일 조회 테스트 - 여러 개의 데이터가 있는 목록")
    public void getStyleListTest() {
        // 3개 데이터가 들어간 리스트 생성
        int cnt = 3;
        List<Style> styleList = new ArrayList<>();
        for(int i=0; i<cnt; i++) {
            styleList.add(createStyleEntity(i));
        }

        // style 레파지토리 findAll 메서드의 동작 세팅
        when(styleRepository.findAll()).thenReturn(styleList);

        // 스타일 전체 조회 서비스 실행 및 검증
        StyleListDto styleListDto = styleService.getStyleList();
        assertEquals( styleListDto.getStyleList().get(0).getContents(), styleList.get(0).getContents());
        assertEquals(styleListDto.getStyleList().get(1).getMood(), styleList.get(1).getMood());
        assertEquals(styleListDto.getStyleList().get(2).getDesc(), styleList.get(2).getDescription());
    }

    @Test
    @DisplayName("스타일 조회 테스트 - 빈 목록")
    public void getEmptyStyleListTest() {
        // 빈 리스트 생성
        List<Style> styleList = new ArrayList<>();

        // style 레파지토리 findAll 메서드의 동작 세팅
        when(styleRepository.findAll()).thenReturn(styleList);

        // 스타일 전체 조회 서비스 실행 및 검증
        StyleListDto styleListDto = styleService.getStyleList();
        assertTrue(styleListDto.getStyleList().isEmpty()); // 비어있어야함
    }

    // 스타일 엔티티 생성 메서드
    private Style createStyleEntity(int n) {
        return Style.builder()
                .styleSeq((long)n)
                .name("style-name"+n)
                .mood("style-mood"+n)
                .description("style-desc"+n)
                .isRecommend('n')
                .contents("contents-test"+n)
                .useCnt(n)
                .build();
    }
}