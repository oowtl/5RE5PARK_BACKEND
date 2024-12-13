package com.oreo.finalproject_5re5_be.audio.service;

import static org.junit.jupiter.api.Assertions.*;

import com.oreo.finalproject_5re5_be.global.component.AudioInfo;
import com.oreo.finalproject_5re5_be.global.dto.response.AudioFileInfo;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
@Slf4j
@TestPropertySource(locations = "classpath:application-ndb-test.properties")
class AudioInfoTest {
    @Autowired private AudioInfo audioInfo;

    @Test
    @DisplayName("mp3 파일 정보 추출 확인 테스트")
    void extractAudioMp3FileInfo() throws IOException {
        // 실제 파일을 사용하여 MockMultipartFile 생성
        File mp3 = new File("test.mp3");

        // FileInputStream을 통해 파일 데이터를 읽어서 MockMultipartFile 생성
        try (FileInputStream mp3InputStream = new FileInputStream(mp3)) {
            MultipartFile multipartMp3 =
                    new MockMultipartFile(
                            "file", // 파라미터 이름
                            mp3.getName(), // 파일 이름
                            "audio/mpeg", // MIME 타입
                            mp3InputStream // 파일 데이터
                            );

            AudioFileInfo fileInfo = audioInfo.extractAudioFileInfo(multipartMp3); // 오디오파일 info 추출
            log.info(fileInfo.toString()); // 값 확인
            assertNotNull(fileInfo); // 값 생성 확인
            assertEquals(mp3.getName(), fileInfo.getName()); // 이름 값 확인
            assertEquals(
                    mp3.getName().substring(mp3.getName().lastIndexOf(".") + 1).toLowerCase(),
                    fileInfo.getExtension()); // 확장자 확인
            assertEquals(String.valueOf(mp3.length()), fileInfo.getSize()); // 파일 용량 확인
        }
    }

    @Test
    @DisplayName("wav 파일 정보 추출 확인 테스트")
    void extractAudioWavFileInfo() throws IOException {
        // 실제 파일을 사용하여 MockMultipartFile 생성
        File wav = new File("test.wav");

        // FileInputStream을 통해 파일 데이터를 읽어서 MockMultipartFile 생성
        try (FileInputStream wavInputStream = new FileInputStream(wav)) {
            MultipartFile multipartWav =
                    new MockMultipartFile(
                            "file", // 파라미터 이름
                            wav.getName(), // 파일 이름
                            "audio/wav", // MIME 타입
                            wavInputStream // 파일 데이터
                            );
            // multipart 파일 입력해서 audio파일 정보 추출
            AudioFileInfo fileInfo = audioInfo.extractAudioFileInfo(multipartWav);
            log.info(fileInfo.toString()); // 값 확인
            assertNotNull(fileInfo); // 값 생성 확인
            assertEquals(wav.getName(), fileInfo.getName()); // 파일 이름 값 확인
            assertEquals(
                    wav.getName().substring(wav.getName().lastIndexOf(".") + 1).toLowerCase(),
                    fileInfo.getExtension()); // 파일 확장자 값 확인
            assertEquals(String.valueOf(wav.length()), fileInfo.getSize()); // 파일 용량 확인
        }
    }
}
