package com.oreo.finalproject_5re5_be.global.component;

import com.amazonaws.services.sqs.AmazonSQSRequester;
import com.oreo.finalproject_5re5_be.global.dto.request.SqsRequestDto;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

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

    public Message sendMessage(SqsRequestDto sqsRequestDto) throws TimeoutException {
        // 변수 설정
        // 가상 대기열 사용
        String requestQueueUrl = sqsQueueUrl + virtualQueueName;

        // 메세지 타입 설정
        String messageType = sqsRequestDto.getMessageType().getType();

        // message attribute 설정
        Map<String, MessageAttributeValue>  messageAttributes = new HashMap<>();

        // message attribute 추가
        messageAttributes.put("messageType",
            MessageAttributeValue.builder().dataType("String").stringValue(messageType).build());

        // messgae 생성
        String message = sqsRequestDto.getMessage();

        // sqs에 메세지 보내기
        SendMessageRequest sendRequest = SendMessageRequest.builder()
            .queueUrl(requestQueueUrl)
            .messageBody(message)
            .messageAttributes(messageAttributes)
            .messageGroupId("messageGroup1")
            .build();

        Message response = amazonSQSRequester.sendMessageAndGetResponse(sendRequest, 50,
            TimeUnit.SECONDS);

        return response;
    }
}
