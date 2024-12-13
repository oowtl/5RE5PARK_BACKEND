package com.oreo.finalproject_5re5_be.global.component;

import com.mpatric.mp3agic.Mp3File;
import com.oreo.finalproject_5re5_be.global.dto.response.AudioFileInfo;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
public class AudioInfo {
    // 파일 이름,길이,크기,확장자 추출 메서드
    public AudioFileInfo extractAudioFileInfo(MultipartFile audioFile) {
        String fileName = audioFile.getOriginalFilename();
        String fileSize = String.valueOf(audioFile.getSize());

        // 파일 확장자 추출
        String fileExtension = "";
        if (fileName != null && fileName.contains(".")) {
            fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        }

        Integer fileLength = 0;

        // 임시 파일로 변환하여 파일 길이 추출
        try {
            File tempFile = File.createTempFile("temp", "." + fileExtension);
            audioFile.transferTo(tempFile);

            if ("mp3".equalsIgnoreCase(fileExtension)) {
                // mp3 파일 길이 추출
                Mp3File mp3File = new Mp3File(tempFile);
                fileLength = Math.toIntExact(mp3File.getLengthInSeconds());
            } else if ("wav".equalsIgnoreCase(fileExtension)) {
                // wav 파일 길이 추출
                fileLength = Math.toIntExact(getWavFileDuration(tempFile));
            }

            boolean delete = tempFile.delete(); // 임시 파일 삭제
            if (!delete) {
                throw new RuntimeException("임시 파일 삭제 실패");
            }
        } catch (Exception e) {
            log.error("오디오 파일 정보를 추출하는 중 오류 발생: ", e);
        }
        return AudioFileInfo.builder()
                .name(fileName)
                .size(fileSize)
                .length(fileLength)
                .extension(fileExtension)
                .build();
    }

    public List<AudioFileInfo> extractAudioFileInfo(List<MultipartFile> audioFiles) {
        List<AudioFileInfo> audioFileInfos = new ArrayList<>();
        for (MultipartFile audioFile : audioFiles) {
            String fileName = audioFile.getOriginalFilename();
            String fileSize = String.valueOf(audioFile.getSize());

            // 파일 확장자 추출
            String fileExtension = "";
            if (fileName != null && fileName.contains(".")) {
                fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
            }

            Integer fileLength = 0;

            // 임시 파일로 변환하여 파일 길이 추출
            try {
                File tempFile = File.createTempFile("temp", "." + fileExtension);
                audioFile.transferTo(tempFile);

                if ("mp3".equalsIgnoreCase(fileExtension)) {
                    // mp3 파일 길이 추출
                    Mp3File mp3File = new Mp3File(tempFile);
                    fileLength = Math.toIntExact(mp3File.getLengthInSeconds());
                } else if ("wav".equalsIgnoreCase(fileExtension)) {
                    // wav 파일 길이 추출
                    fileLength = Math.toIntExact(getWavFileDuration(tempFile));
                }

                boolean delete = tempFile.delete(); // 임시 파일 삭제
                if (!delete) {
                    throw new RuntimeException("임시 파일 삭제 실패");
                }
            } catch (Exception e) {
                log.error("오디오 파일 정보를 추출하는 중 오류 발생: ", e);
            }
            audioFileInfos.add(
                    AudioFileInfo.builder()
                            .name(fileName)
                            .size(fileSize)
                            .length(fileLength)
                            .extension(fileExtension)
                            .build());
        }

        return audioFileInfos;
    }

    // wav 파일 일경우 파일 길이 추출하는 메서드
    private long getWavFileDuration(File wavFile) {
        try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(wavFile)) {
            AudioFormat format = audioInputStream.getFormat();
            long frames = audioInputStream.getFrameLength();
            return (long) (frames / format.getFrameRate()); // 초 단위 길이 반환
        } catch (Exception e) {
            log.error("WAV 파일 길이 추출 중 오류 발생: ", e);
            return 0L;
        }
    }
}
