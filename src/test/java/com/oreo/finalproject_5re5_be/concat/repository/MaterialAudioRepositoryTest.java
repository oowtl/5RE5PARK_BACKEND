package com.oreo.finalproject_5re5_be.concat.repository;

import com.oreo.finalproject_5re5_be.concat.entity.*;
import com.oreo.finalproject_5re5_be.member.entity.Member;
import com.oreo.finalproject_5re5_be.member.repository.MemberRepository;
import com.oreo.finalproject_5re5_be.project.entity.Project;
import com.oreo.finalproject_5re5_be.project.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class MaterialAudioRepositoryTest {
    @Autowired
    private ConcatRowRepository concatRowRepository;
    @Autowired
    private AudioFileRepository audioFileRepository;
    @Autowired
    private ConcatResultRepository concatResultRepository;
    @Autowired
    private MaterialAudioRepository materialAudioRepository;
    @Autowired
    private ConcatTabRepository concatTabRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ProjectRepository projectRepository;


    /**
    *  [ result seq로 concat row 리스트 읽기 테스트 ]
    *   1. 재료 2개로 만든 resultSeq로 조회 - 성공
    *   2. 존재하지 않는 resultSeq로 조회 - 실패
    * */

    // result seq로 concat row 리스트 읽기 테스트 -  1. 재료 2개로 만든 resultSeq로 조회 - 성공
    @Test
    @Transactional
    @DisplayName("resultSeq로 concatRow 리스트 읽기 테스트 - 재료 2개 넣고 제대로 읽히는 지 확인")
    public void findConcatRowListByResultSeqTest() {
        // 1. Given: concatTab, concatResult, concatRow, audioFile, materialAudio 생성 및 저장

        Member member = Member.builder().seq(1L).id("id").email("email").name("name").password("123")
                .birthDate(LocalDateTime.now().toString()).chkValid('Y')
                .detailAddr("").build();
        Member save1 = memberRepository.save(member);
        Project project = Project.builder().member(save1).proSeq(1L).build();
        Project save = projectRepository.save(project);
        // 1-1. ConcatTab 저장
        ConcatTab concatTab = ConcatTab.builder().status('Y').frontSilence(0.0f).option(null).project(save).build();
        ConcatTab savedConcatTab = concatTabRepository.save(concatTab);
        assertNotNull(savedConcatTab);

        // 1-2. ConcatResult 저장 (concatTab과 연결)
        ConcatResult concatResult = ConcatResult.builder()
                .concatTab(savedConcatTab) // 연관 관계 설정
                .audioUrl("test/result-audio.test")
                .extension(".result")
                .fileLength(0.0f)
                .fileName("test-result-audio-name")
                .build();
        ConcatResult savedConcatResult = concatResultRepository.save(concatResult);
        assertNotNull(savedConcatResult);

        // 1-3. ConcatRow와 AudioFile 저장
        ConcatRow concatRow1 = createConcatRowEntity(100);
        ConcatRow concatRow2 = createConcatRowEntity(200);
        List<ConcatRow> savedConcatRowList = concatRowRepository.saveAll(List.of(concatRow1, concatRow2));
        assertNotNull(savedConcatRowList);

        AudioFile afOfConcatRow1 = createAudioFileEntity(1000, concatRow1);
        AudioFile afOfConcatRow2 = createAudioFileEntity(1000, concatRow2);
        List<AudioFile> savedAudioFileList = audioFileRepository.saveAll(List.of(afOfConcatRow1, afOfConcatRow2));
        assertNotNull(savedAudioFileList);

        // 1-4. MaterialAudio 저장 (concatResult와 audioFile 연결)
        MaterialAudio maOfConcatRow1af = createMaterialAudioEntity(savedConcatResult, afOfConcatRow1);
        MaterialAudio maOfConcatRow2af = createMaterialAudioEntity(savedConcatResult, afOfConcatRow2);
        List<MaterialAudio> savedMaterialAudioList = materialAudioRepository.saveAll(List.of(maOfConcatRow1af, maOfConcatRow2af));
        assertNotNull(savedMaterialAudioList);

        // 2. When: 저장된 concatResultSeq로 row 리스트 읽기
        Long concatResultSeq = savedConcatResult.getConcatResultSequence();
        assertTrue(concatResultRepository.findById(concatResultSeq).isPresent());
        List<ConcatRow> findConcatRowListByResultSeq = materialAudioRepository.findConcatRowListByConcatResultSeq(concatResultSeq);

        // 3. Then: 저장된 ConcatRow와 조회한 ConcatRow 비교
        assertEquals(findConcatRowListByResultSeq.size(), savedConcatRowList.size());
        assertTrue(savedConcatRowList.containsAll(findConcatRowListByResultSeq));
    }

    // result seq로 concat row 리스트 읽기 테스트 - 2. 존재하지 않는 resultSeq로 조회
    @Test
    @DisplayName("resultSeq로 concatRow 리스트 읽기 테스트 - 존재하지 않는 resultSeq로 조회")
    public void findConcatRowListByResultSeqTest_NotExistSeq() {
        // given: 존재하지 않는 resultSeq 세팅
        Long notSeq = -999L;
        assertTrue(concatResultRepository.findById(notSeq).isEmpty());

        // when, then: 존재하지 않는 resultSeq로 읽을 경우 빈 리스트 반환
        List<ConcatRow> findConcatRowList = materialAudioRepository.findConcatRowListByConcatResultSeq(notSeq);
        assertTrue(findConcatRowList.isEmpty());

    }

    // concatRow 엔티티 생성 메서드
    private ConcatRow createConcatRowEntity(int i) {
        return ConcatRow.builder()
                .rowText("여기 왜 테스트 코드가 없죠?")
                .rowIndex(i)
                .silence((float)(i+0.22))
                .status('y')
                .selected('n')
                .build();
    }

    // audioFile 엔티티 생성 메서드
    private AudioFile createAudioFileEntity(int i, ConcatRow concatRow) {
        return AudioFile.builder()
                .concatRow(concatRow)
                .audioUrl("test/audio.test")
                .extension(".test")
                .fileLength((long)i*1000)
                .fileSize((long)i*1024)
                .fileName("test-audio-name")
                .createdDate(LocalDateTime.now())
                .build();
    }

    // concatResult 엔티티 생성 메서드
    private ConcatResult createConcatResultEntity(int i) {
        return ConcatResult.builder()
                .concatResultSequence((long)i)
                .audioUrl("test/result-audio.test")
                .extension(".result")
                .fileLength(0.0f)
                .fileName("test-result-audio-name")
                .build();
    }

    // materialAudio 엔티티 생성 메서드
    private MaterialAudio createMaterialAudioEntity(ConcatResult concatResult, AudioFile audioFile) {
        return MaterialAudio.builder()
                .concatResult(concatResult)
                .audioFile(audioFile)
                .build();
    }

}