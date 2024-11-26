package com.oreo.finalproject_5re5_be.global.component;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.oreo.finalproject_5re5_be.vc.dto.request.VcUrlRequest;
import com.oreo.finalproject_5re5_be.global.component.audio.AudioExtensionConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class S3Service {
    private AmazonS3 s3Client;
    @Autowired
    public S3Service(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }
    @Value("${aws.s3.bucket}")
    private String buketName;


    public String upload(MultipartFile file, String dirName) {
        return uploadSingleFile(file, dirName);
    }
    /**
     * 키 값은 주소에서 버킷주소 뒤로 [폴더/파일명] 입력
     * @param key
     * @return file
     * @throws IOException
     */
    public File downloadFile(String key) throws IOException {
        return downloadSingleFile(key);
    }
    /**
     * 파일명을 배열로 받아서 생성
     * @param vcUrlRequest
     * @return List of downloaded files
     * @throws IOException
     */
    public List<File> downloadFile(List<VcUrlRequest> vcUrlRequest) throws IOException {
        List<File> files = new ArrayList<>();

        for (VcUrlRequest request : vcUrlRequest) {
            String key = extractFileKeyFromUrl(request.getUrl());
            log.info("[S3Service] downloadFile - key: {}", key);
            files.add(downloadSingleFile(key));
        }
        return files;
    }



    public List<String> upload(List<MultipartFile> files, String dirName) {
        if (files.isEmpty()) {
            throw new IllegalArgumentException("파일이 없습니다");
        }

        List<String> uploadedUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            uploadedUrls.add(uploadSingleFile(file, dirName));
        }
        return uploadedUrls;
    }

    // 공통 업로드 로직을 처리하는 메서드
    private String uploadSingleFile(MultipartFile file, String dirName) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("파일이 없습니다");
        }

        // 업로드할 객체의 메타데이터 정보 추출 및 ObjectMetadata 초기화
        ObjectMetadata objectMetadata = createObjectMetadata(file);

        // 버킷 내 저장 경로(key) 설정
        String key = generateFileKey(dirName, file.getOriginalFilename());

        // 객체 추가 요청 정보 초기화
        PutObjectRequest request = createPutObjectRequest(file, key, objectMetadata);

        // S3 버킷에 객체 추가
        s3Client.putObject(request);

        // 업로드한 파일의 S3 URL 반환
        return s3Client.getUrl(buketName, key).toString();
    }

    // 파일의 메타데이터 생성
    private ObjectMetadata createObjectMetadata(MultipartFile file) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());
        return objectMetadata;
    }

    // 파일의 키 생성
    private String generateFileKey(String dirName, String originalFilename) {
        return dirName + "/" + UUID.randomUUID() + "_" + originalFilename;
    }

    // PutObjectRequest 생성
    private PutObjectRequest createPutObjectRequest(MultipartFile file, String key, ObjectMetadata objectMetadata) {
        try {
            return new PutObjectRequest(
                    buketName,
                    key,
                    file.getInputStream(),
                    objectMetadata
            );
        } catch (IOException e) {
            throw new IllegalArgumentException("입력 파라미터에 문제가 있습니다. 파일 업로드 불가!", e);
        }
    }


    /**
     * S3에서 파일을 다운로드하고 로컬에 저장
     * @param key
     * @return file
     * @throws IOException
     */
    private File downloadSingleFile(String key) throws IOException {
        GetObjectRequest getObjectRequest = new GetObjectRequest(buketName, key);
        log.info("[S3Service] downloadSingleFile - getObjectRequest: {}", getObjectRequest);

        S3ObjectInputStream inputStream = s3Client.getObject(getObjectRequest).getObjectContent();
        log.info("[S3Service] downloadSingleFile - inputStream: {}", inputStream);

        File file = new File(Paths.get("file", key).toString()); // 로컬에 저장할 경로
        log.info("[S3Service] downloadSingleFile - file: {}", file);

        file.getParentFile().mkdirs(); // 파일이 저장될 디렉토리 생성

        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        } catch (Exception e) {
            log.error("[S3Service] 파일 다운로드 실패: {}", e.getMessage(), e);
            throw new IOException("파일 다운로드 실패", e);
        }

        return file;
    }

    /**
     * URL에서 파일 키를 추출
     * @param url
     * @return key
     */
    private String extractFileKeyFromUrl(String url) {
        return "vc/src/" + url.substring(url.lastIndexOf("/") + 1);
    }


    /**
     * File folder = new File("경로")
     * 파일 삭제
     * @param folder
     */
    public void deleteFolder(File folder) {
        if (folder.exists()) {
            // 폴더 안의 파일과 서브 디렉토리 삭제
            File[] files = folder.listFiles();
            if (files != null) { // 폴더에 내용물이 있을 경우
                for (File file : files) {
                    if (file.isDirectory()) {
                        // 서브 디렉토리 재귀 삭제
                        deleteFolder(file);
                    } else {
                        // 파일 삭제
                        if (!file.delete()) {
                            log.error("파일 삭제 실패: {}" ,file.getAbsolutePath());
                        }
                    }
                }
            }
            // 폴더 삭제
            if (!folder.delete()) {
                log.error("폴더 삭제 실패: {}" , folder.getAbsolutePath());
            }
        } else {
            log.error("폴더가 존재하지 않음: {} " , folder.getAbsolutePath());
        }
    }


    public String upload(InputStream audioInputStream, String dirName, String fileName
            , long fileSize, String contentType) {
        if (audioInputStream == null) {
            throw new IllegalArgumentException("AudioInputStream이 null입니다.");
        }

        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("파일 이름이 유효하지 않습니다.");
        }

        // 업로드할 객체의 메타데이터 정보 설정
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(contentType != null ? contentType : "audio/wav"); // 기본 MIME 타입 설정
        objectMetadata.setContentLength(fileSize);

        // S3 버킷 내 저장 경로(key) 설정
        String key = dirName + "/" + UUID.randomUUID() + "_" + fileName;

        // S3에 업로드 요청
        try (InputStream inputStream = audioInputStream) {
            PutObjectRequest request = new PutObjectRequest(
                    buketName, // S3 버킷 이름
                    key,       // 저장 경로(key)
                    inputStream,
                    objectMetadata
            );

            // S3 버킷에 객체 업로드
            s3Client.putObject(request);
        } catch (IOException e) {
            throw new IllegalArgumentException("AudioInputStream을 처리하는 중 문제가 발생했습니다. 업로드 실패!", e);
        }

        // 업로드된 파일의 S3 URL 반환
        return s3Client.getUrl(buketName, key).toString();
    }


    public static AudioInputStream load(String s3Url)  {
        try {
            URL url = new URL(s3Url);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
            byte[] bytes = AudioExtensionConverter.mp3ToWav(audioInputStream);

            return AudioSystem.getAudioInputStream(new ByteArrayInputStream(bytes));
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("잘못된 URL입니다");
        } catch (IOException e) {
            throw new IllegalArgumentException("오디오 파일이 아닙니다.");
        } catch (UnsupportedAudioFileException e) {
            throw new IllegalArgumentException("지원하지 않는 오디오 형식입니다");
        }
    }

    // s3 파일 삭제 메서드
    public void deleteFile(String buketName, String key) {
        try {
            s3Client.deleteObject(buketName, key);
        } catch (SdkClientException e) {
            throw new RuntimeException("S3 파일 삭제 요청 중 에러 발생, buketName:"+buketName+", key:"+key);
        }

    }

}
