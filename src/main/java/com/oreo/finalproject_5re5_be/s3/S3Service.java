package com.oreo.finalproject_5re5_be.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3Service {
    @Autowired
    private AmazonS3 s3Client;

    @Value("${aws.s3.bucket}")
    private String buketName;


    public String upload(MultipartFile file, String dirName){
        if(file.isEmpty()) {
            throw new IllegalArgumentException("파일이 없습니다");
        }

        // 업로드할 객체의 메타데이터 정보 추출 및 ObjectMetadata 초기화
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());

        // 버킷 내 저장 경로(key) 설정
        String key = dirName + "/" + UUID.randomUUID() + "_"+ file.getOriginalFilename();

        // 객체 추가 요청 정보 초기화
        PutObjectRequest request = null;
        try {
            request = new PutObjectRequest(
                    buketName,
                    key,
                    file.getInputStream(),
                    objectMetadata
            );
        } catch (IOException e) {
            throw new IllegalArgumentException("입력 파라미터에 문제가 있습니다. 파일 업로드 불가!");
        }

        // S3 버킷에 객체 추가
        s3Client.putObject(request);

        // 업로드한 파일의 S3 URL 반환
        return s3Client.getUrl(buketName, key).toString();
    }



}
