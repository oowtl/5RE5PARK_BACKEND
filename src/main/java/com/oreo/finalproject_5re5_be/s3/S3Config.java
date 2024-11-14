package com.oreo.finalproject_5re5_be.s3;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {
    @Value("${aws.s3.accessKey}")
    private String accessKey;

    @Value("${aws.s3.secretKey}")
    private String secretKey;

    @Value("${aws.s3.region}")
    private String region;

    @Bean
    public AmazonS3 amazonS3Client() {
        // AWS 자격 증명 객체 생성
        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKey, secretKey);

        // AmazonS3 객체 생성 및 반환
        return AmazonS3ClientBuilder.standard()
                .withRegion(Regions.fromName(region)) // 리전 설정
                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials)) // 자격 증명 등록
                . build();
    }
}
