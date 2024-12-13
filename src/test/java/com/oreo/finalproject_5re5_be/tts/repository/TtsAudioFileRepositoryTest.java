package com.oreo.finalproject_5re5_be.tts.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.oreo.finalproject_5re5_be.tts.entity.TtsAudioFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class TtsAudioFileRepositoryTest {

    @Autowired private TtsAudioFileRepository ttsAudioFileRepository;

    /*
    TtsAudiFileRepository 생성 테스트
    1. 생성 테스트 - 전체 데이터 입력
    2. 생성 테스트 - 일부 데이터 입력 (필수 값 포함)
    3. 생성 테스트 - 일부 데이터 입력 (필수 값 미포함)
    4. 생성 테스트 - 데이터 미포함

    TtsAudiFileRepository 조회 테스트
    1. 조회 테스트 - 단건 전체 데이터 조회
    2. 조회 테스트 - 다건 전체 데이터 조회

    TtsAudiFileRepository 수정 테스트
    1. 수정 테스트 - 전체 데이터 수정
    2. 수정 테스트 - 일부 데이터 수정
    3. 수정 테스트 - 일부 데이터 수정 시 필수 값 미포함

    TtsAudiFileRepository 삭제 테스트
    1. 삭제 테스트
     */

    // 1. 생성 테스트 - 전체 데이터 입력
    @Test
    @DisplayName("TtsAudioFile 생성 테스트 - 전체 데이터 입력")
    public void createEntireData() {
        // given
        // 1. TtsAudioFile 객체 생성
        TtsAudioFile createTtsAudioFile =
                TtsAudioFile.builder()
                        .audioName("testAudio1")
                        .audioPath("testUrl.com")
                        .audioExtension("wav")
                        .audioSize("1MB")
                        .audioTime(1000)
                        .downloadCount(1)
                        .downloadYn('Y')
                        .audioPlayYn('Y')
                        .build();

        // when
        // 2. TtsAudioFileRepository.save()
        TtsAudioFile savedTtsAudioFile = ttsAudioFileRepository.save(createTtsAudioFile);
        // 조회용 seq 값 저장
        Long savedTtsAudioFileTtsAudioSeq = savedTtsAudioFile.getTtsAudioSeq();

        // then
        // 3. 생성된 객체 조회 및 검증
        Optional<TtsAudioFile> optionalTtsAudioFile =
                ttsAudioFileRepository.findById(savedTtsAudioFileTtsAudioSeq);
        assertTrue(optionalTtsAudioFile.isPresent());

        // 4. 생성된 객체의 값과 조회된 객체의 값 비교
        TtsAudioFile searchedTtsAudioFile = optionalTtsAudioFile.get();
        assertEquals(createTtsAudioFile, searchedTtsAudioFile);
    }

    // 2. 생성 테스트 - 일부 데이터 입력 (필수 값 포함)
    @Test
    @DisplayName("TtsAudioFile 생성 테스트 - 일부 데이터 입력 (필수 값만 포함)")
    public void createPartialDataWithOnlyRequired() {
        // given
        // 1. TtsAudioFile 객체 생성 (필수 값만 포함)
        TtsAudioFile createTtsAudioFile =
                TtsAudioFile.builder().audioName("testAudio2").audioPath("testUrl2.com").build();

        // when
        // 2. TtsAudioFileRepository.save()
        TtsAudioFile savedTtsAudioFile = ttsAudioFileRepository.save(createTtsAudioFile);

        // then
        // 3. 생성된 객체 조회 및 검증
        Optional<TtsAudioFile> optionalTtsAudioFile =
                ttsAudioFileRepository.findById(savedTtsAudioFile.getTtsAudioSeq());
        assertTrue(optionalTtsAudioFile.isPresent());

        // 4. 생성된 객체의 값과 조회된 객체의 값 비교
        TtsAudioFile searchedTtsAudioFile = optionalTtsAudioFile.get();
        assertEquals(createTtsAudioFile, searchedTtsAudioFile);
    }

    @Test
    @DisplayName("TtsAudioFile 생성 테스트 - 일부 데이터 입력 (필수 값 포함)")
    public void createPartialDataWithRequired() {
        // given
        // 1. TtsAudioFile 객체 생성 (필수 값 포함)
        TtsAudioFile createTtsAudioFile =
                TtsAudioFile.builder()
                        .audioName("testAudio3")
                        .audioPath("testUrl3.com")
                        .audioExtension("wav")
                        .audioSize("1MB")
                        .audioTime(1000)
                        .build();

        // when
        // 2. TtsAudioFileRepository.save()
        TtsAudioFile savedTtsAudioFile = ttsAudioFileRepository.save(createTtsAudioFile);

        // then
        // 3. 생성된 객체 조회 및 검증
        Optional<TtsAudioFile> optionalTtsAudioFile =
                ttsAudioFileRepository.findById(savedTtsAudioFile.getTtsAudioSeq());
        assertTrue(optionalTtsAudioFile.isPresent());

        // 4. 생성된 객체의 값과 조회된 객체의 값 비교
        TtsAudioFile searchedTtsAudioFile = optionalTtsAudioFile.get();
        assertEquals(createTtsAudioFile, searchedTtsAudioFile);
    }

    // 3. 생성 테스트 - 일부 데이터 입력 (필수 값 미포함)
    @Test
    @DisplayName("TtsAudioFile 생성 테스트 - 일부 데이터 입력 (필수 값 미포함)")
    public void createPartialDataWithoutRequired() throws Exception {
        // given
        // 1. TtsAudioFile 객체 생성 (필수 값 미포함)
        TtsAudioFile createTtsAudioFile =
                TtsAudioFile.builder()
                        .audioExtension("wav")
                        .audioSize("1MB")
                        .audioTime(1000)
                        .downloadCount(1)
                        .build();

        // when, then
        // 2. TtsAudioFileRepository.save() : 데이터 미포함으로 인한 에러 발생
        assertThrows(
                DataIntegrityViolationException.class,
                () -> {
                    TtsAudioFile savedTtsAudioFile = ttsAudioFileRepository.save(createTtsAudioFile);
                });
    }

    // 4. 생성 테스트 - 데이터 미포함
    @Test
    @DisplayName("TtsAudioFile 생성 테스트 - 데이터 미포함")
    public void createWithoutData() throws Exception {
        // given
        // 1. TtsAudioFile 객체 생성 (데이터 미포함)
        TtsAudioFile createTtsAudioFile = TtsAudioFile.builder().build();

        // when, then
        // 2. TtsAudioFileRepository.save() : 데이터 미포함으로 인한 에러 발생
        assertThrows(
                DataIntegrityViolationException.class,
                () -> {
                    TtsAudioFile savedTtsAudioFile = ttsAudioFileRepository.save(createTtsAudioFile);
                });
    }

    // 1. 조회 테스트 - 단건 전체 데이터 조회
    @Test
    @DisplayName("TtsAudioFile 조회 테스트 - 단건 전체 데이터 조회")
    public void searchOneData() {
        // given
        // 1. TtsAudioFile 객체 생성
        TtsAudioFile createTtsAudioFile =
                TtsAudioFile.builder()
                        .audioName("testAudio4")
                        .audioPath("testUrl4.com")
                        .audioExtension("wav")
                        .audioSize("1MB")
                        .audioTime(1000)
                        .downloadCount(1)
                        .downloadYn('Y')
                        .audioPlayYn('Y')
                        .build();

        // 2. TtsAudioFileRepository.save()
        TtsAudioFile savedTtsAudioFile = ttsAudioFileRepository.save(createTtsAudioFile);

        // when
        // 3. TtsAudioFileRepository.findById()
        Optional<TtsAudioFile> optionalTtsAudioFile =
                ttsAudioFileRepository.findById(savedTtsAudioFile.getTtsAudioSeq());

        // then
        // 4. 조회된 객체의 값과 생성된 객체의 값 비교
        assertTrue(optionalTtsAudioFile.isPresent());
        TtsAudioFile searchedTtsAudioFile = optionalTtsAudioFile.get();
        assertEquals(createTtsAudioFile, searchedTtsAudioFile);
    }

    // 2. 조회 테스트 - 다건 전체 데이터 조회
    @Test
    @DisplayName("TtsAudioFile 조회 테스트 - 다건 전체 데이터 조회")
    public void searchManyData() {

        // given
        int repeat = 10; // 반복 횟수
        ArrayList<TtsAudioFile> ttsAudioFileList = new ArrayList<>(); // 생성된 TtsAudioFile 객체 저장용 리스트

        // 1. TtsAudioFile 객체 10개 생성
        for (int i = 0; i < repeat; i++) {
            TtsAudioFile createTtsAudioFile =
                    TtsAudioFile.builder()
                            .audioName("testAudio" + i)
                            .audioPath("testUrl" + i + ".com")
                            .audioExtension("wav")
                            .audioSize("1MB")
                            .audioTime(1000)
                            .downloadCount(1)
                            .downloadYn('Y')
                            .audioPlayYn('Y')
                            .build();

            // 2. TtsAudioFileRepository.save()
            TtsAudioFile savedTtsAudioFile = ttsAudioFileRepository.save(createTtsAudioFile);

            // 3. 생성된 객체 저장
            ttsAudioFileList.add(savedTtsAudioFile);
        }

        // when
        // 4. TtsAudioFileRepository.findAll()
        List<TtsAudioFile> searchedTtsAudioFileList = ttsAudioFileRepository.findAll();

        // then
        // 5. 조회된 객체의 값과 생성된 객체의 값 비교
        assertEquals(repeat, searchedTtsAudioFileList.size());

        // 순회 하면서 포함하고 있는지 검증
        for (int i = 0; i < ttsAudioFileList.size(); i++) {
            assertTrue(searchedTtsAudioFileList.contains(ttsAudioFileList.get(i)));
        }
    }

    // 1. 수정 테스트 - 전체 데이터 수정
    @Test
    @DisplayName("TtsAudioFile 수정 테스트 - 전체 데이터 수정")
    public void updateAllData() {
        // given
        String updateAudioName = "testAudio5-1";
        String updateAudioPath = "testUrl5-1.com";
        String updateAudioExtension = "mp3";
        String updateAudioSize = "2MB";
        Integer updateAudioTime = 2000;
        Integer updateDownloadCount = 2;
        char updateDownloadYn = 'N';
        char updateAudioPlayYn = 'N';

        // 1. TtsAudioFile 객체 생성
        TtsAudioFile createTtsAudioFile =
                TtsAudioFile.builder()
                        .audioName("testAudio5")
                        .audioPath("testUrl5.com")
                        .audioExtension("wav")
                        .audioSize("1MB")
                        .audioTime(1000)
                        .downloadCount(1)
                        .downloadYn('Y')
                        .audioPlayYn('Y')
                        .build();

        // 2. TtsAudioFileRepository.save()
        TtsAudioFile savedTtsAudioFile = ttsAudioFileRepository.save(createTtsAudioFile);
        Long savedTtsAudioFileTtsAudioSeq = savedTtsAudioFile.getTtsAudioSeq();

        // when
        // 3. TtsAudioFile 객체 수정
        TtsAudioFile updateTtsAudioFile =
                TtsAudioFile.builder()
                        .ttsAudioSeq(savedTtsAudioFileTtsAudioSeq)
                        .audioName(updateAudioName)
                        .audioPath(updateAudioPath)
                        .audioExtension(updateAudioExtension)
                        .audioSize(updateAudioSize)
                        .audioTime(updateAudioTime)
                        .downloadCount(updateDownloadCount)
                        .downloadYn(updateDownloadYn)
                        .audioPlayYn(updateAudioPlayYn)
                        .build();

        // 4. TtsAudioFileRepository.save()
        TtsAudioFile savedUpdateTtsAudioFile = ttsAudioFileRepository.save(updateTtsAudioFile);

        // then
        // 5. 수정된 객체 조회 및 검증
        // 변경한 객체와 저장된 객체가 같은 객체인지 검증
        assertEquals(updateTtsAudioFile, savedUpdateTtsAudioFile);

        // 수정한 객체의 seq 값으로 조회
        Optional<TtsAudioFile> optionalTtsAudioFile =
                ttsAudioFileRepository.findById(savedTtsAudioFileTtsAudioSeq);
        assertTrue(optionalTtsAudioFile.isPresent());

        // 조회된 객체와 변경한 객체가 같은 객체인지 검증
        TtsAudioFile manipulatedTtsAudioFile = optionalTtsAudioFile.get();
        assertEquals(updateTtsAudioFile, manipulatedTtsAudioFile);
    }

    // 2. 수정 테스트 - 일부 데이터 수정
    @Test
    @DisplayName("TtsAudioFile 수정 테스트 - 일부 데이터 수정(필수 값 수정 포함)")
    public void updatePartialDataWithRequired() {
        // given
        String updateAudioName = "testAudio6-1";
        String updateAudioPath = "testUrl6-1.com";
        Integer updateDownloadCount = 2;

        // 1. TtsAudioFile 객체 생성
        TtsAudioFile createTtsAudioFile =
                TtsAudioFile.builder()
                        .audioName("testAudio6")
                        .audioPath("testUrl6.com")
                        .audioExtension("wav")
                        .audioSize("1MB")
                        .audioTime(1000)
                        .downloadCount(1)
                        .downloadYn('Y')
                        .audioPlayYn('Y')
                        .build();

        // 2. TtsAudioFileRepository.save()
        TtsAudioFile savedTtsAudioFile = ttsAudioFileRepository.save(createTtsAudioFile);
        Long savedTtsAudioFileTtsAudioSeq = savedTtsAudioFile.getTtsAudioSeq();

        // when
        // 3. TtsAudioFile 객체 수정
        TtsAudioFile updateTtsAudioFile =
                TtsAudioFile.builder()
                        .ttsAudioSeq(savedTtsAudioFileTtsAudioSeq)
                        .audioName(updateAudioName)
                        .audioPath(updateAudioPath)
                        .downloadCount(updateDownloadCount)
                        .build();

        // 4. TtsAudioFileRepository.save()
        TtsAudioFile savedUpdateTtsAudioFile = ttsAudioFileRepository.save(updateTtsAudioFile);

        // then
        // 5. 수정된 객체 조회 및 검증
        // 변경한 객체와 저장된 객체가 같은 객체인지 검증
        assertEquals(updateTtsAudioFile, savedUpdateTtsAudioFile);

        // 수정한 객체의 seq 값으로 조회
        Optional<TtsAudioFile> optionalTtsAudioFile =
                ttsAudioFileRepository.findById(savedTtsAudioFileTtsAudioSeq);
        assertTrue(optionalTtsAudioFile.isPresent());

        // 조회된 객체와 변경한 객체가 같은 객체인지 검증
        TtsAudioFile manipulatedTtsAudioFile = optionalTtsAudioFile.get();
        assertEquals(updateTtsAudioFile, manipulatedTtsAudioFile);
    }

    // 3. 수정 테스트 - 일부 데이터 수정 시 필수 값 미포함
    @Test
    @DisplayName("TtsAudioFile 수정 테스트 - 일부 데이터 수정(필수 값 미포함)")
    public void updatePartialDataWithoutRequired() {
        // given
        String updateAudioExtension = "mp3";
        String updateAudioSize = "2MB";
        Integer updateAudioTime = 2000;
        Integer updateDownloadCount = 2;
        char updateDownloadYn = 'N';
        char updateAudioPlayYn = 'N';

        // 1. TtsAudioFile 객체 생성
        TtsAudioFile createTtsAudioFile =
                TtsAudioFile.builder()
                        .audioName("testAudio7")
                        .audioPath("testUrl7.com")
                        .audioExtension("wav")
                        .audioSize("1MB")
                        .audioTime(1000)
                        .downloadCount(1)
                        .downloadYn('Y')
                        .audioPlayYn('Y')
                        .build();

        // 2. TtsAudioFileRepository.save()
        TtsAudioFile savedTtsAudioFile = ttsAudioFileRepository.save(createTtsAudioFile);
        Long savedTtsAudioFileTtsAudioSeq = savedTtsAudioFile.getTtsAudioSeq();

        // when
        // 3. TtsAudioFile 객체 수정
        TtsAudioFile updateTtsAudioFile =
                createTtsAudioFile.toBuilder()
                        .ttsAudioSeq(savedTtsAudioFileTtsAudioSeq)
                        .audioExtension(updateAudioExtension)
                        .audioSize(updateAudioSize)
                        .audioTime(updateAudioTime)
                        .downloadCount(updateDownloadCount)
                        .downloadYn(updateDownloadYn)
                        .audioPlayYn(updateAudioPlayYn)
                        .build();

        // 4. TtsAudioFileRepository.save()
        TtsAudioFile savedUpdateTtsAudioFile = ttsAudioFileRepository.save(updateTtsAudioFile);

        // then
        // 5. 수정된 객체 조회 및 검증
        // 변경한 객체와 저장된 객체가 같은 객체인지 검증
        assertEquals(updateTtsAudioFile, savedUpdateTtsAudioFile);

        // 수정한 객체의 seq 값으로 조회
        Optional<TtsAudioFile> optionalTtsAudioFile =
                ttsAudioFileRepository.findById(savedTtsAudioFileTtsAudioSeq);
        assertTrue(optionalTtsAudioFile.isPresent());

        // 조회된 객체와 변경한 객체가 같은 객체인지 검증
        TtsAudioFile manipulatedTtsAudioFile = optionalTtsAudioFile.get();
        assertEquals(updateTtsAudioFile, manipulatedTtsAudioFile);
    }

    // 1. 삭제 테스트
    @Test
    @DisplayName("TtsAudioFile 삭제 테스트")
    public void deleteData() {
        // given
        // 1. TtsAudioFile 객체 생성
        TtsAudioFile createTtsAudioFile =
                TtsAudioFile.builder()
                        .audioName("testAudio8")
                        .audioPath("testUrl8.com")
                        .audioExtension("wav")
                        .audioSize("1MB")
                        .audioTime(1000)
                        .downloadCount(1)
                        .downloadYn('Y')
                        .audioPlayYn('Y')
                        .build();

        // 2. TtsAudioFileRepository.save()
        TtsAudioFile savedTtsAudioFile = ttsAudioFileRepository.save(createTtsAudioFile);
        Long savedTtsAudioFileTtsAudioSeq = savedTtsAudioFile.getTtsAudioSeq();

        // 3. 저장 객체 조회 및 검증
        Optional<TtsAudioFile> optionalResearchedTtsAudioFile =
                ttsAudioFileRepository.findById(savedTtsAudioFileTtsAudioSeq);
        assertTrue(optionalResearchedTtsAudioFile.isPresent());

        // 조회된 객체와 저장된 객체가 같은 객체인지 검증
        TtsAudioFile researchedTtsAudioFile = optionalResearchedTtsAudioFile.get();
        assertEquals(savedTtsAudioFileTtsAudioSeq, researchedTtsAudioFile.getTtsAudioSeq());

        // when
        // 4. TtsAudioFileRepository.delete()
        ttsAudioFileRepository.delete(savedTtsAudioFile);

        // then
        // 4. 삭제된 객체 조회 및 검증
        Optional<TtsAudioFile> optionalTtsAudioFile =
                ttsAudioFileRepository.findById(savedTtsAudioFileTtsAudioSeq);
        assertFalse(optionalTtsAudioFile.isPresent());
    }
}
