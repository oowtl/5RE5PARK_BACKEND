package com.oreo.finalproject_5re5_be.global.component;

import com.amazonaws.services.sqs.AmazonSQSRequester;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oreo.finalproject_5re5_be.global.constant.MessageType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
public class SqsService {
    private final SqsClient sqsClient;
    private final AmazonSQSRequester amazonSQSRequester;

    @Value("${AWS_SQS_QUEUE_URL}")
    private String sqsQueueUrl;

    @Value("${AWS_SQS_VIRTUAL_QUEUE_NAME}")
    private String virtualQueueName;

    public SqsService(SqsClient sqsClient, AmazonSQSRequester amazonSQSRequester) {
        this.sqsClient = sqsClient;
        this.amazonSQSRequester = amazonSQSRequester;
    }

    public <T> Message sendMessage(T dto, MessageType messageType) throws TimeoutException, JsonProcessingException {
        // 변수 설정
        // 가상 대기열 사용
        String requestQueueUrl = sqsQueueUrl + virtualQueueName;

        // DTO를 JSON으로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        String messageBody = objectMapper.writeValueAsString(dto);

        // 메시지 속성 설정
        Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
        messageAttributes.put("messageType",
                MessageAttributeValue.builder().dataType("String").stringValue(messageType.getType()).build());

        // sqs에 메세지 보내기
        SendMessageRequest sendRequest = SendMessageRequest.builder()
                .queueUrl(requestQueueUrl)
                .messageBody(messageBody)
                .messageAttributes(messageAttributes)
                .messageGroupId("messageGroup1")
                .build();

        Message response = amazonSQSRequester.sendMessageAndGetResponse(sendRequest, 50,
                TimeUnit.SECONDS);

        return response;
    }
}
