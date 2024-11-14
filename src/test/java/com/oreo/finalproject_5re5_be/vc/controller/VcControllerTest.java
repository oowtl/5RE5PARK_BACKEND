package com.oreo.finalproject_5re5_be.vc.controller;

import com.oreo.finalproject_5re5_be.global.dto.response.AudioFileInfo;
import com.oreo.finalproject_5re5_be.global.component.AudioInfo;
import com.oreo.finalproject_5re5_be.global.dto.response.ResponseDto;
import com.oreo.finalproject_5re5_be.global.component.S3Service;
import com.oreo.finalproject_5re5_be.vc.dto.request.VcSrcRequest;
import com.oreo.finalproject_5re5_be.vc.service.VcService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Nested
@ExtendWith(MockitoExtension.class)
class VcControllerTest {
    @Mock
    private VcService vcService;

    @Mock
    private AudioInfo audioInfo;

    @Mock
    private S3Service s3Service;

    @InjectMocks
    private VcController vcController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void srcSave() {
        Long proSeq = 1L;
        MultipartFile file = mock(MultipartFile.class);
        AudioFileInfo audioFileInfo = AudioFileInfo.builder()
                .name("name")
                .size("100")
                .length(100)
                .extension("wav")
                .build();

        when(audioInfo.extractAudioFileInfo(file)).thenReturn(audioFileInfo);
        when(s3Service.upload(file, "vc/src")).thenReturn("file_url");

        ResponseDto<Map<String, Object>> response = vcController.srcSave(proSeq, file);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("src 파일 저장 완료되었습니다.", response.getResponse().get("message"));
        verify(vcService).srcSave(any(VcSrcRequest.class));
    }


    @Test
    void trgSave() {

    }

    @Test
    void resultSave() {
    }

    @Test
    void textSave() {
    }

    @Test
    void srcURL() {
    }

    @Test
    void resultURL() {
    }

    @Test
    void vc() {
    }

    @Test
    void deleteSrc() {
    }

    @Test
    void updateText() {
    }
}