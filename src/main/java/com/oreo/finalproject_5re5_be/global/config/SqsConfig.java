package com.oreo.finalproject_5re5_be.global.config;

import com.amazonaws.services.sqs.AmazonSQSRequester;
import com.amazonaws.services.sqs.AmazonSQSRequesterClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
public class SqsConfig {

    @Value("${AWS_SQS_ACCESS_KEY_ID}")
    private String sqsAccessKey;

    @Value("${AWS_SQS_SECRET_ACCESS_KEY}")
    private String sqsSecretKey;

    @Bean
    public SqsClient sqsClient() {
        // 임시 자격 증명 사용
        StaticCredentialsProvider staticCredentialsProvider = StaticCredentialsProvider.create(
            software.amazon.awssdk.auth.credentials.AwsBasicCredentials.create(sqsAccessKey,
                sqsSecretKey)
        );

        return SqsClient.builder()
            .region(Region.AP_NORTHEAST_2)
            .credentialsProvider(staticCredentialsProvider)
            .build();
    }

    @Bean
    public AmazonSQSRequester amazonSQSRequester(SqsClient sqsClient) {
        return AmazonSQSRequesterClientBuilder.standard()
            .withAmazonSQS(sqsClient)
            .build();
    }
}
