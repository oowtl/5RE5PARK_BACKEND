package com.oreo.finalproject_5re5_be.tts.repository;

import com.oreo.finalproject_5re5_be.tts.entity.Style;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class StyleRepositoryTest {

    @Autowired
    private StyleRepository styleRepository;

    /*
    StyleRepository 생성 테스트
    1. 생성 테스트 - 전체 데이터 입력
    2. 생성 테스트 - 일부 데이터 입력 (필수 값 포함)
    3. 생성 테스트 - 일부 데이터 입력 (필수 값 미포함)

    StyleRepository 조회 테스트
    1. 조회 테스트 - 단건 데이터 조회
    2. 조회 테스트 - 다건 데이터 조회

    StyleRepository 수정 테스트
    1. 수정 테스트 - 전체 데이터 수정
    2. 수정 테스트 - 일부 데이터 수정 (필수 값 변경 포함)
    3. 수정 테스트 - 일부 데이터 수정 (필수 값 변경 제외)
    4. 수정 테스트 - 일부 데이터 수정 (필수 값 제외 변경)

    StyleRepository 삭제 테스트
    1. 삭제 테스트 - 데이터 삭제
     */

    // StyleRepository 생성 테스트
    // 1. 생성 테스트 - 전체 데이터 입력
    @Test
    @DisplayName("StyleRepository 생성 테스트 - 전체 데이터 입력")
    public void createEntireDataTest() {
        // given
        // 1. Style Entity 객체 생성
        Style createStyle = createStyleEntity();

        // when
        // 2. Style Entity 객체 저장
        Style savedStyle = styleRepository.save(createStyle);
        // 조회용 seq 저장
        Long savedStyleSeq = savedStyle.getStyleSeq();

        // then
        // 3. Style Entity 객체 조회
        Optional<Style> optionalResearchedStyle = styleRepository.findById(savedStyleSeq);
        assertTrue(optionalResearchedStyle.isPresent());

        // 4. 조회한 Style Entity 객체의 값 검증
        Style researchedStyle = optionalResearchedStyle.get();
        assertEquals(researchedStyle, savedStyle);
    }

    // 2. 생성 테스트 - 일부 데이터 입력 (필수 값 포함)
    @Test
    @DisplayName("StyleRepository 생성 테스트 - 일부 데이터 입력 (필수 값 포함)")
    public void createPartialDataWithRequiredTest() {
        // given
        // 1. Style Entity 객체 생성
        Style createStyle = Style.builder()
                .name("styleName")
                .mood("styleMood")
                .build();

        // when
        // 2. Style Entity 객체 저장
        Style savedStyle = styleRepository.save(createStyle);
        // 조회용 seq 저장
        Long savedStyleSeq = savedStyle.getStyleSeq();

        // then
        // 3. Style Entity 객체 조회
        Optional<Style> optionalResearchedStyle = styleRepository.findById(savedStyleSeq);
        assertTrue(optionalResearchedStyle.isPresent());

        // 4. 조회한 Style Entity 객체의 값 검증
        Style researchedStyle = optionalResearchedStyle.get();
        assertEquals(researchedStyle, savedStyle);
    }

    // 3. 생성 테스트 - 일부 데이터 입력 (필수 값 미포함)
    @Test
    @DisplayName("StyleRepository 생성 테스트 - 일부 데이터 입력 (필수 값 미포함)")
    public void createPartialDataWithoutRequiredTest() {
        // given
        // 1. Style Entity 객체 생성
        Style createStyle = Style.builder()
                .mood("styleMood")
                .contents("styleContents")
                .description("styleDescription")
                .useCnt(0)
                .isRecommend('N')
                .build();

        // when, then
        // 2. Style Entity 객체 저장
        assertThrows(DataIntegrityViolationException.class, () -> styleRepository.save(createStyle));
    }

    // 1. 조회 테스트 - 단건 데이터 조회
    @Test
    @DisplayName("StyleRepository 조회 테스트 - 단건 데이터 조회")
    public void searchOneDataTest() {
        // given
        // 1. Style Entity 객체 생성
        Style createStyle = createStyleEntity();

        // 2. Style Entity 객체 저장
        Style savedStyle = styleRepository.save(createStyle);
        // 조회용 seq 저장
        Long savedStyleSeq = savedStyle.getStyleSeq();

        // when
        // 3. Style Entity 객체 조회
        Optional<Style> optionalResearchedStyle = styleRepository.findById(savedStyleSeq);
        assertTrue(optionalResearchedStyle.isPresent());

        // then
        Style researchedStyle = optionalResearchedStyle.get();
        assertEquals(researchedStyle, savedStyle);
    }

    // 2. 조회 테스트 - 다건 데이터 조회
    @Test
    @DisplayName("StyleRepository 조회 테스트 - 다건 데이터 조회")
    public void searchManyDataTest() {
        // given
        int repeatCnt = 10;
        List<Style> sytleList = new ArrayList<Style>();

        // 1. Style Entity 객체 생성 및 저장
        for (int i = 0; i < repeatCnt; i++) {
            Style createStyle = createStyleEntity(i);
            styleRepository.save(createStyle);

            // 조회용 리스트에 저장
            sytleList.add(createStyle);
        }

        // when, then
        // 2. Style Entity 객체 전체 조회
        for (Style style : sytleList) {
            // 3. Style Entity 조회 및 존재 여부 검증
            Optional<Style> optionalResearchedStyle = styleRepository.findById(style.getStyleSeq());
            assertTrue(optionalResearchedStyle.isPresent());

            Style researchedStyle = optionalResearchedStyle.get();
            assertEquals(researchedStyle, style);
        }
    }

    // 1. 수정 테스트 - 전체 데이터 수정
    @Test
    @DisplayName("StyleRepository 수정 테스트 - 전체 데이터 수정")
    public void updateEntireDataTest() {
        // given
        String updateName = "updateStyleName";
        String updateMood = "updateStyleMood";
        String updateContents = "updateStyleContents";
        String updateDescription = "updateStyleDescription";
        int updateUseCnt = 3;
        char updateIsRecommend = 'N';

        // 1. Style Entity 객체 생성
        Style createStyle = createStyleEntity();

        // 2. Style Entity 객체 저장
        Style savedStyle = styleRepository.save(createStyle);
        // 조회용 seq 저장
        Long savedStyleSeq = savedStyle.getStyleSeq();

        // when
        // 4. 조회한 seq 로 Style Entity 객체 수정
        Style updateStyle = Style.builder()
                .styleSeq(savedStyleSeq)
                .name(updateName)
                .mood(updateMood)
                .contents(updateContents)
                .description(updateDescription)
                .useCnt(updateUseCnt)
                .isRecommend(updateIsRecommend)
                .build();

        // 5. 수정된 Style Entity 객체 저장
        Style manipulatedStyle = styleRepository.save(updateStyle);

        // then
        // 6. 수정된 Style Entity 객체 조회
        Optional<Style> optionalResearchedStyle = styleRepository.findById(manipulatedStyle.getStyleSeq());
        assertTrue(optionalResearchedStyle.isPresent());

        // 7. 수정된 Style Entity 객체의 값 검증
        Style researchedStyle = optionalResearchedStyle.get();
        assertEquals(updateStyle, researchedStyle);
    }

    // 2. 수정 테스트 - 일부 데이터 수정 (필수 값 변경 포함)
    @Test
    @DisplayName("StyleRepository 수정 테스트 - 일부 데이터 수정 (필수 값 변경 포함)")
    public void updatePartialDataWithRequiredTest() {
        // given
        String updateName = "updateStyleName";
        String updateMood = "updateStyleMood";

        // 1. Style Entity 객체 생성
        Style createStyle = createStyleEntity();

        // 2. Style Entity 객체 저장
        Style savedStyle = styleRepository.save(createStyle);
        assertEquals(createStyle, savedStyle);

        // 조회용 seq 저장
        Long savedStyleSeq = savedStyle.getStyleSeq();

        // when
        // 3. 조회한 seq 로 Style Entity 객체 수정
        Style updateStyle = Style.builder()
                .styleSeq(savedStyleSeq)
                .name(updateName)
                .mood(updateMood)
                .build();

        // 4. 수정된 Style Entity 객체 저장
        Style manipulatedStyle = styleRepository.save(updateStyle);
        assertEquals(updateStyle, manipulatedStyle);

        // then
        // 5. 수정된 Style Entity 객체 조회
        Optional<Style> optionalResearchedStyle = styleRepository.findById(manipulatedStyle.getStyleSeq());
        assertTrue(optionalResearchedStyle.isPresent());

        // 6. 수정된 Style Entity 객체의 값 검증
        Style researchedStyle = optionalResearchedStyle.get();
        assertEquals(updateStyle, researchedStyle);
    }

    // 3. 수정 테스트 - 일부 데이터 수정 (필수 값 변경 제외)
    @Test
    @DisplayName("StyleRepository 수정 테스트 - 일부 데이터 수정 (필수 값 변경 제외)")
    public void updatePartialDataWithoutRequiredTest() {
        // given
        String updateMood = "updateStyleMood";
        String updateContents = "updateStyleContents";
        String updateDescription = "updateStyleDescription";
        int updateUseCnt = 3;
        char updateIsRecommend = 'N';

        // 1. Style Entity 객체 생성
        Style createStyle = createStyleEntity();

        // 2. Style Entity 객체 저장
        Style savedStyle = styleRepository.save(createStyle);
        assertEquals(createStyle, savedStyle);

        // 조회용 seq 저장
        Long savedStyleSeq = savedStyle.getStyleSeq();

        // when
        // 3. 조회한 seq 로 Style Entity 객체 수정
        Style updateStyle = savedStyle.toBuilder()
                .mood(updateMood)
                .contents(updateContents)
                .description(updateDescription)
                .useCnt(updateUseCnt)
                .isRecommend(updateIsRecommend)
                .build();

        // 4. 수정된 Style Entity 객체 저장
        Style manipulatedStyle = styleRepository.save(updateStyle);
        assertEquals(updateStyle, manipulatedStyle);

        // then
        // 5. 수정된 Style Entity 객체 조회
        Optional<Style> optionalResearchedStyle = styleRepository.findById(manipulatedStyle.getStyleSeq());
        assertTrue(optionalResearchedStyle.isPresent());

        // 6. 수정된 Style Entity 객체의 값 검증
        Style researchedStyle = optionalResearchedStyle.get();
        assertEquals(updateStyle, researchedStyle);
    }

    // 4. 수정 테스트 - 데이터 수정 실패 (존재하지 않는 데이터)
    @Test
    @DisplayName("StyleRepository 수정 테스트 - 일부 데이터 수정 (필수 값 제외 변경)")
    public void updateFailTest() {
        // given
        String updateStyleMood = "updateStyleMood";
        // 1. Style Entity 객체 생성
        Style createStyle = createStyleEntity();

        // 2. Style Entity 객체 저장
        Style savedStyle = styleRepository.save(createStyle);
        assertEquals(createStyle, savedStyle);

        // 수정용 seq 저장
        Long savedStyleSeq = savedStyle.getStyleSeq();

        // when, then
        // 3. 존재하지 않는 seq 로 Style Entity 객체 수정
        Style updateStyle = Style.builder()
                .styleSeq(savedStyleSeq)
                .mood(updateStyleMood)
                .build();

        // 4. 수정된 Style Entity 객체 저장
        assertThrows(DataIntegrityViolationException.class, () -> styleRepository.save(updateStyle));
    }

    // 1. 삭제 테스트 - 데이터 삭제
    @Test
    @DisplayName("StyleRepository 삭제 테스트 - 데이터 삭제")
    public void deleteDataTest() {
        // given
        // 1. Style Entity 객체 생성
        Style createStyle = createStyleEntity();

        // 2. Style Entity 객체 저장
        Style savedStyle = styleRepository.save(createStyle);
        assertEquals(createStyle, savedStyle);

        // 삭제용 seq 저장
        Long savedStyleSeq = savedStyle.getStyleSeq();

        // when
        // 3. 저장한 Style Entity 객체 삭제
        styleRepository.deleteById(savedStyleSeq);

        // then
        // 4. 삭제한 Style Entity 객체 조회
        Optional<Style> optionalResearchedStyle = styleRepository.findById(savedStyleSeq);
        assertTrue(optionalResearchedStyle.isEmpty());
    }


    // Style Entity 객체 생성 메서드
    private Style createStyleEntity() {
        return Style.builder()
                .name("styleName")
                .mood("styleMood")
                .contents("styleContents")
                .description("styleDescription")
                .useCnt(0)
                .isRecommend('N')
                .build();
    }

    private Style createStyleEntity(int cnt) {
        return Style.builder()
                .name("styleName" + cnt)
                .mood("styleMood" + cnt)
                .contents("styleContents" + cnt)
                .description("styleDescription" + cnt)
                .useCnt(cnt)
                .isRecommend('N')
                .build();
    }
}
