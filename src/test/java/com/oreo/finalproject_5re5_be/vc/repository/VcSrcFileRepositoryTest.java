package com.oreo.finalproject_5re5_be.vc.repository;

import com.oreo.finalproject_5re5_be.vc.entity.VcSrcFile;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class VcSrcFileRepositoryTest {
    private final VcSrcFileRepository vcSrcFileRepository;

    @Autowired
    private VcSrcFileRepositoryTest(VcSrcFileRepository vcSrcFileRepository){
        this.vcSrcFileRepository = vcSrcFileRepository;
    }

    /*
    SrcAudioFile 생성 테스트
    1. 생성 테스트 - 전체 데이터 입력( Default값 미포함 )
    2. 생성 테스트 - 전체 데이터 입력( Default값 포함 )
    3. 생성 테스트 - 일부 데이터 입력 (필수 값만 포함)
    4. 생성 테스트 - 생성 테스트 - SEQ값 입력
    5. 생성 테스트 - 필수값 미입력
    6. 생성 테스트 - 데이터 미입력

    SrcAudioFile 조회 테스트
    1. 조회 테스트 - 단건 전체 데이터 조회
    2. 조회 테스트 - 다건 전체 데이터 조회

    SrcAudioFile 수정 테스트
    1. 수정 테스트 - 전체 데이터 수정
    2. 수정 테스트 - 일부 데이터 수정
    3. 수정 테스트 - 일부 데이터 수정 시 필수 값 미포함

    SrcAudioFile 삭제 테스트
    1. 삭제 테스트 - 단건 삭제
    2. 삭제 테스트 - 다건 삭제
    3. 삭제 테스트 - 없는 데이터 삭제
     */

    @Test
    @DisplayName("1. SrcAudioFile 생성 테스트 - 전체 데이터 입력( Default값 미포함 )")
    void SrcAudioFile전체데이터생성Default값미포함(){
        //생성할 오디오 정보 생성
        VcSrcFile createFile = VcSrcFile.builder()
                .fileName("testAudio")
                .fileUrl("https://s3/test")
                .localUrl("USER/Test")
                .fileLength("100KB")
                .extension("Wav").build();
        //오디오 정보 저장
        VcSrcFile createSrcFile = vcSrcFileRepository.save(createFile);
        //오디오 정보 생성확인 
        Long srcSeq = createSrcFile.getSrcSeq();

        //생성 파일 확인 isPresent()값생성 확인
        Optional<VcSrcFile> findSrcFile = vcSrcFileRepository.findById(srcSeq);
        assertTrue(findSrcFile.isPresent());

        //생성할 오디오 정보와 생성 파일 확인 정보 비교
        VcSrcFile getSrcFile = findSrcFile.get();
        log.info("pk값 - 비교 {} : {} ",getSrcFile.getSrcSeq(), createFile.getSrcSeq());
        log.info("기본값 - 비교 {} : {} ",getSrcFile.getActivate(), createFile.getActivate());

        assertEquals(getSrcFile, createFile);
    }

    @Test
    @DisplayName("2. SrcAudioFile 생성 테스트 - 전체 데이터 입력( Default값 포함 )")
    void SrcAudioFile전체데이터생성Default값포함(){
        //생성할 오디오 정보 생성
        VcSrcFile createFile = VcSrcFile.builder()
                .fileName("testAudio")
                .fileUrl("https://s3/test")
                .localUrl("USER/Test")
                .fileLength("100KB")
                .extension("Wav")
                .activate('N')
                .downloadStatus('N')
                .startStatus('N').build();
        //오디오 정보 저장
        VcSrcFile createSrcFile = vcSrcFileRepository.save(createFile);
        //오디오 정보 생성확인
        Long srcSeq = createSrcFile.getSrcSeq();

        //생성 파일 확인 isPresent()값생성 확인
        Optional<VcSrcFile> findSrcFile = vcSrcFileRepository.findById(srcSeq);
        assertTrue(findSrcFile.isPresent());

        //생성할 오디오 정보와 생성 파일 확인 정보 비교
        VcSrcFile getSrcFile = findSrcFile.get();
        log.info("pk값 - 비교 {} : {} ",getSrcFile.getSrcSeq(), createFile.getSrcSeq());
        log.info("기본값 - 비교 {} : {} ",getSrcFile.getActivate(), createFile.getActivate());

        assertEquals(getSrcFile, createFile);
    }

    @Test
    @DisplayName("3. SrcAudioFile 생성 테스트 - 일부 데이터 입력 (필수 값만 포함)")
    void SrcAudioFile필수데이터만생성(){
        // 객체 생성
        VcSrcFile createFile = VcSrcFile.builder()
                .fileName("testAudio")
                .fileUrl("https://s3/test")
                .fileLength("100KB")
                .extension("Wav").build();

        //오디오 정보 저장
        VcSrcFile createSrcFile = vcSrcFileRepository.save(createFile);
        //오디오 정보 생성확인
        Long srcSeq = createSrcFile.getSrcSeq();

        //생성 파일 확인 isPresent()값생성 확인
        Optional<VcSrcFile> findSrcFile = vcSrcFileRepository.findById(srcSeq);
        assertTrue(findSrcFile.isPresent());

        //생성할 오디오 정보와 생성 파일 확인 정보 비교
        VcSrcFile getSrcFile = findSrcFile.get();
        log.info("pk값 - 비교 {} : {} ",getSrcFile.getSrcSeq(), createFile.getSrcSeq());
        log.info("기본값 - 비교 {} : {} ",getSrcFile.getActivate(), createFile.getActivate());
        log.info("미필수값 - 비교 {} : {} ",getSrcFile.getLocalUrl(), createFile.getLocalUrl());
        assertEquals(getSrcFile, createFile);
    }

    @Test
    @DisplayName("4. SrcAudioFile 생성 테스트 - SEQ값 입력")
    void SrcAudioFileSEQ값입력() throws Exception{
        // 객체 생성
        VcSrcFile createFile = VcSrcFile.builder()
                .srcSeq(999L)
                .fileName("testAudio")
                .fileUrl("https://s3/test")
                .fileLength("100KB")
                .extension("Wav").build();

        //오디오 정보 저장
        VcSrcFile createSrcFile = vcSrcFileRepository.save(createFile);
        //오디오 정보 생성확인
        Long srcSeq = createSrcFile.getSrcSeq();

        //생성 파일 확인 isPresent()값생성 확인
        Optional<VcSrcFile> findSrcFile = vcSrcFileRepository.findById(srcSeq);
        assertTrue(findSrcFile.isPresent());

        //생성할 오디오 정보와 생성 파일 확인 정보 비교
        VcSrcFile getSrcFile = findSrcFile.get();
        log.info("pk값 - 비교 {} : {} ",getSrcFile.getSrcSeq(), createFile.getSrcSeq());

        //시퀀스로 생성이 되는지 확인
        assertEquals(getSrcFile.getSrcSeq(), createSrcFile.getSrcSeq());
    }
    @Test
    @DisplayName("5. SrcAudioFile 생성 테스트 - 필수값 미입력")
    void SrcAudioFile필수값미포함() throws Exception{
        // 객체 생성
        VcSrcFile createFile = VcSrcFile.builder()
                .fileUrl("https://s3/test")
                .fileLength("100KB")
                .extension("Wav").build();

        //DATA 미포함 에러
        assertThrows(DataIntegrityViolationException.class, () ->{
            VcSrcFile saveAudioFile = vcSrcFileRepository.save(createFile);
        });
    }

    @Test
    @DisplayName("6. SrcAudioFile 생성 테스트 - 데이터 미입력")
    void SrcAudioFile데이터미입력() throws Exception{
        // 객체 생성
        VcSrcFile createFile = VcSrcFile.builder().build();

        //DATA 미포함 에러
        assertThrows(DataIntegrityViolationException.class, () ->{
            VcSrcFile saveAudioFile = vcSrcFileRepository.save(createFile);
        });
    }

    @Test
    @DisplayName("7. SrcAudioFile 조회 테스트 - 다건 데이터 조회")
    void SrcAudioFile단건데이터조회(){

        //반복횟수
        int repeat = 10;
        //vcSrcFile 객체 10개 생성
        for (int i = 0; i < repeat; i++) {
            VcSrcFile createFile = VcSrcFile.builder()
                    .fileName("testAudio"+i)
                    .fileUrl("https://s3/test"+i)
                    .localUrl("USER/Test"+i)
                    .fileLength("100KB")
                    .extension("Wav").build();
            //객체 10개 저장
            VcSrcFile save = vcSrcFileRepository.save(createFile);
            //조회
            Optional<VcSrcFile> byId = vcSrcFileRepository.findById(save.getSrcSeq());
            
            assertTrue(byId.isPresent());//조회 되었는지 확인
            VcSrcFile searchVcSrcFile = byId.get();
            assertEquals(createFile, searchVcSrcFile);//비교
        }
    }
    @Test
    @DisplayName("7. SrcAudioFile 조회 테스트 - 다건 전체 데이터 조회")
    void SrcAudioFile다건전체데이터조회(){

        //db데이터 초기화
        vcSrcFileRepository.deleteAll();
        //반복횟수
        int repeat = 10;
        // 저장용 리스트 생성
        ArrayList<VcSrcFile> srcFilesList = new ArrayList<>();
        //vcSrcFile 객체 10개 생성
        for (int i = 0; i < repeat; i++) {
            VcSrcFile createFile = VcSrcFile.builder()
                    .fileName("testAudio"+i)
                    .fileUrl("https://s3/test"+i)
                    .localUrl("USER/Test"+i)
                    .fileLength("100KB")
                    .extension("Wav").build();
            //객체 10개 저장
            VcSrcFile save = vcSrcFileRepository.save(createFile);

            //저장용 리스트에 저장
            srcFilesList.add(save);
        }
        //조회
        List<VcSrcFile> searchAllSrcFileList = vcSrcFileRepository.findAll();

        assertEquals(repeat, searchAllSrcFileList.size());//조회개수 확인
        for (int i = 0; i < searchAllSrcFileList.size(); i++) {
            //데이터 검증
            assertTrue(searchAllSrcFileList.contains(searchAllSrcFileList.get(i)));
            //위에서 생성된 리스트랑 비교
            assertEquals(srcFilesList.get(i), searchAllSrcFileList.get(i));
        }
    }

    @Test
    @DisplayName("8. SrcAudioFile 수정 테스트 - 전체 데이터 수정")
    void SrcAudioFile전체데이터수정(){
        // 저장할 객체 생성
        VcSrcFile createFile = VcSrcFile.builder()
                .fileName("testAudio")
                .fileUrl("https://s3/test")
                .localUrl("USER/Test")
                .fileLength("100KB")
                .extension("Wav").build();
        //DB 저장
        VcSrcFile save = vcSrcFileRepository.save(createFile);
        Long saveSrcSeq = save.getSrcSeq();// 저장한 pk 추출

        VcSrcFile updateFile = VcSrcFile.builder() //pk로 변경할 데이터들 생성
                .srcSeq(saveSrcSeq)
                .fileName(save.getFileName()+"1")
                .fileUrl(save.getFileUrl()+"1")
                .localUrl(save.getLocalUrl()+"1")
                .fileLength("200KB")
                .extension("Wav")
                .activate('N')
                .startStatus('N')
                .downloadStatus('N').build();
        //데이터 변경후 확인
        VcSrcFile updateSave = vcSrcFileRepository.save(updateFile);
        assertEquals(updateFile, updateSave);

        //데이터 검증
        Optional<VcSrcFile> optionalAudioFile = vcSrcFileRepository.findById(saveSrcSeq);
        assertTrue(optionalAudioFile.isPresent());

        //객체 검증
        VcSrcFile searchAudioFile = optionalAudioFile.get();
        assertEquals(saveSrcSeq, searchAudioFile.getSrcSeq()); // pk 확인
        assertEquals(updateFile, searchAudioFile);
    }
    @Test
    @DisplayName("9. SrcAudioFile 수정 테스트 - 일부 데이터 수정")
    void SrcAudioFile일부데이터수정(){
        // 저장할 객체 생성
        VcSrcFile createFile = VcSrcFile.builder()
                .fileName("testAudio")
                .fileUrl("https://s3/test")
                .localUrl("USER/Test")
                .fileLength("100KB")
                .extension("Wav").build();
        //DB 저장
        VcSrcFile save = vcSrcFileRepository.save(createFile);
        Long saveSrcSeq = save.getSrcSeq();// 저장한 pk 추출

        VcSrcFile updateFile = VcSrcFile.builder() //pk로 변경할 데이터들 생성
                .srcSeq(saveSrcSeq)
                .fileName(createFile.getFileName()+"1")
                .fileUrl(createFile.getFileUrl()+"1")
                .fileLength("200KB")
                .extension("Wav")
                .startStatus('N')
                .downloadStatus('N').build();
        //데이터 변경후 확인
        VcSrcFile updateSave = vcSrcFileRepository.save(updateFile);
        assertEquals(updateFile, updateSave);

        //데이터 검증
        Optional<VcSrcFile> optionalAudioFile = vcSrcFileRepository.findById(saveSrcSeq);
        assertTrue(optionalAudioFile.isPresent());

        //객체 검증
        VcSrcFile searchAudioFile = optionalAudioFile.get();
        assertEquals(saveSrcSeq, searchAudioFile.getSrcSeq()); // pk 확인
        assertEquals(updateFile, searchAudioFile);

    }
    @Test
    @DisplayName("10. SrcAudioFile 수정 테스트 - 일부 데이터 수정 시 필수 값 미포함")
    void SrcAudioFile일부데이터수정시필수값미포함(){
        // 저장할 객체 생성
        VcSrcFile createFile = VcSrcFile.builder()
                .fileName("testAudio")
                .fileUrl("https://s3/test")
                .localUrl("USER/Test")
                .fileLength("100KB")
                .extension("Wav").build();
        //DB 저장
        VcSrcFile save = vcSrcFileRepository.save(createFile);
        Long saveSrcSeq = save.getSrcSeq();// 저장한 pk 추출

        VcSrcFile updateFile = createFile.toBuilder() //pk로 변경할 데이터들 생성
                .srcSeq(saveSrcSeq)
                .localUrl(createFile.getLocalUrl()+"1").build();
        //데이터 변경후 확인
        VcSrcFile updateSave = vcSrcFileRepository.save(updateFile);
        assertEquals(updateFile, updateSave);

        //데이터 검증
        Optional<VcSrcFile> optionalAudioFile = vcSrcFileRepository.findById(saveSrcSeq);
        assertTrue(optionalAudioFile.isPresent());

        //객체 검증
        VcSrcFile searchAudioFile = optionalAudioFile.get();
        assertEquals(saveSrcSeq, searchAudioFile.getSrcSeq()); // pk 확인
        assertEquals(updateFile, searchAudioFile);
    }

    @Test
    @DisplayName("11. SrcAudioFile 삭제 테스트 - 단건삭제(ID)")
    void SrcAudioFile단건ID삭제(){
        // 저장할 객체 생성
        VcSrcFile createFile = VcSrcFile.builder()
                .fileName("testAudio")
                .fileUrl("https://s3/test")
                .localUrl("USER/Test")
                .fileLength("100KB")
                .extension("Wav").build();
        //DB 저장
        VcSrcFile save = vcSrcFileRepository.save(createFile);
        Long saveSrcSeq = save.getSrcSeq();// 저장한 pk 추출
        //데이터 조회, 검증
        Optional<VcSrcFile> optionalVcSrcFile = vcSrcFileRepository.findById(saveSrcSeq);
        assertTrue(optionalVcSrcFile.isPresent());

        //데이터 비교
        VcSrcFile resultVcSrcFile = optionalVcSrcFile.get();
        assertEquals(save, resultVcSrcFile);

        //ID로 삭제
        vcSrcFileRepository.deleteById(saveSrcSeq);

        Optional<VcSrcFile> optionalVcSrcFile2 = vcSrcFileRepository.findById(saveSrcSeq);
        assertFalse(optionalVcSrcFile2.isPresent());
    }
    @Test
    @DisplayName("12. SrcAudioFile 삭제 테스트 - 단건삭제(객체)")
    void SrcAudioFile단건객체삭제(){
        // 저장할 객체 생성
        VcSrcFile createFile = VcSrcFile.builder()
                .fileName("testAudio")
                .fileUrl("https://s3/test")
                .localUrl("USER/Test")
                .fileLength("100KB")
                .extension("Wav").build();
        //DB 저장
        VcSrcFile save = vcSrcFileRepository.save(createFile);
        Long saveSrcSeq = save.getSrcSeq();// 저장한 pk 추출
        //데이터 조회, 검증
        Optional<VcSrcFile> optionalVcSrcFile = vcSrcFileRepository.findById(saveSrcSeq);
        assertTrue(optionalVcSrcFile.isPresent());

        //데이터 비교
        VcSrcFile resultVcSrcFile = optionalVcSrcFile.get();
        assertEquals(save, resultVcSrcFile);

        //객체로 삭제
        vcSrcFileRepository.delete(save);

        Optional<VcSrcFile> optionalVcSrcFile2 = vcSrcFileRepository.findById(saveSrcSeq);
        assertFalse(optionalVcSrcFile2.isPresent());
    }
    @Test
    @DisplayName("13. SrcAudioFile 삭제 테스트 - 다건삭제")
    void SrcAudioFile다건삭제(){
        //반복횟수
        int repeat = 10;
        //vcSrcFile 객체 10개 생성
        for (int i = 0; i < repeat; i++) {
            VcSrcFile createFile = VcSrcFile.builder()
                    .fileName("testAudio" + i)
                    .fileUrl("https://s3/test" + i)
                    .localUrl("USER/Test" + i)
                    .fileLength("100KB")
                    .extension("Wav").build();
            //객체 10개 저장
            VcSrcFile save = vcSrcFileRepository.save(createFile);
            //조회
            Optional<VcSrcFile> byId = vcSrcFileRepository.findById(save.getSrcSeq());
            assertTrue(byId.isPresent());//조회 되었는지 확인

            VcSrcFile searchVcSrcFile = byId.get();
            assertEquals(createFile, searchVcSrcFile);//비교

            vcSrcFileRepository.delete(save);
            Optional<VcSrcFile> byId2 = vcSrcFileRepository.findById(save.getSrcSeq());
            assertFalse(byId2.isPresent());
        }
    }
}