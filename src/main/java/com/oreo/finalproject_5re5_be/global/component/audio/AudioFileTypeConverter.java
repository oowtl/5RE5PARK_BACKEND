package com.oreo.finalproject_5re5_be.global.component.audio;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class AudioFileTypeConverter {
    // File을  MultipartFile로 변경
    public static MultipartFile convertFileToMultipartFile(File file) throws IOException {
        try (FileInputStream inputStream = new FileInputStream(file)) {
            return new MockMultipartFile(
                    "file", // 파라미터 이름
                    file.getName(), // 파일 이름
                    Files.probeContentType(file.toPath()), // MIME 타입
                    inputStream // 파일 내용
                    );
        }
    }
}
